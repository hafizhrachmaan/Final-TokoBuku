package com.example.hrdapp.repository;

import com.example.hrdapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionDateDesc();
    List<Transaction> findByUser(com.example.hrdapp.model.User user);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.details d JOIN FETCH d.product WHERE t.id = :id")
    Optional<Transaction> findByIdWithDetails(Long id);
}
