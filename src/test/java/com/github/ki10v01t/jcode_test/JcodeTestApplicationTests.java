package com.github.ki10v01t.jcode_test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.github.ki10v01t.jcode_test.entity.OperationType;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.exception.NotFoundException;
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
		PaymentDto payment1 = new PaymentDto.PaymentDtoBuilder().setWalletId(walletId).setOperationType(OperationType.DEPOSIT).setAmount(1000L).build();
		PaymentDto payment2 = new PaymentDto.PaymentDtoBuilder().setWalletId(walletId).setOperationType(OperationType.DEPOSIT).setAmount(500L).build();


		return List.of(payment1, payment2);
	}

	@Test
	public void testUnexistingdPaymentGetNotFoundExample() throws Exception {
		String invalidTemplate = "cf9e7d45-151d-45c4-a26b-3c57642d561d";
		//UUID invalidUUID = UUID.randomUUID();
		UUID invalidUUID = UUID.fromString(invalidTemplate);
		
		// Исключение не выбрасывается, хотя в проде всё норм.
		// assertThrows(PaymentNotFoundException.class, () ->
		// paymentService.getPaymentsByWalletId(invalidUUID));

		// То же самое
		Mockito.when(paymentService.getPaymentsByWalletId(invalidUUID)).thenThrow(new NotFoundException("The wallet for the wallet_id you specified was not found"));
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
	public void testUnexistingWalletBalanceInvalidUUIDExample() throws Exception, RuntimeException {
		String invalidTemplate = "cf9e7d45-151d-45c4-a26b-3c57642d561d"; 
		UUID invalidUUID = UUID.fromString(invalidTemplate);

		//сломалось из-за обработки ошибок в сервисе
		Mockito.when(walletService.getWalletById(invalidUUID)).thenThrow(new NotFoundException("The wallet for the wallet_id you specified was not found"));
		String req = "/api/v1/wallets/"+ invalidUUID.toString();
		mockMvc.perform(get(req))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$.message").value("The wallet for the wallet_id you specified was not found"));
	}

	@Test
	public void testValidWalletBalanceExample() throws Exception {
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
