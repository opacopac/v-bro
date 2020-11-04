package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.view.StatusBarView;
import com.tschanz.v_bro.app.presentation.viewmodel.StatusItem;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Flow;


public class StatusBarPanel extends JPanel implements StatusBarView {
    private final Color STATUS_INFO_COLOR = Color.DARK_GRAY;
    private final Color STATUS_ERROR_COLOR = Color.RED;
    private final JLabel statusLabel = new JLabel();


    public StatusBarPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(this.statusLabel);
    }


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


    @Override
    public void bindStatus(Flow.Publisher<StatusItem> status) {
        status.subscribe(new GenericSubscriber<>(this::onStatusChanged));
    }


    private void onStatusChanged(StatusItem status) {
        if (status == null) {
            this.statusLabel.setForeground(STATUS_INFO_COLOR);
            this.statusLabel.setText("");
        } else {
            this.statusLabel.setForeground(status.isError() ? STATUS_ERROR_COLOR : STATUS_INFO_COLOR);
            this.statusLabel.setText(status.getStatusText());
        }

        this.repaint();
        this.revalidate();
    }
}
