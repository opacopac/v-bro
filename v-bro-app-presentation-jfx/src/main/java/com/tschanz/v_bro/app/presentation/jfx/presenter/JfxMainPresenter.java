package com.tschanz.v_bro.app.presentation.jfx.presenter;

import com.tschanz.v_bro.app.presentation.presenter.*;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassPresenter;
import com.tschanz.v_bro.app.presenter.repo_connection.RepoConnectionPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import lombok.Getter;


@Getter
public class JfxMainPresenter extends MainPresenter {
    private final StatusPresenter statusPresenter;
    private final RepoConnectionPresenter repoConnectionPresenter;
    private final ElementClassPresenter elementClassPresenter;
    private final DenominationsPresenter denominationsPresenter;
    private final ElementPresenter elementPresenter;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final DependencyPresenter dependencyPresenter;
    private final VersionAggregatePresenter versionAggregatePresenter;


    public JfxMainPresenter(MainViewModel mainViewModel) {
        super(mainViewModel);

        this.statusPresenter = new JfxStatusPresenter(mainViewModel.appStatus);
        this.repoConnectionPresenter = new JfxRepoConnectionPresenter(mainViewModel.currentRepoConnection);
        this.elementClassPresenter = new JfxElementClassPresenter(mainViewModel.elementClasses);
        this.denominationsPresenter = new JfxDenominationsPresenter(mainViewModel.elementDenominations);
        this.elementPresenter = new JfxElementPresenter(mainViewModel.currentElement);
        this.versionTimelinePresenter = new JfxVersionTimelinePresenter(mainViewModel.versions);
        this.dependencyPresenter = new JfxDependenciesPresenter(mainViewModel.fwdDependencies);
        this.versionAggregatePresenter = new JfxVersionAggregatePresenter(mainViewModel.versionAggregate);
    }
}
