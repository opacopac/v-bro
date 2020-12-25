package com.tschanz.v_bro.data_structure.persistence.jdbc.model;

import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoField;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class VersionTable {
    public final static String TABLE_SUFFIX = "_V";
    public static String ELEMENT_ID_COLNAME = "ID_ELEMENT";
    public static String GUELTIG_VON_COLNAME = "GUELTIG_VON";
    public static String GUELTIG_BIS_COLNAME = "GUELTIG_BIS";
    public static String PFLEGESTATUS_COLNAME = "PFLEGEZYKLUS";
    @NonNull private final RepoTable repoTable;


    public RepoField getIdField() {
        return this.repoTable.findfirstIdField();
    }


    public RepoField getGueltigVonField() {
        return this.repoTable.findField(GUELTIG_VON_COLNAME);
    }


    public RepoField getGueltigBisField() {
        return this.repoTable.findField(GUELTIG_BIS_COLNAME);
    }


    public RepoField getElementIdField() {
        return this.repoTable.findField(ELEMENT_ID_COLNAME);
    }


    public List<RepoField> getFields() {
        return this.repoTable.getFields();
    }


    public List<RepoField> getFields(Collection<String> fieldNames) {
        return this.getFields()
            .stream()
            .filter(field -> fieldNames.contains(field.getName()))
            .collect(Collectors.toList());
    }
}
