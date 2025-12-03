package ru.itrum.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.itrum.api.dto.WalletChangeBalanceDtoRequest;
import ru.itrum.api.dto.WalletDtoResponse;
import ru.itrum.api.entity.OperationType;
import ru.itrum.api.facade.WalletFacade;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WalletFacade walletFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID TEST_WALLET_ID = UUID.randomUUID();
    private static final BigDecimal TEST_WALLET_AMOUNT = new BigDecimal("100.00");
    private static final WalletDtoResponse TEST_RESPONSE =
            new WalletDtoResponse(TEST_WALLET_ID, TEST_WALLET_AMOUNT);

    @Test
    void changeBalance_Success() throws Exception {
        WalletChangeBalanceDtoRequest request = new WalletChangeBalanceDtoRequest(
                TEST_WALLET_ID,
                OperationType.DEPOSIT,
                TEST_WALLET_AMOUNT
        );

        when(walletFacade.changeBalance(any())).thenReturn(TEST_RESPONSE);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(TEST_WALLET_ID.toString()));
    }

    @Test
    void changeBalance_InvalidAmount_Negative() throws Exception {
        WalletChangeBalanceDtoRequest request = new WalletChangeBalanceDtoRequest(
                TEST_WALLET_ID,
                OperationType.WITHDRAW,
                new BigDecimal("-1.00")
        );

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeBalance_InvalidOperationType() throws Exception {
        String invalidJson = """
                {
                    "walletId": "%s",
                    "operationType": "INVALID",
                    "amount": 100.00
                }
                """.formatted(TEST_WALLET_ID);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeBalance_MissingWalletId() throws Exception {
        String invalidJson = """
                {
                    "operationType": "DEPOSIT",
                    "amount": 100.00
                }
                """;

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_Success() throws Exception {
        when(walletFacade.getById(TEST_WALLET_ID)).thenReturn(TEST_RESPONSE);

        mockMvc.perform(get("/api/v1/wallets/" + TEST_WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(TEST_WALLET_ID.toString()));
    }

    @Test
    void get_InvalidWalletId() throws Exception {
        when(walletFacade.getById(TEST_WALLET_ID)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/api/v1/wallets/" + TEST_WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
