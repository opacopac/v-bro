package com.tschanz.v_bro.app.presentation.jfx.presenter;

import com.tschanz.v_bro.app.presentation.presenter.MainPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import lombok.Getter;


@Getter
public class JfxMainPresenter extends MainPresenter {
    public JfxMainPresenter(MainViewModel mainViewModel) {
        super(mainViewModel);
    }
}
