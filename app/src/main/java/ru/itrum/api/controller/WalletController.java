package ru.itrum.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.itrum.api.dto.WalletChangeBalanceDtoRequest;
import ru.itrum.api.dto.WalletDtoResponse;
import ru.itrum.api.facade.WalletFacade;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/wallets", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WalletController {
    private final WalletFacade walletFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDtoResponse changeBalance(@RequestBody @Valid WalletChangeBalanceDtoRequest walletRequest) {
        return walletFacade.changeBalance(walletRequest);
    }

    @GetMapping(path = "/{walletId}")
    public WalletDtoResponse get(@PathVariable("walletId") UUID walletId) {
        return walletFacade.getById(walletId);
    }
}
