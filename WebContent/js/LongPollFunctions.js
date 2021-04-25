/**
 * 
 */

function displayLobby(resp) {
	// Extract array of players
	var currPlayers = resp.players;
	const lobbyIFRAME = document.getElementById("game-start-window");
	const lobbyContentContainer = lobbyIFRAME.contentWindow.document; // get iframe html document
	// TODO: Get inner content container (will display the player cards)
	for (player in currPlayers) {
		console.log(player);
		// TODO: create player card for each player, display in div within the Lobby html container div
		// if playercard already in div, skip it
		
	}
	lobbyIFRAME.style.display = 'initial';
	
}