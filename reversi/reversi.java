import java.util.ArrayList;

public class reversi {
	
	public static long maxTime = 500;
	public static long startTime = 0;
	public static boolean cutOff = false;
	public static board best;
	
	public static int minimax(boolean maxPlayer, int level, int alpha, int beta, board b, int maxLevel) {		
		//elegxos gia to ean exei upervei ton megisto epitrepto xrono h epipedo
		if ((System.currentTimeMillis() - startTime >= maxTime) || (level > maxLevel)) {  
			if (level <= maxLevel) {
				cutOff = true;
			}
			return b.disks(1) - b.disks(2);
		}
				
		ArrayList<board> moves = b.ValidMoves(maxPlayer ? 1 : 2);		
		//an exei ftasei se leaf
		if (moves.size() == 0) {
			return maxPlayer ? 64 : -64;
		}
		
		if (maxPlayer) {
			int top = 0;			
			//elegxei oles tis diathesimes kinhseis
			for (int i = 0; i < moves.size(); i++) {
				//gia to score ekteleitai MiniMax ksekinwntas apo to min tou epomenou epipedou
				int score = minimax(false, level + 1, alpha, beta, moves.get(i), maxLevel);				
				//neo fragma
				if (score > alpha) {
					alpha = score;
					top = i;
				}				
				//prionisma
				if (alpha >= beta) {
					break;
				}
			}
			//kathe fora pou auksanetai to maxLevel otan ginei to prionisma apothhkeyei sto best thn kaluterh epilogh gia ton upologisth
			if (level == 0) { // set board corresponding to optimal value (0-63)
				best = moves.get(top);
			}
			//alpha = katw fragma
			return alpha;
		} else {
			//elegxei oles tis diathesimes kinhseis
			for (int i = 0; i < moves.size(); i++) {
				//gia to score ekteleitai MiniMax ksekinwntas apo to max tou epomenou epipedou
				int score = minimax(true, level + 1, alpha, beta, moves.get(i), maxLevel);				
				//neo fragma
				if (score < beta)
					beta = score;				
				//prionisma
				if (alpha >= beta) {
					break;
				}
			}
			//beta = anw fragma
			return beta;
		}		
	}	
}