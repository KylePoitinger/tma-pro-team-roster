let allTeams = [];

function initializeTradeModal() {
    const teamsDataElem = document.getElementById('teamsData');
    if (teamsDataElem) {
        allTeams = JSON.parse(teamsDataElem.getAttribute('data-teams'));
    }
}

function handleTradeButtonClick(btn) {
    const playerId = btn.getAttribute('data-player-id');
    const playerName = btn.getAttribute('data-player-name');
    const currentTeamId = btn.getAttribute('data-current-team-id');
    openTradeModal(playerId, playerName, currentTeamId);
}

function openTradeModal(playerId, playerName, currentTeamId) {
    document.getElementById('modalPlayerId').value = playerId;
    document.getElementById('modalPlayerName').innerText = playerName;
    
    const teamsList = document.getElementById('teamsList');
    teamsList.innerHTML = '';
    
    allTeams.forEach(team => {
        if (team.teamId !== currentTeamId) {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'team-btn';
            btn.innerHTML = `<strong>${team.city}</strong> ${team.name}`;
            btn.onclick = () => selectTeam(team.teamId);
            teamsList.appendChild(btn);
        }
    });
    
    document.getElementById('tradeModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('tradeModal').style.display = 'none';
}

function selectTeam(teamId) {
    document.getElementById('modalTargetTeamId').value = teamId;
    document.getElementById('tradeForm').submit();
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('tradeModal');
    if (event.target == modal) {
        closeModal();
    }
};

// Initialize if we are in a browser
if (typeof document !== 'undefined') {
    document.addEventListener('DOMContentLoaded', initializeTradeModal);
}

// Export for testing if needed
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        initializeTradeModal,
        handleTradeButtonClick,
        openTradeModal,
        closeModal,
        selectTeam,
        getAllTeams: () => allTeams,
        setAllTeams: (teams) => { allTeams = teams; }
    };
}
