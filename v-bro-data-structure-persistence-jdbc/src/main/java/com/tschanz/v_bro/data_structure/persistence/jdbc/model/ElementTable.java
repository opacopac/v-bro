package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoRelation;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class ElementTable {
    @NonNull private final RepoTable repoTable;


    public String getName() {
        return this.repoTable.getName();
    }


    public RepoField getIdField() {
        return this.repoTable.findfirstPkField();
    }


    public List<RepoField> getFields() {
        return this.repoTable.getFields();
    }


    public List<RepoRelation> getIncomingRelations() {
        return this.repoTable.getIncomingRelations();
    }


    public List<RepoField> getFields(Collection<String> fieldNames) {
        return this.getFields()
            .stream()
            .filter(field -> fieldNames.contains(field.getName()))
            .collect(Collectors.toList());
    }
}
