package com.github.ki10v01t.jcode_test.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.ki10v01t.jcode_test.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Query("from Payment p where p.wallet.walletId = :walletId")
    Optional<Payment> findByWalletId(@Param("walletId") UUID walletId);
}