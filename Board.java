

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// This is the Board class. It is responsible for initialising an array of BoardPosition objects, and

// setting their information, such as whether they're filled or not. Other objects, such as Player and Game,

// only call on the board, sometimes with a column input. The board then process which is the correct

// board position to process.

public class Board {

    private BoardPosition[][] boardArray;

    private Integer boardWidth;

    private Integer boardHeight;

// The board has access to the playerList which is required to know which score to return.
    
    private Player[] playerList;

    // The board calculates a boardValue, based on potential runs that are available and stores it in a hashMap.
    
    // These can then be called on by the botPlayer t0 choose the best move. 
    
    private Map<Player, Integer> boardValues;


    // This is the constructor for the board. Having the playerList as a parameter seemed the best way
    
    // for the board to have access to the players.
    
    public Board(Integer thisBoardHeight, Integer thisBoardWidth, Player[] thisPlayerList) {

	playerList = thisPlayerList;
	boardHeight = thisBoardHeight;
	boardWidth = thisBoardWidth;

	boardArray = new BoardPosition[boardHeight][boardWidth];

	boardValues = new HashMap<Player, Integer>();

	
	// This is the initialiser for the BoardPositions. The constructor includes their position,
	
	// so they can refer to the correct relative positions from them, and the Board objects itself.
	
	for (Integer row = 0; row < thisBoardHeight; row++) {
	    for (Integer column = 0; column < thisBoardWidth; column++) {
		boardArray[row][column] = new BoardPosition(row, column, this);
	
	    }
	}

	
	// Board is the class which has an object of itself for the game. boardArray is the 2D array of 
	
	// BoardPositions. Each position needs to know all the others and call on them by their co-ordinates,
	
	// so they need to be set with them. 
	
	for (Integer row = 0; row < thisBoardHeight; row++) {
	    for (Integer column = 0; column < thisBoardWidth; column++) {
		boardArray[row][column].setBoardArray(boardArray);
	    }
	}

	// This is the method that collects the values from the BoardPositions and sums it. It only checks unfilled positions.
	
	setPotentialBoardValues();
    }


    
    public Integer getBoardWidth() {
	return boardWidth;
    }


    public Integer getBoardHeight() {
	return boardHeight;
    }
    

    public BoardPosition[][] getBoard() {

	return boardArray;
    }



    // A method to calculate which row to add a new token to.
    
    public Integer dropRowForColumn(Integer column) {

	for (Integer rowCheck = boardHeight - 1; rowCheck >= 0; rowCheck--) {

	    if (boardArray[rowCheck][column].getFilledStatus() == false) {

		return rowCheck;

	    } 
	    else {
		continue;
	    }
	}
	return 0;
    }
    
    
    // This is for the top filled row, which is used for removing tokens and checking for wins.
    
    public Integer topRowForColumn(Integer column) {

	for (Integer rowCheck = 0; rowCheck < boardHeight; rowCheck++) {

	    if (boardArray[rowCheck][column].getFilledStatus() == true) {
		return rowCheck;

	    } else {
		continue;
	    }
	}

	return boardHeight - 1;
    }
    
    
    // placeToken method. Finds the top free row, if there is one, and calls on the BoardPosition object to change it's status.
    
    public void placeToken (Player thisPlayer, Integer thisColumn) {

	if ( checkFilled ( thisColumn ) == true ) {

	    System.out.print( " column full ");

	    return;
	}

	else {

	    Integer row = this.dropRowForColumn(thisColumn);

	    boardArray[row][thisColumn].setFilledStatus( true );

	    Character playerToken = thisPlayer.getToken();

	    boardArray[row][thisColumn].setToken(playerToken);

	    boardArray[row][thisColumn].setPositionValue( thisPlayer );

	    setPotentialBoardValues();
	}

	return;
    }
    
    // Similarly removeToken method. This one uses the top row for column

    public void removeToken(Integer column) {
	Integer row = topRowForColumn(column);

	boardArray[row][column].setFilledStatus( false );
	boardArray[row][column].setToken(' ');

	setPotentialBoardValues();

	return;
    }


    

    public Integer getPotentialBoardValue ( Player thisPlayer ) {

	return boardValues.get( thisPlayer);
    }
    
    
    public void setPotentialBoardValues () {

 	for ( Player player : playerList ) {

 	    setPotentialBoardValue( player );
 	}
 	return;
     }


    // This calls on the BoardPositions to update their values, then sums all the values and stores it.
    
    // This is the information the bot uses to calculate it's best move.
    
    // I've set it to only count empty spaces a.
    
    public void setPotentialBoardValue (Player thisPlayer) {

	Integer potentialBoardValue = 0;

	for (Integer row = 0; row < boardHeight; row++) {

	    for (Integer column = 0; column < boardWidth; column++) {

		if ( boardArray[row][column].getFilledStatus() == false ) {

		    boardArray[row][column].setPositionValue(thisPlayer);

		    potentialBoardValue = potentialBoardValue + boardArray[row][column].getPositionValue(thisPlayer);

		}
		continue;
	    }
	}

	boardValues.put( thisPlayer , potentialBoardValue);

	return;
    }
    

    // This method is just a direct call on the boardPosition.
    
    public Integer getPositionValue ( Player thisPlayer, Integer thisColumn) {

	Integer row = topRowForColumn ( thisColumn );

	return boardArray[row][thisColumn].getPositionValue( thisPlayer );

    }
    
    
    // The game terminates on a draw. This method assumes true then is false if any column is not filled.
    
    public Boolean checkDraw () {

	Boolean isDraw = true;

	for ( Integer column = 0; column < boardWidth; column++ ) {

	    if ( checkFilled( column ) == false ) {

		isDraw = false;

		return isDraw;
	    }

	    else continue;
	}

	return isDraw;
    }

    
    // The Board method check filled, takes in a column and checks that column.
    
    public Boolean checkFilled(Integer thisColumn) {

	return boardArray[0][thisColumn].getFilledStatus();

    }

    
    // This method checks all columns to see if the last move was a winner.
    
    public Boolean checkAllColumnWin ( Player thisPlayer  ) {

	Boolean hasWon = false;

	for ( Integer column = 0; column < boardWidth; column++) {

	    if (checkWin(thisPlayer,  column) == true ) {

		hasWon = true;

		return hasWon;
	    }

	    else {
		continue;
	    }
	}

	return hasWon;
    }

    
    // This method just checks the latest token added to a column.

    public Boolean checkWin ( Player thisPlayer, Integer column ) {

	Boolean hasWon = false;

	Integer row = topRowForColumn ( column );

	if ( boardArray[row][column].getHasWon(thisPlayer) == true ) {

	    hasWon = true;
	}

	return hasWon;
    }


    //  The printBoard method.
    
    public void printBoard() {

	setPotentialBoardValues();

	System.out.print("\n \n");

	for (Integer row = 0; row < boardHeight; row++) {
	    for (Integer column = 0; column < boardWidth; column++) {

		if (boardArray[row][column].getFilledStatus() == true) {

		    System.out.print("| " + boardArray[row][column].getToken() + " ");
		} 
		else {
		    System.out.print("|   " );
		}
	    }
	    System.out.println("|");
	}
	
	// This would be better if it adapted to the size of the board.
	
	System.out.println("  1   2   3   4   5   6   7");
    }
    

    // I used these for finding errors and I've left them in as they show how the positionScores are calculated.
    
    public void printInformation ( Integer thisRow, Integer thisColumn) {

	System.out.print("\n \n \n");

	for (Player player : playerList ) {

	    System.out.print(player.getPlayerName() + " \n");

	    System.out.print("for position ["+thisRow+", "+thisColumn+": ");

	    String num4s3s2sVoids =  boardArray[thisRow][5].get4s3s2sVoids(player);

	    System.out.print("runTotals: "+num4s3s2sVoids);

	    System.out.print("\n");

	    System.out.print(boardArray[thisRow][thisColumn].getPositionValue(player));

	    System.out.print("\n");

	    System.out.print("stringOfAllPotential4s: "+boardArray[thisRow][thisColumn].getAllPotential4s(player));

	    System.out.print("\n");

	    System.out.print("vertical4s: "+boardArray[thisRow][thisColumn].getAllVertical4s(player));

	    System.out.print("\n");

	    System.out.print("horizontal4s: "+boardArray[thisRow][thisColumn].getAllHorizontal4s(player));

	    System.out.print("\n");

	    System.out.print("downLeft4s: "+boardArray[thisRow][thisColumn].getAllDownLeft4s(player));

	    System.out.print("\n");

	    System.out.print("downRight4s: "+boardArray[thisRow][thisColumn].getAllDownRight4s(player));

	    System.out.print("\n");

	    System.out.print(player+"  "+boardArray[thisRow][thisColumn].getPositionValue(player));

	    System.out.print("\n \n");

	}
	return;
    }
    
    
    public void printPlayerPotentialBoardValuesForColumn () {

	for (Player player : playerList ) {

	    System.out.print( player.getPlayerName() );

	    for ( Integer column = 0; column < boardWidth; column ++) {

		if ( checkFilled(column) == true ) {
		    continue;
		}

		else {

		    placeToken ( player, column);

		    System.out.print("Column:  "+column+"   BoardValue:  "  +this.getPotentialBoardValue(player) +"\n   ");

		    System.out.print(" \n");

		    removeToken ( column );

		    continue;
		}
	    }

	    System.out.print(" \n \n \n");
	}
	return;
    }
    
    

    
    
}

