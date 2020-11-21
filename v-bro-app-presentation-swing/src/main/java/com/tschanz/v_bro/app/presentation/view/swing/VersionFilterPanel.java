package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.SelectVersionFilterAction;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.app.presentation.viewmodel.VersionFilterItem;
import com.tschanz.v_bro.app.presentation.viewmodel.PflegestatusItem;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.app.presentation.view.VersionFilterView;
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
    private SelectVersionFilterAction selecVersionFilterAction;


    public VersionFilterPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Min. gültig von"));
        this.add((Component) this.datePickerVon);
        this.add(new JLabel("Max. gültig bis"));
        this.add((Component) this.datePickerBis);
        this.add(new JLabel("Min. Status"));
        this.add(this.pflegestatusList);
        this.datePickerVon.setTextEditable(true);
        this.datePickerBis.setTextEditable(true);
        ((Component) this.datePickerVon).setPreferredSize(new Dimension(110, 30));
        ((Component) this.datePickerBis).setPreferredSize(new Dimension(110, 30));

        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.IN_ARBEIT));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.TEST));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.ABNAHME));
        this.pflegestatusList.addItem(new PflegestatusItem(Pflegestatus.PRODUKTIV));

        this.datePanelVon.addActionListener(this::onFilterSelected);
        this.datePanelBis.addActionListener(this::onFilterSelected);
        this.pflegestatusList.addActionListener(this::onFilterSelected);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<VersionFilterItem> versionFilter,
        SelectVersionFilterAction selectVersionFilterAction
    ) {
        this.selecVersionFilterAction = selectVersionFilterAction;
        versionFilter.subscribe(new GenericSubscriber<>(this::onVersionFilterChanged));
    }


    private void onVersionFilterChanged(VersionFilterItem versionFilter) {
        if (versionFilter == null) {
            return;
        }

        this.setPickerDate(this.datePickerVon, versionFilter.getMinGueltigVon());
        this.setPickerDate(this.datePickerBis, versionFilter.getMaxGueltigBis());
        this.pflegestatusList.setSelectedItem(versionFilter.getMinPflegestatus());

        this.repaint();
        this.revalidate();
    }


    private void onFilterSelected(ActionEvent e) {
        this.selecVersionFilterAction.next(
            new VersionFilterItem(
                LocalDate.ofInstant(this.modelVon.getValue().toInstant(), ZoneId.systemDefault()),
                LocalDate.ofInstant(this.modelBis.getValue().toInstant(), ZoneId.systemDefault()),
                ((PflegestatusItem) this.pflegestatusList.getSelectedItem()).getPflegestatus()
            )
        );
    }


    private void setPickerDate(JDatePicker datePicker, LocalDate newDate) {
        datePicker.getModel().setDate(newDate.getYear(), newDate.getMonthValue() - 1, newDate.getDayOfMonth());
        datePicker.getModel().setSelected(true);
    }
}
