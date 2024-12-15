package com.github.ki10v01t.jcode_test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.github.ki10v01t.jcode_test.controller.WalletApiRestController;
import com.github.ki10v01t.jcode_test.entity.OperationType;
import com.github.ki10v01t.jcode_test.entity.Payment;
import com.github.ki10v01t.jcode_test.entity.Wallet;
import com.github.ki10v01t.jcode_test.entity.Dto.PaymentDto;
import com.github.ki10v01t.jcode_test.entity.Dto.WalletDto;
import com.github.ki10v01t.jcode_test.exception.NotFoundException;
import com.github.ki10v01t.jcode_test.service.PaymentService;
import com.github.ki10v01t.jcode_test.service.PaymentValidator;
import com.github.ki10v01t.jcode_test.service.WalletService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ContextConfiguration(classes=JcodeTestApplication.class)
@WebMvcTest(WalletApiRestController.class)
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

	private WalletDto getWalletDtoForBalanceCheck() {
		return new WalletDto.WalletDtoBuilder().setBalance(500000L).build();
	}

	/**
	 * В проде выбрасывается иключение с ошибкой 404. Здесь с какого-то лешего не выбрасывается. Из-за этого, код 200.
	 * @throws Exception
	 */
	@Test
	public void testUnexistingdPaymentGetNotFoundExample() throws Exception {
		String unexistingTemplate = "922f4f87-8016-4637-abd5-deaa7ee68aab";
		//UUID invalidUUID = UUID.randomUUID();
		UUID unexistingWalletUUID = UUID.fromString(unexistingTemplate);

		// В сервисе вернётся пустой список из БД и будет выброшено исключение из сервиса. Но mock его не видит.
		Mockito.when(paymentService.getPaymentsByWalletId(unexistingWalletUUID)).thenThrow(new NotFoundException("The wallet for the wallet_id you specified was not found"));
		String req = "/api/v1/wallets/payments/"+ unexistingTemplate;
		mockMvc.perform(get(req))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$.message").value("The wallet for the wallet_id you specified was not found"));
	}

	@Test
	public void testInvalidPaymentGetInvalidUUIDExample() throws Exception {
		String invalidUUID = "1"; 

		Mockito.when(walletService.transformWalletUUID(invalidUUID, Wallet.uuidRegex)).thenThrow(new IllegalArgumentException("The argument you passed is not valid"));
		String req = "/api/v1/wallets/payments/"+ invalidUUID;
		mockMvc.perform(get(req))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$.message").value("The argument you passed is not valid"));
	}

	/**
	* Не проходит.
	* та же проблема, что и в {@link JcodeTestApplicationTests#testUnexistingdPaymentGetNotFoundExample()}
	*/
	@Test
	public void testUnexistingWalletBalanceInvalidUUIDExample() throws Exception {
		String unexistingTemplate = "d4bce27d-c78f-47e4-8cae-f2ca073207a3"; 
		UUID unexistingUUID = UUID.fromString(unexistingTemplate);

		//assertThrows(NotFoundException.class, () -> walletService.getWalletById(unexistingUUID));

		Mockito.when(walletService.getWalletById(unexistingUUID)).thenThrow(new NotFoundException("The wallet for the wallet_id you specified was not found"));
		String req = "/api/v1/wallets/"+ unexistingTemplate;

		mockMvc.perform(get(req))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$.message").value("The wallet for the wallet_id you specified was not found"));
	}

	
	/**
	 * Не проходит, т.к. wallet == null. На проде всё работает с тем же UUID.
	 * @throws Exception
	 */
	@Test
	public void testValidWalletBalanceExample() throws Exception {
		String walletId = "194d99be-1fb1-4024-bcd4-8e83ba5bc2a8";
		UUID validWalletId = UUID.fromString(walletId);
		//List<PaymentDto> returnedPaymentValues = paymentService.getPaymentsByWalletId(UUID.fromString(walletId));
		Wallet wallet = walletService.getWalletById(validWalletId);
		WalletDto walletDto = new WalletDto.WalletDtoBuilder().setBalance(wallet.getBalance()).build();

		Mockito.when(walletDto).thenReturn(getWalletDtoForBalanceCheck());
		String req = "/api/v1/wallets/"+ walletId;
		mockMvc.perform(get(URI.create(req)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].walletId").value("cf9e7d45-151d-45c4-a26b-3c57642d561e"))
			.andExpect(jsonPath("$[0].operationType").value("DEPOSIT"))
			.andExpect(jsonPath("$[0].amount").value(1000));
	}


	/**
	* Не проходит.
	* та же проблема, что и в {@link JcodeTestApplicationTests#testValidWalletBalanceExample()}
	*/
	@Test
	public void testValidPaymentGetExample() throws Exception {
		String walletId = "cf9e7d45-151d-45c4-a26b-3c57642d561e";
		UUID validWalletId = UUID.fromString(walletId);
		//List<PaymentDto> returnedPaymentValues = paymentService.getPaymentsByWalletId(UUID.fromString(walletId));

		List<Payment> payments= paymentService.getPaymentsByWalletId(validWalletId);

		List<PaymentDto> paymentsResult = new ArrayList<>(payments.size());
        for(Payment p : payments) {
            paymentsResult.add(new PaymentDto.PaymentDtoBuilder().setWalletId(validWalletId).setOperationType(p.getOperationType()).setAmount(p.getAmount()).build());
            //paymentsResult.add(new PaymentDto.PaymentDtoBuilder().setOperationType(p.getOperationType()).setAmount(p.getAmount()).build());
        }

		Mockito.when(paymentsResult).thenReturn(getPaymentsByWalletId(walletId));
		String req = "/api/v1/wallets/payments"+ walletId;
		mockMvc.perform(get(URI.create(req)))
			.andExpect(status().isOk())
			// Не работает. Просто не видит значения. Возращается список из json.
			.andExpect(jsonPath("$[0].walletId").value("cf9e7d45-151d-45c4-a26b-3c57642d561e"))
			.andExpect(jsonPath("$[0].operationType").value("DEPOSIT"))
			.andExpect(jsonPath("$[0].amount").value(1000));
	}
	
	// @Test
	// public void testInvalidPaymentPostExample() throws Exception {
	// 	String invalidUUID = "1";

	
	// 	Mockito.when(walletService.transformWalletUUID(invalidUUID, Wallet.uuidRegex)).thenThrow(new IllegalArgumentException("The argument you passed is not valid"));
	// 	String req = "/api/v1/wallets/";
	// 	mockMvc.perform(post(req))
	// 		.andExpect(status().isBadRequest())
	// 		.andExpect(jsonPath("$.length()").value(2))
	// 		.andExpect(jsonPath("$.message").value("The argument you passed is not valid"));
	// }

}
