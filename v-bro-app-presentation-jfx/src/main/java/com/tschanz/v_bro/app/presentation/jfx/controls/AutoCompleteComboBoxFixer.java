package com.tschanz.v_bro.app.presentation.jfx.controls;

import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.scene.control.ComboBox;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AutoCompleteComboBoxFixer {
    // workaround to fix problem described here: https://github.com/controlsfx/controlsfx/issues/927
    public static <T> void fix(ComboBox<T> comboBox, SuggestionProvider<T> provider) {
        AtomicReference<Boolean> cleared = new AtomicReference<>(false);

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, b, c) -> {
            provider.clearSuggestions();
            cleared.set(true);
        });

        comboBox.getEditor().setOnKeyPressed(e -> {
            if (cleared.get()){
                provider.clearSuggestions();
                provider.addPossibleSuggestions(comboBox.getItems());
                cleared.set(false);
            }
        });
    }
}
