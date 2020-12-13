package com.tschanz.v_bro.common.selected_list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SelectedListTest {
    @BeforeEach
    void setUp() {
    }


    @Test
    void constructor__creates_string_list_and_selects_item() {
        var slist = new SelectedList<>(List.of("A", "B", "C"), "B");

        assertEquals(3, slist.getItems().size());
        assertEquals("A", slist.getItems().get(0));
        assertEquals("B", slist.getItems().get(1));
        assertEquals("C", slist.getItems().get(2));
        assertEquals("B", slist.getSelectedItem());
    }


    @Test
    void constructor__creates_string_list_and_selects_no_item() {
        var slist = new SelectedList<>(List.of("A", "B", "C"), null);

        assertEquals(3, slist.getItems().size());
        assertEquals("A", slist.getItems().get(0));
        assertEquals("B", slist.getItems().get(1));
        assertEquals("C", slist.getItems().get(2));
        assertNull(slist.getSelectedItem());
    }


    @Test
    void constructor__creates_object_list_and_selects_item() {
        var item1 = List.of("A");
        var item2 = List.of("A", "B");
        var item3 = List.of("A", "B", "C");

        var slist = new SelectedList<>(List.of(item1, item2, item3), item2);

        assertEquals(3, slist.getItems().size());
        assertEquals(item1, slist.getItems().get(0));
        assertEquals(item2, slist.getItems().get(1));
        assertEquals(item3, slist.getItems().get(2));
        assertEquals(item2, slist.getSelectedItem());
    }


    @Test
    void constructor__throws_exception_on_null_item_list() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SelectedList<>(null, null);
        });
    }


    @Test
    void constructor__throws_exception_on_selected_item_not_in_item_list() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SelectedList<>(List.of("A", "B", "C"), "D");
        });
    }
}
