package com.tschanz.v_bro.elements.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.elements.swing.model.PflegestatusItem;
import com.tschanz.v_bro.elements.swing.model.VersionFilter;
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


public class VersionFilterPanel extends JPanel {
    private final UtilDateModel modelVon = new UtilDateModel();
    private final UtilDateModel modelBis = new UtilDateModel();
    private final JDatePanelImpl datePanelVon = new JDatePanelImpl(this.modelVon, new Properties());
    private final JDatePanelImpl datePanelBis = new JDatePanelImpl(this.modelBis, new Properties());
    private final JDatePicker datePickerVon = new JDatePickerImpl(this.datePanelVon, new DateComponentFormatter());
    private final JDatePicker datePickerBis = new JDatePickerImpl(this.datePanelBis, new DateComponentFormatter());
    private final JComboBox<PflegestatusItem> pflegestatusList = new JComboBox<>();
    private BehaviorSubject<VersionFilter> versionFilter;


    public VersionFilterPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(new JLabel("Min. Gültig von"));
        this.add((Component) this.datePickerVon);
        this.add(new JLabel("Max. Gültig bis"));
        this.add((Component) this.datePickerBis);
        this.add(new JLabel("Min. Pflegestatus"));
        this.add(this.pflegestatusList);

        this.modelVon.setValue(new Date(2015 - 1900, Calendar.JANUARY, 1));
        this.modelBis.setValue(new Date(9999 - 1900, Calendar.DECEMBER, 31));

        this.datePanelVon.addActionListener(this::onFilterChanged);
        this.datePanelBis.addActionListener(this::onFilterChanged);
        this.pflegestatusList.addActionListener(this::onFilterChanged);
    }


    public void bindVersionFilter(BehaviorSubject<VersionFilter> versionFilter) {
        this.versionFilter = versionFilter;
        VersionFilter versionFilterValue = versionFilter.getCurrentValue();
        this.modelVon.setValue(Date.from(versionFilterValue.getMinGueltiVon().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        this.modelBis.setValue(Date.from(versionFilterValue.getMaxGueltigBis().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        this.pflegestatusList.setSelectedItem(versionFilterValue.getMinPflegestatus());
    }


    private void onFilterChanged(ActionEvent e) {
        this.versionFilter.next(
            this.getVersionFilter()
        );
    }


    private VersionFilter getVersionFilter() {
        return new VersionFilter(
            LocalDate.ofInstant(this.modelVon.getValue().toInstant(), ZoneId.systemDefault()),
            LocalDate.ofInstant(this.modelBis.getValue().toInstant(), ZoneId.systemDefault()),
            ((PflegestatusItem) this.pflegestatusList.getSelectedItem()).getPflegestatus()
        );
    }
}
