package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.view.StatusBarView;

import javax.swing.*;
import java.awt.*;


public class StatusBarPanel extends JPanel implements StatusBarView {
    private final Color STATUS_INFO_COLOR = Color.DARK_GRAY;
    private final Color STATUS_ERROR_COLOR = Color.RED;
    private final JLabel statusLabel = new JLabel();


    public void setStatusInfo(String infoText) {
        this.statusLabel.setForeground(STATUS_INFO_COLOR);
        this.statusLabel.setText(infoText);
        this.statusLabel.repaint();
    }


    public void setStatusError(String errorText) {
        this.statusLabel.setForeground(STATUS_ERROR_COLOR);
        this.statusLabel.setText(errorText);
        this.statusLabel.repaint();
    }


    public StatusBarPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(this.statusLabel);
    }
}
