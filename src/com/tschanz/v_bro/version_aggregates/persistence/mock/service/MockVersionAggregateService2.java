package com.tschanz.v_bro.version_aggregates.persistence.mock.service;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.version_aggregates.domain.model.AggregateNode;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.versions.domain.model.Pflegestatus;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class MockVersionAggregateService2 implements VersionAggregateService {
    @Override
    public VersionAggregate readVersionAggregate(String elementClass, String elementId, String versionId) throws RepoException {
        return new VersionAggregate(
            new VersionInfo(
                "111",
                LocalDate.of(2020,1,1),
                LocalDate.of(2020,5,31),
                Pflegestatus.TEST
            ),
            new AggregateNode(
                "A_SORTIMENT_E",
                List.of(
                    new KeyValue("ID", "19213012"),
                    new KeyValue("CODE", "SBB_WEBSHOP")
                ),
                List.of(
                    new AggregateNode(
                        "A_SORTIMENT_V",
                        List.of(
                            new KeyValue("ID", "19213012"),
                            new KeyValue("BEZEICHNUNG", "SBB Webshop"),
                            new KeyValue("GUELTIG_VON", "2020-01-01"),
                            new KeyValue("GUELTIG_BIS", "9999-12-31")
                        ),
                        List.of(
                            new AggregateNode(
                                "A_SORTIMENT_X_PRODUKT",
                                List.of(
                                    new KeyValue("ID", "19213012"),
                                    new KeyValue("ID_PRODUKT", "19213012")
                                ),
                                Collections.emptyList()
                            ),
                            new AggregateNode(
                                "A_SORTIMENT_X_PRODUKT",
                                List.of(
                                    new KeyValue("ID", "123412322"),
                                    new KeyValue("ID_PRODUKT", "1234134")
                                ),
                                Collections.emptyList()
                            )
                        )
                    )
                )
            )
        );
    }
}
