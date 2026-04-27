/**
 * @jest-environment jsdom
 */

const { 
    openTradeModal, 
    closeModal, 
    selectTeam, 
    setAllTeams,
    handleTradeButtonClick
} = require('../public/js/trade-modal.js');

describe('Trade Modal Logic', () => {
    beforeEach(() => {
        // Set up our document body
        document.body.innerHTML = `
            <div id="tradeModal" style="display: none;">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <strong id="modalPlayerName"></strong>
                    <div id="teamsList"></div>
                    <form id="tradeForm">
                        <input type="hidden" name="playerId" id="modalPlayerId">
                        <input type="hidden" name="targetTeamId" id="modalTargetTeamId">
                    </form>
                </div>
            </div>
            <div id="teamsData" data-teams='[{"teamId":"1","name":"Team A","city":"City A"},{"teamId":"2","name":"Team B","city":"City B"}]'></div>
            <button id="tradeBtn" 
                    data-player-id="p1" 
                    data-player-name="Player 1" 
                    data-current-team-id="1">Trade</button>
        `;

        // Mock form submit
        document.getElementById('tradeForm').submit = jest.fn();

        // Initialize teams
        setAllTeams([
            { teamId: "1", name: "Team A", city: "City A" },
            { teamId: "2", name: "Team B", city: "City B" },
            { teamId: "3", name: "Team C", city: "City C" }
        ]);
    });

    test('openTradeModal should show modal and populate player data', () => {
        openTradeModal('p1', 'Player 1', '1');

        expect(document.getElementById('tradeModal').style.display).toBe('block');
        expect(document.getElementById('modalPlayerName').innerText).toBe('Player 1');
        expect(document.getElementById('modalPlayerId').value).toBe('p1');
    });

    test('openTradeModal should list target teams excluding current team', () => {
        openTradeModal('p1', 'Player 1', '1');

        const teamsList = document.getElementById('teamsList');
        const buttons = teamsList.querySelectorAll('button');

        // Total teams are 3, current team is "1", so we expect 2 buttons
        expect(buttons.length).toBe(2);
        expect(buttons[0].innerHTML).toContain('City B');
        expect(buttons[1].innerHTML).toContain('City C');
    });

    test('selectTeam should set targetTeamId and submit form', () => {
        selectTeam('2');

        expect(document.getElementById('modalTargetTeamId').value).toBe('2');
        expect(document.getElementById('tradeForm').submit).toHaveBeenCalled();
    });

    test('closeModal should hide modal', () => {
        document.getElementById('tradeModal').style.display = 'block';
        closeModal();
        expect(document.getElementById('tradeModal').style.display).toBe('none');
    });

    test('handleTradeButtonClick should extract data and open modal', () => {
        const btn = document.getElementById('tradeBtn');
        handleTradeButtonClick(btn);

        expect(document.getElementById('tradeModal').style.display).toBe('block');
        expect(document.getElementById('modalPlayerName').innerText).toBe('Player 1');
        expect(document.getElementById('modalPlayerId').value).toBe('p1');
    });
});
