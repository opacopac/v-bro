package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.view.DependencyFilterView;
import com.tschanz.v_bro.app.presentation.viewmodel.DependencyFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDependencyFilterAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Flow;


public class DependencyFilterPanel extends JPanel implements DependencyFilterView {
    private final JRadioButton fwdRadio = new JRadioButton("FWD", true);
    private final JRadioButton bwdRadio = new JRadioButton("BWD", false);
    private SelectDependencyFilterAction selecDependencyFilterAction;


    public DependencyFilterPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Dependencies:"));

        ButtonGroup bg = new ButtonGroup();
        bg.add(this.bwdRadio);
        bg.add(this.fwdRadio);
        this.add(this.bwdRadio);
        this.add(this.fwdRadio);
        this.bwdRadio.setEnabled(false); // TODO

        this.bwdRadio.addActionListener(this::onBwdFilterSelected);
        this.fwdRadio.addActionListener(this::onFwdFilterSelected);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<DependencyFilterItem> dependencyFilter,
        SelectDependencyFilterAction selectDependencyFilterAction
    ) {
        this.selecDependencyFilterAction = selectDependencyFilterAction;
        dependencyFilter.subscribe(new GenericSubscriber<>(this::onInitialFilterChanged));
    }


    private void onInitialFilterChanged(DependencyFilterItem dependencyFilter) {
        if (dependencyFilter == null) {
            return;
        } else if (dependencyFilter.isFwd) {
            this.fwdRadio.setSelected(true);
            this.bwdRadio.setSelected(false);
        } else {
            this.fwdRadio.setSelected(false);
            this.bwdRadio.setSelected(true);
        }

        this.repaint();
        this.revalidate();

        if (this.selecDependencyFilterAction != null) {
            this.selecDependencyFilterAction.next(dependencyFilter);
        }
    }


    private void onFwdFilterSelected(ActionEvent e) {
        if (this.fwdRadio.isSelected()) {
            this.selecDependencyFilterAction.next(
                new DependencyFilterItem(true)
            );
        }
    }


    private void onBwdFilterSelected(ActionEvent e) {
        if (this.bwdRadio.isSelected()) {
            this.selecDependencyFilterAction.next(
                new DependencyFilterItem(false)
            );
        }
    }
}
