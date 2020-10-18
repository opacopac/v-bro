package com.tschanz.v_bro.elements.swing.element_class_selection;

import com.tschanz.v_bro.elements.swing.model.NameFieldItem;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FieldNameSelectionPanel extends JPanel {
    private final Map<JCheckBox, NameFieldItem> checkBoxFieldNameMap = new HashMap<>();
    private List<NameFieldItem> nameFieldItems = new ArrayList<>();
    private ActionListener listener;


    public FieldNameSelectionPanel() {
    }


    public void addActionListener(ActionListener listener) {
        this.listener = listener;
    }


    public List<NameFieldItem> getSelectedFieldNames() {
        return this.checkBoxFieldNameMap.keySet()
            .stream()
            .filter(AbstractButton::isSelected)
            .map(this.checkBoxFieldNameMap::get)
            .collect(Collectors.toList());
    }


    public void setFieldNameItems(List<NameFieldItem> nameFieldItems) {
        if (nameFieldItems == null) {
            throw new IllegalArgumentException("fieldNameItems");
        }

        this.nameFieldItems = nameFieldItems;
        this.rebuildCheckboxList();
    }


    private void rebuildCheckboxList() {
        this.removeAll();
        this.checkBoxFieldNameMap.clear();

        for (NameFieldItem item : nameFieldItems) {
            JCheckBox checkBox = new JCheckBox(item.getName());
            checkBox.addActionListener(this.listener);
            this.add(checkBox);
            this.checkBoxFieldNameMap.put(checkBox, item);
        }

        this.repaint();
    }
}
