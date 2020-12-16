package com.tschanz.v_bro.app.usecase.read_denominations;

import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;


@Log
@RequiredArgsConstructor
public class ReadDenominationUseCaseImpl implements ReadDenominationUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final DenominationsPresenter denominationsPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDenominationRequest request) {
        var repoType = mainState.getRepoState().getRepoType();
        var elementClass = mainState.getElementClassState().getSelectedName();

        if (repoType != null && elementClass != null) {
            try {
                log.info(String.format("UC: reading denominations for element class '%s'...", elementClass));

                var elementClassService = this.elementClassServiceProvider.getService(repoType);
                var denominations = elementClassService.readDenominations(elementClass);

                var mslist = new MultiSelectedList<>(denominations, Collections.emptyList());
                this.mainState.getDenominationState().setDenominations(mslist);

                var message = String.format("successfully read %d denominations.", denominations.size());
                log.info(message);
                var statusResponse = new StatusResponse(message, false);
                this.statusPresenter.present(statusResponse);

                var denominationResponse = DenominationListResponse.fromDomain(mslist);
                this.denominationsPresenter.present(denominationResponse);
            } catch (RepoException exception) {
                var message = String.format("error reading denominations: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true);
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
