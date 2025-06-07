(function() {
    // Intercepta fetch para adicionar token JWT no header Authorization
    const originalFetch = window.fetch;
    window.fetch = function(input, init = {}) {
        const token = localStorage.getItem('jwtToken'); // <<< CHAVE CORRIGIDA AQUI!
        if (token) {
            init.headers = init.headers || {};
            // Adiciona o cabeçalho Authorization APENAS se já não estiver lá
            if (!init.headers['Authorization']) {
                init.headers['Authorization'] = 'Bearer ' + token;
            }
        }
        // Se a requisição retornar 401 ou 403, pode-se adicionar lógica aqui para redirecionar
        // Por exemplo, usando um wrapper customizado que lida com a resposta
        return originalFetch(input, init).then(response => {
            if ((response.status === 401 || response.status === 403) && window.location.pathname !== '/login') {
                // Redireciona para o login se não autorizado e não estiver já na página de login
                localStorage.removeItem('jwtToken'); // Remove token inválido
                window.location.href = '/login';
            }
            return response;
        });
    };
})();