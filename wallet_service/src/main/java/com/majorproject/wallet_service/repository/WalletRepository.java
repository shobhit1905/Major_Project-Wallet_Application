package com.majorproject.wallet_service.repository;

import com.majorproject.wallet_service.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query(value = "SELECT * FROM wallet WHERE user_id = ?", nativeQuery = true)
    public Wallet findByUserId(Long userId) ;
}
