package com.tschanz.v_bro.app;

import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.dependencies.persistence.jdbc.service.JdbcDependencyService;
import com.tschanz.v_bro.dependencies.persistence.mock.service.MockDependencyService2;
import com.tschanz.v_bro.dependencies.persistence.xml.service.XmlDependencyService;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.element_classes.persistence.jdbc.service.JdbcElementClassService;
import com.tschanz.v_bro.element_classes.persistence.mock.service.MockElementClassService2;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.persistence.mock.service.MockElementService2;
import com.tschanz.v_bro.element_classes.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.element_classes.usecase.read_element_classes.ReadElementClassesUseCaseImpl;
import com.tschanz.v_bro.element_classes.usecase.read_element_denominations.ReadElementDenominationsUseCase;
import com.tschanz.v_bro.element_classes.usecase.read_element_denominations.ReadElementDenominationsUseCaseImpl;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCaseImpl;
import com.tschanz.v_bro.elements.persistence.xml.service.ElementParser;
import com.tschanz.v_bro.elements.persistence.jdbc.service.JdbcElementService;
import com.tschanz.v_bro.elements.persistence.xml.service.ElementClassParser;
import com.tschanz.v_bro.elements.persistence.xml.service.XmlElementService;
import com.tschanz.v_bro.app.presentation.controller.MainController;
import com.tschanz.v_bro.app.presentation.view.swing.MainPanel;
import com.tschanz.v_bro.app.presentation.view.MainView;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoData;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataService;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilderImpl;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoService2;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCaseImpl;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCaseImpl;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.service.JdbcVersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.mock.service.MockVersionAggregateService2;
import com.tschanz.v_bro.version_aggregates.persistence.xml.service.XmlVersionAggregateService;
import com.tschanz.v_bro.versions.domain.service.VersionService;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;
import com.tschanz.v_bro.versions.persistence.mock.service.MockVersionService2;
import com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies.ReadFwdDependenciesUseCase;
import com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies.ReadFwdDependenciesUseCaseImpl;
import com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate.ReadVersionAggregateUseCase;
import com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate.ReadVersionAggregateUseCaseImpl;
import com.tschanz.v_bro.versions.usecase.read_version_timeline.ReadVersionTimelineUseCase;
import com.tschanz.v_bro.versions.usecase.read_version_timeline.ReadVersionTimelineUseCaseImpl;
import com.tschanz.v_bro.versions.persistence.xml.service.VersionParser;
import com.tschanz.v_bro.versions.persistence.xml.service.XmlVersionService;

import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, RepoException, IOException {
        /// poor man's DI

        Properties appProperties = loadProperties();

        // persistence jdbc
        Class.forName("oracle.jdbc.OracleDriver");
        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        JdbcRepoService jdbcRepo = new JdbcRepoService(jdbcConnectionFactory);
        JdbcRepoMetadataService jdbcRepoMetadata = new JdbcRepoMetadataService(jdbcConnectionFactory);
        JdbcQueryBuilder jdbcQueryBuilder = new JdbcQueryBuilderImpl(jdbcConnectionFactory);
        JdbcRepoData jdbcRepoData = new JdbcRepoData(jdbcConnectionFactory, jdbcQueryBuilder);
        JdbcElementClassService jdbcElementClassService = new JdbcElementClassService(jdbcRepo, jdbcRepoMetadata);
        JdbcElementService jdbcElementService = new JdbcElementService(jdbcRepo, jdbcRepoMetadata, jdbcRepoData);
        JdbcVersionService jdbcVersionService = new JdbcVersionService(jdbcRepo, jdbcRepoMetadata, jdbcRepoData, jdbcElementService);
        JdbcVersionAggregateService jdbcVersionAggregateService = new JdbcVersionAggregateService(jdbcRepo, jdbcRepoMetadata, jdbcRepoData, jdbcElementService, jdbcVersionService);
        JdbcDependencyService jdbcDependencyService = new JdbcDependencyService(jdbcRepo, jdbcRepoMetadata, jdbcRepoData, jdbcVersionService, jdbcVersionAggregateService);

        // persistence xml
        XmlRepoService xmlRepo = new XmlRepoService();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        ElementClassParser elementClassParser = new ElementClassParser(saxParserFactory);
        ElementParser elementParser = new ElementParser(saxParserFactory);
        VersionParser versionParser = new VersionParser(saxParserFactory);
        XmlElementService xmlElementService = new XmlElementService(xmlRepo, elementClassParser, elementParser);
        XmlVersionService xmlVersionDataService = new XmlVersionService(xmlRepo, versionParser);
        XmlVersionAggregateService xmlVersionAggregateService = new XmlVersionAggregateService(xmlRepo, versionParser);
        XmlDependencyService xmlDependencyService = new XmlDependencyService(xmlRepo, versionParser);

        // persistence mock
        MockRepoService2 mockRepo = new MockRepoService2();
        MockElementService2 mockElementService = new MockElementService2();
        MockElementClassService2 mockElementClassService = new MockElementClassService2();
        MockVersionService2 mockVersionDataService = new MockVersionService2();
        MockVersionAggregateService2 mockVersionAggregateService = new MockVersionAggregateService2();
        MockDependencyService2 mockDependencyService = new MockDependencyService2();

        // persistence service maps
        Map<RepoType, RepoService> repoMap = Map.of(RepoType.JDBC, jdbcRepo, RepoType.XML, xmlRepo, RepoType.MOCK, mockRepo);
        Map<RepoType, ElementClassService> elementClassServiceMap = Map.of(RepoType.JDBC, jdbcElementClassService, RepoType.XML, xmlElementService, RepoType.MOCK, mockElementClassService);
        Map<RepoType, ElementService> elementServiceMap = Map.of(RepoType.JDBC, jdbcElementService, RepoType.XML, xmlElementService, RepoType.MOCK, mockElementService);
        Map<RepoType, VersionService> versionServiceMap = Map.of(RepoType.JDBC, jdbcVersionService, RepoType.XML, xmlVersionDataService, RepoType.MOCK, mockVersionDataService);
        Map<RepoType, VersionAggregateService> versionAggregateServiceMap = Map.of(RepoType.JDBC, jdbcVersionAggregateService, RepoType.XML, xmlVersionAggregateService, RepoType.MOCK, mockVersionAggregateService);
        Map<RepoType, DependencyService> dependencyServiceMap = Map.of(RepoType.JDBC, jdbcDependencyService, RepoType.XML, xmlDependencyService, RepoType.MOCK, mockDependencyService);

        // app use cases
        OpenConnectionUseCase openConnectionUc = new OpenConnectionUseCaseImpl(repoMap);
        CloseConnectionUseCase closeConnectionUc = new CloseConnectionUseCaseImpl(repoMap);
        ReadElementClassesUseCase readElementClassesUc = new ReadElementClassesUseCaseImpl(elementClassServiceMap);
        ReadElementDenominationsUseCase readElementNameFieldsUc = new ReadElementDenominationsUseCaseImpl(elementClassServiceMap);
        ReadElementsUseCase readElementsUc = new ReadElementsUseCaseImpl(elementServiceMap);
        ReadVersionTimelineUseCase readVersionsUc = new ReadVersionTimelineUseCaseImpl(versionServiceMap);
        ReadVersionAggregateUseCase readVersionAggregateUc = new ReadVersionAggregateUseCaseImpl(versionAggregateServiceMap);
        ReadFwdDependenciesUseCase readFwdDependenciesUc = new ReadFwdDependenciesUseCaseImpl(dependencyServiceMap);

        // presentation
        MainView mainView = new MainPanel();
        MainController mainController = new MainController(
            appProperties,
            mainView,
            openConnectionUc,
            closeConnectionUc,
            readElementClassesUc,
            readElementNameFieldsUc,
            readElementsUc,
            readVersionsUc,
            readFwdDependenciesUc,
            readVersionAggregateUc
        );
        mainController.start();;
    }


    private static Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        String propFileName = "/application.properties";

        InputStream inputStream = Main.class.getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        return prop;
    }
}
