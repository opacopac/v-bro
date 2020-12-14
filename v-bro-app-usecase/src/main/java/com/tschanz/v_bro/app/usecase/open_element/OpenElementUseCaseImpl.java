package com.tschanz.v_bro.app.usecase.open_element;

import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class OpenElementUseCaseImpl implements OpenElementUseCase {
    private final MainState mainState;
    private final ReadVersionsUseCase readVersionsUc;


    @Override
    public void execute(OpenElementRequest request) {
        var requestTimestamp = System.currentTimeMillis();
        var elementId = Objects.requireNonNull(request.getElementId());

        log.info(String.format("UC: opening element id '%s'...", elementId));
        this.mainState.getElementState().setCurrentElementId(elementId);

        var readVersionRequest = new ReadVersionsRequest(elementId, true);
        this.readVersionsUc.execute(readVersionRequest);
    }
}
