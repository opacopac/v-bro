package com.tschanz.v_bro.app;

import com.tschanz.v_bro.app.presentation.controller.MainControllerImpl;
import com.tschanz.v_bro.app.presentation.jfx.JfxApplication;
import com.tschanz.v_bro.app.presentation.jfx.presenter.JfxMainPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.close_repo.CloseRepoUseCaseImpl;
import com.tschanz.v_bro.app.usecase.open_dependency_version.OpenDependencyVersionUseCaseImpl;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCaseImpl;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCaseImpl;
import com.tschanz.v_bro.app.usecase.open_repo.OpenRepoUseCaseImpl;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCaseImpl;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_version_aggregate.ReadVersionAggregateUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCaseImpl;
import com.tschanz.v_bro.common.cache.LastNCache;
import com.tschanz.v_bro.data_structure.persistence.jdbc.service.*;
import com.tschanz.v_bro.data_structure.persistence.mock.service.*;
import com.tschanz.v_bro.data_structure.persistence.xml.service.*;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilderImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoService2;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import lombok.SneakyThrows;

import javax.xml.stream.XMLInputFactory;
import java.io.FileNotFoundException;
import java.util.Properties;


public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        /// poor man's DI ///

        var appProperties = loadProperties();

        // persistence jdbc
        Class.forName("oracle.jdbc.OracleDriver");
        var jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        var jdbcRepo = new JdbcRepoService(jdbcConnectionFactory);
        var jdbcRepoMetadata = new JdbcRepoMetadataServiceImpl(jdbcConnectionFactory);
        var jdbcQueryBuilder = new JdbcQueryBuilderImpl(jdbcConnectionFactory);
        var jdbcRepoDataService = new JdbcRepoDataService(jdbcConnectionFactory, jdbcQueryBuilder);
        var jdbcElementClassService = new JdbcElementClassService(jdbcRepoMetadata);
        var jdbcElementService = new JdbcElementService(jdbcRepo, jdbcRepoMetadata, jdbcRepoDataService);
        var jdbcVersionService = new JdbcVersionService(jdbcRepo, jdbcRepoDataService, jdbcElementService);
        var jdbcDenominationService = new JdbcDenominationService(jdbcElementService);
        var jdbcVersionAggregateService = new JdbcVersionAggregateService(jdbcRepo, jdbcRepoMetadata, jdbcRepoDataService, jdbcElementService, jdbcVersionService);
        var jdbcVersionAggregateServiceCache = new JdbcVersionAggregateServiceCache(jdbcVersionAggregateService, new LastNCache<>(10));
        var jdbcDependencyService = new JdbcDependencyService(jdbcElementService, jdbcVersionService, jdbcVersionAggregateServiceCache);

        // persistence xml
        var xmlRepoService = new XmlRepoService();
        var xmlDataStructureService = new XmlDataStructureService(xmlRepoService);
        var xmlInputFactory = XMLInputFactory.newInstance();
        var xmlNodeParser = new XmlNodeParser(xmlInputFactory);
        var xmlElementClassService = new XmlElementClassService(xmlDataStructureService);
        var xmlElementService = new XmlElementService(xmlDataStructureService, xmlNodeParser);
        var xmlVersionService = new XmlVersionService(xmlDataStructureService, xmlNodeParser);
        var xmlDenominationService = new XmlDenominationService(xmlDataStructureService, xmlNodeParser);
        var xmlVersionAggregateService = new XmlVersionAggregateService(xmlDataStructureService, xmlNodeParser);
        var xmlDependencyService = new XmlDependencyService(xmlDataStructureService, xmlElementService, xmlVersionService, xmlVersionAggregateService);

        // persistence mock
        var mockRepo = new MockRepoService2();
        var mockElementService = new MockElementService();
        var mockElementClassService = new MockElementClassService();
        var mockVersionDataService = new MockVersionService();
        var mockDenominationService = new MockDenominationService();
        var mockVersionAggregateService = new MockVersionAggregateService();
        var mockDependencyService = new MockDependencyService();

        // persistence service providers
        var repoServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcRepo, RepoType.XML, xmlRepoService, RepoType.MOCK, mockRepo);
        var elementClassServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementClassService, RepoType.XML, xmlElementClassService, RepoType.MOCK, mockElementClassService);
        var elementServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementService, RepoType.XML, xmlElementService, RepoType.MOCK, mockElementService);
        var versionServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcVersionService, RepoType.XML, xmlVersionService, RepoType.MOCK, mockVersionDataService);
        var denominationServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcDenominationService, RepoType.XML, xmlDenominationService, RepoType.MOCK, mockDenominationService);
        var versionAggregateServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcVersionAggregateServiceCache, RepoType.XML, xmlVersionAggregateService, RepoType.MOCK, mockVersionAggregateService);
        var dependencyServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcDependencyService, RepoType.XML, xmlDependencyService, RepoType.MOCK, mockDependencyService);

        // presentation viewmodel & presenter
        var mainModel = new MainViewModel();
        var mainPresenter = new JfxMainPresenter(mainModel);

        // app use cases
        var mainState = new MainState();
        var readVersionAggregateUc = new ReadVersionAggregateUseCaseImpl(mainState, versionAggregateServiceProvider, mainPresenter.getVersionAggregatePresenter(), mainPresenter.getStatusPresenter());
        var readDependenciesUc = new ReadDependenciesUseCaseImpl(mainState, dependencyServiceProvider, mainPresenter.getDependencyPresenter(), mainPresenter.getStatusPresenter());
        var selectDependencyFilterUc = new SelectDependencyFilterUseCaseImpl(mainState, readDependenciesUc, mainPresenter.getStatusPresenter());
        var openVersionUc = new OpenVersionUseCaseImpl(mainState, readVersionAggregateUc, readDependenciesUc, mainPresenter.getVersionTimelinePresenter(), mainPresenter.getStatusPresenter());
        var readVersionsUc = new ReadVersionsUseCaseImpl(mainState, versionServiceProvider, mainPresenter.getStatusPresenter(), mainPresenter.getVersionTimelinePresenter());
        var selectVersionFilterUc = new SelectVersionFilterUseCaseImpl(mainState, readVersionsUc, mainPresenter.getStatusPresenter());
        var queryElementsUc = new QueryElementsUseCaseImpl(mainState, elementServiceProvider);
        var openElementUc = new OpenElementUseCaseImpl(mainState, elementServiceProvider, mainPresenter.getElementPresenter(), readVersionsUc, openVersionUc, mainPresenter.getStatusPresenter());
        var selectDenominationsUc = new SelectDenominationsUseCaseImpl(mainState, mainPresenter.getDenominationsPresenter());
        var readDenominationUc = new ReadDenominationUseCaseImpl(mainState, denominationServiceProvider, mainPresenter.getDenominationsPresenter(), mainPresenter.getStatusPresenter());
        var openElementClassUc = new OpenElementClassUseCaseImpl(mainState, mainPresenter.getElementClassListPresenter(), readDenominationUc, selectDenominationsUc, queryElementsUc, openElementUc, mainPresenter.getStatusPresenter());
        var readElementClassesUc = new ReadElementClassesUseCaseImpl(mainState, elementClassServiceProvider, mainPresenter.getStatusPresenter(), mainPresenter.getElementClassListPresenter());
        var openRepoUc = new OpenRepoUseCaseImpl(mainState, repoServiceProvider, mainPresenter.getRepoPresenter(), mainPresenter.getStatusPresenter(), readElementClassesUc, openElementClassUc);
        var closeRepoUc = new CloseRepoUseCaseImpl(mainState, repoServiceProvider, mainPresenter.getRepoPresenter(), mainPresenter.getStatusPresenter(), readElementClassesUc, openElementClassUc);
        var openDependencyVersionUc = new OpenDependencyVersionUseCaseImpl(openElementClassUc, openElementUc, openVersionUc, mainPresenter.getStatusPresenter());

        // presentation controller
        var mainController = new MainControllerImpl(
            appProperties, // TODO => move to app
            mainModel,
            openRepoUc,
            closeRepoUc,
            openElementClassUc,
            selectDenominationsUc,
            queryElementsUc,
            openElementUc,
            selectVersionFilterUc,
            openVersionUc,
            selectDependencyFilterUc,
            openDependencyVersionUc
        );

        // presentation view
        JfxApplication.main(args, mainModel, mainController);
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
