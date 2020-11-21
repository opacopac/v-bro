package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTableRecord;
import com.tschanz.v_bro.data_structure.domain.model.AggregateNode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


public class JdbcAggregateNode extends AggregateNode {
    @Getter private final RepoTableRecord repoTableRecord;
    @Getter private final List<JdbcAggregateNode> jdbcChildNodes;


    public JdbcAggregateNode(RepoTableRecord repoTableRecord, List<JdbcAggregateNode> jdbcChildNodes) {
        super(
            repoTableRecord.getRepoTable().getName(),
            repoTableRecord.getFieldValues()
                .stream()
                .map(fieldValue -> new KeyValue(fieldValue.getName(), fieldValue.getValueString()))
                .collect(Collectors.toList()),
            jdbcChildNodes
                .stream()
                .map(node -> new AggregateNode(node.getNodeName(), node.getFieldValues(), node.getChildNodes()))
                .collect(Collectors.toList())
        );

        this.repoTableRecord = repoTableRecord;
        this.jdbcChildNodes = jdbcChildNodes;
    }
}
