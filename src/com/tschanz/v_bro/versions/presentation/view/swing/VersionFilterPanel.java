package com.tschanz.v_bro.versions.presentation.view.swing;

import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.elements.presentation.viewmodel.VersionFilter;
import com.tschanz.v_bro.elements.presentation.viewmodel.PflegestatusItem;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.presentation.view.VersionFilterView;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Flow;


public class VersionFilterPanel extends JPanel implements VersionFilterView {
    private final UtilDateModel modelVon = new UtilDateModel();
    private final UtilDateModel modelBis = new UtilDateModel();
    private final JDatePanelImpl datePanelVon = new JDatePanelImpl(this.modelVon, new Properties());
    private final JDatePanelImpl datePanelBis = new JDatePanelImpl(this.modelBis, new Properties());
    private final JDatePicker datePickerVon = new JDatePickerImpl(this.datePanelVon, new DateComponentFormatter());
    private final JDatePicker datePickerBis = new JDatePickerImpl(this.datePanelBis, new DateComponentFormatter());
    private final JComboBox<PflegestatusItem> pflegestatusList = new JComboBox<>();
    private BehaviorSubject<VersionFilter> selecVersionFilterAction;


    public VersionFilterPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Min. gültig von"));
        this.add((Component) this.datePickerVon);
        this.add(new JLabel("Max. gültig bis"));
        this.add((Component) this.datePickerBis);
        this.add(new JLabel("Min. Status"));
        this.add(this.pflegestatusList);
        ((Component) this.datePickerVon).setPreferredSize(new Dimension(110, 30));
        ((Component) this.datePickerBis).setPreferredSize(new Dimension(110, 30));

        this.modelVon.setValue(new Date(2015 - 1900, Calendar.JANUARY, 1));
        this.modelBis.setValue(new Date(9999 - 1900, Calendar.DECEMBER, 31));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.IN_ARBEIT));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.TEST));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.ABNAHME));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.PRODUKTIV));

        this.datePanelVon.addActionListener(this::onFilterSelected);
        this.datePanelBis.addActionListener(this::onFilterSelected);
        this.pflegestatusList.addActionListener(this::onFilterSelected);
    }


    @Override
    public void bindVersionFilter(Flow.Publisher<VersionFilter> versionFilter) {
        versionFilter.subscribe(new GenericSubscriber<>(this::onFilterChanged));
    }


    @Override
    public void bindSelectVersionFilterAction(BehaviorSubject<VersionFilter> selectVersionFilterAction) {
        this.selecVersionFilterAction = selectVersionFilterAction;
    }


    private void onFilterChanged(VersionFilter versionFilter) {
        if (versionFilter == null) {
            return;
        }

        this.modelVon.setValue(Date.from(versionFilter.getMinGueltiVon().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        this.modelBis.setValue(Date.from(versionFilter.getMaxGueltigBis().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        this.pflegestatusList.setSelectedItem(versionFilter.getMinPflegestatus());

        this.selecVersionFilterAction.next(versionFilter); // auto-select
    }


    private void onFilterSelected(ActionEvent e) {
        this.selecVersionFilterAction.next(
            new VersionFilter(
                LocalDate.ofInstant(this.modelVon.getValue().toInstant(), ZoneId.systemDefault()),
                LocalDate.ofInstant(this.modelBis.getValue().toInstant(), ZoneId.systemDefault()),
                ((PflegestatusItem) this.pflegestatusList.getSelectedItem()).getPflegestatus()
            )
        );
    }
}
