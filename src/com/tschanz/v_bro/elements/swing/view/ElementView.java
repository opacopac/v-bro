package com.tschanz.v_bro.elements.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.elements.swing.model.DenominationItem;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.VersionFilter;

import java.util.List;


public interface ElementView {
    void setElementClassList(BehaviorSubject<List<ElementClassItem>> elementClassList);

    void setDenominationList(BehaviorSubject<List<DenominationItem>> denominationList);

    void setElementList(BehaviorSubject<List<ElementItem>> elementList);

    void setSelectedElementClass(BehaviorSubject<ElementClassItem> selectedElementClass);

    void setSelectedDenominations(BehaviorSubject<List<DenominationItem>> selectedDenominations);

    void setSelectedElement(BehaviorSubject<ElementItem> selectedElement);

    void setSelectedVersionFilter(BehaviorSubject<VersionFilter> selectedVersionFilter);
}
