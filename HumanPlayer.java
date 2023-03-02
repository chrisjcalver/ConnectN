
import java.util.Scanner;

// The subClass for human player. It overrides the getMove method and asks for a column.

public class HumanPlayer extends Player {


    public HumanPlayer(Character thisToken, String thisPlayerName) {
	super(thisToken, thisPlayerName);

    }


    // This isn't as robust as I'd like as it crashes on letter input, although handles incorrect column numbers.
    
    @Override
    public Integer getMove() {

	System.out.println( getPlayerName()+": your move.");

	Integer columnChoice = 0;

	Integer columnInteger = 0;

	Scanner columnChoiceScan = new Scanner(System.in);

	columnChoice = null;

	while (!columnChoiceScan.hasNextInt() ) {

	    System.out.println("Enter a valid column number.");

	    columnChoiceScan.nextInt();

	}

	columnChoice = columnChoiceScan.nextInt();

	if ( columnChoice >= 1 && columnChoice <= boardWidth ) {

	    columnInteger = columnChoice - 1;

	    return columnInteger;
	}

	else {

	    getMove();

	}

	return -1;
    }
}


