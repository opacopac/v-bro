package com.tschanz.v_bro.app.presentation.jfx.view;

import com.tschanz.v_bro.app.presentation.controller.ElementController;
import com.tschanz.v_bro.app.presentation.view.ElementView;
import com.tschanz.v_bro.app.presentation.viewmodel.element.ElementItem;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;


@Log
public class JfxElementView implements ElementView, Initializable {
    @FXML public TextField elementQueryTextField;
    private final SuggestionProvider<ElementItem> suggestionProvider = new ElementSuggestionProvider();
    private ElementController elementController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var binding = new AutoCompletionTextFieldBinding<>(this.elementQueryTextField, this.suggestionProvider);
        binding.setOnAutoCompleted(this::onElementSelected);
    }


    @Override
    public void bindViewModel(ElementController elementController) {
        this.elementController = elementController;
    }


    @FXML private void onElementSelected(AutoCompletionBinding.AutoCompletionEvent<ElementItem> event) {
        var elementId = event.getCompletion().getId();
        this.elementController.onElementSelected(elementId);
    }


    public class ElementSuggestionProvider extends SuggestionProvider<ElementItem> {
        @Override
        public Collection<ElementItem> call(AutoCompletionBinding.ISuggestionRequest request) {
            return JfxElementView.this.elementController.onQueryElement(request.getUserText());
        }


        @Override
        protected Comparator<ElementItem> getComparator() {
            return Comparator.comparing(ElementItem::getName);
        }


        @Override
        protected boolean isMatch(ElementItem elementItem, AutoCompletionBinding.ISuggestionRequest iSuggestionRequest) {
            return elementItem.getName().toLowerCase().contains(iSuggestionRequest.getUserText().toLowerCase());
        }
    };
}
