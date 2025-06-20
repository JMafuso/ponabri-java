document.addEventListener('DOMContentLoaded', () => {
    // This script should be loaded on the page that handles the OAuth2 login redirect

    // Fetch the token from the backend response (which returns JSON with the token)
    fetch(window.location.href, {
        method: 'GET',
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        if (data.token) {
            // Store the token in localStorage or sessionStorage
            localStorage.setItem('jwtToken', data.token);
            // Redirect to home page or desired page after login
            window.location.href = '/home';
        } else {
            console.error('Token not found in OAuth2 login response');
        }
    })
    .catch(error => {
        console.error('Error fetching OAuth2 login token:', error);
    });
});
