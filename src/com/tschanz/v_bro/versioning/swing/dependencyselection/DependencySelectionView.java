package com.tschanz.v_bro.versioning.swing.dependencyselection;

import com.tschanz.v_bro.elements.swing.model.ElementClassItem;

import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.util.List;


public interface DependencySelectionView {
    void addSelectFwDependencyListener(ChangeListener listener);

    void addSelectBwDependencyListener(ChangeListener listener);

    void addSelectElementTableListener(ActionListener listener);

    ElementClassItem getSelectedElementTable();

    void setElementTableNames(List<ElementClassItem> elementClassItem);
}
