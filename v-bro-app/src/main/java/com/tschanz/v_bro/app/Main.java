package com.tschanz.v_bro.app;

import com.tschanz.v_bro.app.presentation.controller.MainControllerImpl;
import com.tschanz.v_bro.app.presentation.jfx.JfxApplication;
import com.tschanz.v_bro.app.presentation.jfx.presenter.JfxMainPresenter;
import com.tschanz.v_bro.app.presentation.viewmodel.MainViewModel;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
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
import com.tschanz.v_bro.app.usecase.read_dependency_denominations.ReadDependencyDenominationsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_dependency_element_classes.ReadDependencyElementClassesUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_version_aggregate.ReadVersionAggregateUseCaseImpl;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_dependency_denominations.SelectDependencyDenominationsUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.SelectDependencyFilterUseCaseImpl;
import com.tschanz.v_bro.app.usecase.select_version_filter.SelectVersionFilterUseCaseImpl;
import com.tschanz.v_bro.common.cache.LastNCache;
import com.tschanz.v_bro.data_structure.persistence.jdbc.service.*;
import com.tschanz.v_bro.data_structure.persistence.mock.service.*;
import com.tschanz.v_bro.data_structure.persistence.xml.service.*;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.jdbc.querybuilder.JdbcQueryBuilderImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcRepoConnectionService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_data.JdbcRepoDataService;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoConnectionService;
import com.tschanz.v_bro.repo.persistence.xml.node_parser.XmlNodeParser;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoConnectionService;
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
        var jdbcRepoConnectionService = new JdbcRepoConnectionService(jdbcConnectionFactory);
        var jdbcRepoMetadataService = new JdbcRepoMetadataServiceImpl(jdbcConnectionFactory);
        var jdbcQueryBuilder = new JdbcQueryBuilderImpl(jdbcConnectionFactory);
        var jdbcRepoDataService = new JdbcRepoDataService(jdbcConnectionFactory, jdbcQueryBuilder);
        var jdbcDataStructureService = new JdbcDataStructureService(jdbcRepoMetadataService);
        var jdbcRepoConnectionServiceWrapper = new JdbcRepoConnectionServiceWrapper(jdbcRepoConnectionService, jdbcDataStructureService);
        var jdbcElementClassService = new JdbcElementClassService(jdbcRepoMetadataService);
        var jdbcDependencyStructureService = new JdbcDependencyStructureService(jdbcDataStructureService);
        var jdbcElementService = new JdbcElementService(jdbcRepoMetadataService, jdbcRepoDataService);
        var jdbcVersionService = new JdbcVersionService(jdbcRepoDataService, jdbcElementService);
        var jdbcDenominationService = new JdbcDenominationService(jdbcElementService);
        var jdbcVersionAggregateService = new JdbcVersionAggregateService(jdbcRepoMetadataService, jdbcRepoDataService, jdbcElementService, jdbcVersionService);
        var jdbcVersionAggregateServiceCache = new JdbcVersionAggregateServiceCache(jdbcVersionAggregateService, new LastNCache<>(10));
        var jdbcDependencyService = new JdbcDependencyService(jdbcRepoMetadataService, jdbcRepoDataService, jdbcDataStructureService, jdbcElementService, jdbcVersionService, jdbcVersionAggregateServiceCache);

        // persistence xml
        var xmlRepoConnectionService = new XmlRepoConnectionService();
        var xmlDataStructureService = new XmlDataStructureService(xmlRepoConnectionService);
        var xmlRepoConnectionServiceWrapper = new XmlRepoConnectionServiceWrapper(xmlRepoConnectionService, xmlDataStructureService);
        var xmlInputFactory = XMLInputFactory.newInstance();
        var xmlNodeParser = new XmlNodeParser(xmlInputFactory);
        var xmlElementClassService = new XmlElementClassService(xmlDataStructureService);
        var xmlDependencyStructureService = new XmlDependencyStructureService(xmlDataStructureService);
        var xmlElementService = new XmlElementService(xmlDataStructureService, xmlNodeParser);
        var xmlVersionService = new XmlVersionService(xmlDataStructureService, xmlNodeParser);
        var xmlDenominationService = new XmlDenominationService(xmlDataStructureService, xmlNodeParser);
        var xmlVersionAggregateService = new XmlVersionAggregateService(xmlDataStructureService, xmlNodeParser);
        var xmlDependencyService = new XmlDependencyService(xmlDataStructureService, xmlElementService, xmlVersionService, xmlVersionAggregateService);

        // persistence mock
        var mockRepo = new MockRepoConnectionService();
        var mockElementService = new MockElementService();
        var mockElementClassService = new MockElementClassService();
        var mockDependencyStructureService = new MockDependencyStructureService();
        var mockVersionDataService = new MockVersionService();
        var mockDenominationService = new MockDenominationService();
        var mockVersionAggregateService = new MockVersionAggregateService();
        var mockDependencyService = new MockDependencyService();

        // persistence service providers
        var repoConnectionServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcRepoConnectionServiceWrapper, RepoType.XML, xmlRepoConnectionServiceWrapper, RepoType.MOCK, mockRepo);
        var elementClassServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcElementClassService, RepoType.XML, xmlElementClassService, RepoType.MOCK, mockElementClassService);
        var dependencyStructureServiceProvider = new RepoServiceProvider<>(RepoType.JDBC, jdbcDependencyStructureService, RepoType.XML, xmlDependencyStructureService, RepoType.MOCK, mockDependencyStructureService);
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
        var readDependencyElementClassesUc = new ReadDependencyElementClassesUseCaseImpl(mainState, dependencyStructureServiceProvider, mainPresenter.getStatusPresenter(), mainPresenter.getDependencyElementClassPresenter());
        var selectDependencyDenominationsUc = new SelectDependencyDenominationsUseCaseImpl(mainState, readDependenciesUc);
        var readDepdendencyDenominationsUc = new ReadDependencyDenominationsUseCaseImpl(mainState, denominationServiceProvider, mainPresenter.getDependencyDenominationsPresenter(), mainPresenter.getStatusPresenter());
        var selectDependencyElementClassUc = new SelectDependencyElementClassUseCaseImpl(mainState, readDepdendencyDenominationsUc, readDependenciesUc);
        var selectDependencyFilterUc = new SelectDependencyFilterUseCaseImpl(mainState, readDependenciesUc, mainPresenter.getStatusPresenter(), readDependencyElementClassesUc, selectDependencyElementClassUc);
        var openVersionUc = new OpenVersionUseCaseImpl(mainState, readVersionAggregateUc, readDependenciesUc, mainPresenter.getVersionTimelinePresenter(), mainPresenter.getStatusPresenter());
        var readVersionsUc = new ReadVersionsUseCaseImpl(mainState, versionServiceProvider, openVersionUc, mainPresenter.getStatusPresenter(), mainPresenter.getVersionTimelinePresenter());
        var selectVersionFilterUc = new SelectVersionFilterUseCaseImpl(mainState, readVersionsUc, mainPresenter.getStatusPresenter(), mainPresenter.getVersionFilterPresenter());
        var openElementUc = new OpenElementUseCaseImpl(mainState, elementServiceProvider, mainPresenter.getElementPresenter(), readVersionsUc, mainPresenter.getStatusPresenter());
        var queryElementsUc = new QueryElementsUseCaseImpl(mainState, elementServiceProvider);
        var selectDenominationsUc = new SelectDenominationsUseCaseImpl(mainState, mainPresenter.getDenominationsPresenter());
        var readDenominationUc = new ReadDenominationUseCaseImpl(mainState, denominationServiceProvider, mainPresenter.getDenominationsPresenter(), mainPresenter.getStatusPresenter());
        var openElementClassUc = new OpenElementClassUseCaseImpl(mainState, mainPresenter.getElementClassPresenter(), mainPresenter.getStatusPresenter(), readDenominationUc, selectDenominationsUc, queryElementsUc, openElementUc, readDependencyElementClassesUc, selectDependencyElementClassUc);
        var readElementClassesUc = new ReadElementClassesUseCaseImpl(mainState, elementClassServiceProvider, mainPresenter.getStatusPresenter(), mainPresenter.getElementClassPresenter());
        var openRepoUc = new OpenRepoUseCaseImpl(mainState, repoConnectionServiceProvider, mainPresenter.getRepoConnectionPresenter(), mainPresenter.getStatusPresenter(), readElementClassesUc, openElementClassUc);
        var closeRepoUc = new CloseRepoUseCaseImpl(mainState, repoConnectionServiceProvider, mainPresenter.getRepoConnectionPresenter(), mainPresenter.getStatusPresenter(), readElementClassesUc, openElementClassUc, selectDependencyElementClassUc);
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
            selectDependencyElementClassUc,
            selectDependencyDenominationsUc,
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
