package com.tschanz.v_bro.elements.swing.element_selection;

import com.tschanz.v_bro.elements.swing.model.ElementItem;

import java.awt.event.ActionListener;
import java.util.List;


public interface ElementSelectionView {
    void addSelectElementListener(ActionListener listener);

    ElementItem getSelectedElement();

    void updateElementList(List<ElementItem> elements);
}
