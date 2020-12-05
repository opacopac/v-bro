package com.tschanz.v_bro.app.presentation.viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class FwdDependencyItem {
    protected final String elementClass;
    protected final String elementId;
    protected final List<VersionItem> versions;
}
