import java.util.ArrayList;
import java.util.Scanner;

public class start {

    int column;
    int row;
    int maxDepth;
    boolean c_moves;
    int[][] game;
	int my_depth;
    board b;
	
    public void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
		System.out.print("\n\nIs the computer the first player? Enter true or false ");
		c_moves = myObj.nextBoolean();
        System.out.print("\n\nEnter Depth ");
        my_depth = myObj.nextInt();
		start game_reversi = new start(my_depth);
        while (true){
             game_reversi.reversiGame();
            //emfanish apotelesmatwn otan gemisei o pinakas h otan den uparxoun alles diathesimes kinhseis gia upologisth h user
			if ((((Integer)game_reversi.b.disks(1)).intValue() + ((Integer)game_reversi.b.disks(2)).intValue() == 64) || (((b.ValidMoves(1)).size() == 0) && ((b.ValidMoves(2)).size() == 0))){
				if ((((Integer)game_reversi.b.disks(1)).intValue()) < (((Integer)game_reversi.b.disks(2)).intValue())) {
					System.out.printf("The user won"+ '\n' + "User: %d", game_reversi.b.disks(1) + '\n' + "Computer: %d", game_reversi.b.disks(2)); 
				} else if ((((Integer)game_reversi.b.disks(1)).intValue()) > (((Integer)game_reversi.b.disks(2)).intValue())) {
					System.out.printf("The computer won" + '\n' + "Computer: %d", game_reversi.b.disks(2) + '\n' +"User: %d", game_reversi.b.disks(1)); 
				} else {
					System.out.printf("Tie: %d - %d", game_reversi.b.disks(1)); 
				}
			break;
            }
        }
    }

    //enarksh paixnidiou
	public start(int depth) {
        maxDepth = depth;
        initialState();
        b = new board(game);
        printGame();
    }

    //dialegei o paixths syntetagmenes
	public void reversiGame(){
        //paizei o upologisths
		if (c_moves){
			computerMoves(maxDepth);
		}
		//paizei o user
		else {
			Scanner Obj1 = new Scanner(System.in);
			System.out.print("\nEnter Column ");
			column = Obj1.nextInt();
			System.out.print("\nEnter Row ");
			row = Obj1.nextInt();
			playerMoves(column, row); }
        System.out.printf("%User Score: %d", b.disks(1));
        System.out.printf("%nComputer Score: %d", b.disks(2));
    }
	
	//thesh enarkshs tou paixnidiou
    public void initialState() {
        game = new int[8][8];
        game[3][3] = 1;
        game[4][4] = 1;
        game[3][4] = 2;
        game[4][3] = 2;
    }
	
	//kinhsh tou user kai meta synexizei to computer
	public void playerMoves(int x, int y) {
		column = x;
        row = y;
		
		//elegxos gia thn diathesimothta kinhsewn tou user
		if (b.ValidMoves(2).size() == 0) {
			System.out.print("No Valid Moves for the user. Computer's turn again");			
			printGame();
			c_moves = true;
			return;
        }
		//elegxos egkurothtas kinhshs tou user
		if (!b.move(x, y, 2)){
			System.out.printf("Make an other move");
			return; 
		}	
		printGame();
        System.out.println("\nUser's choice (" + column + " , " + row + ")");
        computerMoves(maxDepth);
    }
	
	//"epilogh" kinhshs tou computer
    public void computerMoves(int depth) {
		
		//emfanish mhnumatos gia mh diathesimes kinhseis tou upologisth
		if (b.ValidMoves(1).size() == 0) {
			System.out.printf("No Valid Moves for the computer. User's turn again");
			c_moves = false;
		return; }
		
        reversi.startTime = System.currentTimeMillis();
        reversi.cutOff = false;
        int[][] boardCpy = board.cloneGrid(b.state);

        //epilogh kaluterhs dunaths kinhshs tou upologisth mexris otou kseperastoun ta oria
		for (int i = 1; i <= depth && !reversi.cutOff; i++) {
            reversi.minimax(true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, new board(boardCpy), i);
            if (!reversi.cutOff) {
                //kataxwreitai ston board h kinhsh tou upologisth me thn kaluterh apodosh
				b = reversi.best;
            } else {
                System.out.println("\nComputer's Move depth: " + (i - 1));
            }
        }

        //an den lhksei o diathesimos xronos emfanizei oti egine elegxos se olo to epitrepto vathos
		if (!reversi.cutOff){
            System.out.println("\nComputer's Move depth: " + depth + " (full)");
		}
        printGame();
		Scanner Obj2 = new Scanner(System.in);
		System.out.println("\nDo you want to make the next move?");		
		c_moves = (!Obj2.nextBoolean());
    }
    
	//emfanish katastashs paixnidiou
    void printGame() {
        for (int y = 0; y < 8; y++){
            System.out.println("\n");
            for (int x = 0; x < 8; x++) {
                SetBlock(x, y, b.state[x][y]);
            }
        }
    }
 
	//katastash paixnidiou
    public void SetBlock(int x, int y, int c) {
        switch (c) {
            case 0:
                System.out.print("- ");
                break;
            case 1:
                System.out.print("X ");
                break;
            case 2:
                System.out.print("O ");
                break;
        }
    }
}