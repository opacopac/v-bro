package com.tschanz.v_bro.app.usecase.read_dependency_element_classes;

import com.tschanz.v_bro.app.presenter.dependency_element_class.DependencyElementClassPresenter;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.service.DependencyStructureService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class ReadDependencyElementClassesUseCaseImpl implements ReadDependencyElementClassesUseCase {
    private final AppState appState;
    private final RepoServiceProvider<DependencyStructureService> dependencyStructureServiceProvider;
    private final StatusPresenter statusPresenter;
    private final DependencyElementClassPresenter dependencyElementClassPresenter;


    @Override
    public void execute(ReadDependencyElementClassesRequest request) {
        var repoType = appState.getCurrentRepoType();
        var elementClass = appState.getCurrentElementClass();
        var isFwd = appState.isFwdDependencies();
        var fwdBwdText = isFwd ? "FWD" : "BWD";

        if (repoType != null && elementClass != null) {
            try {
                var msgStart = String.format("UC: reading %s dependency element classes for element class '%s'...", fwdBwdText, elementClass.getName());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                var dependencyStructureService = this.dependencyStructureServiceProvider.getService(repoType);
                var dependencyElementClasses = isFwd
                    ? dependencyStructureService.readFwdDependencies(elementClass)
                    : dependencyStructureService.readBwdDependencies(elementClass);

                var msgSuccess = String.format("successfully read %d %s dependency element classes", dependencyElementClasses.size(), fwdBwdText);
                log.info(msgSuccess);
                var statusResponse2 = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse2);

                var elementClassList = new SelectedList<>(dependencyElementClasses, null);
                this.appState.setDependencyElementClasses(elementClassList);

                var response = ElementClassResponse.fromDomain(elementClassList);
                this.dependencyElementClassPresenter.present(response);
            } catch (RepoException exception) {
                var message = String.format("error reading %s dependency element classes: %s", fwdBwdText, exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC clearing dependency element classes");

            this.appState.setDependencyElementClasses(SelectedList.createEmpty());

            var response = ElementClassResponse.fromDomain(SelectedList.createEmpty());
            this.dependencyElementClassPresenter.present(response);
        }
    }
}
