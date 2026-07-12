document.addEventListener('DOMContentLoaded', () => {
    // CSRF token for AJAX requests
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    /**
     * Displays a toast notification.
     * @param {string} message The message to display.
     * @param {boolean} isError If true, styles the toast as an error.
     */
    const showToast = (message, isError = false) => {
        const container = document.getElementById('toast-container');
        if (!container) return;
        const toast = document.createElement('div');
        toast.className = `px-4 py-3 rounded-md text-sm font-semibold shadow-lg transition-all duration-300 ${isError ? 'bg-red-600' : 'bg-green-600'} text-white`;
        toast.textContent = message;
        container.appendChild(toast);
        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    };

    /**
     * Generic API call handler.
     * @param {string} method The HTTP method (e.g., 'POST').
     * @param {string} url The API endpoint.
     * @param {object|null} body The request body for POST/PUT requests.
     * @returns {Promise<any>} The response data.
     */
    const apiCall = async (method, url, body = null) => {
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            }
        };
        if (body) {
            options.body = JSON.stringify(body);
        }

        const response = await fetch(url, options);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'An unknown error occurred.');
        }
        // For 204 No Content, response.json() will fail
        if (response.status === 204) {
            return null;
        }
        return response.json();
    };

    const pendingTbody = document.getElementById('pending-employees-tbody');
    const verifiedTbody = document.getElementById('verified-employees-tbody');

    // Event Delegation for handling clicks on action buttons
    document.body.addEventListener('click', async (event) => {
        const target = event.target;
        const userId = target.dataset.userId;

        try {
            if (target.matches('.accept-btn')) {
                const acceptedUser = await apiCall('POST', `/hrd/api/employees/accept/${userId}`);
                removePendingRow(userId);
                addVerifiedRow(acceptedUser);
                showToast('Applicant accepted successfully!');
            } else if (target.matches('.reject-btn')) {
                await apiCall('POST', `/hrd/api/employees/reject/${userId}`);
                removePendingRow(userId);
                showToast('Applicant rejected.', true);
            } else if (target.matches('.cut-btn')) {
                if (confirm('Are you sure you want to terminate this employee?')) {
                    await apiCall('POST', `/hrd/api/employees/cut/${userId}`);
                    removeVerifiedRow(userId);
                    showToast('Employee terminated.', true);
                }
            } else if (target.matches('#add-employee-btn')) {
                await handleAddEmployee();
            }
        } catch (error) {
            showToast(error.message, true);
        }
    });
    
    /**
     * Handles adding a new employee via the form.
     */
    const handleAddEmployee = async () => {
        const form = document.getElementById('add-employee-form');
        const username = form.querySelector('[name="username"]').value;
        const password = form.querySelector('[name="password"]').value;
        const role = form.querySelector('[name="role"]').value;

        if (!username || !password) {
            showToast('Username and password are required.', true);
            return;
        }

        const payload = { username, password, role };

        try {
            const newUser = await apiCall('POST', '/hrd/api/employees', payload);
            addVerifiedRow(newUser);
            showToast('Employee added successfully!');
            form.reset(); // Clear the form fields
        } catch (error) {
            showToast(error.message, true);
        }
    };

    /**
     * Removes a row from the pending employees table.
     * @param {string|number} userId The ID of the user whose row should be removed.
     */
    const removePendingRow = (userId) => {
        const row = document.getElementById(`pending-row-${userId}`);
        if (row) {
            row.remove();
            const countEl = document.getElementById('pending-count');
            const currentCount = parseInt(countEl.textContent.split(' ')[0]) - 1;
            countEl.textContent = `${currentCount} Menunggu`;
            if (pendingTbody.children.length === 0) {
                pendingTbody.innerHTML = '<tr><td colspan="3" class="px-5 py-6 text-center text-gray-400 italic">Tidak ada permohonan baru.</td></tr>';
            }
        }
    };
    
    /**
     * Removes a row from the verified employees table.
     * @param {string|number} userId The ID of the user whose row should be removed.
     */
    const removeVerifiedRow = (userId) => {
        const row = document.getElementById(`verified-row-${userId}`);
        if(row) row.remove();
        if (verifiedTbody.children.length === 0) {
            verifiedTbody.innerHTML = '<tr><td colspan="3" class="px-5 py-6 text-center text-gray-400 italic">Tidak ada karyawan aktif.</td></tr>';
        }
    };

    /**
     * Adds a new row to the verified employees table.
     * @param {object} user The user object returned from the API.
     */
    const addVerifiedRow = (user) => {
        // If the placeholder "no employees" row exists, remove it.
        const placeholderRow = verifiedTbody.querySelector('td[colspan="3"]');
        if (placeholderRow) placeholderRow.parentElement.remove();

        const newRow = document.createElement('tr');
        newRow.id = `verified-row-${user.id}`;
        newRow.innerHTML = `
            <td class="px-5 py-3">
                <p class="font-bold text-gray-800">${user.username}</p>
                <p class="text-xs text-gray-500">ID: #${user.id}</p>
            </td>
            <td class="px-5 py-3 text-center">
                <span class="bg-gray-100 text-gray-700 text-xs font-semibold px-2 py-1 rounded">${user.role}</span>
            </td>
            <td class="px-5 py-3 text-center">
                <button type="button" class="cut-btn bg-red-500 hover:bg-red-600 text-white text-xs font-bold px-3 py-1.5 rounded transition" data-user-id="${user.id}">PECAT</button>
            </td>
        `;
        verifiedTbody.appendChild(newRow);
    };
});
