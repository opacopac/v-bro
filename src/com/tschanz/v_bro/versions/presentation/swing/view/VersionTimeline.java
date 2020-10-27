package com.tschanz.v_bro.versions.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.versions.presentation.swing.viewmodel.VersionVonBisPx;
import com.tschanz.v_bro.versions.presentation.swing.viewmodel.VersionItem;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;
import java.util.concurrent.Flow;


public class VersionTimeline extends JPanel implements VersionsView {
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
    private final Set<VersionVonBisPx> versionVonBisXPixelCache = new HashSet<>();
    private BehaviorSubject<VersionItem> selectVersionAction;


    public VersionTimeline() {
        this.addMouseListener(new VersionTimelineMouseListener());
        this.addMouseMotionListener(new VersionTimelineMouseMotionListener());
    }


    @Override
    public void bindVersionList(Flow.Publisher<List<VersionItem>> versionList) {
        versionList.subscribe(new GenericSubscriber<>(this::onVersionListChanged));
    }


    @Override
    public void bindSelectVersionAction(BehaviorSubject<VersionItem> selectVersionAction) {
        this.selectVersionAction = selectVersionAction;
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


    private void onVersionListChanged(List<VersionItem> versionList) {
        this.versionList = versionList;
        this.repaint();
    }


    private void onVersionHover(VersionItem version) {
        this.hoverVersion = version;
        this.repaint();
    }


    private void onVersionSelected(VersionItem selectedVersion) {
        if (this.selectVersionAction == null) {
            return;
        }

        this.selectVersionAction.next(selectedVersion);
        this.repaint();
    }


    private VersionItem getMousPosVersion(int x, int y) {
        if (y < BAR_OFFSET_Y_PX || y > BAR_OFFSET_Y_PX + BAR_HEIGHT_PX) {
            return null;
        }

        return this.versionVonBisXPixelCache
            .stream()
            .filter(vonBis -> x >= vonBis.getX1() && x <= vonBis.getX2())
            .map(VersionVonBisPx::getVersion)
            .findFirst()
            .orElse(null);
    }


    private void drawVersionBar(Graphics g, VersionItem version) {
        int x1 = this.getXpixel(version.getGueltigVon());
        int x2 = this.getXpixel(version.getGueltigBis());
        this.versionVonBisXPixelCache.add(new VersionVonBisPx(version, x1, x2));

        // bar
        VersionItem selectedVersion = this.selectVersionAction != null ? this.selectVersionAction.getCurrentValue() : null;
        g.setColor((version.equals(selectedVersion)) ? Color.GREEN : Color.RED);
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
            return firstVersion.getGueltigBis().atStartOfDay(ZoneOffset.UTC).toEpochSecond() - ONE_YEAR_IN_S;
        } else {
            return firstVersion.getGueltigVon().atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }
    }


    private long getMaxTime() {
        VersionItem lastVersion = this.versionList.get(this.versionList.size() - 1);

        if (lastVersion.getGueltigBis().equals(VersionInfo.HIGH_DATE)) {
            return lastVersion.getGueltigVon().atStartOfDay(ZoneOffset.UTC).toEpochSecond() + ONE_YEAR_IN_S;
        } else {
            return lastVersion.getGueltigBis().atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }
    }


    private class VersionTimelineMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            VersionItem selectedVersionItem = VersionTimeline.this.getMousPosVersion(e.getX(), e.getY());
            VersionTimeline.this.onVersionSelected(selectedVersionItem);
        }

        @Override public void mousePressed(MouseEvent e) { }

        @Override public void mouseReleased(MouseEvent e) { }

        @Override public void mouseEntered(MouseEvent e) { }

        @Override public void mouseExited(MouseEvent e) { }
    }


    private class VersionTimelineMouseMotionListener implements MouseMotionListener {
        @Override public void mouseDragged(MouseEvent e) { }

        @Override
        public void mouseMoved(MouseEvent e) {
            VersionItem hoverVersion = VersionTimeline.this.getMousPosVersion(e.getX(), e.getY());
            if (hoverVersion != VersionTimeline.this.hoverVersion) {
                VersionTimeline.this.onVersionHover(hoverVersion);
            }
        }
    }
}
