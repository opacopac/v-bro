package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.view.ElementView;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.Flow;


public class ElementSelectionPanel extends JPanel implements ElementView {
    private final JComboBox<ElementItem> elementsList = new JComboBox<>();
    private boolean isPopulating = false;
    private SelectElementAction selectElementAction;


    public ElementSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.elementsList);
        this.elementsList.addActionListener(this::onElementSelected);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<List<ElementItem>> elementList,
        SelectElementAction selectElementAction
    ) {
        this.selectElementAction = selectElementAction;
        elementList.subscribe(new GenericSubscriber<>(this::onElementListChanged));
    }


    private void onElementListChanged(List<ElementItem> elementItems) {
        this.isPopulating = true;
        this.elementsList.removeAllItems();
        elementItems.forEach(this.elementsList::addItem);
        this.isPopulating = false;

        this.repaint();
        this.revalidate();
    }


    private void onElementSelected(ActionEvent e) {
        ElementItem selectedItem = (ElementItem) this.elementsList.getSelectedItem();

        if (!isPopulating && selectedItem != null) {
            this.selectElementAction.next(selectedItem.getId());
        }
    }
}
