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
    private BehaviorSubject<ElementItem> selectElementAction;


    public ElementSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Element"));
        this.add(this.elementsList);
        this.elementsList.addActionListener(this::onElementSelected);
    }


    public void bindElementList(BehaviorSubject<List<ElementItem>> elementList) {
        elementList.subscribe(new GenericSubscriber<>(this::onElementListChanged));
    }


    public void setSelectElementAction(BehaviorSubject<ElementItem> selectElementAction) {
        this.selectElementAction = selectElementAction;
    }


    private void onElementListChanged(List<ElementItem> elementItems) {
        this.elementsList.removeAllItems();
        elementItems.forEach(this.elementsList::addItem);
    }


    private void onElementSelected(ActionEvent e) {
        this.selectElementAction.next(
            (ElementItem) this.elementsList.getSelectedItem()
        );
    }
}
