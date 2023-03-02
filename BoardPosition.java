import java.util.Arrays;
import java.util.HashMap;

//This is the BoardPosition class for which each position on the board is an object.

//It is where the bulk of the processing occurs to provide the information to the Board and onto the BotPlayers.

public class BoardPosition {

    // It stores information that the Board uses when dropping a token or printing the board.

    private Integer row;

    private Integer column;

    private Character fillToken;

    private boolean filledStatus = false;

    // Each position can check all the potential 4s it could be a part of, including non-adjacent ones.

    // It assigns a value for each run. The powers of 10 work well as it scales up to connectN and avoids

    // a collection of 2s being worth more than a 3 for example.

    // Changing the relative values changes how the bot plays it's moves.

    private Integer valueFrom4s = 10000;

    private Integer valueFrom3s = 1000;

    private Integer valueFrom2s = 100;

    private Integer valueFrom1s = 10;

    private Integer valueFrom0s = 1;

    // Void values are if that particular 4 is blocked for the player.

    private Integer valueFromVoids = -1;

    // The BoardPosition stores each players score and is called on by the Board for them when getting total board value.

    private HashMap<Player, Integer> playerPositionValues = new HashMap<Player, Integer>();

    private BoardPosition[][] board;

    Integer boardHeight;

    Integer boardWidth;

    // This is to allow scaling to connectN.

    Integer connectN = 4;

    public BoardPosition(Integer thisRow, Integer thisColumn, Board thisGameBoard){

	this.row = thisRow;
	this.column = thisColumn;
	this.filledStatus = false;
	this.fillToken = ' ';

	board = thisGameBoard.getBoard();

	//The board dimensions are needed to know which potential 4 are void and not call on any out of bounds indexes.

	boardWidth = thisGameBoard.getBoardWidth();
	boardHeight = thisGameBoard.getBoardHeight();
    }

    // Getters and setters.

    public void setBoardArray (BoardPosition[][] thisBoardArray) {
	board = thisBoardArray;
	return;
    }

    
    public void setBoardHeight( Integer thisBoardHeight ) {
	boardHeight = thisBoardHeight;
	return;
    }
    
    
    public void setBoardWidth( Integer thisBoardWidth ) {
	boardHeight = thisBoardWidth;
	return;
    }
    
    
    public boolean getFilledStatus() {
	return this.filledStatus;
    }

    public Character getToken() {
	return this.fillToken;
    }


    public void setFilledStatus( Boolean fillstatus) {
	this.filledStatus = fillstatus;
	return;
    }

    public void setToken(Character token) {
	this.fillToken = token;
	return;
    }

    public Integer getRow() {
	return row;
    }

    public Integer getColumn() {
	return column;
    }


    // This is how each position calculates it's values. It produces a 16 digit string, one for each

    // potential 4 it could be a part of ( |, -, /, \ , then 4 different potential 4s in each direction)
    
    // and counts how many are currently filled in that 4, with 9 for a void. 

    // It then converts this string to an array of the totals and passes this to a method to calculate

    // it's value, based on these totals.


    public void setPositionValue (Player thisPlayer) {

	String stringOfPotential4s = checkAllPotential4s(thisPlayer);

	Integer[] num4s3s2s1sVoids = processAllPotential4s( stringOfPotential4s );

	Integer positionValue = calculatePositionValue(num4s3s2s1sVoids);

	playerPositionValues.put( thisPlayer , positionValue);

	return;
    }


    // This method is so the information can be printed.

    public String get4s3s2sVoids (Player thisPlayer) {

	String stringOfPotential4s = checkAllPotential4s(thisPlayer);

	Integer[] num4s3s2s1sVoids = processAllPotential4s( stringOfPotential4s );

	stringOfPotential4s = Arrays.deepToString(num4s3s2s1sVoids);

	return stringOfPotential4s;	
    }


    // Called on by the board to get the total board value.

    public Integer getPositionValue (Player thisPlayer) {

	return playerPositionValues.get( thisPlayer );

    }

    // Method to count if the position is part of a winning run.


    public Boolean getHasWon ( Player thisPlayer) {

	String stringOfPotential4s = checkAllPotential4s(thisPlayer);

	Integer[] num4s3s2s1sVoids = processAllPotential4s( stringOfPotential4s );

	Integer num4s = num4s3s2s1sVoids[0];

	if ( num4s > 0 ) {

	    return true;
	}
	else {

	    return false;

	}
    }


    // This method takes in an integer array, with each position being the total 4s, 3s etc. it then calculates the total position based on the values

    // defined as fields at the top.


    private Integer calculatePositionValue (Integer[] thisNum4s3s2s1sVoids) {

	Integer[] num4s3s2s1sVoids = thisNum4s3s2s1sVoids;

	Integer positionValue = ( num4s3s2s1sVoids[0] * valueFrom4s ) + ( num4s3s2s1sVoids[1] * valueFrom3s ) + ( num4s3s2s1sVoids[2] * valueFrom2s ) +

		( num4s3s2s1sVoids[3] * valueFrom1s ) + ( num4s3s2s1sVoids[4] * valueFrom0s ) + ( num4s3s2s1sVoids[4] * valueFromVoids );

	return positionValue;
    }



    // This method takes in the 16 digit string and counts instances of 4s, 3s, 2s etc. 9s are voids.

    private Integer[] processAllPotential4s (String thisStringAllPotential4s) {

	String stringAllPotential4s = thisStringAllPotential4s;

	Integer num4s = 0;

	Integer num3s = 0;

	Integer num2s = 0;

	Integer num1s = 0;

	Integer num0s = 0;

	Integer numVoids = 0;

	//For every character of the string:

	for(Integer index = 0; index < stringAllPotential4s.length(); index++) {


	    //If it catches the right one it adds one to the count and continues to next character.

	    if (stringAllPotential4s.charAt(index) == '4') {
		num4s = num4s + 1;
		continue;
	    }

	    else if (stringAllPotential4s.charAt(index) == '3') {
		num3s = num3s + 1;
		continue;
	    }

	    else if (stringAllPotential4s.charAt(index) == '2') {
		num2s = num2s + 1;
		continue;
	    }

	    else if (stringAllPotential4s.charAt(index) == '1') {
		num1s = num1s + 1;
		continue;
	    }

	    else if (stringAllPotential4s.charAt(index) == '0') {
		num0s = num0s + 1;
		continue;
	    }


	    else if (stringAllPotential4s.charAt(index) == '9') {
		numVoids = numVoids + 1;
		continue;
	    }

	    //This else shouldn't be reached.

	    else {
		continue;
	    }
	}

	// This part then stores the total of each run in an array.

	Integer[] num4s3s2s1s0sVoids = new Integer[6];

	num4s3s2s1s0sVoids[0] = num4s;

	num4s3s2s1s0sVoids[1] = num3s;

	num4s3s2s1s0sVoids[2] = num2s;

	num4s3s2s1s0sVoids[3] = num1s;

	num4s3s2s1s0sVoids[4] = num0s;

	num4s3s2s1s0sVoids[5] = numVoids;

	return num4s3s2s1s0sVoids;

    }


    // This method groups together all the counts.

    private String checkAllPotential4s (Player thisPlayer) {

	String allPotential4s = checkAllVertical4s(thisPlayer) + checkAllHorizontal4s(thisPlayer) + checkAllDownLeft4s(thisPlayer) + checkAllDownRight4s(thisPlayer);

	return allPotential4s;

    }

    // These getters are used so the information can be printed.

    public String getAllPotential4s ( Player thisPlayer) {

	return checkAllPotential4s( thisPlayer );
    }


    public String getAllVertical4s ( Player thisPlayer) {

	return checkAllVertical4s( thisPlayer );
    }


    public String getAllHorizontal4s ( Player thisPlayer) {

	return checkAllHorizontal4s( thisPlayer );
    }


    public String getAllDownLeft4s ( Player thisPlayer) {

	return checkAllDownLeft4s( thisPlayer );
    }


    public String getAllDownRight4s ( Player thisPlayer) {

	return checkAllDownRight4s( thisPlayer );
    }



    // Each position can be a part of potentially 16 different 4s, 4 in each direction.

    // I counted them using two for loops. I saw it as always counting down or left for a single 4,

    // then iterating up to three away in the opposite direction to catch each possible one in that direction.


    //This is the iteration to catch each instance of a vertical

    public String checkAllVertical4s(Player thisPlayer) {

	String stringVerticalRuns = "";

	for ( Integer rowsAbove = 0; rowsAbove < connectN; rowsAbove ++) {

	    String singleRun = checkSingleVertical4(rowsAbove, thisPlayer);

	    stringVerticalRuns = stringVerticalRuns + singleRun;
	}

	return stringVerticalRuns;
    }


    // Then this is to count the vertical, conditions are stated to stop out of bounds index calls.

    public String checkSingleVertical4( Integer thisRowsAbove, Player thisPlayer) {

	Integer count = 0;

	//count down

	for ( Integer downIndex = 0; (downIndex < connectN) ; downIndex ++) {

	    if ( row - thisRowsAbove + downIndex >= boardHeight || row - thisRowsAbove + downIndex < 0) { 

		return Integer.toString(9);
	    }

	    else {

		if ( board[this.row - thisRowsAbove  + downIndex ][this.column].getToken() == thisPlayer.getToken() ) {

		    count = count + 1;

		    continue;
		}

		else if (board[this.row - thisRowsAbove + downIndex ][this.column].getFilledStatus() == true) {

		    return Integer.toString(9);
		}

		continue;
	    }
	    
	    // return 9 if can't be made into a winning move,  otherwise return the count it is currently at
	}

	String countString = Integer.toString(count);

	return countString;
    }



    //This is the same reasoning but horizontal. Iterates left to catch all instances, then each 4 is counted right.

    public String checkAllHorizontal4s(Player thisPlayer) {

	String stringHorizontalRuns = "";

	for ( Integer columnsLeft = 0; columnsLeft < connectN; columnsLeft++) {

	    String singleRun = checkSingleHorizontal4(columnsLeft, thisPlayer);

	    stringHorizontalRuns = stringHorizontalRuns + singleRun;
	}

	return stringHorizontalRuns;
    }



    public String checkSingleHorizontal4( Integer thisColumnsLeft, Player thisPlayer) {

	Integer count = 0;

	//count down

	for ( Integer columnsRight = 0; columnsRight < connectN; columnsRight ++) {

	    if ( this.column - thisColumnsLeft + columnsRight >= boardWidth || column - thisColumnsLeft + columnsRight < 0) { 

		return Integer.toString(9);
	    }

	    else {

		if ( board[this.row][this.column - thisColumnsLeft + columnsRight ].getToken() == thisPlayer.getToken() ) {

		    count = count + 1;

		    continue;
		}

		else if (board[this.row][this.column - thisColumnsLeft + columnsRight ].getFilledStatus() == true) {

		    return Integer.toString(9);
		}

		else {
		    continue;
		}
		// return 9 if can't be made into a 4 otherwise return the count it is currently at
	    }
	}

	String countString = Integer.toString(count);

	return countString;
    }


    
    // And the same again for downLeft. Now the rows and columns are both changing. I used the idea of first iterator being 
    
    // progressive upRight steps, then counting 4 from the each time.
    
    public String checkAllDownLeft4s(Player thisPlayer) {

	String stringDownLeftRuns = "";

	for ( Integer upRightIndex = 0; upRightIndex < connectN; upRightIndex++) {

	    String singleRun = checkSingleDownLeft4(upRightIndex, thisPlayer);

	    stringDownLeftRuns = stringDownLeftRuns + singleRun;
	}

	return stringDownLeftRuns;
    }



    public String checkSingleDownLeft4( Integer thisUpRightIndex, Player thisPlayer) {

	Integer count = 0;

	//count down

	for ( Integer downLeftIndex = 0; (downLeftIndex < connectN) ; downLeftIndex ++) {

	    if ( this.column - downLeftIndex + thisUpRightIndex < 0 

		    || this.column - downLeftIndex + thisUpRightIndex >= this.boardWidth

		    || this.row + downLeftIndex - thisUpRightIndex >= boardHeight

		    || this.row + downLeftIndex - thisUpRightIndex < 0 ) {

		return Integer.toString(9);
	    }

	    else {

		if ( board[this.row + downLeftIndex - thisUpRightIndex][this.column - downLeftIndex + thisUpRightIndex ].getToken() == thisPlayer.getToken() ) {

		    count = count + 1;

		    continue;
		}

		else if (board[this.row + downLeftIndex - thisUpRightIndex][this.column - downLeftIndex + thisUpRightIndex ].getFilledStatus() == true) {

		    return Integer.toString(9);
		}

		else {
		    continue;
		}
		// return 9 if can't be made into a 4 otherwise return the count it is currently at
	    }
	}

	String countString = Integer.toString(count);

	return countString;
    }


    //And finally for downRight.
    
    public String checkAllDownRight4s(Player thisPlayer) {

	String stringDownRightRuns = "";

	for ( Integer upLeftIndex = 0; upLeftIndex < connectN; upLeftIndex++) {

	    String singleRun = checkSingleDownRight4(upLeftIndex, thisPlayer);

	    stringDownRightRuns = stringDownRightRuns + singleRun;
	}

	return stringDownRightRuns;
    }


    public String checkSingleDownRight4( Integer thisUpLeftIndex, Player thisPlayer) {

	Integer count = 0;

	//count down

	for ( Integer downRightIndex = 0; downRightIndex < connectN ; downRightIndex ++) {

	    if ( this.column - thisUpLeftIndex + downRightIndex < 0 

		    || this.column - thisUpLeftIndex + downRightIndex >= this.boardWidth

		    || this.row + downRightIndex - thisUpLeftIndex >= boardHeight

		    || this.row + downRightIndex - thisUpLeftIndex < 0 ) {

		return Integer.toString(9);
	    }

	    else {

		if ( board[this.row + downRightIndex - thisUpLeftIndex][this.column + downRightIndex - thisUpLeftIndex  ].getToken() == thisPlayer.getToken() ) {

		    count = count + 1;

		    continue;
		}

		else if (board[this.row + downRightIndex - thisUpLeftIndex][this.column + downRightIndex - thisUpLeftIndex ].getFilledStatus() == true) {

		    return Integer.toString(9);
		}

		else {
		    continue;
		}
		// return 9 if can't be made into a 4 otherwise return the count it is currently at
	    }
	}

	String countString = Integer.toString(count);

	return countString;
    }

}
