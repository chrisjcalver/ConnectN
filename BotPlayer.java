


public class BotPlayer extends Player { 

    // This is the BotPlayer class. It has an algorithm to calculate the best move based on relative board positions.
    
    // It essentially checks for priority moves, and otherwise places moves that maximises it's board value,
    
    // linking different twos and threes and valuable and reducing the next players board position is valuable as well.
    
    
    // The winValue is here to mark which moves ( such as choosing a full column, are a really bad option. )

    Integer winValue = 10000;

    public BotPlayer(Character thisToken, String thisPlayerName) {
	super(thisToken, thisPlayerName);

    }


    // This is the override method for the getMove function.
    
    @Override
    public Integer getMove() {

	// The priority of actions is first to check if can win this turn.
	
	// checkForWin returns the winning column if possible, and -1 if not.
	
	Integer thisPlayerWinColumn = 	checkForWin ( this );

	if ( thisPlayerWinColumn >= 0 ) {

	    return thisPlayerWinColumn;
	}


	// Next check to see if the nextPlayer can win can win this turn.
	
	Integer nextPlayerWinColumn = checkForWin ( nextPlayer );

	if ( nextPlayerWinColumn >= 0 ) {

	    return nextPlayerWinColumn;
	}


	// Then check if a two move win is on
	
	Integer thisPlayerTwoMoveWinColumn = checkForThisPlayerTwoMoveWin ();

	if ( thisPlayerTwoMoveWinColumn >= 0 ) {

	    return thisPlayerTwoMoveWinColumn;
	}


	// and final priority move is to check whether the next player can win in two moves.
	
	Integer nextPlayerTwoMoveWinColumn = checkForNextPlayerTwoMoveWin ();

	if ( nextPlayerTwoMoveWinColumn >= 0 ) {

	    return nextPlayerTwoMoveWinColumn;
	}


	// after this the relative value of each move is calculated and the best column return here.
	
	return getBestRelativeMoveColumnValue ( this )[0];
    }


    // The getWinCountWinningColumn method returns a 2 digit array with the number of wins in
    
    // position 0, and a winning column or -1 in position 1.
    
    // Having this method made the initial getMove method look a bit neater.
    

    public Integer checkForWin ( Player thisPlayer ) {

	Integer[] nextPlayerWinCountAndWinningColumn = getWinCountWinningColumn  ( thisPlayer );

	Integer nextPlayerNumPossibleWins = nextPlayerWinCountAndWinningColumn[0];

	Integer nextPlayerWinningColumn = nextPlayerWinCountAndWinningColumn[1];

	if ( nextPlayerNumPossibleWins > 0 ) {

	    return nextPlayerWinningColumn;  
	}

	return -1;
    }


    // There are separate methods for the two win move, as for this player it's no good
    
    // if it allows the next player to win.
    
    // The method works by placing a token in each column, if they're empty, and counting how
    
    // many winning moves there are. If there's 2 or more, and it wont set the next player up
    
    // for a win, it'll return that column. Otherwise it'll return -1.
    
    
    public Integer checkForThisPlayerTwoMoveWin() {

	for ( Integer column = 0; column < boardWidth; column++ ) {

	    if ( gameBoard.checkFilled ( column ) == true ) {

		continue;
	    }

	    else {

		gameBoard.placeToken( this, column);

		// The [0] position of the getWinCountWinningColumn is an integer of the win count.
		
		Integer thisPlayerNextTurnNumWins = getWinCountWinningColumn  ( this )[0];

		Integer nextPlayerNextTurnNumWins = getWinCountWinningColumn  ( nextPlayer )[0];


		if ( thisPlayerNextTurnNumWins >= 2 && nextPlayerNextTurnNumWins < 1 ) {

		    gameBoard.removeToken(column);

		    return column;
		}

		gameBoard.removeToken(column);

		continue;   
	    }
	}

	return -1;
    }


    // This one is very similar but doesn't have the condition of allowing a win.
    
    public Integer checkForNextPlayerTwoMoveWin () {

	for ( Integer column = 0; column < boardWidth; column++ ) {

	    if ( gameBoard.checkFilled ( column ) == true ) {

		continue;
	    }

	    else {

		gameBoard.placeToken( nextPlayer, column);

		Integer nextPlayerNextTurnNumWins = getWinCountWinningColumn  ( nextPlayer )[0];

		if ( nextPlayerNextTurnNumWins >= 2 ) {

		    gameBoard.removeToken(column);

		    gameBoard.placeToken( this, column);

		    // This is here so it won't block a potential two move win, that might be missed,
		    
		    // and create an opportunity for a won turn win.
		    
		    Integer nextPlayerPotentialWins = getWinCountWinningColumn  ( nextPlayer )[0];

		    if ( nextPlayerPotentialWins < 1 ) {

			gameBoard.removeToken(column);	

			return column;
		    }
		}

		gameBoard.removeToken(column);

		continue;   
	    }
	}

	return -1;
    }



    //This gets the Integer array of WinCount and winning column that was mentioned previously.
    
    public Integer[] getWinCountWinningColumn ( Player thisPlayer) {

	Integer winCount = 0;

	Integer winningColumn = -1;

	// We don't ever want to place a token in a full column. It isn't a problem to place it,
	
	// as it just won't change anything, but the following removeToken call will then take
	
	// the top row of game tokens off.
	
	for ( Integer column = 0; column < boardWidth; column++ ) {

	    if ( gameBoard.checkFilled(column) == true ) {

		continue;
	    }

	    else {

		// For each column, place a token and see if it's a winner. Keep count of how many are.
		
		gameBoard.placeToken( thisPlayer, column );

		if ( gameBoard.checkWin ( thisPlayer, column ) == true ) {

		    winCount = winCount + 1;

		    // We only need one winning column so it just records the latest witnessed.
		    
		    winningColumn = column; 
		}

		gameBoard.removeToken( column);

		continue;
	    }
	}

	Integer[] winCountWinningColumn = { winCount, winningColumn };

	return winCountWinningColumn;
    }


    
    // After checking for priority moves the bot then gets the move that would give it
    
    // the best relative board position. This method checks each column and returns the best one.
    
    // It returns a 2D array with position [0] the column and [1] the value. This helped as sometimes different ones
    
    // are needed and it prevented it needing to be counted twice.
    

    public Integer[] getBestRelativeMoveColumnValue (Player thisPlayer) {

	Integer bestRelativeBoardValue =  ( -5 * winValue * winValue );

	Integer bestColumn = 0;

	for (Integer column = 0; column < boardWidth; column++) {

	    if ( gameBoard.checkFilled(column) == true ) {
		continue;
	    }

	    else {

		Integer relativeBoardValue = getRelativeMoveValue ( thisPlayer, column );

		if ( relativeBoardValue > bestRelativeBoardValue) {

		    bestRelativeBoardValue = relativeBoardValue;

		    bestColumn = column;
		}

		else {
		    continue;
		}
	    }
	}

	Integer[] bestRelativeMoveColumnValue = { bestColumn, bestRelativeBoardValue };

	return bestRelativeMoveColumnValue;
    }



    // This is where the bulk of the processing happens for how good a move is.
    

    public Integer getRelativeMoveValue (Player thisPlayer, Integer thisColumn) {

	// We don't want choosing a full column to ever be a good idea.
	
	if ( gameBoard.checkFilled( thisColumn ) == true ) {

	    return - ( winValue * winValue);
	}

	else {

	    // if it's available, place a token and see how the board looks.
	   
	    gameBoard.placeToken( thisPlayer , thisColumn );

	    Integer nextPlayerWinCount = getWinCountWinningColumn( nextPlayer )[0];

	    Integer thisPlayerWinCount = getWinCountWinningColumn( thisPlayer )[0];


	    // if the next player can win, it's a very bad turn. But not as bad as a full column.
	    
	    if ( nextPlayerWinCount >= 1 ) {

		gameBoard.removeToken( thisColumn );

		return winValue * ( -10 );
	    }


	    // if this player can win in two moves it's a very good turn.
	    
	    if ( thisPlayerWinCount >= 2 ) {

		gameBoard.removeToken( thisColumn );

		return winValue * ( 10 );
	    }

	    
	    if ( nextPlayerWinCount >= 2 ) {

		gameBoard.removeToken( thisColumn );

		return winValue * ( -5 );
	    }


	    Integer relativeTurnValue = 0;

	    Integer thisPlayerTotalPotentialBoardValue = 0;

	    Integer nextPlayerTotalPotentialBoardValue = 0;

	    Integer columnCount = 0;


	    // if going here means this player can win next turn, assume the next player will place their token in that column,
	    
	    // and then calculate the best position that can be played off from there.
	    
	    // The getBestRelativeMoveColumnValue() call is recursive as this method is called in that one.

	    if ( thisPlayerWinCount >= 1 ) {

		Integer thisPlayerNextTurnWinColumn = getWinCountWinningColumn( thisPlayer )[1];

		gameBoard.placeToken( nextPlayer, thisPlayerNextTurnWinColumn );

		relativeTurnValue = getBestRelativeMoveColumnValue( thisPlayer )[1];

		gameBoard.removeToken( thisPlayerNextTurnWinColumn );

		gameBoard.removeToken( thisColumn );

		return relativeTurnValue;

	    }

	    // otherwise if there isn't a winning move that the player will block,
	    
	    // try placing the nextPlayers token in each column and seeing what the board looks like for them.
	    
	    else for ( Integer column = 0 ; column < boardWidth; column++ ) {

		if ( gameBoard.checkFilled(column) == true ) {
		    continue;
		}

		else {

		    // We don't want to set them up for a 2 move win.
		    
		    gameBoard.placeToken( nextPlayer, column );

		    Integer nextPlayer2ndMoveWinCount = getWinCountWinningColumn( nextPlayer )[0];

		    if ( nextPlayer2ndMoveWinCount >= 2 ) {

			gameBoard.removeToken( column );

			gameBoard.removeToken( thisColumn );

			return winValue * ( -5 );
		    }

		    // If no wins are on then the relative board value is calculated by comparing the board value of this player if they go there,
		    
		    // with the average of the next players move subtracted from it. This means setting up the next player for a good move,
		    
		    // is processed as a bad idea. Alternatively the bot is just optimising board value and it's chances to win until there
		    
		    // is one available it can go for.
		    
		    thisPlayerTotalPotentialBoardValue = thisPlayerTotalPotentialBoardValue + gameBoard.getPotentialBoardValue( thisPlayer );

		    nextPlayerTotalPotentialBoardValue =  nextPlayerTotalPotentialBoardValue + gameBoard.getPotentialBoardValue( nextPlayer );

		    columnCount = columnCount + 1;

		    gameBoard.removeToken( column );

		    continue;
		}
	    }

	    // This is here as we don't ever want to divide by 0, this shouldn't get called as then the game would be a draw.
	    
	    if ( columnCount > 0 ) {

		relativeTurnValue = ( thisPlayerTotalPotentialBoardValue ) - ( nextPlayerTotalPotentialBoardValue / 10 ) / columnCount;
	    }

	    // I added a / 10 to the next player potential board value so the bot favours making moves itself. / 10 keeps it in scale with the scoring system.
	    
	    else {

		relativeTurnValue = ( thisPlayerTotalPotentialBoardValue ) - ( nextPlayerTotalPotentialBoardValue / 10 );
	    }

	    gameBoard.removeToken( thisColumn );

	    return relativeTurnValue;
	}
    }

}


