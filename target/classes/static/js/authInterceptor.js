// Interceptor para adicionar o token JWT no cabeçalho Authorization de todas as requisições fetch
(function() {
    const originalFetch = window.fetch;
    window.fetch = function(input, init = {}) {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            init.headers = init.headers || {};
            if (!init.headers['Authorization'] && !init.headers['authorization']) {
                init.headers['Authorization'] = 'Bearer ' + token;
            }
        }
        return originalFetch(input, init);
    };
})();
