package com.tschanz.v_bro.elements.swing.elementselection;

import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.ElementTableItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class ElementSelectionPanel extends JPanel implements ElementSelectionView {
    private final JComboBox<ElementTableItem> elementTablesList = new JComboBox<>();
    private final JComboBox<ElementItem> elementsList = new JComboBox<>();


    public ElementSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Element Tables"));
        this.add(this.elementTablesList);
        this.add(new JLabel("Element"));
        this.add(this.elementsList);
    }


    @Override
    public void addSelectElementTableListener(ActionListener listener) {
        this.elementTablesList.addActionListener(listener);
    }


    @Override
    public void addSelectElementListener(ActionListener listener) {
        this.elementsList.addActionListener(listener);
    }


    @Override
    public ElementTableItem getSelectedElementTable() {
        return (ElementTableItem) this.elementTablesList.getSelectedItem();
    }


    @Override
    public ElementItem getSelectedElement() {
        return (ElementItem) this.elementsList.getSelectedItem();
    }


    @Override
    public void setElementTables(List<ElementTableItem> elementTableItems) {
        this.elementTablesList.removeAllItems();
        elementTableItems.forEach(this.elementTablesList::addItem);
    }


    @Override
    public void setElements(List<ElementItem> elementItems) {
        this.elementsList.removeAllItems();
        elementItems.forEach(this.elementsList::addItem);
    }
}
