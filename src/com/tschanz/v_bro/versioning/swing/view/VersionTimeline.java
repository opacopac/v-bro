package com.tschanz.v_bro.versioning.swing.view;

import com.tschanz.v_bro.versioning.swing.model.VersionItem;
import com.tschanz.v_bro.versioning.domain.model.VersionInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;


public class VersionTimeline extends JPanel implements VersionTimelineView {
    private static final int SPACER_HEIGHT = 3;
    private static final int TEXT_HEIGHT = 10;
    private static final int TEXT_VON_OFFSET_Y_PX = TEXT_HEIGHT;
    private static final int BAR_OFFSET_Y_PX = TEXT_VON_OFFSET_Y_PX + SPACER_HEIGHT;
    private static final int BAR_HEIGHT_PX = 30;
    private static final int TEXT_BIS_OFFSET_Y_PX = BAR_OFFSET_Y_PX + BAR_HEIGHT_PX + TEXT_HEIGHT + SPACER_HEIGHT;
    private static final long ONE_YEAR_IN_S = 365L * 24 * 60 * 60;
    private static final Font DATE_FONT = new Font("SansSerif", Font.PLAIN, TEXT_HEIGHT);
    private List<VersionItem> versionList;
    private VersionItem hoverVersion;
    private VersionItem selectedVersion;
    private final Set<VersionVonBisPx> versionVonBisXPixelCache = new HashSet<>();
    private final List<ActionListener> versionSelectedListeners = new ArrayList<>();


    public VersionTimeline() {
    }


    @Override
    public void setVersionList(List<VersionItem> versionList) {
        this.versionList = versionList;
        this.repaint();
    }


    @Override
    public VersionItem getHoverVersion() {
        return this.hoverVersion;
    }


    @Override
    public void setHoverVersion(VersionItem version) {
        this.hoverVersion = version;
        this.repaint();
    }


    @Override
    public void setSelectedVersion(VersionItem version) {
        this.selectedVersion = version;
        this.repaint();

        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_FIRST, "VERSION_SELECTED");
        this.versionSelectedListeners.forEach(listener -> listener.actionPerformed(event));
    }


    @Override
    public VersionItem getSelectedVersion() {
        return this.selectedVersion;
    }


    public VersionItem getMousPosVersion(int x, int y) {
        if (y < BAR_OFFSET_Y_PX || y > BAR_OFFSET_Y_PX + BAR_HEIGHT_PX) {
            return null;
        }

        return this.versionVonBisXPixelCache
            .stream()
            .filter(vonBis -> x >= vonBis.x1 && x <= vonBis.x2)
            .map(vonBis -> vonBis.version)
            .findFirst()
            .orElse(null);
    }


    @Override
    public void addVersionSelectedListener(ActionListener listener) {
        this.versionSelectedListeners.add(listener);
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 2 * TEXT_HEIGHT + BAR_HEIGHT_PX + 2 * SPACER_HEIGHT);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.versionList == null || this.versionList.size() == 0) {
            return;
        }

        this.versionVonBisXPixelCache.clear();
        this.versionList.forEach(version -> this.drawVersionBar(g, version));
    }


    private void drawVersionBar(Graphics g, VersionItem version) {
        int x1 = this.getXpixel(version.getGueltigVon());
        int x2 = this.getXpixel(version.getGueltigBis());
        this.versionVonBisXPixelCache.add(new VersionVonBisPx(version, x1, x2));

        // bar
        g.setColor(version.equals(this.selectedVersion) ? Color.GREEN : Color.RED);
        g.fillRect(x1, BAR_OFFSET_Y_PX,x2 - x1, BAR_HEIGHT_PX);

        // border
        g.setColor(version.equals(this.hoverVersion) ? Color.RED : Color.BLACK);
        g.drawRect(x1, BAR_OFFSET_Y_PX,x2 - x1, BAR_HEIGHT_PX);

        // von text
        g.setFont(DATE_FONT);
        g.drawString(version.getGueltigVonText(), x1, TEXT_VON_OFFSET_Y_PX);

        // bis text
        String bisText = version.getGueltigBisText();
        int textWidth = g.getFontMetrics().stringWidth(bisText);
        g.drawString(bisText, x2 - textWidth, TEXT_BIS_OFFSET_Y_PX);
    }


    private int getXpixel(LocalDate date) {
        long minPx = this.getMinPixel();
        long maxPx = this.getMaxPixel();
        long minTime = this.getMinTime();
        long maxTime = this.getMaxTime();

        long dateTime;
        if (date.equals(VersionItem.HIGH_DATE)) {
            dateTime = maxTime;
        } else if (date.equals(VersionItem.LOW_DATE)) {
            dateTime = minTime;
        } else {
            dateTime = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }

        return Math.round(((float) dateTime - minTime) * (maxPx - minPx) / (maxTime - minTime));
    }


    private int getMinPixel() {
        return 0;
    }


    private int getMaxPixel() {
        return this.getWidth() - 1;
    }


    private long getMinTime() {
        VersionItem firstVersion = this.versionList.get(0);

        if (firstVersion.getGueltigVon().equals(VersionInfo.LOW_DATE)) {
            return VersionInfo.LOW_DATE.atStartOfDay(ZoneOffset.UTC).toEpochSecond() - ONE_YEAR_IN_S;
        } else {
            return firstVersion.getGueltigVon().atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }
    }


    private long getMaxTime() {
        VersionItem lastVersion = this.versionList.get(this.versionList.size() - 1);

        if (lastVersion.getGueltigBis().equals(VersionInfo.HIGH_DATE)) {
            return VersionInfo.HIGH_DATE.atStartOfDay(ZoneOffset.UTC).toEpochSecond() + ONE_YEAR_IN_S;
        } else {
            return lastVersion.getGueltigBis().atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }
    }


    private static class VersionVonBisPx {
        private final VersionItem version;
        private final int x1;
        private final int x2;

        public VersionItem getVersion() { return version; }
        public int getX1() { return x1; }
        public int getX2() { return x2; }

        public VersionVonBisPx(VersionItem version, int x1, int x2) {
            this.version = version;
            this.x1 = x1;
            this.x2 = x2;
        }
    }
}
