package ru.itrum.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.itrum.api.entity.Wallet;
import ru.itrum.api.repository.WalletRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional
    public Wallet save(Wallet model) {
        return walletRepository.save(model);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Wallet getByIdLockForUpdate(UUID id) {
        return walletRepository.findByIdLockForUpdate(id).orElseThrow(
                () -> new NoSuchElementException("Wallet not found, id: " + id)
        );
    }
}
