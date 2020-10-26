package com.tschanz.v_bro.elements.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.elements.swing.model.DenominationItem;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.elements.swing.model.ElementItem;
import com.tschanz.v_bro.elements.swing.model.VersionFilter;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ElementPanel extends JPanel implements ElementView {
    private final ElementClassSelectionPanel elementClassSelectionPanel = new ElementClassSelectionPanel();
    private final DenominatonSelectionPanel denominationSelectionPanel = new DenominatonSelectionPanel();
    private final ElementSelectionPanel elementSelectionPanel = new ElementSelectionPanel();
    private final VersionFilterPanel versionFilterPanel = new VersionFilterPanel();


    public ElementPanel() {
        // first row
        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEADING));
        row1.add(new JLabel("Element Classes"));
        row1.add(this.elementClassSelectionPanel);
        row1.add(this.denominationSelectionPanel);

        // second row
        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.LEADING));
        row2.add(new JLabel("Element"));
        row2.add(this.elementSelectionPanel);
        row2.add(this.versionFilterPanel);

        // container
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(row1);
        this.add(row2);
    }


    @Override
    public void bindElementClassList(BehaviorSubject<List<ElementClassItem>> elementClassList) {
        this.elementClassSelectionPanel.bindElementClassList(elementClassList);
    }


    @Override
    public void bindDenominationList(BehaviorSubject<List<DenominationItem>> denominationList) {
        this.denominationSelectionPanel.bindDenominationList(denominationList);
    }


    @Override
    public void bindElementList(BehaviorSubject<List<ElementItem>> elementList) {
        this.elementSelectionPanel.bindElementList(elementList);
    }


    @Override
    public void bindSelectElementClassAction(BehaviorSubject<ElementClassItem> selectElementClassAction) {
        this.elementClassSelectionPanel.bindSelectElementClassAction(selectElementClassAction);
    }


    @Override
    public void bindSelectDenominationsAction(BehaviorSubject<List<DenominationItem>> selectDenominationsAction) {
        this.denominationSelectionPanel.setSelectDenominationsAction(selectDenominationsAction);
    }


    @Override
    public void bindSelectElementAction(BehaviorSubject<ElementItem> selectElementAction) {
        this.elementSelectionPanel.setSelectElementAction(selectElementAction);
    }


    @Override
    public void bindVersionFilter(BehaviorSubject<VersionFilter> versionFilter) {
        this.versionFilterPanel.bindVersionFilter(versionFilter);
    }
}
