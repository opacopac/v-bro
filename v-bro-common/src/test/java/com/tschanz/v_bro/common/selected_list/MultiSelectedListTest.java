package com.tschanz.v_bro.common.selected_list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MultiSelectedListTest {
    @BeforeEach
    void setUp() {
    }


    @Test
    void constructor__creates_string_list_and_selects_items() {
        var mslist = new MultiSelectedList<>(List.of("A", "B", "C"), List.of("A", "C"));

        assertEquals(3, mslist.getItems().size());
        assertEquals("A", mslist.getItems().get(0));
        assertEquals("B", mslist.getItems().get(1));
        assertEquals("C", mslist.getItems().get(2));
        assertEquals(2, mslist.getSelectedItems().size());
        assertEquals("A", mslist.getSelectedItems().get(0));
        assertEquals("C", mslist.getSelectedItems().get(1));
    }


    @Test
    void constructor__creates_string_list_and_selects_no_items() {
        var mslist = new MultiSelectedList<>(List.of("A", "B", "C"), Collections.emptyList());

        assertEquals(3, mslist.getItems().size());
        assertEquals("A", mslist.getItems().get(0));
        assertEquals("B", mslist.getItems().get(1));
        assertEquals("C", mslist.getItems().get(2));
        assertEquals(0, mslist.getSelectedItems().size());
    }


    @Test
    void constructor__creates_object_list_and_selects_items() {
        var item1 = List.of("A");
        var item2 = List.of("A", "B");
        var item3 = List.of("A", "B", "C");

        var mslist = new MultiSelectedList<>(List.of(item1, item2, item3), List.of(item2));

        assertEquals(3, mslist.getItems().size());
        assertEquals(item1, mslist.getItems().get(0));
        assertEquals(item2, mslist.getItems().get(1));
        assertEquals(item3, mslist.getItems().get(2));
        assertEquals(1, mslist.getSelectedItems().size());
        assertEquals(item2, mslist.getSelectedItems().get(0));
    }


    @Test
    void constructor__throws_exception_on_null_item_list() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new MultiSelectedList<>(null, List.of("A", "C"));
        });
    }


    @Test
    void constructor__throws_exception_on_null_selected_list() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new MultiSelectedList<>(List.of("A", "B", "C"), null);
        });
    }


    @Test
    void constructor__throws_exception_on_selected_item_not_in_item_list() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SelectedList<>(List.of("A", "B", "C"), List.of("A", "D"));
        });
    }
}
