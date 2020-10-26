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
    private BehaviorSubject<ElementClassItem> selectElementClassAction;


    public ElementClassSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.elementClassesComboBox);
        this.elementClassesComboBox.addActionListener(this::onElementClassSelected);
    }


    public void bindElementClassList(BehaviorSubject<List<ElementClassItem>> elementClassList) {
        elementClassList.subscribe(new GenericSubscriber<>(this::onElementClassListChanged));
    }


    public void bindSelectElementClassAction(BehaviorSubject<ElementClassItem> selectElementClassAction) {
        this.selectElementClassAction = selectElementClassAction;
    }


    private void onElementClassListChanged(List<ElementClassItem> elementClassList) {
        this.elementClassesComboBox.removeAllItems();
        elementClassList.forEach(this.elementClassesComboBox::addItem);
    }


    private void onElementClassSelected(ActionEvent e) {
        this.selectElementClassAction.next(
            (ElementClassItem) this.elementClassesComboBox.getSelectedItem()
        );
    }
}
