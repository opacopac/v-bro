package com.tschanz.v_bro.app.usecase.query_elements;

import com.tschanz.v_bro.app.presenter.element_list.ElementListPresenter;
import com.tschanz.v_bro.app.presenter.element_list.ElementListResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class QueryElementsUseCaseImpl implements QueryElementsUseCase {
    private final static int MAX_RESULTS = 100;
    private final MainState mainState;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final ElementListPresenter presenter;
    private final StatusPresenter statusPresenter;
    private final OpenElementUseCase openElementUc;


    @Override
    public void execute(QueryElementsRequest request) {
        var requestTimestamp = System.currentTimeMillis();
        var repoType = Objects.requireNonNull(mainState.getRepoState().getConnectionParameters().getRepoType());
        var elementClassName = Objects.requireNonNull(mainState.getElementClassState().getSelectedName());
        var query = Objects.requireNonNull(request.getQuery());
        var selectedDenominationNames = this.mainState.getDenominationState().getSelectedNames();

        try {
            log.info(String.format("UC: query elements of class '%s' for text '%s'...", elementClassName, query));

            var elementService = this.elementServiceProvider.getService(repoType);
            var elements = elementService.readElements(elementClassName, selectedDenominationNames, query, MAX_RESULTS);
            var slist = new SelectedList<>(elements, null);

            var success = this.mainState.getElementState().setElements(slist, requestTimestamp);
            if (!success) {
                log.info("superseded by a more recent request, discarding results");
                return;
            }

            var message = String.format("found %d elements", elements.size());
            log.info(message);
            var statusResponse = new StatusResponse(message, false);
            this.statusPresenter.present(statusResponse);

            var response = ElementListResponse.fromDomain(slist);
            this.presenter.present(response);

            if (request.isAutoOpenFirstElement() && slist.getItems().size() > 0) {
                var elementId = slist.getItems().get(0).getId();
                var openElementRequest = new OpenElementRequest(elementId);
                this.openElementUc.execute(openElementRequest);
            }
        } catch (RepoException exception) {
            var message = String.format("error querying elements: %s", exception.getMessage());
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }
    }
}
