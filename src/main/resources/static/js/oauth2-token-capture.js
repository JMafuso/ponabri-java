// Script para capturar o token JWT da URL após o redirecionamento do login OAuth2
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    if (token) {
        // Armazena o token no localStorage
        localStorage.setItem('jwtToken', token);
        // Remove o token da URL para limpar a barra de endereço
        window.history.replaceState({}, document.title, window.location.pathname);
        // Redireciona para a página inicial ou outra página protegida
        window.location.href = '/home';
    }
});
