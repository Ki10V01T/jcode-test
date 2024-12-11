package com.github.ki10v01t.jcode_test.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ki10v01t.jcode_test.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {}
