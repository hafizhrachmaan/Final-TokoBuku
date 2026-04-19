## File: src/main/resources/static/css/pos-style.css
```css

```

## File: src/main/resources/static/js/kasir-pos.js
```javascript
// Function to update cart UI
function updateCartUI(cart) {
    const cartItemsContainer = document.getElementById('cart-items-container');
    const cartTotalElement = document.getElementById('cart-total');
    const cartItemCountElement = document.getElementById('cart-item-count');
    const checkoutButton = document.getElementById('checkout-button');

    cartItemsContainer.innerHTML = ''; // Clear current items

    if (cart.items && cart.items.length > 0) {
        cart.items.forEach(item => {
            const itemElement = `
                <div class="flex items-center gap-4 bg-white p-4 rounded-xl shadow-sm border border-slate-100 mb-3">
                    <div class="w-10 h-10 bg-blue-50 rounded-lg flex items-center justify-center text-blue-500 flex-shrink-0">
                        <i data-lucide="book" class="w-5 h-5"></i>
                    </div>
                    <div class="flex-1">
                        <h4 class="font-bold text-slate-800 text-sm">${item.productName}</h4>
                        <p class="text-blue-600 font-black text-xs">Rp ${new Intl.NumberFormat('id-ID').format(item.price)}</p>
                    </div>
                    <div class="flex items-center space-x-2">
                        <button type="button" class="decrease-qty-btn bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-lg w-7 h-7 flex items-center justify-center transition-all" data-product-id="${item.productId}">
                            <i data-lucide="minus" class="w-4 h-4"></i>
                        </button>
                        <span class="font-bold text-slate-700">${item.quantity}</span>
                        <button type="button" class="add-qty-btn bg-slate-100 hover:bg-slate-200 text-slate-600 rounded-lg w-7 h-7 flex items-center justify-center transition-all" data-product-id="${item.productId}">
                            <i data-lucide="plus" class="w-4 h-4"></i>
                        </button>
                    </div>
                    <button type="button" class="remove-item-btn text-red-400 hover:text-red-600 transition-colors" data-product-id="${item.productId}">
                        <i data-lucide="trash-2" class="w-5 h-5"></i>
                    </button>
                </div>
            `;
            cartItemsContainer.insertAdjacentHTML('beforeend', itemElement);
        });
        lucide.createIcons(); // Re-render lucide icons for new elements
        checkoutButton.removeAttribute('disabled'); // Enable checkout button
        checkoutButton.classList.remove('disabled:opacity-40');
    } else {
        cartItemsContainer.innerHTML = `
            <div class="flex flex-col items-center justify-center min-h-[150px] text-slate-400">
                <i data-lucide="shopping-cart" class="w-16 h-16 opacity-20 mb-4"></i>
                <p class="text-sm font-medium italic">Keranjang kosong. Tambahkan buku!</p>
            </div>
        `;
        checkoutButton.setAttribute('disabled', 'true'); // Disable checkout button
        checkoutButton.classList.add('disabled:opacity-40');
    }

    cartTotalElement.innerText = `Rp ${new Intl.NumberFormat('id-ID').format(cart.total)}`;
    cartItemCountElement.innerText = cart.itemCount;

    // Attach event listeners to new buttons
    document.querySelectorAll('.add-qty-btn').forEach(button => {
        button.onclick = (event) => {
            const productId = event.currentTarget.dataset.productId;
            addToCart(productId);
        };
    });

    document.querySelectorAll('.decrease-qty-btn').forEach(button => {
        button.onclick = (event) => {
            const productId = event.currentTarget.dataset.productId;
            decreaseCartItem(productId);
        };
    });

    document.querySelectorAll('.remove-item-btn').forEach(button => {
        button.onclick = (event) => {
            const productId = event.currentTarget.dataset.productId;
            removeCartItem(productId);
        };
    });
}

// Function to fetch CSRF token
function getCsrfToken() {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    return { token, header };
}

// Generic function to send cart updates
async function sendCartUpdate(url, productId) {
    const { token, header } = getCsrfToken();
    const headers = {
        'Content-Type': 'application/json',
        [header]: token
    };

    const response = await fetch(url.replace('{productId}', productId), {
        method: 'POST',
        headers: headers
    });

    if (!response.ok) {
        // Optionally, parse error message from backend
        const errorData = await response.json();
        alert('Error updating cart: ' + errorData.message);
        throw new Error('Network response was not ok');
    }
    const cart = await response.json();
    updateCartUI(cart);
}

// Add to cart function
async function addToCart(productId) {
    await sendCartUpdate(`/kasir/api/cart/add/${productId}`, productId);
}

// Decrease cart item function
async function decreaseCartItem(productId) {
    await sendCartUpdate(`/kasir/api/cart/decrease/${productId}`, productId);
}

// Remove cart item completely
async function removeCartItem(productId) {
    await sendCartUpdate(`/kasir/api/cart/remove/${productId}`, productId);
}

// Initial cart load
async function loadCart() {
    const response = await fetch('/kasir/api/cart');
    const cart = await response.json();
    updateCartUI(cart);
}

document.addEventListener('DOMContentLoaded', () => {
    lucide.createIcons();
    loadCart();

    // Attach event listeners to initial "Add to Cart" buttons
    document.querySelectorAll('.add-to-cart-btn').forEach(button => {
        button.onclick = (event) => {
            const productId = event.currentTarget.dataset.productId;
            addToCart(productId);
        };
    });

    // Product Search functionality
    const productSearchInput = document.getElementById('productSearch');
    if (productSearchInput) {
        productSearchInput.addEventListener('keyup', () => {
            const searchTerm = productSearchInput.value.toLowerCase();
            document.querySelectorAll('.product-item').forEach(item => {
                const productName = item.querySelector('h3').innerText.toLowerCase();
                if (productName.includes(searchTerm)) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }
});

```

## File: src/main/resources/application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hrd_app?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.thymeleaf.cache=false

logging.level.org.springframework.web=DEBUG
logging.level.com.tokobuku.nitnot=DEBUG

# CLI
cli.admin.username=admin
cli.admin.password=admin

```