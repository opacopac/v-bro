package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.MainActions;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;


public interface MainView {
    void bindViewModel(MainViewModel mainViewModel, MainActions mainActions);
}
