package com.tschanz.v_bro.app.usecase.select_element_class;

import com.tschanz.v_bro.app.usecase.common.converter.DenominationConverter;
import com.tschanz.v_bro.app.usecase.common.converter.ElementConverter;
import com.tschanz.v_bro.app.usecase.select_element_class.requestmodel.SelectElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_element_class.responsemodel.SelectElementClassResponse;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class SelectElementClassUseCaseImpl implements SelectElementClassUseCase {
    private final Logger logger = Logger.getLogger(SelectElementClassUseCaseImpl.class.getName());
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final SelectElementClassPresenter presenter;


    public SelectElementClassUseCaseImpl(
        RepoServiceProvider<ElementClassService> elementClassServiceProvider,
        RepoServiceProvider<ElementService> elementServiceProvider,
        SelectElementClassPresenter presenter
    ) {
        this.elementClassServiceProvider = elementClassServiceProvider;
        this.elementServiceProvider = elementServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectElementClassRequest request) {
        this.logger.info("UC: select element class '" + request.elementClass + "'...");

        try {
            // read element denominations
            ElementClassService elementClassService = this.elementClassServiceProvider.getService(request.repoType);
            List<Denomination> denominations = elementClassService.readDenominations(request.elementClass);

            // read elements
            ElementService elementService = this.elementServiceProvider.getService(request.repoType);
            List<ElementData> elements = elementService.readElements(request.elementClass, Collections.emptyList());

            String message = "successfully read " + denominations.size() + " denominations and " + elements.size() + " elements";
            this.logger.info(message);

            SelectElementClassResponse response = new SelectElementClassResponse(
                DenominationConverter.toResponse(denominations),
                ElementConverter.toResponse(elements),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading element denominations and elements: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementClassResponse response = new SelectElementClassResponse(null, null, message, true);
            this.presenter.present(response);
        }
    }
}
