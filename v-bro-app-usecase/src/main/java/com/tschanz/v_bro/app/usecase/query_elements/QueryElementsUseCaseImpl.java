package com.tschanz.v_bro.app.usecase.query_elements;

import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.Objects;


@Log
@RequiredArgsConstructor
public class QueryElementsUseCaseImpl implements QueryElementsUseCase {
    private final static int MAX_RESULTS = 100;
    private final MainState mainState;
    private final RepoServiceProvider<ElementService> elementServiceProvider;


    @Override
    public QueryElementsResponse execute(QueryElementsRequest request) {
        var requestTimestamp = System.currentTimeMillis();
        var repoType = Objects.requireNonNull(mainState.getRepoState().getConnectionParameters().getRepoType());
        var elementClassName = Objects.requireNonNull(mainState.getElementClassState().getSelectedName());
        var query = Objects.requireNonNull(request.getQuery());
        var selectedDenominationNames = this.mainState.getDenominationState().getSelectedNames();

        try {
            log.info(String.format("UC: query elements of class '%s' for text '%s'...", elementClassName, query));

            var elementService = this.elementServiceProvider.getService(repoType);
            var elements = elementService.readElements(elementClassName, selectedDenominationNames, query, MAX_RESULTS);

            var message = String.format("found %d elements", elements.size());
            log.info(message);

            return QueryElementsResponse.fromDomain(elements);
        } catch (RepoException exception) {
            var message = String.format("error querying elements: %s", exception.getMessage());
            log.severe(message);

            return new QueryElementsResponse(Collections.emptyList());
        }
    }
}
