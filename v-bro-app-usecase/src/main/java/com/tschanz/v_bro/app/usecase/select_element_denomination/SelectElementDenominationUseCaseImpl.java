package com.tschanz.v_bro.app.usecase.select_element_denomination;

import com.tschanz.v_bro.app.usecase.common.converter.ElementConverter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel.SelectElementDenominationRequest;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;

import java.util.List;
import java.util.logging.Logger;


public class SelectElementDenominationUseCaseImpl implements SelectElementDenominationUseCase {
    private final Logger logger = Logger.getLogger(SelectElementDenominationUseCaseImpl.class.getName());
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
        this.logger.info("UC: select element denominations...");

        try {
            ElementService elementService = this.elementServiceProvider.getService(request.repoType);
            List<ElementData> elements = elementService.readElements(request.elementClass, request.denominations);

            String message = "successfully read " + elements.size() + " elements";
            this.logger.info(message);

            SelectElementDenominationResponse response = new SelectElementDenominationResponse(
                ElementConverter.toResponse(elements),
                request.denominations,
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading element denominations and elements: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementDenominationResponse response = new SelectElementDenominationResponse(null, null, message, true);
            this.presenter.present(response);
        }
    }
}
