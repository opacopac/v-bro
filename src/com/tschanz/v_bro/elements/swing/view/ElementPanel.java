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
    public void setElementClassList(BehaviorSubject<List<ElementClassItem>> elementClassList) {
        this.elementClassSelectionPanel.setElementClassList(elementClassList);
    }


    @Override
    public void setDenominationList(BehaviorSubject<List<DenominationItem>> denominationList) {
        this.denominationSelectionPanel.setDenominationList(denominationList);
    }


    @Override
    public void setElementList(BehaviorSubject<List<ElementItem>> elementList) {
        this.elementSelectionPanel.setElementListPublisher(elementList);
    }


    @Override
    public void setSelectedElementClass(BehaviorSubject<ElementClassItem> selectedElementClass) {
        this.elementClassSelectionPanel.setSelectedElementClass(selectedElementClass);
    }


    @Override
    public void setSelectedDenominations(BehaviorSubject<List<DenominationItem>> selectedDenominations) {
        this.denominationSelectionPanel.setSelectedDenominations(selectedDenominations);
    }


    @Override
    public void setSelectedElement(BehaviorSubject<ElementItem> selectedElement) {
        this.elementSelectionPanel.setSelectedElement(selectedElement);
    }


    @Override
    public void setSelectedVersionFilter(BehaviorSubject<VersionFilter> selectedVersionFilter) {
        this.versionFilterPanel.setSelectedVersionFilter(selectedVersionFilter);
    }
}
