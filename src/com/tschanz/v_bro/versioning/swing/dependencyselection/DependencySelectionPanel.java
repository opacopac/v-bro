package com.tschanz.v_bro.versioning.swing.dependencyselection;

import com.tschanz.v_bro.elements.swing.model.ElementTableItem;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;


public class DependencySelectionPanel extends JPanel implements DependencySelectionView {
    private final JRadioButton fwdDependencyRadio = new JRadioButton("FW");
    private final JRadioButton bwdDependencyRadio = new JRadioButton("BW");
    private final JComboBox<ElementTableItem> elementTablesList = new JComboBox<>();


    public DependencySelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Dependencies:"));
        this.add(this.fwdDependencyRadio);
        this.add(this.bwdDependencyRadio);
        this.add(this.elementTablesList);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.fwdDependencyRadio);
        buttonGroup.add(this.bwdDependencyRadio);
    }


    @Override
    public void addSelectFwDependencyListener(ChangeListener listener) {
        this.fwdDependencyRadio.addChangeListener(listener);
    }


    @Override
    public void addSelectBwDependencyListener(ChangeListener listener) {
        this.bwdDependencyRadio.addChangeListener(listener);
    }


    @Override
    public void addSelectElementTableListener(ActionListener listener) {
        this.elementTablesList.addActionListener(listener);
    }


    @Override
    public ElementTableItem getSelectedElementTable() {
        return (ElementTableItem) this.elementTablesList.getSelectedItem();
    }


    @Override
    public void setElementTableNames(List<ElementTableItem> elementTableItems) {
        this.elementTablesList.removeAllItems();
        elementTableItems.forEach(this.elementTablesList::addItem);
    }
}
