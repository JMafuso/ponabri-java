(function() {
    // Intercepta fetch para adicionar token JWT no header Authorization
    const originalFetch = window.fetch;
    window.fetch = function(input, init = {}) {
        const token = localStorage.getItem('token');
        if (token) {
            init.headers = init.headers || {};
            if (!init.headers['Authorization']) {
                init.headers['Authorization'] = 'Bearer ' + token;
            }
        }
        return originalFetch(input, init);
    };
})();
