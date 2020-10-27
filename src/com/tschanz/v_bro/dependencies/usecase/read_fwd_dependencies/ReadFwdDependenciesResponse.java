package com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies;

import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import java.util.List;


public class ReadFwdDependenciesResponse {
    public final List<FwdDependencyInfo> fwdDependencies;


    public ReadFwdDependenciesResponse(List<FwdDependencyInfo> fwdDependencies) {
        this.fwdDependencies = fwdDependencies;
    }


    public static class FwdDependencyInfo {
        public final String elementClass;
        public final String elementId;
        public final List<VersionInfo> versions;


        public FwdDependencyInfo(String elementClass, String elementId, List<VersionInfo> versions) {
            this.elementClass = elementClass;
            this.elementId = elementId;
            this.versions = versions;
        }
    }
}
