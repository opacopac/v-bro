package com.tschanz.v_bro.app.presentation.view;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;


public interface MainView {
    void bindViewModel(MainModel mainModel);

    void start();

    StatusBarView getStatusBarView(); // TODO: remove
}