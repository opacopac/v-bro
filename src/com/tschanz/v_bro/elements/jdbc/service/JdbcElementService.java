package com.tschanz.v_bro.elements.jdbc.service;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.NameField;
import com.tschanz.v_bro.repo.jdbc.service.JdbcRepoService;
import com.tschanz.v_bro.repo.jdbc.service.JdbcRepoMetadata;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


public class JdbcElementService implements ElementService {
    public static String ELEMENT_TABLE_SUFFIX2 = "_E";

    private final Logger logger = Logger.getLogger(JdbcVersioningStructure.class.getName());
    private final JdbcRepoService repo;
    private final JdbcRepoMetadata repoMetaData;


    public JdbcElementService(
        JdbcRepoService repo,
        JdbcRepoMetadata repoMetaData
    ) {
        this.repo = repo;
        this.repoMetaData = repoMetaData;
    }


    @Override
    public Collection<ElementClass> readElementClasses() throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }

        List<ElementClass> elementStructureList;
        try {
            ResultSet tablesResult = this.findElementTables();
            elementStructureList = this.readTables(tablesResult);
        } catch (SQLException exception) {
            String msg = "error reading element structure: " + exception.getMessage();
            this.logger.severe (msg);
            throw new RepoException(msg, exception);
        }

        return new ArrayList<>(elementStructureList);
    }


    @Override
    public List<NameField> readNameFields(String elementName) throws RepoException {
        return null;
    }


    @Override
    public Collection<ElementData> readElementData(String elementName, Collection<String> fieldNames) throws RepoException {
        if (!this.repo.isConnected()) {
            throw new RepoException("Not connected to repo!");
        }


        return null;
    }


    private ResultSet findElementTables() throws SQLException {
        this.logger.info("finding tables with suffix " + ELEMENT_TABLE_SUFFIX2);
        String tableNamePattern = JdbcRepoMetadata.WILDCARD + this.repoMetaData.escapeUnderscore(ELEMENT_TABLE_SUFFIX2);

        return this.repoMetaData.readTableResults(tableNamePattern);
    }


    private List<ElementClass> readTables(ResultSet tablesResult) throws SQLException {
        List<ElementClass> elementStructureList = new ArrayList<>();

        while (tablesResult.next()) {
            String name = tablesResult.getString("TABLE_NAME");
            //TODO: name fields
            ElementClass element = new ElementClass(name);
            elementStructureList.add(element);
        }

        tablesResult.close();

        return elementStructureList;
    }
}
