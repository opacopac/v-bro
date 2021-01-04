package com.tschanz.v_bro.app.usecase.read_dependency_denominations;

import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.dependency_denominations.DependencyDenominationsPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.service.DenominationService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;


@Log
@RequiredArgsConstructor
public class ReadDependencyDenominationsUseCaseImpl implements ReadDependencyDenominationsUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<DenominationService> denominationServiceProvider;
    private final DependencyDenominationsPresenter denominationsPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDependencyDenominationsRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var elementClass = mainState.getDependencyState().getDependencyElementClasses().getSelectedItem();

        if (repoType != null && elementClass != null) {
            try {
                var msgStart =  String.format("UC: reading dependency denominations for element class '%s'...", elementClass.getName());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                var denominationService = this.denominationServiceProvider.getService(repoType);
                var denominations = denominationService.readDenominations(elementClass);

                var mslist = new MultiSelectedList<>(denominations, Collections.emptyList());
                this.mainState.getDependencyState().setDependencyDenominations(mslist);

                var msgSuccess = String.format("successfully read %d dependency denominations.", denominations.size());
                log.info(msgSuccess);
                var statusResponse = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse);

                var denominationResponse = DenominationListResponse.fromDomain(mslist);
                this.denominationsPresenter.present(denominationResponse);
            } catch (RepoException exception) {
                var message = String.format("error reading dependency denominations: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing dependency denominations");

            MultiSelectedList<Denomination> mslist = MultiSelectedList.createEmpty();
            this.mainState.getDependencyState().setDependencyDenominations(mslist);

            var denominationListResponse = DenominationListResponse.fromDomain(mslist);
            this.denominationsPresenter.present(denominationListResponse);
        }
    }
}
