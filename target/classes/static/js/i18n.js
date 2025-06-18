// Função para obter o idioma atual da URL
function getCurrentLanguage() {
    const urlParams = new URLSearchParams(window.location.search);
    const lang = urlParams.get('lang');
    
    if (lang) {
        return lang;
    }
    
    // Verificar se há um idioma salvo no localStorage
    const savedLang = localStorage.getItem('preferredLanguage');
    if (savedLang) {
        return savedLang;
    }
    
    // Idioma padrão
    return 'pt_BR';
}

// Função para atualizar o seletor de idioma
function updateLanguageSelector() {
    const currentLang = getCurrentLanguage();
    const currentLanguageSpan = document.querySelector('.current-language span');
    
    if (currentLanguageSpan) {
        const languageMap = {
            'pt_BR': 'Português',
            'en': 'English',
            'es': 'Español'
        };
        
        currentLanguageSpan.textContent = languageMap[currentLang] || 'Português';
    }
}

// Função para salvar a preferência de idioma
function saveLanguagePreference(lang) {
    localStorage.setItem('preferredLanguage', lang);
}

// Função para mudar o idioma
function changeLanguage(lang) {
    saveLanguagePreference(lang);
    
    // Adicionar o parâmetro de idioma à URL atual
    const url = new URL(window.location);
    url.searchParams.set('lang', lang);
    
    // Redirecionar para a nova URL
    window.location.href = url.toString();
}

// Inicializar quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function() {
    updateLanguageSelector();
    
    // Adicionar event listeners aos links de idioma
    const languageLinks = document.querySelectorAll('.language-option');
    languageLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const lang = this.getAttribute('href').split('=')[1];
            changeLanguage(lang);
        });
    });
});

// Função para obter mensagens traduzidas (fallback)
function getMessage(key, defaultValue) {
    // Esta função pode ser expandida para buscar mensagens do servidor
    // Por enquanto, retorna o valor padrão
    return defaultValue;
} 