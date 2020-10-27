package com.tschanz.v_bro.versions.presentation.swing.viewmodel;


public class VersionVonBisPx {
    private final VersionItem version;
    private final int x1;
    private final int x2;

    public VersionItem getVersion() { return version; }
    public int getX1() { return x1; }
    public int getX2() { return x2; }

    public VersionVonBisPx(VersionItem version, int x1, int x2) {
        this.version = version;
        this.x1 = x1;
        this.x2 = x2;
    }
}
