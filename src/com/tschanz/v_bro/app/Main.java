package com.tschanz.v_bro.app;

import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.mock.service.MockElementService2;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCaseImpl;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCase;
import com.tschanz.v_bro.elements.usecase.read_elements.ReadElementsUseCaseImpl;
import com.tschanz.v_bro.elements.xml.service.ElementParser;
import com.tschanz.v_bro.elements.jdbc.service.JdbcElementService;
import com.tschanz.v_bro.elements.xml.service.ElementClassParser;
import com.tschanz.v_bro.elements.xml.service.XmlElementService;
import com.tschanz.v_bro.app.swing.MainController;
import com.tschanz.v_bro.app.swing.MainPanel;
import com.tschanz.v_bro.app.swing.MainView;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.jdbc.service.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.jdbc.service.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.jdbc.service.JdbcRepoService;
import com.tschanz.v_bro.repo.jdbc.service.JdbcRepoMetadata;
import com.tschanz.v_bro.repo.mock.service.MockRepoService2;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCaseImpl;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCaseImpl;
import com.tschanz.v_bro.repo.xml.service.XmlRepoService;
import com.tschanz.v_bro.versioning.domain.service.VersionService;
import com.tschanz.v_bro.versioning.jdbc.service.JdbcVersionService;
import com.tschanz.v_bro.versioning.mock.service.MockVersionService2;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.versioning.usecase.read_versions.ReadVersionsUseCaseImpl;
import com.tschanz.v_bro.versioning.xml.service.VersionParser;
import com.tschanz.v_bro.versioning.xml.service.XmlVersionService;

import javax.xml.parsers.SAXParserFactory;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws ClassNotFoundException, RepoException {
        /// poor man's DI

        // xml repo
        XmlRepoService xmlRepo = new XmlRepoService();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        ElementClassParser elementClassParser = new ElementClassParser(saxParserFactory);
        ElementParser elementParser = new ElementParser(saxParserFactory);
        VersionParser versionParser = new VersionParser(saxParserFactory);
        XmlElementService xmlElementClassService = new XmlElementService(xmlRepo, elementClassParser, elementParser);
        XmlVersionService xmlVersionDataService = new XmlVersionService(xmlRepo, versionParser);

        // jdbc repo
        JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        JdbcRepoService jdbcRepo = new JdbcRepoService(jdbcConnectionFactory);
        JdbcRepoMetadata jdbcRepoMetadata = new JdbcRepoMetadata(jdbcConnectionFactory);
        JdbcElementService jdbcElementClassService = new JdbcElementService(jdbcRepo, jdbcRepoMetadata);
        JdbcVersionService jdbcVersionDataService = new JdbcVersionService(jdbcRepo, jdbcRepoMetadata);

        // mock repo
        MockRepoService2 mockRepo = new MockRepoService2();
        MockElementService2 mockElementClassService = new MockElementService2();
        MockVersionService2 mockVersionDataService = new MockVersionService2();

        // use cases
        Map<RepoType, RepoService> repoMap = Map.of(RepoType.JDBC, jdbcRepo, RepoType.XML, xmlRepo, RepoType.MOCK, mockRepo);
        Map<RepoType, ElementService> elementClassServiceMap = Map.of(RepoType.JDBC, jdbcElementClassService, RepoType.XML, xmlElementClassService, RepoType.MOCK, mockElementClassService);
        Map<RepoType, VersionService> versionDataServiceMap = Map.of(RepoType.JDBC, jdbcVersionDataService, RepoType.XML, xmlVersionDataService, RepoType.MOCK, mockVersionDataService);
        OpenConnectionUseCase openConnectionUc = new OpenConnectionUseCaseImpl(repoMap);
        CloseConnectionUseCase closeConnectionUc = new CloseConnectionUseCaseImpl(repoMap);
        ReadElementClassesUseCase readElementClassesUc = new ReadElementClassesUseCaseImpl(elementClassServiceMap);
        ReadElementsUseCase readElementsUc = new ReadElementsUseCaseImpl(elementClassServiceMap);
        ReadVersionsUseCase readVersionsUc = new ReadVersionsUseCaseImpl(versionDataServiceMap);

        // view
        MainView mainView = new MainPanel();
        new MainController(
            mainView,
            openConnectionUc,
            closeConnectionUc,
            readElementClassesUc,
            readElementsUc,
            readVersionsUc
        );
        mainView.start();;
    }
}
