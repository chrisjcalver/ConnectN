
public class Player {

    //This is the Player class. It contains fields that are common to all players, human and bot,
    
    // such as name and token.
    
    private Character token;

    // These are set to protected as I wanted the BotPlayer to have access to them.
    
    protected Integer playerNumber;

    protected String playerName;

    // each player knows who's next, which is used for checking if they can win.
    
    // This is for the BotPlayer, but it was easier to define it here so I could assign for
    
    // all Player in playerList without checking if they were bots or not.
    
    protected Player nextPlayer;

    protected Board gameBoard;

    protected Integer boardWidth;

    
    public Player(Character thisToken, String thisPlayerName) {
	token = thisToken;
	playerName = thisPlayerName;
    }

    
    public void setNextPlayer(Player thisNextPlayer) {
	nextPlayer = thisNextPlayer;
    }
    
    
    //This method is always overridden.
    
    public Integer getMove() {
	return -1;
    }
    
    // Used for printing.

    public String getPlayerName () {
	
	return playerName;
    }
    
    // Other getters and setters.
    
    public void setBoardWidth (Integer thisBoardWidth) {
	
	boardWidth = thisBoardWidth;
    }
    
    
    public void setBoard(Board thisBoard) {
	gameBoard = thisBoard;

	boardWidth = gameBoard.getBoardWidth();
    }


    public Character getToken() {
	return token;
    }
}


