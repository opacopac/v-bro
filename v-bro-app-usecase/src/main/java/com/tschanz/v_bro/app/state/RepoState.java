package com.tschanz.v_bro.app.state;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;


@Getter(onMethod = @__({@Synchronized}))
@Setter(onMethod = @__({@Synchronized}))
public class RepoState {
    private ConnectionParameters connectionParameters;


    public RepoType getCurrentRepoType() {
        return this.connectionParameters != null
            ? this.connectionParameters.getRepoType()
            : null;
    }
}