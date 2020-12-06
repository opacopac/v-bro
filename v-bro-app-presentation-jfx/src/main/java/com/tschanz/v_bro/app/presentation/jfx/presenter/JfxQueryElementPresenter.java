package com.tschanz.v_bro.app.presentation.jfx.presenter;

import com.tschanz.v_bro.app.usecase.query_element.QueryElementPresenter;
import com.tschanz.v_bro.app.usecase.query_element.responsemodel.QueryElementResponse;
import javafx.application.Platform;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JfxQueryElementPresenter implements QueryElementPresenter {
    private final QueryElementPresenter innerPresenter;


    @Override
    public void present(@NonNull QueryElementResponse response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                innerPresenter.present(response);
            }
        });
    }
}
