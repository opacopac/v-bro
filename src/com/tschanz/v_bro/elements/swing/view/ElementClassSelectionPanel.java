package com.tschanz.v_bro.elements.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


public class ElementClassSelectionPanel extends JPanel {
    private final JComboBox<ElementClassItem> elementClassesComboBox = new JComboBox<>();
    private BehaviorSubject<ElementClassItem> selectedElementClass;


    public ElementClassSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.elementClassesComboBox);
        this.elementClassesComboBox.addActionListener(this::onElementClassSelected);
    }


    public void setElementClassList(BehaviorSubject<List<ElementClassItem>> elementClassList) {
        elementClassList.subscribe(new GenericSubscriber<>(this::updateElementClassList));
    }


    public void setSelectedElementClass(BehaviorSubject<ElementClassItem> selectedElementClass) {
        this.selectedElementClass = selectedElementClass;
    }


    private void updateElementClassList(List<ElementClassItem> elementClassList) {
        this.elementClassesComboBox.removeAllItems();
        elementClassList.forEach(this.elementClassesComboBox::addItem);
    }


    private void onElementClassSelected(ActionEvent e) {
        this.selectedElementClass.next(
            (ElementClassItem) this.elementClassesComboBox.getSelectedItem()
        );
    }
}
