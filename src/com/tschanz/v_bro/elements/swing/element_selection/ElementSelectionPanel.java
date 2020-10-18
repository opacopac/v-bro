package com.tschanz.v_bro.elements.swing.element_selection;

import com.tschanz.v_bro.elements.swing.model.ElementItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class ElementSelectionPanel extends JPanel implements ElementSelectionView {
    private final JComboBox<ElementItem> elementsList = new JComboBox<>();


    public ElementSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Element"));
        this.add(this.elementsList);
    }


    @Override
    public void addSelectElementListener(ActionListener listener) {
        this.elementsList.addActionListener(listener);
    }


    @Override
    public ElementItem getSelectedElement() {
        return (ElementItem) this.elementsList.getSelectedItem();
    }


    @Override
    public void updateElementList(List<ElementItem> elementItems) {
        this.elementsList.removeAllItems();
        elementItems.forEach(this.elementsList::addItem);
    }
}
