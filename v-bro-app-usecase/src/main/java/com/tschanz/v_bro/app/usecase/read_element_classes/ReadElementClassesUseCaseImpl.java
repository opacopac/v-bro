package com.tschanz.v_bro.app.usecase.read_element_classes;

import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListPresenter;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class ReadElementClassesUseCaseImpl implements ReadElementClassesUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final StatusPresenter statusPresenter;
    private final ElementClassListPresenter elementClassListPresenter;


    @Override
    public void execute(ReadElementClassesRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();

        if (repoType != null) {
            try {
                var msgStart =  "UC: reading element classes...";
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                var elementClassService = this.elementClassServiceProvider.getService(repoType);
                var elementClasses = elementClassService.readAllElementClasses();
                var slist = new SelectedList<>(elementClasses, null);

                this.mainState.getElementClassState().setElementClasses(slist);

                var msgSuccess = String.format("successfully read %d element classes.", elementClasses.size());
                log.info(msgSuccess);
                var statusResponse2 = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse2);
            } catch (RepoException exception) {
                var message = String.format("error reading version timeline: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing element classes");

            this.mainState.getElementClassState().setElementClasses(SelectedList.createEmpty());
        }

        var elementClasses = this.mainState.getElementClassState().getElementClasses();
        var elementClassListResponse = ElementClassListResponse.fromDomain(elementClasses);
        this.elementClassListPresenter.present(elementClassListResponse);
    }
}
