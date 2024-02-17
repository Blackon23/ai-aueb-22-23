import java.util.ArrayList;

public class board {
	
	public int[][] state;
	public int movedX, movedY;
	
	public board(int[][] state) {
		this.state = state;	
	}
	
	public boolean move(int X, int Y, int player) {
		//kalumenh thesh
		if (state[X][Y] != 0)
			return false;
		
		boolean legalMove = false;		
		//dunatothta kinhsewn (-1:dutika/katw, 0:trexousa thesh, 1:anatolika/panw)
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				//gia thn trexousa thesh
				if (i == 0 && j == 0) {
					continue;
				}
				
				boolean flip = false, passed = false;
				//k = poses theseis deksiotera h aristerotera
				int k = 1;				
				//elegxos gia tis dunates kinhseis entos twn oriwn tou pinaka
				while (X + j * k >= 0 && X + j * k < 8 && Y + i * k >= 0 && Y + i * k < 8) { 
			
					//kenh thesh h thesh pou anhkei se paixth kai den exei perasei diskous antipalou
					if (state[X + j * k][Y + i * k] == 0 || (state[X + j * k][Y + i * k] == player && !passed)) {
						break;
					}
					//kalummenh thesh kai perasma apo disko antipalou
					if (state[X + j * k][Y + i * k] == player && passed) {
						flip = true;
						break;
					}
					//thesh pou anhkei ston antipalo
					else if (state[X + j * k][Y + i * k] == player % 2 + 1) {
						passed = true;
						//kinhsh k fores deksiotera kai aristerotera
						k++;
					}
				}
				
				//allagh me diskous tou paixth tous diskous tou antipalou pou prosperase
				if (flip) {
					state[X][Y] = player;					
					for (int h = 1; h <= k; h++) {
						state[X + j * h][Y + i * h] = player;
					}					
					legalMove = true;
				}
			}
		}
		this.movedX = X;
		this.movedY = Y;
		
		return legalMove;
	}
	
	//upologismos diskwn paixth
	public int disks(int player) {
		int v = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (state[j][i] == player) {
					v++;
				}
			}
		}
		return v;		
	}

	//epistrefei lista me tis egkures kinhseis tou paixth dhmiourgwntas kai tous antistoixous klwnous se kathe egkurh kinhsh
	public ArrayList<board> ValidMoves(int player) {		
		ArrayList<board> boardList = new ArrayList<board>();
		board b = new board(cloneGrid(state));		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (b.move(j, i, player)) {
					boardList.add(b);
					b = new board(cloneGrid(state));
				}
			}
		}		
		return boardList;
	}
	
	//ftiaxnei klwno tou state xrhsimopoiwntas thn clone gia na dhmiourghthei ena vathu antigrafo tou pinaka xwris na exoun thn idia anafora
	public static int[][] cloneGrid(int[][] state) {
	    int[][] r = new int[8][];
	    for (int i = 0; i < 8; i++) {
	        r[i] = state[i].clone();
	    }
	    return r;
	}
}