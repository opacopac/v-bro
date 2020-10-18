package com.tschanz.v_bro.elements.swing.element_class_selection;

import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.NameFieldItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class ElementClassSelectionPanel extends JPanel implements ElementClassSelectionView {
    private final JComboBox<ElementClassItem> elementClassesList = new JComboBox<>();
    private final FieldNameSelectionPanel fieldNameSelectionPanel = new FieldNameSelectionPanel();


    public ElementClassSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Element Classes"));
        this.add(this.elementClassesList);
        this.add(this.fieldNameSelectionPanel);
    }


    @Override
    public void addSelectElementClassListener(ActionListener listener) {
        this.elementClassesList.addActionListener(listener);
    }


    @Override
    public void addSelectElementFieldNameListener(ActionListener listener) {
        this.fieldNameSelectionPanel.addActionListener(listener);
    }


    @Override
    public ElementClassItem getSelectedElementClass() {
        return (ElementClassItem) this.elementClassesList.getSelectedItem();
    }


    @Override
    public List<NameFieldItem> getSelectedFieldNames() {
        return this.fieldNameSelectionPanel.getSelectedFieldNames();
    }


    @Override
    public void updateElementClassList(List<ElementClassItem> elementClassItems) {
        this.elementClassesList.removeAllItems();
        elementClassItems.forEach(this.elementClassesList::addItem);
    }


    @Override
    public void updateNameFieldsList(List<NameFieldItem> fieldNames) {
        this.fieldNameSelectionPanel.setFieldNameItems(fieldNames);
    }
}
