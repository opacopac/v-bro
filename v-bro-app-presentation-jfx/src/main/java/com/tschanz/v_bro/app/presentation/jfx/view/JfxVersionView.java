package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.actions.SelectVersionAction;
import com.tschanz.v_bro.app.presentation.view.VersionsView;
import com.tschanz.v_bro.app.presentation.viewmodel.SelectableItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionVonBisPx;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Flow;


public class JfxVersionView implements VersionsView {
    private static final Color BAR_BORDER_COLOR = Color.BLACK;
    private static final Color BAR_BORDER_COLOR_SELECTED = Color.WHITE;
    private static final java.util.List<Color> BAR_FILL_COLORS = List.of(
        Color.rgb(0, 182, 235),
        Color.rgb(165, 138, 255),
        Color.rgb(251, 97, 215),
        Color.rgb(248, 118, 109),
        Color.rgb(196, 154, 0),
        Color.rgb(83, 180, 0),
        Color.rgb(0, 192, 148)
    );
    private static final double TEXT_LINE_WIDTH = 0.35;
    private static final double TEXT_LINE_WIDTH_SELECTED = 1.0;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR_SELECTED = Color.WHITE;
    private static final int SPACER_HEIGHT = 2;
    private static final int TEXT_HEIGHT = 10;
    private static final int TEXT_VON_OFFSET_REL_X_PX = SPACER_HEIGHT;
    private static final int TEXT_VON_OFFSET_Y_PX = TEXT_HEIGHT + SPACER_HEIGHT;
    private static final int BAR_OFFSET_Y_PX = 0;
    private static final int BAR_HEIGHT_PX = 30;
    private static final int TEXT_BIS_OFFSET_X_REL_PX = -SPACER_HEIGHT;
    private static final int TEXT_BIS_OFFSET_Y_PX = BAR_OFFSET_Y_PX + BAR_HEIGHT_PX - SPACER_HEIGHT;
    private static final Font DATE_FONT = Font.font("System", TEXT_HEIGHT);

    @FXML private Canvas canvas;
    private SelectableItemList<VersionItem> versionList;
    private VersionFilterItem effectiveVersionFilter;
    private final Set<VersionVonBisPx> versionVonBisXPixelCache = new HashSet<>();
    private SelectVersionAction selectVersionAction;


    @Override
    public void bindViewModel(
        Flow.Publisher<SelectableItemList<VersionItem>> versionList,
        Flow.Publisher<VersionFilterItem> effectiveVersionFilter,
        SelectVersionAction selectVersionAction
    ) {
        this.selectVersionAction = selectVersionAction;
        effectiveVersionFilter.subscribe(new GenericSubscriber<>(this::onEffectiveVersionFilterChanged));
        versionList.subscribe(new GenericSubscriber<>(this::onVersionListChanged));
    }


    private void onVersionListChanged(SelectableItemList<VersionItem> versionList) {
        this.versionList = versionList;
        this.repaint();
    }


    private void onEffectiveVersionFilterChanged(VersionFilterItem effectiveVersionFilter) {
        this.effectiveVersionFilter = effectiveVersionFilter;
        this.repaint();
    }



    public void onMouseMoved(MouseEvent mouseEvent) {
        VersionItem hoverVersion = this.getMousPosVersion(mouseEvent.getX(), mouseEvent.getY());
        if (hoverVersion != null) {
            this.canvas.getScene().setCursor(Cursor.HAND);
        } else {
            this.canvas.getScene().setCursor(Cursor.DEFAULT);
        }
    }


    public void onMouseClicked(MouseEvent mouseEvent) {
        VersionItem selectedVersionItem = this.getMousPosVersion(mouseEvent.getX(), mouseEvent.getY());

        if (this.selectVersionAction == null || selectedVersionItem == null) {
            return;
        }

        this.selectVersionAction.next(selectedVersionItem.getId());
        this.repaint();
    }


    private void repaint() {
        var gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (this.versionList == null || this.versionList.getItems().size() == 0) {
            return;
        }

        this.versionVonBisXPixelCache.clear();
        this.versionList.getItems()
            .stream()
            .filter(version -> version.getGueltigBis().isAfter(this.effectiveVersionFilter.getMinGueltigVon()))
            .filter(version -> version.getGueltigVon().isBefore(this.effectiveVersionFilter.getMaxGueltigBis()))
            .forEach(this::drawVersionBar);
    }


    private void drawVersionBar(VersionItem version) {
        var gc = this.canvas.getGraphicsContext2D();

        int x1 = this.getXpixel(version.getGueltigVon());
        int x2 = this.getXpixel(version.getGueltigBis());
        this.versionVonBisXPixelCache.add(new VersionVonBisPx(version, x1, x2));

        // bar
        String selectedVersionId = versionList.getSelectedItem() != null ? versionList.getSelectedItem().getId() : "";
        int colorIndex = this.versionList.getItems().indexOf(version) % BAR_FILL_COLORS.size();
        gc.setFill(BAR_FILL_COLORS.get(colorIndex));
        gc.fillRect(x1, BAR_OFFSET_Y_PX,x2 - x1, BAR_HEIGHT_PX);

        // border
        if (version.getId().equals(selectedVersionId)) {
            gc.setStroke(BAR_BORDER_COLOR_SELECTED);
        } else {
            gc.setStroke(BAR_BORDER_COLOR);
        }
        gc.strokeRect(x1, BAR_OFFSET_Y_PX,x2 - x1, BAR_HEIGHT_PX);

        // von text
        if (version.getId().equals(selectedVersionId)) {
            gc.setStroke(TEXT_COLOR_SELECTED);
            gc.setLineWidth(TEXT_LINE_WIDTH_SELECTED);
        } else {
            gc.setStroke(TEXT_COLOR);
            gc.setLineWidth(TEXT_LINE_WIDTH);
        }

        gc.setFont(DATE_FONT);
        gc.strokeText(version.getGueltigVonText(), x1 + TEXT_VON_OFFSET_REL_X_PX, TEXT_VON_OFFSET_Y_PX);

        // bis text
        String bisText = version.getGueltigBisText();
        var measureTxt = new Text(bisText);
        measureTxt.setFont(DATE_FONT);
        double textWidth = measureTxt.getBoundsInLocal().getWidth();
        gc.strokeText(bisText, x2 - textWidth + TEXT_BIS_OFFSET_X_REL_PX, TEXT_BIS_OFFSET_Y_PX);
    }


    private int getXpixel(LocalDate date) {
        double minPx = 0;
        double maxPx = this.canvas.getWidth() - 1;
        double minTime = this.effectiveVersionFilter.getMinGueltigVon().atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        double maxTime = this.effectiveVersionFilter.getMaxGueltigBis().atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        double dateTime;
        if (date.isAfter(this.effectiveVersionFilter.getMaxGueltigBis())) {
            dateTime = maxTime;
        } else if (date.isBefore(this.effectiveVersionFilter.getMinGueltigVon())) {
            dateTime = minTime;
        } else {
            dateTime = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        }

        return (int) Math.round(((float) dateTime - minTime) * (maxPx - minPx) / (maxTime - minTime));
    }


    private VersionItem getMousPosVersion(double x, double y) {
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
}
