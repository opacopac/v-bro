package com.tschanz.v_bro.versioning.swing.versionfilter;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class VersionFilterPanel extends JPanel implements VersionFilterView {
    private final UtilDateModel modelVon = new UtilDateModel();
    private final UtilDateModel modelBis = new UtilDateModel();
    private final JDatePanelImpl datePanelVon = new JDatePanelImpl(this.modelVon, new Properties());
    private final JDatePanelImpl datePanelBis = new JDatePanelImpl(this.modelBis, new Properties());
    private final JDatePicker datePickerVon = new JDatePickerImpl(this.datePanelVon, new DateComponentFormatter());
    private final JDatePicker datePickerBis = new JDatePickerImpl(this.datePanelBis, new DateComponentFormatter());
    private final JComboBox<String> pflegestatusList = new JComboBox<>(new String[] {"PRODUKTIV", "ABNAHME", "TEST", "IN_ARBEIT"}); // TODO => model


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
    }


    @Override
    public Date getMinVonDate() {
        return this.modelVon.getValue();
    }


    @Override
    public Date getMaxBisDate() {
        return this.modelBis.getValue();
    }


    @Override
    public String getMinPflegestatus() {
        return String.valueOf(this.pflegestatusList.getSelectedItem());
    }
}
