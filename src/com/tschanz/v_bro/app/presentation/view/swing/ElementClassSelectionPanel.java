package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.view.ElementClassView;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementClassItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.Flow;


public class ElementClassSelectionPanel extends JPanel implements ElementClassView {
    private final JComboBox<ElementClassItem> elementClassesComboBox = new JComboBox<>();
    private SelectElementClassAction selectElementClassAction;
    private boolean isPopulating = false;


    public ElementClassSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.elementClassesComboBox);
        this.elementClassesComboBox.addActionListener(this::onElementClassSelected);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<List<ElementClassItem>> elementClassList,
        SelectElementClassAction selectElementClassAction
    ) {
        this.selectElementClassAction = selectElementClassAction;
        elementClassList.subscribe(new GenericSubscriber<>(this::onElementClassListChanged));
    }


    private void onElementClassListChanged(List<ElementClassItem> elementClassList) {
        this.isPopulating = true;
        this.elementClassesComboBox.removeAllItems();
        elementClassList.forEach(this.elementClassesComboBox::addItem);
        this.isPopulating = false;

        this.repaint();
        this.revalidate();
    }


    private void onElementClassSelected(ActionEvent e) {
        ElementClassItem selectedItem = (ElementClassItem) this.elementClassesComboBox.getSelectedItem();

        if (!isPopulating && selectedItem != null) {
            this.selectElementClassAction.next(selectedItem.getName());
        }
    }
}
