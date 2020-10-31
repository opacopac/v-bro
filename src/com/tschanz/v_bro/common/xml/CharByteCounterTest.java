package com.tschanz.v_bro.common.xml;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CharByteCounterTest {
    @BeforeEach
    void setUp() {
    }


    @Test
    void countExtraBytes_counts_one_byte_characters_in_a_full_string_correctly() throws RepoException {
        String text1 = "lorem ipsum dolor sit amet 4!";

        int result = CharByteCounter.countExtraBytes(text1.toCharArray(), 0, text1.length());

        assertEquals(0, result);
    }


    @Test
    void countExtraBytes_counts_one_byte_characters_in_a_partial_string_correctly() throws RepoException {
        String text1 = "lorem ipsum dolor sit amet 4!";

        int result = CharByteCounter.countExtraBytes(text1.toCharArray(), 6, 5);

        assertEquals(0, result);
    }


    @Test
    void countExtraBytes_counts_two_byte_characters_correctly() throws RepoException {
        String text1 = "Äin täkst müt 5 ömlàuten";

        int result = CharByteCounter.countExtraBytes(text1.toCharArray(), 0, text1.length());

        assertEquals(5, result);
    }
}
