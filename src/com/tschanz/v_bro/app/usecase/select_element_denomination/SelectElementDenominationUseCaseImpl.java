package com.tschanz.v_bro.app.usecase.select_element_denomination;

import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel.SelectElementDenominationRequest;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import com.tschanz.v_bro.elements.domain.model.DenominationData;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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
                this.getElementResponse(elements),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading element denominations and elements: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementDenominationResponse response = new SelectElementDenominationResponse(null, message, true);
            this.presenter.present(response);
        }
    }


    private List<ElementResponse> getElementResponse(List<ElementData> elements) {
        return elements
            .stream()
            .map(element -> new ElementResponse(element.getId(), this.getElementName(element)))
            .sorted(Comparator.comparing(e -> e.name))
            .collect(Collectors.toList());
    }


    private String getElementName(ElementData element) {
        if (element.getNameFieldValues().size() == 0) {
            return element.getId();
        } else {
            return element.getNameFieldValues()
                .stream()
                .map(DenominationData::getValue)
                .collect(Collectors.joining(" - "));
        }
    }
}

