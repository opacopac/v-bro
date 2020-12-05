package com.tschanz.v_bro.app.presentation.viewmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public abstract class RepoConnectionItem {
    @NonNull public final RepoType repoType;
}
