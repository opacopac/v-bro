package com.tschanz.v_bro.app.usecase.query_elements;

import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
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
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var elementClass = mainState.getElementClassState().getCurrentElementClass();
        var query = Objects.requireNonNull(request.getQuery());
        var selectedDenominationFields = this.mainState.getDenominationState().getCurrentDenominations();

        if (repoType != null && elementClass != null) {
            try {
                log.info(String.format("UC: query elements of class '%s' for text '%s'...", elementClass, query));

                var elementService = this.elementServiceProvider.getService(repoType);
                var elements = elementService.queryElements(elementClass, selectedDenominationFields, query, MAX_RESULTS);

                this.mainState.getElementState().setQueryResult(elements);

                var message = String.format("found %d elements", elements.size());
                log.info(message);

                return QueryElementsResponse.fromDomain(elements);
            } catch (RepoException exception) {
                var message = String.format("error querying elements: %s", exception.getMessage());
                log.severe(message);

                this.mainState.getElementState().setQueryResult(Collections.emptyList());

                return new QueryElementsResponse(Collections.emptyList());
            }
        } else {
            log.info("UC: clearing query results");

            this.mainState.getElementState().setQueryResult(Collections.emptyList());

            return new QueryElementsResponse(Collections.emptyList());
        }
    }
}
