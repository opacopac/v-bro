package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
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
    private BehaviorSubject<ElementItem> selectElementAction;


    public ElementSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.elementsList);
        this.elementsList.addActionListener(this::onElementSelected);
    }


    @Override
    public void bindElementList(Flow.Publisher<List<ElementItem>> elementList) {
        elementList.subscribe(new GenericSubscriber<>(this::onElementListChanged));
    }


    @Override
    public void bindSelectElementAction(BehaviorSubject<ElementItem> selectElementAction) {
        this.selectElementAction = selectElementAction;
    }


    private void onElementListChanged(List<ElementItem> elementItems) {
        this.elementsList.removeAllItems();
        elementItems.forEach(this.elementsList::addItem);

        this.repaint();
        this.revalidate();
    }


    private void onElementSelected(ActionEvent e) {
        this.selectElementAction.next(
            (ElementItem) this.elementsList.getSelectedItem()
        );
    }
}
