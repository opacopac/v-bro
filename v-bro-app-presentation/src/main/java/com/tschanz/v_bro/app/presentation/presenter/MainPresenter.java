package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListPresenter;
import com.tschanz.v_bro.app.presenter.element_list.ElementListPresenter;
import com.tschanz.v_bro.app.presenter.repo.RepoPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import lombok.Getter;


@Getter
public class MainPresenter {
    private final StatusPresenter statusPresenter;
    private final RepoPresenter repoPresenter;
    private final ElementClassListPresenter elementClassListPresenter;
    private final DenominationsPresenter denominationsPresenter;
    private final ElementListPresenter elementListPresenter;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final DependencyPresenter dependencyPresenter;
    private final VersionAggregatePresenter versionAggregatePresenter;


    public MainPresenter(MainViewModel mainViewModel) {
        this.statusPresenter = new StatusPresenterImpl(mainViewModel.appStatus);
        this.repoPresenter = new RepoPresenterImpl(mainViewModel.currentRepoConnection);
        this.elementClassListPresenter = new ElementClassListPresenterImpl(mainViewModel.elementClasses);
        this.denominationsPresenter = new DenominationsPresenterImpl(mainViewModel.elementDenominations);
        this.elementListPresenter = new ElementListPresenterImpl(mainViewModel.elements);
        this.versionTimelinePresenter = new VersionTimelinePresenterImpl(mainViewModel.versions);
        this.dependencyPresenter = new DependenciesPresenterImpl(mainViewModel.fwdDependencies);
        this.versionAggregatePresenter = new VersionAggregatePresenterImpl(mainViewModel.versionAggregate);
    }
}
