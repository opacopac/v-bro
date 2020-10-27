package com.tschanz.v_bro.elements.persistence.xml.service;

import java.util.Stack;


public class XmlPathTracker {
    private final Stack<String> currentPath = new Stack<>();
    private int markerPathLevel = -1;
    private String markedNode;


    public Stack<String> getCurrentPath() { return currentPath; }

    public String getCurrentNode() { return this.currentPath.peek(); }

    public String getMarkedNode() { return markedNode; }

    public boolean isCurrentNodeMarked() { return this.markerPathLevel == this.currentPath.size(); }


    public void startNode(String name) {
        this.currentPath.push(name);
    }


    public void endNode() {
        this.currentPath.pop();
    }


    public void setNodeMarker() {
        this.markedNode = this.currentPath.peek();
        this.markerPathLevel = this.currentPath.size();
    }


    public void removeNodeMarker() {
        this.markedNode = null;
        this.markerPathLevel = -1;
    }


    public int getSubLevelFromMarker() {
        if (this.markerPathLevel == -1) {
            throw new IllegalArgumentException("ERROR: markerPathLevel is not set!");
        }

        return this.currentPath.size() - this.markerPathLevel;
    }


    public String getParentNode(int levelsAbove) {
        return this.currentPath.get(this.currentPath.size() - levelsAbove);
    }
}
