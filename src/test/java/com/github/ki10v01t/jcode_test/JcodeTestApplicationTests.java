package com.github.ki10v01t.jcode_test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.github.ki10v01t.jcode_test.entity.OperationType;
import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.exception.PaymentNotFoundException;
import com.github.ki10v01t.jcode_test.service.PaymentService;
import com.github.ki10v01t.jcode_test.service.PaymentValidator;
import com.github.ki10v01t.jcode_test.service.WalletService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ContextConfiguration(classes=JcodeTestApplication.class)
@WebMvcTest
class JcodeTestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PaymentService paymentService;
	@MockitoBean
	private WalletService walletService;
	@MockitoBean
    private PaymentValidator paymentValidator;

	private List<PaymentDto> getPaymentsByWalletId(String template) {
		UUID walletId = UUID.fromString(template);
		PaymentDto payment1 = new PaymentDto(walletId, OperationType.DEPOSIT, 1000L);
		PaymentDto payment2 = new PaymentDto(walletId, OperationType.DEPOSIT, 500L);


		return List.of(payment1, payment2);
	}

	@Test
	public void testUnexistingdPaymentGetNotFoundExample() throws Exception {
		String invalidTemplate = "cf9e7d45-151d-45c4-a26b-3c57642d561d";
		//UUID invalidUUID = UUID.randomUUID();
		UUID invalidUUID = UUID.fromString(invalidTemplate);
		try {
			paymentService.getPaymentsByWalletId(invalidUUID);
		} catch (Exception e) {
			System.out.println("Пусто!");
		}

		Mockito.when(paymentService.getPaymentsByWalletId(invalidUUID)).thenThrow(new PaymentNotFoundException("The wallet for the wallet_id you specified was not found"));
		String req = "/api/v1/wallets/payments/"+ invalidUUID.toString();
		mockMvc.perform(get(req))
		// 200!=404
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$.message").value("The wallet for the wallet_id you specified was not found"));
	}

	@Test
	public void testInvalidPaymentGetInvalidUUIDExample() throws Exception {
		String invalidUUID = "1"; 

		Mockito.when(walletService.transformWalletUUID(invalidUUID, Wallet.uuidRegex)).thenThrow(new IllegalArgumentException("The argument you passed is not valid"));
		String req = "/api/v1/wallets/payments/"+ invalidUUID.toString();
		mockMvc.perform(get(req))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$.message").value("The argument you passed is not valid"));
	}

	@Test
	public void testInvalidWalletBalanceInvalidUUIDExample() {
		String invalidUUID = "1"; 

		Mockito.when(walletService.transformWalletUUID(invalidUUID, Wallet.uuidRegex)).thenThrow(new IllegalArgumentException("The argument you passed is not valid"));
		// String req = "/api/v1/wallets/"+ invalidUUID.toString();
		// mockMvc.perform(get(req))
		// 	.andExpect(status().isBadRequest())
		// 	.andExpect(jsonPath("$.length()").value(2))
		// 	.andExpect(jsonPath("$.message").value("The argument you passed is not valid"));
	}

	@Test
	public void testValidPaymentGetExample() throws Exception {
		String walletId = "cf9e7d45-151d-45c4-a26b-3c57642d561e";
		//List<PaymentDto> returnedPaymentValues = paymentService.getPaymentsByWalletId(UUID.fromString(walletId));

		Mockito.when(paymentService.getPaymentsByWalletId(UUID.fromString(walletId))).thenReturn(getPaymentsByWalletId(walletId));
		String req = "/api/v1/wallets/payments"+ walletId;
		mockMvc.perform(get(URI.create(req)))
			.andExpect(status().isOk())
			// Не работает. Просто не видит значения.
			.andExpect(jsonPath("$[0].walletId").value("cf9e7d45-151d-45c4-a26b-3c57642d561e"))
			.andExpect(jsonPath("$[0].operationType").value("DEPOSIT"))
			.andExpect(jsonPath("$[0].amount").value(1000));
	}
	

}
