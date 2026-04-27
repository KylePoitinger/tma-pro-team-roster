(function() {
    const themeToggleBtn = document.getElementById('themeToggle');
    const currentTheme = localStorage.getItem('theme') || 'light';

    // Set initial theme
    if (currentTheme === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
        if (themeToggleBtn) themeToggleBtn.innerHTML = '🌙';
    } else {
        document.documentElement.setAttribute('data-theme', 'light');
        if (themeToggleBtn) themeToggleBtn.innerHTML = '☀️';
    }

    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            let theme = document.documentElement.getAttribute('data-theme');
            if (theme === 'dark') {
                document.documentElement.setAttribute('data-theme', 'light');
                themeToggleBtn.innerHTML = '☀️';
                localStorage.setItem('theme', 'light');
            } else {
                document.documentElement.setAttribute('data-theme', 'dark');
                themeToggleBtn.innerHTML = '🌙';
                localStorage.setItem('theme', 'dark');
            }
        });
    }
})();
