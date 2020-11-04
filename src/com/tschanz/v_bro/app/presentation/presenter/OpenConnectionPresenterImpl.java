package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;
import com.tschanz.v_bro.app.presentation.viewmodel.JdbcRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MockRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.XmlRepoConnectionItem;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementClassResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionPresenter;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.JdbcConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.OpenConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.RepoConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.XmlConnectionResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class OpenConnectionPresenterImpl implements OpenConnectionPresenter {
    private final MainModel mainModel;


    public OpenConnectionPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }



    @Override
    public void present(OpenConnectionResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.currentRepoConnection.next(this.getConnectionItem(response.repoConnection));
            this.mainModel.elementClasses.next(this.getElementClasses(response.elementClasses));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }


    private RepoConnectionItem getConnectionItem(RepoConnectionResponse repoConnection) {
        if (repoConnection == null) {
            return null;
        }  else {
            switch (repoConnection.repoType) {
                case JDBC:
                    JdbcConnectionResponse jdbcConnection = (JdbcConnectionResponse) repoConnection;
                    return new JdbcRepoConnectionItem(jdbcConnection.url, jdbcConnection.user, jdbcConnection.password);
                case XML:
                    XmlConnectionResponse xmlConnection = (XmlConnectionResponse) repoConnection;
                    return new XmlRepoConnectionItem(xmlConnection.filename);
                case MOCK:
                default:
                    return new MockRepoConnectionItem();
            }
        }
    }


    private List<ElementClassItem> getElementClasses(List<ElementClassResponse> elementClasses) {
        return elementClasses
            .stream()
            .map(elementClass -> new ElementClassItem(elementClass.name))
            .collect(Collectors.toList());
    }
}
