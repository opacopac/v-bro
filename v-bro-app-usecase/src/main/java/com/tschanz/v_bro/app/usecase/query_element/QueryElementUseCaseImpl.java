package com.tschanz.v_bro.app.usecase.query_element;

import com.tschanz.v_bro.app.usecase.common.converter.ElementConverter;
import com.tschanz.v_bro.app.usecase.query_element.requestmodel.QueryElementRequest;
import com.tschanz.v_bro.app.usecase.query_element.responsemodel.QueryElementResponse;
import com.tschanz.v_bro.data_structure.domain.service.*;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class QueryElementUseCaseImpl implements QueryElementUseCase {
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final QueryElementPresenter presenter;


    @Override
    public void execute(QueryElementRequest request) {
        log.info("UC: select element class '" + request.elementQuery + "'...");

        try {
            // elements
            var elementService = this.elementServiceProvider.getService(request.repoType);
            var elements = elementService.readElements(request.elementClass, request.selectedDenominations, request.elementQuery, 100); // TODO
            var selectElementId = elements.size() > 0 ? elements.get(0).getId() : null;

            var message = String.format("successfully read %d elements", elements.size());
            log.info(message);

            var response = new QueryElementResponse(
                ElementConverter.toResponse(elements),
                request.requestTimestamp,
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            var message = "error reading elements: " + exception.getMessage();
            log.severe(message);

            var response = new QueryElementResponse(null, 0, message, true);
            this.presenter.present(response);
        }
    }
}
