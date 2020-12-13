package com.tschanz.v_bro.app.presentation.viewmodel.version;

import com.tschanz.v_bro.app.presentation.viewmodel.common.IdItem;
import com.tschanz.v_bro.app.presentation.viewmodel.common.SelectableItemList;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class VersionItem implements IdItem {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
    private final String id;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;


    public static SelectableItemList<VersionItem> fromResponse(VersionTimelineResponse response) {
        var items = response.getSelectedVersionList().getItems()
            .stream()
            .map(VersionItem::fromResponse)
            .collect(Collectors.toList());
        var selectedId = response.getSelectedVersionList().getSelectedItem() != null
            ? response.getSelectedVersionList().getSelectedItem().getId()
            : null;

        return new SelectableItemList<>(items, selectedId);
    }


    public static List<VersionItem> fromResponse(List<VersionResponse> versionResponses) {
        return versionResponses
            .stream()
            .map(VersionItem::fromResponse)
            .collect(Collectors.toList());
    }


    public static VersionItem fromResponse(VersionResponse response) {
        return new VersionItem(
            response.getId(),
            response.getGueltigVon(),
            response.getGueltigBis()
        );
    }


    public String getGueltigVonText() {
        return DATE_FORMATTER.format(this.getGueltigVon());
    }


    public String getGueltigBisText() {
        return DATE_FORMATTER.format(this.getGueltigBis());
    }


    @Override
    public String toString() {
        return this.id + "";
    }
}
