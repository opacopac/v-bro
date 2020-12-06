package com.tschanz.v_bro.app.usecase.select_element_denomination;

import com.tschanz.v_bro.app.usecase.common.converter.ElementConverter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel.SelectElementDenominationRequest;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.extern.java.Log;

import java.util.List;


@Log
public class SelectElementDenominationUseCaseImpl implements SelectElementDenominationUseCase {
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final SelectElementDenominationPresenter presenter;


    public SelectElementDenominationUseCaseImpl(
        RepoServiceProvider<ElementService> elementServiceProvider,
        SelectElementDenominationPresenter presenter
    ) {
        this.elementServiceProvider = elementServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectElementDenominationRequest request) {
        log.info("UC: select element denominations...");

        try {
            ElementService elementService = this.elementServiceProvider.getService(request.repoType);
            List<ElementData> elements = elementService.readElements(request.elementClass, request.denominations, "", 100); // TODO

            String message = "successfully read " + elements.size() + " elements";
            log.info(message);

            SelectElementDenominationResponse response = new SelectElementDenominationResponse(
                ElementConverter.toResponse(elements),
                request.denominations,
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading element denominations and elements: " + exception.getMessage();
            log.severe(message);

            SelectElementDenominationResponse response = new SelectElementDenominationResponse(null, null, message, true);
            this.presenter.present(response);
        }
    }
}
