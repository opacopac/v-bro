package com.tschanz.v_bro.versioning.swing.controller;

import com.tschanz.v_bro.versioning.swing.model.VersionItem;
import com.tschanz.v_bro.versioning.swing.view.VersionTimelineView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class VersionTimelineController {
    private final VersionTimelineView versionTimelineView;


    public VersionTimelineController(VersionTimelineView versionTimelineView) {
        this.versionTimelineView = versionTimelineView;

        this.versionTimelineView.addMouseListener(new VersionTimelineMouseListener());
        this.versionTimelineView.addMouseMotionListener(new VersionTimelineMouseMotionListener());
    }


    private class VersionTimelineMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            VersionTimelineController.this.versionTimelineView.setSelectedVersion(
                VersionTimelineController.this.versionTimelineView.getMousPosVersion(e.getX(), e.getY())
            );
        }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }


    private class VersionTimelineMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) { }

        @Override
        public void mouseMoved(MouseEvent e) {
            VersionItem hoverVersion = VersionTimelineController.this.versionTimelineView.getMousPosVersion(e.getX(), e.getY());
            if (hoverVersion != VersionTimelineController.this.versionTimelineView.getHoverVersion()) {
                VersionTimelineController.this.versionTimelineView.setHoverVersion(hoverVersion);
            }
        }
    }
}
