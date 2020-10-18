package com.tschanz.v_bro.versioning.swing.dependencyselection;

import com.tschanz.v_bro.elements.swing.model.ElementTableItem;

import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.util.List;


public interface DependencySelectionView {
    void addSelectFwDependencyListener(ChangeListener listener);

    void addSelectBwDependencyListener(ChangeListener listener);

    void addSelectElementTableListener(ActionListener listener);

    ElementTableItem getSelectedElementTable();

    void setElementTableNames(List<ElementTableItem> elementTableItem);
}
