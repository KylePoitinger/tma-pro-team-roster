const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser');
const session = require('express-session');
const path = require('path');

const app = express();
const PORT = 3000;

// Dynamically discover backend URL
let BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

// Function to discover backend port from /health/port endpoint
async function discoverBackendPort() {
    const maxRetries = 10;
    const retryDelay = 1000; // 1 second

    for (let attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            const response = await axios.get('http://localhost:8080/health/port', { timeout: 5000 });
            if (response.data && response.data.url) {
                BACKEND_URL = response.data.url;
                console.log(`[Manager Portal] Backend discovered at: ${BACKEND_URL}`);
                return true;
            }
        } catch (error) {
            if (attempt < maxRetries) {
                console.log(`[Manager Portal] Attempt ${attempt}/${maxRetries} - Backend not ready. Retrying in ${retryDelay}ms...`);
                await new Promise(resolve => setTimeout(resolve, retryDelay));
            }
        }
    }
    console.log(`[Manager Portal] Could not discover backend port. Using default: ${BACKEND_URL}`);
    return false;
}

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(session({
    secret: 'manager-secret',
    resave: false,
    saveUninitialized: true
}));

// Middleware to check if logged in
function isAuthenticated(req, res, next) {
    if (req.session.isManager) {
        return next();
    }
    res.redirect('/login');
}

app.get('/login', (req, res) => {
    res.render('login', { error: null });
});

app.post('/login', (req, res) => {
    const { username, password } = req.body;
    // Hardcoded credentials
    if (username === 'manager' && password === 'password') {
        req.session.isManager = true;
        res.redirect('/');
    } else {
        res.render('login', { error: 'Invalid credentials' });
    }
});

app.get('/logout', (req, res) => {
    req.session.destroy();
    res.redirect('/login');
});

app.get('/', isAuthenticated, async (req, res) => {
    try {
        const teamsResponse = await axios.get(`${BACKEND_URL}/teams`);
        const teams = teamsResponse.data;

        // Fetch rosters for each team to be sure we have the players
        const teamsWithRosters = await Promise.all(teams.map(async (team) => {
            try {
                const rosterResponse = await axios.get(`${BACKEND_URL}/teams/${team.teamId}/roster`);
                return rosterResponse.data;
            } catch (e) {
                console.error(`Failed to fetch roster for team ${team.teamId}`);
                return team;
            }
        }));

        res.render('dashboard', { teams: teamsWithRosters });
    } catch (error) {
        console.error('Error fetching data:', error.message);
        res.render('dashboard', { teams: [], error: 'Failed to fetch teams. Is the backend running?' });
    }
});

app.post('/trade', isAuthenticated, async (req, res) => {
    const { playerId, targetTeamId } = req.body;
    try {
        await axios.put(`${BACKEND_URL}/players/${playerId}/trade/${targetTeamId}`);
        res.redirect('/');
    } catch (error) {
        console.error('Error trading player:', error.message);
        res.status(500).send('Trade failed. Make sure the backend is running and IDs are correct.');
    }
});

const startServer = (port) => {
    const server = app.listen(port, async () => {
        const actualPort = server.address().port;
        console.log(`STATUS: RUNNING`);
        console.log(`URL: http://localhost:${actualPort}`);
        // Discover backend port on startup
        await discoverBackendPort();
    }).on('error', (err) => {
        if (err.code === 'EADDRINUSE') {
            console.log(`[Manager Portal] Port ${port} is already in use. Switching to a dynamic port.`);
            startServer(0);
        } else {
            console.error(`[Manager Portal] Error: ${err.message}`);
        }
    });
};

startServer(process.env.PORT || PORT);
