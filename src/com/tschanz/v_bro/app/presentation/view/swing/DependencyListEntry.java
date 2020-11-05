package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.ElementVersionVector;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectDependencyVersionAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectElementClassAction;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionAction;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.app.presentation.viewmodel.FwdDependencyItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;

import javax.swing.*;
import java.util.Collections;
import java.util.concurrent.Flow;


public class DependencyListEntry extends JPanel {
    private final JLabel dependencyName = new JLabel("");
    private final VersionTimeline versionTimeline = new VersionTimeline();
    private final BehaviorSubject<java.util.List<VersionItem>> versionList = new BehaviorSubject<>(Collections.emptyList());
    private SelectElementClassAction selectElementClassAction;
    private SelectElementAction selectElementAction;
    private SelectDependencyVersionAction selectDependencyVersionAction;


    public DependencyListEntry() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(this.dependencyName);
        this.add(this.versionTimeline);
    }


    public void bindViewModel(
        FwdDependencyItem dependency,
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectElementClassAction selectElementClassAction,
        SelectElementAction selectElementAction,
        SelectDependencyVersionAction selectDependencyVersionAction
    ) {
        this.selectElementClassAction = selectElementClassAction;
        this.selectElementAction = selectElementAction;
        this.selectDependencyVersionAction = selectDependencyVersionAction;
        this.dependencyName.setText(this.createDependencyName(dependency));
        this.versionList.next(dependency.getVersions());

        //SelectVersionAction selectVersionAction = new SelectVersionAction(null);
        //selectVersionAction.subscribe(new GenericSubscriber<>(this::onVersionSelected));
        SelectVersionAction selectVersionAction = null;

        this.versionTimeline.bindViewModel(this.versionList, versionFilter, selectVersionAction);

        this.repaint();
        this.revalidate();
    }


    private String createDependencyName(FwdDependencyItem dependency) {
        return dependency.elementName() + " - " + dependency.elementId();
    }


    private void onVersionSelected(String versionId) {
        if (this.selectDependencyVersionAction == null
            || this.selectElementClassAction == null
            || this.selectElementAction == null
            || versionId == null
        ) {
            return;
        }

        ElementVersionVector dependencyVersion = new ElementVersionVector(
            this.selectElementClassAction.getCurrentValue(),
            this.selectElementAction.getCurrentValue(),
            versionId
        );
        this.selectDependencyVersionAction.next(dependencyVersion);
    }
}
