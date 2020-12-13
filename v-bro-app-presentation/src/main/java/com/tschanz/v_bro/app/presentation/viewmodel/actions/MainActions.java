package com.tschanz.v_bro.app.presentation.viewmodel.actions;

import com.tschanz.v_bro.app.presentation.viewmodel.denominations.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.DependencyFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.dependency.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.element.QueryElementItem;
import com.tschanz.v_bro.app.presentation.viewmodel.repo.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.version.VersionFilterItem;

import java.util.List;


public class MainActions {
    public final ViewAction<RepoConnectionItem> connectToRepoAction = new ViewAction<>();
    public final ViewAction<String> selectElementClassAction = new ViewAction<>();
    public final ViewAction<List<DenominationItem>> selectDenominationsAction = new ViewAction<>();
    public final ViewAction<QueryElementItem> queryElementAction = new ViewAction<>();
    public final ViewAction<String> selectElementAction = new ViewAction<>();
    public final ViewAction<String> selectVersionAction = new ViewAction<>();
    public final ViewAction<VersionFilterItem> selectVersionFilterAction = new ViewAction<>();
    public final ViewAction<DependencyFilterItem> selectDependencyFilterAction = new ViewAction<>();
    public final ViewAction<ElementVersionVector> selectDependencyVersionAction = new ViewAction<>();
}
