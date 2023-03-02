
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//This is the main class. It's role is to initialise all the other game objects then keep track of the turn and 

// call on the right player for a move. It's also responsible for checking for a win or a draw after each turn,

// and keeping the game going in a while true loop otherwise.

public class Game {

    private String columnFullError = "Sorry that column is full, try another";

    private String invalidInputMessage = "Sorry, I didn't catch that. Can you try again?";

    private Player nextPlayer;

    private Board board;

    private Integer turn = 0;

    private Player[] playerList;

    private Integer numberPlayers;

    private Integer boardHeight;

    private Integer boardWidth;


    //This is the main method

    public static void main(String[] args) {

	Game thisGame = new Game();

	Character typeOfGame = thisGame.getGameType();

	if ( typeOfGame == 's') {

	    thisGame.startStandardGame();

	}

	else if ( typeOfGame == 'c' ) {

	    thisGame.startCustomGame();

	}

	thisGame.playGame();
    }



    //This is the function to get the type of game it is.

    private Character getGameType() {

	Scanner scanGameType = new Scanner(System.in);

	System.out.println ( "Hello and welcome to ConnectN. \n \n"
		+ "Press s for a standard game against the computer or\n"
		+ "press c for a custom game." );


	Character userInputGameType = null;

	while (!scanGameType.hasNext("[sc]")) {

	    System.out.println("");

	    System.out.println("Sorry didn't catch that. Enter s or c. \n");

	    scanGameType.next();
	}

	userInputGameType = scanGameType.next().charAt(0);

	return userInputGameType;

    }


    //The simpler set up is a game of standard rules against the computer. Note that the Game object

    // is responsible for setting the other objects with the information they need, or more often

    // providing pointers to each other so they have access to each others methods.

    public void startStandardGame() {

	Player player1 = new HumanPlayer('0', "Human");

	Player player2 = new BotPlayer('X', "Computer");

	numberPlayers = 2;

	playerList = new Player[] {player1, player2};

	board = new Board(6, 7, playerList);

	for(Integer index = 0 ; index < playerList.length; index++ ) {

	    playerList[index].setBoard(board);

	    playerList[index].setNextPlayer( playerList[ ( index + 1 ) % playerList.length ] );

	    playerList[index].setBoardWidth( 7 );
	}

	board.printBoard();

	return;
    }


    // This is the loop that the game stays in until it's won or drawn.

    public Boolean playGame() {

	// This is just so the checkWin method has an input.

	Player player = playerList[0];

	while(board.checkAllColumnWin(player) == false &&board.checkDraw() == false) {

	    // These are here to see how the move values are calculated.
	    
	    board.printInformation( 0, 0);

	    board.printInformation( 4, 3);

	    board.printPlayerPotentialBoardValuesForColumn ();

	    turn = turn + 1;

	    player = playerList [ ( turn - 1 ) % numberPlayers ];

	    Integer columnMove = player.getMove();

	    
	    if ( board.checkFilled(columnMove) == true ) {

		System.out.print(columnFullError+ "\n");

		columnMove = player.getMove();
	    }


	    board.placeToken ( player, columnMove );

	    board.checkWin(player, columnMove);

	    board.checkDraw();

	    board.printBoard();
	}


	if(board.checkAllColumnWin(player) == true) {
	    System.out.print("\nWell done "+player.getPlayerName()+", you've won!");
	}

	if(board.checkDraw() == true) {
	    System.out.print("\nThe game is a draw!");
	}

	return board.checkAllColumnWin(player);
    }




    public Player getNextPlayer() {
	turn = turn + 1;
	Integer playerTurn = turn % numberPlayers;
	nextPlayer = playerList[playerTurn];
	return nextPlayer;
    }



    
    // I left this till last as it's quite lengthy and should be in different functions really.
    
    // It just asks the user for the game conditions and initialises Player objects and a Board object
    
    // with the right specifications.


    public void startCustomGame() {

	Integer userInputNumberOfPlayer = null;

	Scanner scanNumPlayers = new Scanner(System.in);

	System.out.print ( "How many players shall be playing today? Choose between 2 and 5. " );

	while (!scanNumPlayers.hasNext("[2345]")) {

	    System.out.println("");

	    System.out.println("Sorry didn't catch that. Enter 2, 3, 4 or 5");

	    System.out.println("");

	    scanNumPlayers.next();
	}

	userInputNumberOfPlayer = scanNumPlayers.nextInt();

	numberPlayers = userInputNumberOfPlayer;

	// I used a list so I could make a running total of the players.
	
	List <Player> playerWorkingList = new ArrayList <Player>();

	Character userInputHumanOrComputer = null;

	for ( Integer index = 0; index < userInputNumberOfPlayer; index++) {

	    Integer playerNumber = index + 1;

	    System.out.print( "\nIs player"+playerNumber+" a human (h) or a computer (c)? ");

	    Scanner humanOrComputerScan = new Scanner(System.in);

	    while (!humanOrComputerScan.hasNext("[hc]")) {

		System.out.println("Enter h or c");

		humanOrComputerScan.next();
	    }

	    userInputHumanOrComputer = humanOrComputerScan.next().charAt(0);


	    System.out.print( "\nWhat is their name? ");

	    Scanner scanName = new Scanner(System.in);

	    String userInputName = scanName.next();

	    System.out.print( "\nand what will they be using as a counter? Enter any character. ");

	    Scanner tokenScan = new Scanner(System.in);

	    Character userInputToken = tokenScan.next().charAt(0);

	    if ( userInputHumanOrComputer == 'h' ) {

		Player newPlayer = new HumanPlayer( userInputToken, userInputName);

		playerWorkingList.add( newPlayer );

		continue;
	    }

	    else if ( userInputHumanOrComputer == 'c') {

		Player newPlayer = new BotPlayer( userInputToken, userInputName); 

		playerWorkingList.add( newPlayer);

		continue;
	    }
	}
	
	// then converted this list to an array as was easier to use in the game.
	
	playerList = new Player [ numberPlayers ];

	for (Integer index = 0; index < playerWorkingList.size(); index++) {
	    
	    playerList[index] = playerWorkingList.get(index); 
	}


	System.out.println ( "\nWhat size board will we be playing with? " );

	System.out.print ( "Enter the height (Between 3 and 9) " );

	Scanner boardHeightScan = new Scanner(System.in);

	Integer userInputBoardHeight = 0;

	Integer userInputBoardWidth = 0;

	while ( !boardHeightScan.hasNext("[3456789]")) {

	    //System.out.println("");

	    System.out.println("\nI didn't catch that. Enter a number between 3 and 9.");

	    System.out.println("");

	    boardHeightScan.next();
	}

	userInputBoardHeight = boardHeightScan.nextInt();

	System.out.print ( "\nEnter the width (Between 3 and 9) " );

	Scanner boardWidthScan = new Scanner(System.in);

	while ( !boardWidthScan.hasNext("[3456789]")) {

	    //System.out.println("");

	    System.out.println("I didn't catch that. Enter a number between 3 and 9.");

	    System.out.println("");
	}

	userInputBoardWidth = boardWidthScan.nextInt();


	board = new Board ( userInputBoardHeight , userInputBoardWidth , playerList);

	for(Integer index = 0 ; index < playerList.length; index++ ) {

	    playerList[index].setBoard(board);

	    playerList[index].setNextPlayer( playerList[ ( index + 1 ) % playerList.length ] );
	    
	    playerList[index].setBoardWidth( userInputBoardWidth );
	    
	    playerList[index].setBoardWidth( userInputBoardWidth );
	}

	board.printBoard();

	return;
    }
}
