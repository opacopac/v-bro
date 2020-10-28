package com.tschanz.v_bro.version_aggregates.persistence.jdbc.model;

import com.tschanz.v_bro.version_aggregates.domain.model.AggregateNode;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;


public class JdbcVersionAggregate extends VersionAggregate {
    public JdbcVersionAggregate(VersionInfo versionInfo, AggregateNode rootNode) {
        super(versionInfo, rootNode);
    }
}
