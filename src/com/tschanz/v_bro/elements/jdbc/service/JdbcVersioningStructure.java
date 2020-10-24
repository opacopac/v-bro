package com.tschanz.v_bro.elements.jdbc.service;

import com.tschanz.v_bro.repo.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.jdbc.repo_metadata.RepoMetadataService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.jdbc.repo_connection.JdbcConnectionFactory;

import java.util.List;
import java.util.logging.Logger;


public class JdbcVersioningStructure { //implements VersioningStructure {
    public static String ELEMENT_TABLE_SUFFIX = "_E";
    public static String VERSION_TABLE_SUFFIX = "_V";
    public static String ELEMENT_ID_COLNAME = "ID_ELEMENT";
    public static String GUELTIG_VON_COLNAME = "GUELTIG_VON";
    public static String GUELTIG_BIS_COLNAME = "GUELTIG_BIS";

    private final Logger logger = Logger.getLogger(JdbcVersioningStructure.class.getName());
    private final JdbcConnectionFactory connectionFactory;
    private final RepoMetadataService repoMetaDataService;


    public JdbcVersioningStructure(
        JdbcConnectionFactory connectionFactory,
        RepoMetadataService repoMetadataService
    ) {
        this.connectionFactory = connectionFactory;
        this.repoMetaDataService = repoMetadataService;
    }


    // region findElementClassNames

    //@Override
    public List<String> findElementClassNames() throws RepoException {
        this.logger.info("finding tables with suffix " + ELEMENT_TABLE_SUFFIX);

        String tableNamePattern = JdbcRepoMetadataService.WILDCARD + this.repoMetaDataService.escapeUnderscore(ELEMENT_TABLE_SUFFIX);
        List<String> tableNames = this.repoMetaDataService.findTableNames(tableNamePattern);

        return tableNames;
    }

    // endregion


    // region isElementClass

    //@Override
    public boolean isElementClass(String elementClassName) {
        return elementClassName.toUpperCase().endsWith(ELEMENT_TABLE_SUFFIX);
    }

    // endregion


    // region readElementClass

    //@Override
    /*public ElementClass readElementClass(String elementClassName) throws RepoException {
        RepoClass repoClass = this.repoMetaData.readClass(elementClassName);

        return new ElementClass(repoClass);
    }*/

    // endregion


    // region readVersionMasterClass

    /*@Override
    public VersionMasterClass readVersionMasterClass(ElementClass elementClass) throws RepoException {
        RepoRelation elementVersionRelation = elementClass.getIncomingRelations()
            .stream()
            .filter(rel -> rel.getFwdClassName().equals(elementClass.getName()))
            .filter(rel -> rel.getBwdFieldName().toUpperCase().equals(ELEMENT_ID_COLNAME))
            .filter(rel -> rel.getBwdClassName().toUpperCase().endsWith(VERSION_TABLE_SUFFIX))
            .findFirst()
            .orElse(null);

        if (elementVersionRelation == null) {
            return null;
        }

        RepoClass versionObjectInfo = this.repoMetaData.readClass(elementVersionRelation.getBwdClassName());
        return new VersionMasterClass(
            versionObjectInfo,
            ELEMENT_ID_COLNAME,
            GUELTIG_VON_COLNAME,
            GUELTIG_BIS_COLNAME,
            new Dependency<>(elementClass, elementVersionRelation)
        );
    }*/

    // endregion


    // region findVersionAggregate

    /*@Override
    public VersioningAggregate findVersionAggregate(ElementClass elementClass, VersionMasterClass versionMasterClass) throws RepoException {
        List<VersionClass> versionClasses = this.findVersionClasses(versionMasterClass);

        return new VersioningAggregate(
            elementClass,
            versionMasterClass,
            versionClasses
        );
    }*/


    /*public List<VersionClass> findVersionClasses(VersionMasterClass versionMasterTable) {
        ArrayList<VersionClass> unprocessedTables = new ArrayList<>();
        ArrayList<VersionClass> processedTables = new ArrayList<>();

        unprocessedTables.add(versionMasterTable);
        while (unprocessedTables.size() > 0) {
            VersionClass parentTable = unprocessedTables.remove(0);
            processedTables.add(parentTable);
            List<String> processedTableNames = processedTables
                .stream()
                .map(VersionClass::getName)
                .collect(Collectors.toList());
            parentTable.getIncomingRelations()
                .stream()
                .filter(rel -> !processedTableNames.contains(rel.getBwdClassName())) // skip already processed tables
                .map(rel -> this.createVersionPartTable(rel, processedTables))
                .filter(Objects::nonNull)
                .forEach(unprocessedTables::add);
        }

        return processedTables;
    }


    private VersionClass createVersionPartTable(
        RepoRelation parentRelation,
        List<VersionClass> processedTables
    ) {
        VersionClass parentVersion = processedTables
            .stream()
            .filter(versionedTable -> versionedTable.getName().equals(parentRelation.getFwdClassName()))
            .findFirst()
            .orElse(null);

        try {
            return new VersionPartClass(
                this.repoMetaData.readClass(parentRelation.getBwdClassName()),
                new Dependency<>(parentVersion, parentRelation)
            );
        } catch (RepoException exception) {
            return null;
        }
    }*/

    // endregion
}
