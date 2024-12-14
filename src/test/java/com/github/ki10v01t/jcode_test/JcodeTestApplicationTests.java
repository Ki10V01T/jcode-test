package com.github.ki10v01t.jcode_test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.service.PaymentService;
import com.github.ki10v01t.jcode_test.service.WalletService;

@WebMvcTest
@ContextConfiguration(classes=JcodeTestApplication.class)
class JcodeTestApplicationTests {

	private MockMvc mockMvc;

	@MockitoBean
	private PaymentService paymentService;
	@MockitoBean
	private WalletService walletService;

	// @Test
	// public void testGetExample() throws Exception {
	// 	Optional<Payment> payment;
	// 	List<Payment> payments = new ArrayList<>();
	// 	UUID invalidUUID = UUID.randomUUID();

	// 	Mockito.when(paymentService.getOnePaymentByWalletId(invalidUUID)).thenReturn(payment);
	// 	mockMvc.perform(get("/api/v1/wallets/"+ payment.get().toString())).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
	// 		.andExpect(jsonPath("$[0].name", Matchers.equalTo("Mike")));
	// }


	

}
