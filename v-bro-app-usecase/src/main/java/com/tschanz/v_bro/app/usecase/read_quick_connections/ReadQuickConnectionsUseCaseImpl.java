package com.tschanz.v_bro.app.usecase.read_quick_connections;

import com.tschanz.v_bro.app.presenter.quick_connections.QuickConnectionsPresenter;
import com.tschanz.v_bro.app.presenter.quick_connections.QuickConnectionsResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.repo.domain.model.QuickConnection;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class ReadQuickConnectionsUseCaseImpl implements ReadQuickConnectionsUseCase {
    private final static String PROPERTIES_FILE_NAME = "./v-bro.properties";
    private final static String QUICKCONNECTIONS_PROPERTY_PREFIX = "quickconnection.";
    private final AppState appState;
    private final StatusPresenter statusPresenter;
    private final QuickConnectionsPresenter quickConnectionsPresenter;


    @Override
    public void execute(ReadQuickConnectionsRequest request) {
        var msgStart =  String.format("UC: reading quick connections from property file %s...", PROPERTIES_FILE_NAME);
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false);
        this.statusPresenter.present(statusResponse1);

        var properties = this.loadProperties();
        var quickConnections = this.parseQuickConnectionsList(properties);

        String msgSuccess = String.format("successfully read %d quick connections from property file", quickConnections.size());
        log.info(msgSuccess);
        var statusResponse2 = new StatusResponse(msgSuccess, false);
        this.statusPresenter.present(statusResponse2);

        this.appState.setQuickConnections(quickConnections);

        var response = QuickConnectionsResponse.fromDomain(quickConnections);
        this.quickConnectionsPresenter.present(response);
    }


    @SneakyThrows
    private Properties loadProperties() {
        var properties = new Properties();

        var file = new File(PROPERTIES_FILE_NAME);
        if (file.exists()) {
            log.info("reading properties file");
            var stream = new FileInputStream(file);
            properties.load(stream);
        } else {
            log.info("no properties file present");
        }

        return properties;
    }


    private List<QuickConnection> parseQuickConnectionsList(Properties appProperties) {
        return appProperties.stringPropertyNames()
            .stream()
            .filter(key -> key.startsWith(QUICKCONNECTIONS_PROPERTY_PREFIX))
            .sorted()
            .map(key -> this.parseQuickConnectionProperty(appProperties.getProperty(key)))
            .collect(Collectors.toList());
    }


    private QuickConnection parseQuickConnectionProperty(String propertyValue) {
        String[] parts = propertyValue.split(",", -1); // -1: don't cut of trailing empty parts
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid quick connection '" + propertyValue + "': invalid number of comma separators");
        }

        RepoType repoType = RepoType.valueOf(parts[1]);

        return new QuickConnection(parts[0], repoType, parts[2], parts[3], parts[4], parts[5]);
    }
}
