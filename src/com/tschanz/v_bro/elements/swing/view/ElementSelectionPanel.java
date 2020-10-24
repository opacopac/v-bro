package com.tschanz.v_bro.elements.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;
import com.tschanz.v_bro.elements.swing.model.ElementItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


public class ElementSelectionPanel extends JPanel {
    private final JComboBox<ElementItem> elementsList = new JComboBox<>();
    private BehaviorSubject<ElementItem> selectedElement;


    public ElementSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Element"));
        this.add(this.elementsList);
        this.elementsList.addActionListener(this::onElementSelected);
    }


    public void setElementListPublisher(BehaviorSubject<List<ElementItem>> elementListPublisher) {
        elementListPublisher.subscribe(new GenericSubscriber<>(this::updateElementList));
    }


    public void setSelectedElement(BehaviorSubject<ElementItem> selectedElement) {
        this.selectedElement = selectedElement;
    }


    private void updateElementList(List<ElementItem> elementItems) {
        this.elementsList.removeAllItems();
        elementItems.forEach(this.elementsList::addItem);
    }


    private void onElementSelected(ActionEvent e) {
        this.selectedElement.next(
            (ElementItem) this.elementsList.getSelectedItem()
        );
    }
}
