package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterPresenter;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionPresenter;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementPresenter;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassPresenter;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionPresenter;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionPresenter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationPresenter;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionPresenter;


public class MainPresenter {
    private final OpenConnectionPresenter openConnectionPresenter;
    private final CloseConnectionPresenter closeConnectionPresenter;
    private final SelectElementClassPresenter selectElementClassPresenter;
    private final SelectElementDenominationPresenter selectElementDenominationPresenter;
    private final SelectElementPresenter selectElementPresenter;
    private final SelectVersionPresenter selectVersionPresenter;
    private final SelectDependencyFilterPresenter selectDependencyFilterPresenter;
    private final SelectDependencyVersionPresenter selectDependencyVersionPresenter;


    public OpenConnectionPresenter getOpenConnectionPresenter() { return openConnectionPresenter; }
    public CloseConnectionPresenter getCloseConnectionPresenter() { return closeConnectionPresenter; }
    public SelectElementClassPresenter getSelectElementClassPresenter() { return selectElementClassPresenter; }
    public SelectElementDenominationPresenter getSelectElementDenominationPresenter() { return selectElementDenominationPresenter; }
    public SelectElementPresenter getSelectElementPresenter() { return selectElementPresenter; }
    public SelectVersionPresenter getSelectVersionPresenter() { return selectVersionPresenter; }
    public SelectDependencyFilterPresenter getSelectDependencyFilterPresenter() { return selectDependencyFilterPresenter; }
    public SelectDependencyVersionPresenter getSelectDependencyVersionPresenter() { return selectDependencyVersionPresenter; }


    public MainPresenter(MainModel mainModel) {
        this.openConnectionPresenter = new OpenConnectionPresenterImpl(mainModel);
        this.closeConnectionPresenter = new CloseConnectionPresenterImpl(mainModel);
        this.selectElementClassPresenter = new SelectElementClassPresenterImpl(mainModel);
        this.selectElementDenominationPresenter = new SelectElementDenominationPresenterImpl(mainModel);
        this.selectElementPresenter = new SelectElementPresenterImpl(mainModel);
        this.selectVersionPresenter = new SelectVersionPresenterImpl(mainModel);
        this.selectDependencyFilterPresenter = new SelectDependencyFilterPresenterImpl(mainModel);
        this.selectDependencyVersionPresenter = new SelectDependencyVersionPresenterImpl(mainModel);
    }
}
