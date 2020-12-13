package com.tschanz.v_bro.app.presenter.version_timeline;

import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class VersionTimelineResponse {
    private final SelectedList<VersionResponse> selectedVersionList;


    public static VersionTimelineResponse fromDomain(SelectedList<VersionData> versionDataList) {
        var versions = versionDataList.getItems()
            .stream()
            .map(VersionResponse::fromVersionData)
            .collect(Collectors.toList());
        var selectedVersion = versionDataList.getSelectedItem() != null
            ? VersionResponse.fromVersionData(versionDataList.getSelectedItem())
            : null;
        var slist = new SelectedList<>(versions, selectedVersion);

        return new VersionTimelineResponse(slist);
    }
}
