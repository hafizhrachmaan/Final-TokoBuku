package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.dto.CartItem;
import com.tokobuku.nitnot.model.Product;
import com.tokobuku.nitnot.model.Transaction;
import com.tokobuku.nitnot.model.TransactionDetail;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.repository.ProductRepository;
import com.tokobuku.nitnot.repository.TransactionRepository;
import com.tokobuku.nitnot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Transaction processCheckout(String cashierUsername, ShoppingCart shoppingCart) {
        User cashier = userRepository.findByUsername(cashierUsername)
                .orElseThrow(() -> new IllegalStateException("Cashier not found"));

        if (shoppingCart.getItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setUser(cashier);
        transaction.setTotalPrice(shoppingCart.getTotal());

        List<TransactionDetail> details = new ArrayList<>();
        for (CartItem item : shoppingCart.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalStateException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            TransactionDetail detail = new TransactionDetail();
            detail.setTransaction(transaction);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice()); // Record price at time of sale
            details.add(detail);
        }

        transaction.setDetails(details);
        transactionRepository.save(transaction);

        shoppingCart.clear();

        return transaction;
    }
}
