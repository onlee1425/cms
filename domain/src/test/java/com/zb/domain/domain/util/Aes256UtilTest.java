package com.zb.domain.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Aes256UtilTest {

    @Test
    void encrypt() {
        String enc= Aes256Util.encrypt("pass");
        assertEquals(Aes256Util.decrypt(enc),"pass");
    }
}
