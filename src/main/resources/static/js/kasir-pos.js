// Main initializer that runs after the HTML document is fully loaded
document.addEventListener('DOMContentLoaded', function() {
    mainInit();
});

/**
 * Main initialization function.
 * Calls all other initialization functions and wraps them in try...catch
 * to ensure one failure does not prevent others from running.
 */
const mainInit = () => {
    try {
        initSearch();
    } catch (e) {
        console.error("Search initialization failed:", e);
    }
    try {
        initCartAjax();
    } catch (e) {
        console.error("Cart initialization failed:", e);
    }
    try {
        // Initialize icons library if it exists
        if (window.lucide) {
            lucide.createIcons();
        }
    } catch (e) {
        console.error("Icons initialization failed:", e);
    }
};

/**
 * Initializes the client-side product search functionality.
 */
const initSearch = () => {
    const searchInput = document.getElementById('productSearch');
    // Combine mobile and desktop items into one list for simpler logic
    const allProductItems = document.querySelectorAll('.product-item'); 
    if (!searchInput || allProductItems.length === 0) return;

    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase().trim();
        allProductItems.forEach(item => {
            const title = item.querySelector('h3')?.textContent.toLowerCase() || '';
            // Using '' for display property resets it to the stylesheet's default (flex, block, etc.)
            item.style.display = title.includes(searchTerm) ? '' : 'none';
        });
    });
};

/**
 * Initializes all AJAX functionality for the shopping cart.
 */
const initCartAjax = () => {
    // Get references to all UI elements that need to be updated
    const cartItemsContainer = document.getElementById('cart-items-container');
    const cartTotalEl = document.getElementById('cart-total');
    const cartItemCountEl = document.getElementById('cart-item-count');
    const checkoutButton = document.getElementById('checkout-button');

    // Helper to format numbers as Indonesian Rupiah
    const formatCurrency = (number) => 'Rp ' + new Intl.NumberFormat('id-ID').format(number);

    /**
     * Main function to redraw the entire cart UI based on cart data from the server.
     * @param {object} cart - The shopping cart object from the backend.
     */
    const updateCartUI = (cart) => {
        if (!cart || !cart.items) {
            console.error("Invalid cart data received:", cart);
            return;
        }

        cartItemCountEl.textContent = cart.items.length;
        cartTotalEl.textContent = formatCurrency(cart.total);
        checkoutButton.disabled = cart.items.length === 0;

        if (cart.items.length === 0) {
            cartItemsContainer.innerHTML = `<div class="py-10 text-center opacity-30 flex flex-col items-center"><i data-lucide="shopping-bag" class="w-12 h-12 mb-2"></i><p class="font-black uppercase text-[10px]">Kosong</p></div>`;
        } else {
            cartItemsContainer.innerHTML = cart.items.map(item => `
                <div class="flex flex-col border-b border-slate-50 pb-4 mb-4 last:border-0">
                    <div class="flex justify-between items-start">
                        <div class="flex-1 pr-4">
                            <h4 class="font-bold text-slate-800 text-[11px] uppercase">${item.product.name}</h4>
                            <p class="text-blue-600 font-black text-[10px]">${formatCurrency(item.product.price)}</p>
                        </div>
                        <button type="button" class="remove-from-cart-btn text-rose-400 hover:text-rose-600 p-1" data-product-id="${item.product.id}"><i data-lucide="trash-2" class="w-4 h-4"></i></button>
                    </div>
                    <div class="flex justify-between items-center mt-3">
                        <div class="flex items-center gap-2">
                            <button type="button" class="decrease-qty-btn rounded-full w-6 h-6 flex items-center justify-center bg-slate-200 text-slate-600" data-product-id="${item.product.id}">-</button>
                            <span class="text-[11px] font-black text-slate-600 min-w-[20px] text-center">${item.quantity}</span>
                            <button type="button" class="increase-qty-btn rounded-full w-6 h-6 flex items-center justify-center bg-slate-900 text-white" data-product-id="${item.product.id}">+</button>
                        </div>
                        <span class="font-black text-slate-800 text-sm">${formatCurrency(item.subtotal)}</span>
                    </div>
                </div>
            `).join('');
        }
        // Re-initialize icons for any new elements added to the DOM
        if (window.lucide) lucide.createIcons();
    };

    /**
     * Generic handler to perform a POST request and update the cart UI.
     * @param {string} url - The API endpoint to call.
     * @param {HTMLElement} button - The button that was clicked, to disable it during the request.
     */
        const handleCartAction = (url, button) => {
            if (button) button.disabled = true;
    
            // Read CSRF token from meta tags
            const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    
            fetch(url, {
                method: 'POST',
                headers: { 
                    'X-Requested-With': 'XMLHttpRequest',
                    [header]: token // Include CSRF token in the request header
                }
            })
            .then(res => {
                if (!res.ok) return Promise.reject('Network response was not ok.');
                return res.json();
            })
            .then(updateCartUI)
            .catch(err => console.error('Cart Action Error:', err))
            .finally(() => { if (button) button.disabled = false; });
        };
    // Use a single event listener on the body for all cart actions (event delegation)
    document.body.addEventListener('click', function(event) {
        const btn = event.target.closest('.add-to-cart-btn, .remove-from-cart-btn, .decrease-qty-btn, .increase-qty-btn');
        if (!btn) return;
        event.preventDefault(); // Prevent any default button/form behavior

        const productId = btn.dataset.productId;
        if (!productId) return;

        if (btn.classList.contains('add-to-cart-btn') || btn.classList.contains('increase-qty-btn')) {
            handleCartAction(`/kasir/api/cart/add/${productId}`, btn);
        } else if (btn.classList.contains('remove-from-cart-btn')) {
            handleCartAction(`/kasir/api/cart/remove/${productId}`, btn);
        } else if (btn.classList.contains('decrease-qty-btn')) {
            handleCartAction(`/kasir/api/cart/decrease/${productId}`, btn);
        }
    });
    
    // Initial load of the cart data when the page first opens
    fetch('/kasir/api/cart')
        .then(res => res.json())
        .then(updateCartUI)
        .catch(error => console.error('Error loading initial cart:', error));
};
