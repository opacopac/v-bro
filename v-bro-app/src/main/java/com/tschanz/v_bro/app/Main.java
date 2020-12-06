package com.tschanz.v_bro.app;

import com.tschanz.v_bro.app.presentation.jfx.presenter.JfxQueryElementPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.actions.MainActions;
import com.tschanz.v_bro.app.presentation.controller.MainController;
import com.tschanz.v_bro.app.presentation.jfx.JfxApplication;
import com.tschanz.v_bro.app.presentation.presenter.MainPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.connect_repo.OpenConnectionUseCaseImpl;
import com.tschanz.v_bro.app.usecase.disconnect_repo.CloseConnectionUseCaseImpl;
import com.tschanz.v_bro.app.usecase.query_element.QueryElementUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_dependency_version.SelectDependencyVersionUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_element.SelectElementUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_version.SelectVersionUseCaseImpl;
import com.tschanz.v_bro.common.cache.LastNCache;
import com.tschanz.v_bro.data_structure.persistence.jdbc.service.*;
import com.tschanz.v_bro.data_structure.persistence.mock.service.*;
import com.tschanz.v_bro.data_structure.persistence.xml.sax_parser.VersionAggregateParser;
import com.tschanz.v_bro.data_structure.persistence.xml.service.*;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.DenominationsParser;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.ElementParser;
import com.tschanz.v_bro.data_structure.persistence.xml.stax_parser.VersionParser;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilderImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoService2;
import lombok.SneakyThrows;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        /// poor man's DI ///

        var appProperties = loadProperties();

        // persistence jdbc
        Class.forName("oracle.jdbc.OracleDriver");
        var jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        var jdbcRepo = new JdbcRepoService(jdbcConnectionFactory);
        var jdbcRepoMetadata = new JdbcRepoMetadataServiceImpl(jdbcConnectionFactory);
        var jdbcQueryBuilder = new JdbcQueryBuilderImpl(jdbcConnectionFactory);
        var jdbcRepoDataService = new JdbcRepoDataService(jdbcConnectionFactory, jdbcQueryBuilder);
        var jdbcElementClassService = new JdbcElementClassService(jdbcRepo, jdbcRepoMetadata);
        var jdbcElementService = new JdbcElementService(jdbcRepo, jdbcRepoMetadata, jdbcRepoDataService);
        var jdbcVersionService = new JdbcVersionService(jdbcRepo, jdbcRepoMetadata, jdbcRepoDataService, jdbcElementService);
        var jdbcVersionAggregateService = new JdbcVersionAggregateService(jdbcRepo, jdbcRepoMetadata, jdbcRepoDataService, jdbcElementService, jdbcVersionService);
        var jdbcVersionAggregateServiceCache = new JdbcVersionAggregateServiceCache(jdbcVersionAggregateService, new LastNCache<>(10));
        var jdbcDependencyService = new JdbcDependencyService(jdbcVersionService, jdbcVersionAggregateServiceCache);

        // persistence xml
        var xmlRepo = new XmlRepoService();
        var xmlInputFactory = XMLInputFactory.newInstance();
        var saxParserFactory = SAXParserFactory.newInstance();
        var denominationsParser = new DenominationsParser(xmlInputFactory);
        var elementParser = new ElementParser(xmlInputFactory);
        var versionParser = new VersionParser(xmlInputFactory);
        var versionAggregateParser = new VersionAggregateParser(saxParserFactory);
        var xmlElementClassService = new XmlElementClassService(xmlRepo, denominationsParser);
        var xmlElementService = new XmlElementService(xmlRepo, elementParser);
        var xmlVersionService = new XmlVersionService(xmlRepo, versionParser);
        var xmlVersionAggregateService = new XmlVersionAggregateService(xmlRepo, versionAggregateParser);
        var xmlDependencyService = new XmlDependencyService(xmlRepo, xmlVersionService, xmlVersionAggregateService);

        // persistence mock
        var mockRepo = new MockRepoService2();
        var mockElementService = new MockElementService();
        var mockElementClassService = new MockElementClassService();
        var mockVersionDataService = new MockVersionService();
        var mockVersionAggregateService = new MockVersionAggregateService();
        var mockDependencyService = new MockDependencyService();

        // persistence service providers
        var repoServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcRepo, RepoType.XML, xmlRepo, RepoType.MOCK, mockRepo);
        var elementClassServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementClassService, RepoType.XML, xmlElementClassService, RepoType.MOCK, mockElementClassService);
        var elementServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementService, RepoType.XML, xmlElementService, RepoType.MOCK, mockElementService);
        var versionServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcVersionService, RepoType.XML, xmlVersionService, RepoType.MOCK, mockVersionDataService);
        var versionAggregateServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcVersionAggregateServiceCache, RepoType.XML, xmlVersionAggregateService, RepoType.MOCK, mockVersionAggregateService);
        var dependencyServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcDependencyService, RepoType.XML, xmlDependencyService, RepoType.MOCK, mockDependencyService);

        // presentation viewmodel & presenter
        var mainModel = new MainModel();
        var mainActions = new MainActions();
        var mainPresenter = new MainPresenter(mainModel);
        var jfxQueryElementPresenter = new JfxQueryElementPresenter(mainPresenter.getQueryElementPresenter());

        // app use cases
        var openConnectionUc = new OpenConnectionUseCaseImpl(repoServiceProvider, elementClassServiceProvider, elementServiceProvider, versionServiceProvider, dependencyServiceProvider, versionAggregateServiceProvider, mainPresenter.getOpenConnectionPresenter());
        var closeConnectionUc = new CloseConnectionUseCaseImpl(repoServiceProvider, mainPresenter.getCloseConnectionPresenter());
        var selectElementClassUc = new SelectElementClassUseCaseImpl(elementClassServiceProvider, elementServiceProvider, versionServiceProvider, dependencyServiceProvider, versionAggregateServiceProvider, mainPresenter.getSelectElementClassPresenter());
        var selectElementDenominationUc = new SelectElementDenominationUseCaseImpl(elementServiceProvider, mainPresenter.getSelectElementDenominationPresenter());
        var queryElementUc = new QueryElementUseCaseImpl(elementServiceProvider, jfxQueryElementPresenter);
        var selectElementUc = new SelectElementUseCaseImpl(versionServiceProvider, dependencyServiceProvider, versionAggregateServiceProvider, mainPresenter.getSelectElementPresenter());
        var selectVersionUc = new SelectVersionUseCaseImpl(dependencyServiceProvider, versionAggregateServiceProvider, mainPresenter.getSelectVersionPresenter());
        var selectDependencyFilterUc = new SelectDependencyFilterUseCaseImpl(dependencyServiceProvider, mainPresenter.getSelectDependencyFilterPresenter());
        var selectDependencyVersionUc = new SelectDependencyVersionUseCaseImpl(elementClassServiceProvider, elementServiceProvider, versionServiceProvider, dependencyServiceProvider, versionAggregateServiceProvider, mainPresenter.getSelectDependencyVersionPresenter());

        // presentation view
        var mainController = new MainController(
            appProperties, // TODO => move to app
            mainModel,
            mainActions,
            openConnectionUc,
            closeConnectionUc,
            selectElementClassUc,
            selectElementDenominationUc,
            queryElementUc,
            selectElementUc,
            selectVersionUc,
            selectDependencyFilterUc,
            selectDependencyVersionUc
        );

        // presentation view
        JfxApplication.main(args, mainModel, mainActions);
        /*MainView mainView = new MainPanel();
        mainView.bindViewModel(mainModel);
        mainView.start();*/
    }


    @SneakyThrows
    private static Properties loadProperties() {
        var prop = new Properties();
        var propFileName = "/application.properties";

        var inputStream = Main.class.getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        return prop;
    }
}
