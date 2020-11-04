package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
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
    private BehaviorSubject<ElementClassItem> selectElementClassAction;


    public ElementClassSelectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.elementClassesComboBox);
        this.elementClassesComboBox.addActionListener(this::onElementClassSelected);
    }


    @Override
    public void bindElementClassList(Flow.Publisher<List<ElementClassItem>> elementClassList) {
        elementClassList.subscribe(new GenericSubscriber<>(this::onElementClassListChanged));
    }


    @Override
    public void bindSelectElementClassAction(BehaviorSubject<ElementClassItem> selectElementClassAction) {
        this.selectElementClassAction = selectElementClassAction;
    }


    private void onElementClassListChanged(List<ElementClassItem> elementClassList) {
        this.elementClassesComboBox.removeAllItems();
        elementClassList.forEach(this.elementClassesComboBox::addItem);

        this.repaint();
        this.revalidate();
    }


    private void onElementClassSelected(ActionEvent e) {
        this.selectElementClassAction.next(
            (ElementClassItem) this.elementClassesComboBox.getSelectedItem()
        );
    }
}
