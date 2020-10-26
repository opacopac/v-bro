package com.tschanz.v_bro.elements.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.elements.swing.model.DenominationItem;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.VersionFilter;

import java.util.List;


public interface ElementView {
    void bindElementClassList(BehaviorSubject<List<ElementClassItem>> elementClassList);

    void bindDenominationList(BehaviorSubject<List<DenominationItem>> denominationList);

    void bindElementList(BehaviorSubject<List<ElementItem>> elementList);

    void bindSelectElementClassAction(BehaviorSubject<ElementClassItem> selectElementClassAction);

    void bindSelectDenominationsAction(BehaviorSubject<List<DenominationItem>> selectDenominationsAction);

    void bindSelectElementAction(BehaviorSubject<ElementItem> selectElementAction);

    void bindVersionFilter(BehaviorSubject<VersionFilter> versionFilter);
}
