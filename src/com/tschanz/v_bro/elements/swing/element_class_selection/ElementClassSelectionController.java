package com.tschanz.v_bro.elements.swing.element_class_selection;

import com.tschanz.v_bro.elements.swing.element_selection.ElementSelectionView;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.NameFieldItem;
import com.tschanz.v_bro.elements.usecase.read_element_namefields.ReadElementNameFieldsResponse;
import com.tschanz.v_bro.elements.usecase.read_element_namefields.ReadElementNameFieldsUseCase;
import com.tschanz.v_bro.repo.swing.connection.ConnectionController;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsResponse;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;


public class ElementClassSelectionController {
    private final ElementClassSelectionView elementClassSelectionView;
    private final ElementSelectionView elementSelectionView;
    private final StatusBarView statusBarView;
    private final ReadElementNameFieldsUseCase readElementNameFieldsUc;
    private final ReadElementsUseCase readElementsUc;
    private final ConnectionController connectionController;


    public ElementClassSelectionController(
        ElementClassSelectionView elementClassSelectionView,
        ElementSelectionView elementSelectionView,
        StatusBarView statusBarView,
        ReadElementNameFieldsUseCase readElementNameFieldsUc,
        ReadElementsUseCase readElementsUc,
        ConnectionController connectionController
    ) {
        this.elementClassSelectionView = elementClassSelectionView;
        this.elementSelectionView = elementSelectionView;
        this.statusBarView = statusBarView;
        this.readElementNameFieldsUc = readElementNameFieldsUc;
        this.readElementsUc = readElementsUc;
        this.connectionController = connectionController;

        this.elementClassSelectionView.addSelectElementClassListener(this::onElementClassSelected);
        this.elementClassSelectionView.addSelectElementFieldNameListener(this::onFieldNameSelected);
    }


    private void onElementClassSelected(ActionEvent e) {
        try {
            this.readFieldNames();
            this.readElements();
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }
    }


    private void onFieldNameSelected(ActionEvent e) {
        try {
            this.readElements();
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }
    }


    private void readElements() throws VBroAppException {
        ElementClassItem selectedElementClass = this.elementClassSelectionView.getSelectedElementClass();
        if (selectedElementClass == null) {
            return;
        }

        ReadElementsResponse readElementsResponse = this.readElementsUc.readElements(
            this.connectionController.getCurrentConnection(),
            selectedElementClass.getName(),
            this.elementClassSelectionView.getSelectedFieldNames()
            .stream()
            .map(NameFieldItem::getName)
            .collect(Collectors.toList())
        );

        List<ElementItem> elementItems = readElementsResponse.elements
            .stream()
            .map(element -> new ElementItem(element.id, element.name))
            .collect(Collectors.toList());
        this.elementSelectionView.updateElementList(elementItems);
    }


    private void readFieldNames() throws VBroAppException {
        ElementClassItem selectedElementClass = this.elementClassSelectionView.getSelectedElementClass();
        if (selectedElementClass == null) {
            return;
        }

        ReadElementNameFieldsResponse readElementNameFieldsResponse = this.readElementNameFieldsUc.readNameFields(
            this.connectionController.getCurrentConnection(),
            selectedElementClass.getName()
        );
        List<NameFieldItem> nameFieldItems = readElementNameFieldsResponse.nameFields
            .stream()
            .map(nameField -> new NameFieldItem(nameField.name))
            .collect(Collectors.toList());
        this.elementClassSelectionView.updateNameFieldsList(nameFieldItems);
    }
}
