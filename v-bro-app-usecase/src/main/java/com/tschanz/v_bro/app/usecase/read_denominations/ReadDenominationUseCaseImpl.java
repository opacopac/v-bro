package com.tschanz.v_bro.app.usecase.read_denominations;

import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.service.DenominationService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;


@Log
@RequiredArgsConstructor
public class ReadDenominationUseCaseImpl implements ReadDenominationUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<DenominationService> denominationServiceProvider;
    private final DenominationsPresenter denominationsPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDenominationRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var elementClass = mainState.getElementClassState().getCurrentElementClass();

        if (repoType != null && elementClass != null) {
            try {
                var msgStart =  String.format("UC: reading denominations for element class '%s'...", elementClass.getName());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                var denominationService = this.denominationServiceProvider.getService(repoType);
                var denominations = denominationService.readDenominations(elementClass);

                var mslist = new MultiSelectedList<>(denominations, Collections.emptyList());
                this.mainState.getDenominationState().setDenominations(mslist);

                var msgSuccess = String.format("successfully read %d denominations.", denominations.size());
                log.info(msgSuccess);
                var statusResponse = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse);

                var denominationResponse = DenominationListResponse.fromDomain(mslist);
                this.denominationsPresenter.present(denominationResponse);
            } catch (RepoException exception) {
                var message = String.format("error reading denominations: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing denominations");

            MultiSelectedList<Denomination> mslist = MultiSelectedList.createEmpty();
            this.mainState.getDenominationState().setDenominations(mslist);

            var denominationListResponse = DenominationListResponse.fromDomain(mslist);
            this.denominationsPresenter.present(denominationListResponse);
        }
    }
}
