package com.tschanz.v_bro.app.usecase.select_dependency_denominations;

import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequest;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class SelectDependencyDenominationsUseCaseImpl implements SelectDependencyDenominationsUseCase {
    private final AppState appState;
    private final ReadDependenciesUseCase readDependenciesUc;


    @Override
    public void execute(SelectDenominationsRequest request) {
        var selectedDenominationsReq = Objects.requireNonNull(request.toDomain());
        var selectedDenominationNames = selectedDenominationsReq.stream().map(Denomination::getName).collect(Collectors.toList());

        log.info(String.format("UC: selecting dependency denomination(s) '%s'...", String.join("', '", selectedDenominationNames)));

        var oldDenominations = this.appState.getDependencyDenominations();
        var selectedDenominations = oldDenominations.getItems()
            .stream()
            .filter(selectedDenominationsReq::contains)
            .collect(Collectors.toList());

        if (selectedDenominations.size() == 0 && oldDenominations.getItems().size() > 0) {
            selectedDenominations = List.of(oldDenominations.getItems().get(0));
        }

        var dependencyElementClass = this.appState.getDependencyElementClasses().getSelectedItem();
        var newDenominations = new MultiSelectedList<>(oldDenominations.getItems(), selectedDenominations);
        this.appState.setDependencyDenominations(newDenominations);
        if (dependencyElementClass != null) {
            this.appState.getLastSelectedDenominations().put(dependencyElementClass.getName(), selectedDenominations);
        }

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }
}
