package com.example.demo.resetpassword.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    Optional<Token> findByToken(String token);

    @Modifying
    @Query("UPDATE Token tk " +
            "SET confirmed_at = ?2 " +
            "WHERE tk.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime now);
}
