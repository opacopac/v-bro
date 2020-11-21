package com.tschanz.v_bro.common.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class XmlPathTracker2 {
    private final Stack<String> currentPath = new Stack<>();
    private final Map<String, Stack<String>> markerMap = new HashMap<>();


    public Stack<String> getCurrentPath() { return currentPath; }


    public XmlPathTracker2() {
    }


    public void startNode(String name) {
        this.currentPath.push(name);
    }


    public void endNode() {
        String marker = this.getMarker(this.currentPath);
        if (marker != null) {
            this.markerMap.remove(marker);
        }
        this.currentPath.pop();
    }


    public void markNode(String marker) {
        if (this.markerMap.containsKey(marker)) {
            throw new IllegalArgumentException("marker '" + marker + "' is already in use");
        }

        this.markerMap.put(marker, (Stack<String>) this.currentPath.clone());
    }


    public boolean isWithinMarker(String marker) {
        return this.markerMap.containsKey(marker);
    }


    public int getSubLevelOfMarker(String marker) {
        Stack<String> markerPath = this.markerMap.get(marker);
        if (markerPath == null) {
            return -1;
        } else {
            return currentPath.size() - markerPath.size();
        }
    }


    private String getMarker(Stack<String> path) {
        return this.markerMap.entrySet()
            .stream()
            .filter(entry -> entry.getValue().equals(path))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
    }
}
