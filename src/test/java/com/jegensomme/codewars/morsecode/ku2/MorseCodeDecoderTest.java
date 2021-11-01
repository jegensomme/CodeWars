package com.jegensomme.codewars.morsecode.ku2;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runners.JUnit4;

// TODO: Replace examples and use TDD development by writing your own tests

public class MorseCodeDecoderTest {
    @Test
    public void testSomething() {
        assertEquals("HEY JUDE", MorseCodeDecoder.decodeMorse(MorseCodeDecoder.decodeBitsAdvanced("0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000")));
    }
}
