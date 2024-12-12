package com.github.ki10v01t.jcode_test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class SimpleTests {

	@Test
	void contextLoads() {
	}

	@Test
	void uuidCheck() {
		String invalidUUID = "123";

		assertThrows(IllegalArgumentException.class, () -> UUID.fromString(invalidUUID));
	}

}

