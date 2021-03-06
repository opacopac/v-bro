package com.tschanz.v_bro.app.usecase.select_dependency_element_filter;

import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectDependencyElementFilterUseCaseImpl implements SelectDependencyElementFilterUseCase {
    private final AppState appState;
    private final ReadDependenciesUseCase readDependenciesUc;


    @Override
    public void execute(SelectDependencyElementFilterRequest request) {
        var query = request.getQuery();
        log.info(String.format("UC: selecting dependency element query to '%s'...", query));

        this.appState.setDependencyElementQuery(query);

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }
}
