package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.common.types.Triple;
import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.QuickConnection;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.*;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class AppState {
    @NonNull private List<QuickConnection> quickConnections = Collections.emptyList();
    private ConnectionParameters connectionParameters;
    @NonNull private SelectedList<ElementClass> elementClasses = SelectedList.createEmpty();
    @NonNull private MultiSelectedList<Denomination> elementDenominations = MultiSelectedList.createEmpty();
    @NonNull private Map<String, List<Denomination>> lastSelectedDenominations = new HashMap<>();
    @NonNull private List<ElementData> queryResult = Collections.emptyList();
    private ElementData currentElement = null;
    @NonNull private SelectedList<VersionData> versions = SelectedList.createEmpty();
    private VersionFilter versionFilter = VersionFilter.DEFAULT_VERSION_FILTER;
    @NonNull private List<Dependency> dependencies = Collections.emptyList();
    private boolean isFwdDependencies = true;
    @NonNull private SelectedList<ElementClass> dependencyElementClasses = SelectedList.createEmpty();
    @NonNull private MultiSelectedList<Denomination> dependencyDenominations = MultiSelectedList.createEmpty();
    @NonNull private String dependencyElementQuery = "";
    @NonNull private List<Triple<ElementClass, ElementData, VersionData>> versionAggregateHistory = new ArrayList<>();
    private int historyPointer = 0;
    private VersionAggregate versionAggregate;


    public RepoType getCurrentRepoType() {
        return this.connectionParameters != null ? this.connectionParameters.getRepoType() : null;
    }


    public ElementClass getCurrentElementClass() {
        return this.elementClasses.getSelectedItem();
    }


    public VersionData getCurrentVersion() {
        return this.versions.getSelectedItem();
    }


    public Triple<ElementClass, ElementData, VersionData> getCurrentHistoryEntry() {
        if (this.historyPointer < this.getVersionAggregateHistory().size()) {
            return this.getVersionAggregateHistory().get(this.historyPointer);
        } else {
            return null;
        }
    }


    public boolean hasNextHistory() {
        return this.historyPointer < this.versionAggregateHistory.size() - 1;
    }


    public boolean hasPreviousHistory() {
        return this.historyPointer > 0;
    }
}
