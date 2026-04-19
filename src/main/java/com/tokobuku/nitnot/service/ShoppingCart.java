package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.dto.CartItem;
import com.tokobuku.nitnot.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShoppingCart {

    private final List<CartItem> items = new ArrayList<>();

    public void addProduct(Product product) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> Objects.equals(item.getProduct().getId(), product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            if (product.getStock() > 0) {
                items.add(new CartItem(product, 1));
            }
        }
    }

    public void removeProduct(Long productId) {
        items.removeIf(item -> Objects.equals(item.getProduct().getId(), productId));
    }

    public void decreaseProduct(Long productId) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> Objects.equals(item.getProduct().getId(), productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                items.remove(item);
            }
        }
    }
    
    public void updateQuantity(Long productId, int quantity) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> Objects.equals(item.getProduct().getId(), productId))
                .findFirst();

        if (existingItem.isPresent()) {
            if (quantity > 0) {
                 if (quantity <= existingItem.get().getProduct().getStock()){
                    existingItem.get().setQuantity(quantity);
                 }
            } else {
                removeProduct(productId);
            }
        }
    }


    public List<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}
