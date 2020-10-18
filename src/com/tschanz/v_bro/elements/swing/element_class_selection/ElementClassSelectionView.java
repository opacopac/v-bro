package com.tschanz.v_bro.elements.swing.element_class_selection;

import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.NameFieldItem;

import java.awt.event.ActionListener;
import java.util.List;


public interface ElementClassSelectionView {
    void addSelectElementClassListener(ActionListener listener);

    void addSelectElementFieldNameListener(ActionListener listener);

    ElementClassItem getSelectedElementClass();

    List<NameFieldItem> getSelectedFieldNames();

    void updateElementClassList(List<ElementClassItem> tableNames);

    void updateNameFieldsList(List<NameFieldItem> fieldNames);
}
