package com.github.ki10v01t.jcode_test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JcodeTestApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void uuidCheck() {
		String invalidUUID = "123";

		assertThrows(IllegalArgumentException.class, () -> UUID.fromString(invalidUUID));
	}

}
