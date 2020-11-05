package com.tschanz.v_bro.app;

import com.tschanz.v_bro.app.presentation.presenter.MainPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCaseImpl;
import com.tschanz.v_bro.common.cache.EternalCache;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.dependencies.persistence.jdbc.service.JdbcDependencyService;
import com.tschanz.v_bro.dependencies.persistence.mock.service.MockDependencyService2;
import com.tschanz.v_bro.dependencies.persistence.xml.service.XmlDependencyService;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCase;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.element_classes.persistence.jdbc.service.JdbcElementClassService;
import com.tschanz.v_bro.element_classes.persistence.mock.service.MockElementClassService2;
import com.tschanz.v_bro.element_classes.persistence.xml.service.DenominationsParser;
import com.tschanz.v_bro.element_classes.persistence.xml.service.XmlElementClassService;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCase;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCase;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.persistence.mock.service.MockElementService2;
import com.tschanz.v_bro.elements.persistence.xml.service.ElementParser;
import com.tschanz.v_bro.elements.persistence.jdbc.service.JdbcElementService;
import com.tschanz.v_bro.elements.persistence.xml.service.XmlElementService;
import com.tschanz.v_bro.app.presentation.controller.MainController;
import com.tschanz.v_bro.app.presentation.view.swing.MainPanel;
import com.tschanz.v_bro.app.presentation.view.MainView;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCase;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.repo.persistence.jdbc.model.RepoTable;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilder;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilderImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceCache;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoService2;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionUseCase;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionUseCaseImpl;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionUseCase;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionUseCaseImpl;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.jdbc.service.JdbcVersionAggregateService;
import com.tschanz.v_bro.version_aggregates.persistence.mock.service.MockVersionAggregateService2;
import com.tschanz.v_bro.version_aggregates.persistence.xml.service.VersionAggregateParser;
import com.tschanz.v_bro.version_aggregates.persistence.xml.service.XmlVersionAggregateService;
import com.tschanz.v_bro.versions.domain.service.VersionService;
import com.tschanz.v_bro.versions.persistence.jdbc.service.JdbcVersionService;
import com.tschanz.v_bro.versions.persistence.mock.service.MockVersionService2;
import com.tschanz.v_bro.versions.persistence.xml.service.VersionParser;
import com.tschanz.v_bro.versions.persistence.xml.service.XmlVersionService;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCase;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        /// poor man's DI ///

        Properties appProperties = loadProperties();

        // persistence jdbc
        Class.forName("oracle.jdbc.OracleDriver");
        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        JdbcRepoService jdbcRepo = new JdbcRepoService(jdbcConnectionFactory);
        JdbcRepoMetadataServiceImpl jdbcRepoMetadata = new JdbcRepoMetadataServiceImpl(jdbcConnectionFactory);
        JdbcRepoMetadataServiceCache jdbcRepoMetadataCached = new JdbcRepoMetadataServiceCache(jdbcRepoMetadata, new EternalCache<RepoTable>());
        JdbcQueryBuilder jdbcQueryBuilder = new JdbcQueryBuilderImpl(jdbcConnectionFactory);
        JdbcRepoDataService jdbcRepoDataService = new JdbcRepoDataService(jdbcConnectionFactory, jdbcQueryBuilder);
        JdbcElementClassService jdbcElementClassService = new JdbcElementClassService(jdbcRepo, jdbcRepoMetadataCached);
        JdbcElementService jdbcElementService = new JdbcElementService(jdbcRepo, jdbcRepoMetadataCached, jdbcRepoDataService);
        JdbcVersionService jdbcVersionService = new JdbcVersionService(jdbcRepo, jdbcRepoMetadataCached, jdbcRepoDataService, jdbcElementService);
        JdbcVersionAggregateService jdbcVersionAggregateService = new JdbcVersionAggregateService(jdbcRepo, jdbcRepoMetadataCached, jdbcRepoDataService, jdbcElementService, jdbcVersionService);
        JdbcDependencyService jdbcDependencyService = new JdbcDependencyService(jdbcRepo, jdbcRepoMetadataCached, jdbcRepoDataService, jdbcVersionService, jdbcVersionAggregateService);

        // persistence xml
        XmlRepoService xmlRepo = new XmlRepoService();
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        DenominationsParser denominationsParser = new DenominationsParser(xmlInputFactory);
        ElementParser elementParser = new ElementParser(xmlInputFactory);
        VersionParser versionParser = new VersionParser(xmlInputFactory);
        VersionAggregateParser versionAggregateParser = new VersionAggregateParser(saxParserFactory);
        XmlElementClassService xmlElementClassService = new XmlElementClassService(xmlRepo, denominationsParser);
        XmlElementService xmlElementService = new XmlElementService(xmlRepo, elementParser);
        XmlVersionService xmlVersionService = new XmlVersionService(xmlRepo, versionParser);
        XmlVersionAggregateService xmlVersionAggregateService = new XmlVersionAggregateService(xmlRepo, versionAggregateParser);
        XmlDependencyService xmlDependencyService = new XmlDependencyService(xmlRepo, xmlVersionService, xmlVersionAggregateService);

        // persistence mock
        MockRepoService2 mockRepo = new MockRepoService2();
        MockElementService2 mockElementService = new MockElementService2();
        MockElementClassService2 mockElementClassService = new MockElementClassService2();
        MockVersionService2 mockVersionDataService = new MockVersionService2();
        MockVersionAggregateService2 mockVersionAggregateService = new MockVersionAggregateService2();
        MockDependencyService2 mockDependencyService = new MockDependencyService2();

        // persistence service providers
        RepoServiceProvider<RepoService> repoServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcRepo, RepoType.XML, xmlRepo, RepoType.MOCK, mockRepo);
        RepoServiceProvider<ElementClassService> elementClassServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementClassService, RepoType.XML, xmlElementClassService, RepoType.MOCK, mockElementClassService);
        RepoServiceProvider<ElementService> elementServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementService, RepoType.XML, xmlElementService, RepoType.MOCK, mockElementService);
        RepoServiceProvider<VersionService> versionServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcVersionService, RepoType.XML, xmlVersionService, RepoType.MOCK, mockVersionDataService);
        RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcVersionAggregateService, RepoType.XML, xmlVersionAggregateService, RepoType.MOCK, mockVersionAggregateService);
        RepoServiceProvider<DependencyService> dependencyServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcDependencyService, RepoType.XML, xmlDependencyService, RepoType.MOCK, mockDependencyService);

        // presentation viewmodel & presenter
        MainModel mainModel = new MainModel();
        MainPresenter mainPresenter = new MainPresenter(mainModel);

        // app use cases
        OpenConnectionUseCase openConnectionUc = new OpenConnectionUseCaseImpl(repoServiceProvider, elementClassServiceProvider, mainPresenter.getOpenConnectionPresenter());
        CloseConnectionUseCase closeConnectionUc = new CloseConnectionUseCaseImpl(repoServiceProvider, mainPresenter.getCloseConnectionPresenter());
        SelectElementClassUseCase selectElementClassUc = new SelectElementClassUseCaseImpl(elementClassServiceProvider, elementServiceProvider, mainPresenter.getSelectElementClassPresenter());
        SelectElementDenominationUseCase selectElementDenominationUc = new SelectElementDenominationUseCaseImpl(elementServiceProvider, mainPresenter.getSelectElementDenominationPresenter());
        SelectElementUseCase selectElementUc = new SelectElementUseCaseImpl(versionServiceProvider, mainPresenter.getSelectElementPresenter());
        SelectVersionUseCase selectVersionUc = new SelectVersionUseCaseImpl(dependencyServiceProvider, versionAggregateServiceProvider, mainPresenter.getSelectVersionPresenter());
        SelectDependencyFilterUseCase selectDependencyFilterUc = new SelectDependencyFilterUseCaseImpl(dependencyServiceProvider, mainPresenter.getSelectDependencyFilterPresenter());

        // presentation controller
        MainController mainController = new MainController(
            appProperties, // TODO => move to app
            mainModel,
            openConnectionUc,
            closeConnectionUc,
            selectElementClassUc,
            selectElementDenominationUc,
            selectElementUc,
            selectVersionUc,
            selectDependencyFilterUc
        );

        // presentation view
        MainView mainView = new MainPanel();
        mainView.bindViewModel(mainModel);
        mainView.start();
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
