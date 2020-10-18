package com.tschanz.v_bro.elements.swing.elementselection;

import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.ElementTableItem;

import java.awt.event.ActionListener;
import java.util.List;


public interface ElementSelectionView {
    void addSelectElementTableListener(ActionListener listener);

    void addSelectElementListener(ActionListener listener);

    ElementTableItem getSelectedElementTable();

    ElementItem getSelectedElement();

    void setElementTables(List<ElementTableItem> tableNames);

    void setElements(List<ElementItem> elements);
}
