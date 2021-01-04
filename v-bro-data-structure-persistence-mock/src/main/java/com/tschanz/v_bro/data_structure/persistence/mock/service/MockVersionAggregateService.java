package com.tschanz.v_bro.data_structure.persistence.mock.service;

import com.tschanz.v_bro.common.KeyValue;
import com.tschanz.v_bro.data_structure.domain.model.AggregateNode;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;


public class MockVersionAggregateService implements VersionAggregateService {
    @Override
    public VersionAggregate readVersionAggregate(@NonNull VersionData version) {
        return new VersionAggregate(
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
