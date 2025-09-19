package chess;

import java.util.HashSet;


public class Rule {
    private final int repeats;
    private final int[][] coordinates;

    public Rule(int repeats, int[][] coordinates) {
        //making these variables accessible to the whole class
        this.repeats = repeats;
        this.coordinates = coordinates;
    }

    public HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        //moves set
        HashSet<ChessMove> moves = new HashSet<>();
        //row and column
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        //piece and color
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();


        //isPawn
        if (repeats == 2) {
            //ternary operators for universal variables (save lines & easier to remember)
            int verticalMov = (myColor == ChessGame.TeamColor.WHITE) ? +1 : -1;
            int startRow = (myColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
            int promoRow = (myColor == ChessGame.TeamColor.WHITE) ? 8 : 1;

            //two vertical move options
            int singleFRow = myRow + verticalMov;
            int doubleFRow = myRow + (verticalMov * 2);

            //Single forward
            if (isBound(singleFRow, myCol) && isEmpty(board, singleFRow, myCol)) {
                //Promo
                if (singleFRow == promoRow){
                    promotions(moves, myPosition, new ChessPosition(singleFRow, myCol),true);
                }
                //Double forward or Single forward no promo
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(singleFRow, myCol), null));
                    //Double forward
                    if (startRow == myRow && isEmpty(board, doubleFRow, myCol)) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(doubleFRow, myCol), null));
                    }
                }
            }
            int attackRow = myRow+verticalMov;
            int attackColR = myCol-1;
            int attackColL = myCol+1;

            //attack
            attack(moves, board, myColor, myPosition, attackRow, attackColR, promoRow);
            attack(moves, board, myColor, myPosition, attackRow, attackColL, promoRow);
        }


        //iterates through potential moves
        for (int[] coordinate : coordinates) {
            int modifierRow = coordinate[0];
            int modifierCol = coordinate[1];
            int newRow = myRow + modifierRow;
            int newCol = myCol + modifierCol;


            //everything except Pawn
            if (repeats == 0 || repeats == 1) {
                //moves for King, Knight
                if (repeats == 0) {
                    if (isBound(newRow, newCol) && isAvailable(board, newRow, newCol, myColor)) {
                        //King, Knight
                        moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                    }
                    continue;
                }
                //moves for Rook, Bishop, and Queen
                while (isBound(newRow, newCol)) {
                    //Empty
                    if (isEmpty(board,newRow, newCol)) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                    }
                    //Blocked
                    else {
                        if (isEnemy(board,newRow, newCol, myColor)) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
                        }
                        //friend
                        break;
                    }
                    newRow = modifierRow + newRow;
                    newCol = modifierCol + newCol;
                }
            }
        }
        return moves;
    }
    public boolean isBound(int newRow, int newCol){
        return (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8);
    }
    public boolean isAvailable(ChessBoard board, int newRow, int newCol, ChessGame.TeamColor myColor){
        return (isEmpty(board, newRow, newCol) || isEnemy(board, newRow, newCol, myColor));
    }
    public boolean isEnemy(ChessBoard board, int Row, int Col, ChessGame.TeamColor myColor) {
        ChessPiece potentialPiece = board.getPiece(new ChessPosition(Row,Col));
        return (!isEmpty(board,Row, Col) && (potentialPiece.getTeamColor() != myColor));
    }
    public boolean isEmpty(ChessBoard board,int Row,int Col) {
        if (isBound(Row, Col)) {
            ChessPiece potentialPiece = board.getPiece(new ChessPosition(Row, Col));
            return (potentialPiece == null);
        }
        return false;
        }
    public void promotions(HashSet<ChessMove> moves, ChessPosition myPosition, ChessPosition nextPosition, boolean promotion){
        if (promotion) {
            moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, nextPosition, ChessPiece.PieceType.ROOK));
        }
        else {
            moves.add(new ChessMove(myPosition, nextPosition, null));
        }
    }
    public void attack(HashSet<ChessMove> moves, ChessBoard board, ChessGame.TeamColor myColor, ChessPosition myPosition, int row, int col, int promoRow){
        if (isBound(row,col)) {
            if (isEnemy(board, row, col, myColor)) {
                promotions(moves, myPosition, new ChessPosition(row, col), (row == promoRow));
            }
        }
    }
}