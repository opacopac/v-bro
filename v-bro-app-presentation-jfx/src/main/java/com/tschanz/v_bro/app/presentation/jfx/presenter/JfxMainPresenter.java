package com.tschanz.v_bro.app.presentation.jfx.presenter;

import com.tschanz.v_bro.app.presentation.presenter.MainPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.presenter.element_list.ElementListPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import lombok.Getter;


@Getter
public class JfxMainPresenter extends MainPresenter {
    private final StatusPresenter statusPresenter;
    private final ElementListPresenter elementListPresenter;


    public JfxMainPresenter(MainViewModel mainViewModel) {
        super(mainViewModel);
        this.statusPresenter = new JfxStatusPresenter(mainViewModel.appStatus);
        this.elementListPresenter = new JfxElementListPresenter(mainViewModel.elements);
    }
}
