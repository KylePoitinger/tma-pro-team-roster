const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser');
const session = require('express-session');
const path = require('path');

const app = express();
const PORT = 3000;
const BACKEND_URL = 'http://localhost:8080';

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

app.listen(PORT, () => {
    console.log(`Manager Portal running at http://localhost:${PORT}`);
});
