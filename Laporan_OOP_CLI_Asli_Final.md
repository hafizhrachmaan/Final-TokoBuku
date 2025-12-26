# LAPORAN FINAL: ANALISIS OOP DENGAN SIMULASI CLI ASLI

Dokumen ini memecah penerapan pilar OOP dalam format selang-seling, menggunakan simulasi output yang sesuai dengan kode CLI asli dari proyek.

---
---

### 1. INHERITANCE (Pewarisan)

#### CONTOH 1: Pewarisan Fungsionalitas dari Spring Data JPA

**Definisi:** Mekanisme di mana sebuah kelas turunan (subclass) mewarisi atribut dan metode dari kelas induk (superclass).

**Code Implementation**

*File: `src/main/java/com/tokobuku/nitnot/repository/UserRepository.java`*
```java
package com.tokobuku.nitnot.repository;

import com.tokobuku.nitnot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

*File: `src/main/java/com/tokobuku/nitnot/service/DataInitializer.java`*
```java
package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    // ...
    @Override
    public void run(String... args) throws Exception {
        // Metode .save() ini tidak didefinisikan di UserRepository,
        // tapi diwarisi dari JpaRepository.
        userRepository.save(newUser);
    }
}
```

**Output Simulation**
```plaintext
[System] Aplikasi startup...
[System] DataInitializer.run() dieksekusi.
[System] Memanggil userRepository.save(newUser);
[Note] Metode .save() berhasil dijalankan karena diwarisi dari JpaRepository.
[Log] Initializing default users...
[Log] Default users created.
```

---

#### CONTOH 2: Pewarisan Kontrak dari Interface untuk Aksi CLI

**Definisi:** Sebuah kelas dapat `implements` sebuah interface, yang berarti ia mewarisi "tipe" dan "kontrak" (metode abstrak) dari interface tersebut dan berjanji untuk menyediakan implementasinya.

**Code Implementation**

*Parent Interface: `src/main/java/com/tokobuku/nitnot/service/MenuAction.java`*
```java
package com.tokobuku.nitnot.service;

import java.util.Scanner;

public interface MenuAction {
    void execute(Scanner scanner);
}
```

*Child Class: `src/main/java/com/tokobuku/nitnot/service/ProfileAction.java`*
```java
package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.User;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class ProfileAction implements MenuAction { // Mewarisi kontrak MenuAction
    // ...
    @Override
    public void execute(Scanner scanner) {
        System.out.println("\n--- Profile ---");
        // ... (logika menampilkan profil)
    }
}
```

**Output Simulation**
```plaintext
Pilih menu: P
[System] Program memilih aksi untuk "P".
[System] Objek 'ProfileAction' diambil dari Map dan diperlakukan sebagai tipe 'MenuAction'.
[Note] Ini valid karena 'ProfileAction' adalah turunan sah dari 'MenuAction'.

--- Profile ---
Username: stocker
Role: STOCKER
Status: ACTIVE
Press Enter to continue...
```

---
---

### 2. POLYMORPHISM (Banyak Bentuk)

**Definisi:** Kemampuan suatu referensi objek untuk berperilaku secara berbeda tergantung pada objek riil yang ditunjuknya, biasanya melalui *method overriding*.

---

#### CONTOH 1: Eksekusi Aksi Menu yang Dinamis di CLI

**Code Implementation**

*File: `src/main/java/com/tokobuku/nitnot/service/MenuManager.java`*
```java
package com.tokobuku.nitnot.service;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class MenuManager {
    private final Map<String, MenuAction> actions = new HashMap<>();

    public MenuManager(ProductsAction productsAction, KasirAction kasirAction, ProfileAction profileAction) {
        actions.put("1", productsAction);
        actions.put("3", kasirAction);
        actions.put("P", profileAction);
    }
    
    public void run(Scanner scanner) {
        // ... (setelah login & memilih menu)
        String choice = scanner.nextLine().toUpperCase();
        MenuAction action = actions.get(choice); // 'action' adalah referensi polimorfik
        
        if (action != null) {
            // PANGGILAN POLIMORFIK: Metode yang sama, HASIL BERBEDA
            action.execute(scanner); 
        }
    }
}
```

**Output Simulation**
```plaintext
--- Main Menu ---
Logged in as: stocker (STOCKER)
1. Manage Products (Stocker Functionality)
P. View Profile
L. Logout
Choose an option: 1
[System] MenuManager mengambil objek ProductsAction.
[System] Memanggil action.execute().

--- Product Management ---
...

--- Main Menu ---
Logged in as: stocker (STOCKER)
1. Manage Products (Stocker Functionality)
P. View Profile
L. Logout
Choose an option: P
[System] MenuManager mengambil objek ProfileAction.
[System] Memanggil action.execute().

--- Profile ---
...
```

---
---

### 3. ENCAPSULATION (Pembungkusan)

**Definisi:** Menyembunyikan detail data internal dengan menggunakan akses modifer `private` dan menyediakan akses melalui metode publik (*getter/setter* atau *service methods*).

---

#### CONTOH 1: Pembungkusan Logika Bisnis di `ProductService`

**Code Implementation**

*File: `src/main/java/com/tokobuku/nitnot/service/ProductService.java`*
```java
package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.model.Product;
import com.tokobuku.nitnot.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    // ...
    public Product updateProduct(Long id, Product updatedProduct) {
        // Logika kompleks (cari, validasi, ubah, simpan) dibungkus di sini
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found"));

        // Menggunakan metode 'setter' dari objek Product (Enkapsulasi level 2)
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        return productRepository.save(existingProduct);
    }
}
```

**Output Simulation**
```plaintext
--- Manajemen Produk ---
Pilih ID produk untuk diupdate: 1
Masukkan nama baru: Buku Java Super
... (input lainnya)

[System] ProductsAction (layer presentasi) memanggil ProductService.updateProduct(...).
[Note] Controller/Action tidak tahu cara update database. Semua logika itu dienkapsulasi dengan aman di dalam ProductService.
Product updated successfully.
```

---
---

### 4. ABSTRACTION (Abstraksi)

**Definisi:** Menyembunyikan kerumitan implementasi detail dan hanya menampilkan fungsi atau fitur penting kepada pengguna.

---

#### CONTOH 1: Abstraksi Query Database dengan Spring Data JPA

**Code Implementation**

*Interface: `src/main/java/com/tokobuku/nitnot/repository/ProductRepository.java`*
```java
package com.tokobuku.nitnot.repository;

import com.tokobuku.nitnot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Abstraksi: Kita tidak perlu menulis implementasi SQL untuk 'save', 'findAll', 'findById', dll.
    // Spring Data JPA menyembunyikan semua kerumitan itu.
}
```

*Usage in `src/main/java/com/tokobuku/nitnot/service/ProductService.java`*
```java
package com.tokobuku.nitnot.service;
//...
@Service
public class ProductService {
    private final ProductRepository productRepository;
    // ...
    public List<Product> getAllProducts() {
        // Kita tidak tahu kerumitan query SQL "SELECT * FROM product".
        // Kita hanya panggil fungsi yang sudah diabstraksikan.
        return productRepository.findAll(); 
    }
}
```

**Output Simulation**
```plaintext
--- Product Management ---
...
Choose an option: 1

--- All Products ---
[System] ProductsAction -> productService.getAllProducts() -> productRepository.findAll()
[Note] Developer tidak perlu tahu bagaimana Spring Data JPA membuat query SQL.
       Kerumitan itu disembunyikan (diabstraksikan).
ID: 1, Name: Java Programming for Beginners, Price: 150000.00, Stock: 10
ID: 2, Name: Hafizh Boot in Action, Price: 250000.00, Stock: 5
...
```