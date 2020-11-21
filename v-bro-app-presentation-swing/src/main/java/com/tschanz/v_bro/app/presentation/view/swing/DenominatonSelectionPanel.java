package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.MultiSelectedItemList;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDenominationsAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.view.ElementDenominationView;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;


public class DenominatonSelectionPanel extends JPanel implements ElementDenominationView {
    private final Map<JCheckBox, DenominationItem> checkBoxDenominationMap = new HashMap<>();
    private SelectDenominationsAction selectDenominationsAction;
    private boolean isPopulating = false;


    public DenominatonSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<MultiSelectedItemList<DenominationItem>> denominationsList,
        SelectDenominationsAction selectDenominationsAction
    ) {
        this.selectDenominationsAction = selectDenominationsAction;
        denominationsList.subscribe(new GenericSubscriber<>(this::onDenominationsListChanged));
    }


    private void onDenominationsListChanged(MultiSelectedItemList<DenominationItem> denominationItems) {
        if (denominationItems == null) {
            throw new IllegalArgumentException("denominationItems");
        }

        this.isPopulating = true;
        this.removeAll();
        this.checkBoxDenominationMap.clear();

        int denominationCount = 0;
        for (DenominationItem item : denominationItems.getItems()) {
            if (denominationCount < DenominationItem.MAX_DENOMINATIONS) {
                denominationCount++;
            } else {
                break; // TODO: more denominations...
            }

            JCheckBox checkBox = new JCheckBox(item.getName());
            checkBox.addActionListener(this::onDenominationSelected);
            this.add(checkBox);
            this.checkBoxDenominationMap.put(checkBox, item);

            if (denominationItems.getSelectedItems().contains(item)) {
                checkBox.setSelected(true);
            }
        }

        this.isPopulating = false;

        this.repaint();
        this.revalidate();
    }


    private void onDenominationSelected(ActionEvent e) {
        if (!this.isPopulating && this.selectDenominationsAction != null) {
            this.selectDenominationsAction.next(
                this.checkBoxDenominationMap.keySet()
                    .stream()
                    .filter(AbstractButton::isSelected)
                    .map(this.checkBoxDenominationMap::get)
                    .collect(Collectors.toList())
            );
        }
    }
}
