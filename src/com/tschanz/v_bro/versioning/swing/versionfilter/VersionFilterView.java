package com.tschanz.v_bro.versioning.swing.versionfilter;

import java.util.Date;


public interface VersionFilterView {
    Date getMinVonDate();

    Date getMaxBisDate();

    String getMinPflegestatus();
}
