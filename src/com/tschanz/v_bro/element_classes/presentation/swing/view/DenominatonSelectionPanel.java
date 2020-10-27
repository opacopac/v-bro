package com.tschanz.v_bro.element_classes.presentation.swing.view;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.element_classes.presentation.swing.viewmodel.DenominationItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;


public class DenominatonSelectionPanel extends JPanel implements ElementDenominationView  {
    private final Map<JCheckBox, DenominationItem> checkBoxDenominationMap = new HashMap<>();
    private BehaviorSubject<List<DenominationItem>> selectDenominationsAction;


    public DenominatonSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
    }


    @Override
    public void bindDenominationList(Flow.Publisher<List<DenominationItem>> denominationsList) {
        denominationsList.subscribe(new GenericSubscriber<>(this::onDenominationsListChanged));
    }

    @Override
    public void bindSelectDenominationsAction(BehaviorSubject<List<DenominationItem>> selectDenominationsAction) {
        this.selectDenominationsAction = selectDenominationsAction;
    }


    private void onDenominationsListChanged(List<DenominationItem> denominationItems) {
        if (denominationItems == null) {
            throw new IllegalArgumentException("denominationItems");
        }

        // rebuild checkbox list
        this.removeAll();
        this.checkBoxDenominationMap.clear();

        for (DenominationItem item : denominationItems) {
            JCheckBox checkBox = new JCheckBox(item.getName());
            checkBox.addActionListener(this::onDenominationSelected);
            this.add(checkBox);
            this.checkBoxDenominationMap.put(checkBox, item);
        }

        this.repaint();
        this.revalidate();
    }


    private void onDenominationSelected(ActionEvent e) {
        this.selectDenominationsAction.next(
            this.checkBoxDenominationMap.keySet()
                .stream()
                .filter(AbstractButton::isSelected)
                .map(this.checkBoxDenominationMap::get)
                .collect(Collectors.toList())
        );
    }
}
