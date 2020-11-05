package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.view.VersionsView;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionVonBisPx;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;
import java.util.concurrent.Flow;


public class VersionTimeline extends JPanel implements VersionsView {
    private static final Color BAR_BORDER_COLOR = Color.BLACK;
    private static final Color BAR_BORDER_COLOR_HOVER = Color.YELLOW;
    private static final Color BAR_BORDER_COLOR_SELECTED = Color.ORANGE;
    private static final List<Color> BAR_FILL_COLORS = List.of(
        new Color(0, 182, 235),
        new Color(165, 138, 255),
        new Color(251, 97, 215),
        new Color(248, 118, 109),
        new Color(196, 154, 0),
        new Color(83, 180, 0),
        new Color(0, 192, 148)
    );
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR_HOVER = Color.YELLOW;
    private static final Color TEXT_COLOR_SELECTED = Color.ORANGE;
    private static final int SPACER_HEIGHT = 2;
    private static final int TEXT_HEIGHT = 10;
    private static final int TEXT_VON_OFFSET_REL_X_PX = SPACER_HEIGHT;
    private static final int TEXT_VON_OFFSET_Y_PX = TEXT_HEIGHT + SPACER_HEIGHT;
    private static final int BAR_OFFSET_Y_PX = 0;
    private static final int BAR_HEIGHT_PX = 30;
    private static final int TEXT_BIS_OFFSET_X_REL_PX = -SPACER_HEIGHT;
    private static final int TEXT_BIS_OFFSET_Y_PX = BAR_OFFSET_Y_PX + BAR_HEIGHT_PX - SPACER_HEIGHT;
    private static final Font DATE_FONT = new Font("SansSerif", Font.PLAIN, TEXT_HEIGHT);

    public static int TIMELINE_WIDTH = 750;
    public static int TIMELINE_HEIGHT = BAR_HEIGHT_PX + 10;

    private List<VersionItem> versionList;
    private VersionFilterItem effectiveVersionFilter;
    private VersionItem hoverVersion;
    private final Set<VersionVonBisPx> versionVonBisXPixelCache = new HashSet<>();
    private SelectVersionAction selectVersionAction;
    //TODO: private boolean autoSelectLastEntry


    public VersionTimeline() {
        Dimension size = new Dimension(TIMELINE_WIDTH, TIMELINE_HEIGHT);
        this.setMaximumSize(size);
        this.setPreferredSize(size); // TODO
        this.setMinimumSize(size);
        this.addMouseListener(new VersionTimelineMouseListener());
        this.addMouseMotionListener(new VersionTimelineMouseMotionListener());
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<List<VersionItem>> versionList,
        Flow.Publisher<VersionFilterItem> effectiveVersionFilter,
        SelectVersionAction selectVersionAction
    ) {
        this.selectVersionAction = selectVersionAction;
        effectiveVersionFilter.subscribe(new GenericSubscriber<>(this::onEffectiveVersionFilterChanged));
        versionList.subscribe(new GenericSubscriber<>(this::onVersionListChanged));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.versionList == null || this.versionList.size() == 0) {
            return;
        }

        this.versionVonBisXPixelCache.clear();
        this.versionList
            .stream()
            .filter(version -> version.getGueltigBis().isAfter(this.effectiveVersionFilter.getMinGueltigVon()))
            .filter(version -> version.getGueltigVon().isBefore(this.effectiveVersionFilter.getMaxGueltigBis()))
            .forEach(version -> this.drawVersionBar(g, version));
    }


    private void onVersionListChanged(List<VersionItem> versionList) {
        this.versionList = versionList;

        if (this.selectVersionAction != null) {
            if (versionList.size() == 0) {
                this.onVersionSelected(null); // TODO: show zombie alert
            } else {
                this.onVersionSelected(versionList.get(versionList.size() - 1)); // auto-select last entry
            }
        }
    }


    private void onEffectiveVersionFilterChanged(VersionFilterItem effectiveVersionFilter) {
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.repaint();
        this.revalidate();
    }


    private void onVersionHover(VersionItem version) {
        this.hoverVersion = version;
        this.repaint();
    }


    private void onVersionSelected(VersionItem selectedVersion) {
        if (this.selectVersionAction == null || selectedVersion == null) {
            return;
        }

        this.selectVersionAction.next(selectedVersion.getId());
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
        String selectedVersionId = this.selectVersionAction != null ? this.selectVersionAction.getCurrentValue() : "";
        int colorIndex = this.versionList.indexOf(version) % BAR_FILL_COLORS.size();
        //g.setColor((version.equals(selectedVersion)) ? BAR_FILL_COLOR_SELECTED : BAR_FILL_COLORS.get(colorIndex));
        g.setColor(BAR_FILL_COLORS.get(colorIndex));
        g.fillRect(x1, BAR_OFFSET_Y_PX,x2 - x1, BAR_HEIGHT_PX);

        // border
        if (version.equals(this.hoverVersion)) {
            g.setColor(BAR_BORDER_COLOR_HOVER);
        } else if (version.getId().equals(selectedVersionId)) {
            g.setColor(BAR_BORDER_COLOR_SELECTED);
        } else {
            g.setColor(BAR_BORDER_COLOR);
        }
        g.drawRect(x1, BAR_OFFSET_Y_PX,x2 - x1, BAR_HEIGHT_PX);

        // von text
        if (version.equals(this.hoverVersion)) {
            g.setColor(TEXT_COLOR_HOVER);
        } else if (version.getId().equals(selectedVersionId)) {
            g.setColor(TEXT_COLOR_SELECTED);
        } else {
            g.setColor(TEXT_COLOR);
        }
        g.setFont(DATE_FONT);
        g.drawString(version.getGueltigVonText(), x1 + TEXT_VON_OFFSET_REL_X_PX, TEXT_VON_OFFSET_Y_PX);

        // bis text
        String bisText = version.getGueltigBisText();
        int textWidth = g.getFontMetrics().stringWidth(bisText);
        g.drawString(bisText, x2 - textWidth + TEXT_BIS_OFFSET_X_REL_PX, TEXT_BIS_OFFSET_Y_PX);
    }


    private int getXpixel(LocalDate date) {
        long minPx = 0;
        long maxPx = this.getWidth() - 1;
        long minTime = this.effectiveVersionFilter.getMinGueltigVon().atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long maxTime = this.effectiveVersionFilter.getMaxGueltigBis().atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        long dateTime;
        if (date.isAfter(this.effectiveVersionFilter.getMaxGueltigBis())) {
            dateTime = maxTime;
        } else if (date.isBefore(this.effectiveVersionFilter.getMinGueltigVon())) {
            dateTime = minTime;
        } else {
            dateTime = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }

        return Math.round(((float) dateTime - minTime) * (maxPx - minPx) / (maxTime - minTime));
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
