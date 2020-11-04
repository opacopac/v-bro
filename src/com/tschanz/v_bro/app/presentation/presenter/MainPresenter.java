package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterPresenter;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementPresenter;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassPresenter;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionPresenter;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionPresenter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationPresenter;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionPresenter;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterPresenter;


public class MainPresenter {
    private final OpenConnectionPresenter openConnectionPresenter;
    private final CloseConnectionPresenter closeConnectionPresenter;
    private final SelectElementClassPresenter selectElementClassPresenter;
    private final SelectElementDenominationPresenter selectElementDenominationPresenter;
    private final SelectElementPresenter selectElementPresenter;
    private final SelectVersionFilterPresenter selectVersionFilterPresenter;
    private final SelectVersionPresenter selectVersionPresenter;
    private final SelectDependencyFilterPresenter selectDependencyFilterPresenter;


    public OpenConnectionPresenter getOpenConnectionPresenter() { return openConnectionPresenter; }
    public CloseConnectionPresenter getCloseConnectionPresenter() { return closeConnectionPresenter; }
    public SelectElementClassPresenter getSelectElementClassPresenter() { return selectElementClassPresenter; }
    public SelectElementDenominationPresenter getSelectElementDenominationPresenter() { return selectElementDenominationPresenter; }
    public SelectElementPresenter getSelectElementPresenter() { return selectElementPresenter; }
    public SelectVersionFilterPresenter getSelectVersionFilterPresenter() { return selectVersionFilterPresenter; }
    public SelectVersionPresenter getSelectVersionPresenter() { return selectVersionPresenter; }
    public SelectDependencyFilterPresenter getSelectDependencyFilterPresenter() { return selectDependencyFilterPresenter; }


    public MainPresenter(MainModel mainModel) {
        this.openConnectionPresenter = new OpenConnectionPresenterImpl(mainModel);
        this.closeConnectionPresenter = new CloseConnectionPresenterImpl(mainModel);
        this.selectElementClassPresenter = new SelectElementClassPresenterImpl(mainModel);
        this.selectElementDenominationPresenter = new SelectElementDenominationPresenterImpl(mainModel);
        this.selectElementPresenter = new SelectElementPresenterImpl(mainModel);
        this.selectVersionFilterPresenter = new SelectVersionFilterPresenterImpl(mainModel);
        this.selectVersionPresenter = new SelectVersionPresenterImpl(mainModel);
        this.selectDependencyFilterPresenter = new SelectDependencyFilterPresenterImpl(mainModel);
    }
}