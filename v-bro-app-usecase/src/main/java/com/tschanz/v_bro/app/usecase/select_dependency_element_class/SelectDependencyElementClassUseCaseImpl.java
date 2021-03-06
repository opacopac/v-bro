package com.tschanz.v_bro.app.usecase.select_dependency_element_class;

import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import com.tschanz.v_bro.app.usecase.read_dependency_denominations.ReadDependencyDenominationsRequest;
import com.tschanz.v_bro.app.usecase.read_dependency_denominations.ReadDependencyDenominationsUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectDependencyElementClassUseCaseImpl implements SelectDependencyElementClassUseCase {
    private final AppState appState;
    private final ReadDependencyDenominationsUseCase readDependencyDenominationsUc;
    private final ReadDependenciesUseCase readDependenciesUc;


    @Override
    public void execute(SelectDependencyElementClassRequest request) {
        var repoType = this.appState.getCurrentRepoType();
        var elementClassName = request.getElementClassName();
        var oldElementClassList = this.appState.getDependencyElementClasses();

        if (repoType != null && elementClassName != null) {
            var msgStart = String.format("UC: selecting dependency element class '%s'...", elementClassName);
            log.info(msgStart);

            var selectedElementClass = oldElementClassList.getItems()
                .stream()
                .filter(ec -> ec.getName().equals(elementClassName))
                .findFirst()
                .orElse(null);
            var newElementClassList = new SelectedList<>(oldElementClassList.getItems(), selectedElementClass);
            this.appState.setDependencyElementClasses(newElementClassList);
        } else {
            log.info("UC clearing selected dependency element class");

            var newElementClasses = new SelectedList<>(oldElementClassList.getItems(), null);
            this.appState.setDependencyElementClasses(newElementClasses);
        }

        var readDependencyDenominationsRequest = new ReadDependencyDenominationsRequest();
        this.readDependencyDenominationsUc.execute(readDependencyDenominationsRequest);

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }
}
