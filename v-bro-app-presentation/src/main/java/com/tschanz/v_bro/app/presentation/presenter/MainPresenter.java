package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassPresenter;
import com.tschanz.v_bro.app.presenter.repo_connection.RepoConnectionPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryPresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import lombok.Getter;


@Getter
public class MainPresenter {
    private final StatusPresenter statusPresenter;
    private final RepoConnectionPresenter repoConnectionPresenter;
    private final ElementClassPresenter elementClassPresenter;
    private final DenominationsPresenter denominationsPresenter;
    private final ElementPresenter elementPresenter;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final DependencyPresenter dependencyPresenter;
    private final VersionAggregateHistoryPresenter versionAggregateHistoryPresenter;
    private final VersionAggregatePresenter versionAggregatePresenter;


    public MainPresenter(MainViewModel mainViewModel) {
        this.statusPresenter = new StatusPresenterImpl(mainViewModel.appStatus);
        this.repoConnectionPresenter = new RepoConnectionPresenterImpl(mainViewModel.currentRepoConnection);
        this.elementClassPresenter = new ElementClassPresenterImpl(mainViewModel.elementClasses);
        this.denominationsPresenter = new DenominationsPresenterImpl(mainViewModel.elementDenominations);
        this.elementPresenter = new ElementPresenterImpl(mainViewModel.currentElement);
        this.versionTimelinePresenter = new VersionTimelinePresenterImpl(mainViewModel.versions);
        this.dependencyPresenter = new DependenciesPresenterImpl(mainViewModel.fwdDependencies);
        this.versionAggregateHistoryPresenter = new VersionAggregateHistoryPresenterImpl(mainViewModel.versionAggregateHistory);
        this.versionAggregatePresenter = new VersionAggregatePresenterImpl(mainViewModel.versionAggregate);
    }
}
