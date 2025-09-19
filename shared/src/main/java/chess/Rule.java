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


        int startcoor;
        int firstrow;
        int finalrow;
        int doubleforward;
        int attackL;
        int attackR;


        //isPawn
        if (repeats == 2) {
            //Color
            if (ChessGame.TeamColor.WHITE == myColor) {
                startcoor = 0;
                firstrow = 2;
                finalrow = 8;
                doubleforward = 1;
                attackL = 2;
                attackR = 3;
            } else {
                startcoor = 4;
                firstrow = 7;
                finalrow = 1;
                doubleforward = -1;
                attackL = 6;
                attackR = 7;
            }
            //forward vars
            int[] forward = coordinates[startcoor];
            int newforwardRow = myRow + forward[0];
            int newDForwardRow = newforwardRow + doubleforward;

            //attack vars
            int attackLRow = myRow + coordinates[attackL][0];
            int attackLCol = myCol + coordinates[attackL][1];
            int attackRRow = myRow + coordinates[attackR][0];
            int attackRCol = myCol + coordinates[attackR][1];

            //forward
            if (isEmpty(board, newforwardRow,myCol)) {
                //doubleforward
                if (firstrow == myRow) {
                    //empty
                    if (isEmpty(board,newDForwardRow,myCol)){
                        moves.add(new ChessMove(myPosition, new ChessPosition(newDForwardRow, myCol), null));
                    }
                    moves.add(new ChessMove(myPosition, new ChessPosition(newforwardRow, myCol), null));
                }
                //promotion
                else if (newforwardRow == finalrow) {
                    promotions(moves, myPosition, new ChessPosition(newforwardRow, myCol),true);
                }
                else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(newforwardRow, myCol), null));
                }
            }

            //attack
            //attacks R
            if (isBound(attackRRow,attackRCol)) {
                if (isEnemy(board, attackRRow, attackRCol, myColor)) {
                    promotions(moves, myPosition, new ChessPosition(attackRRow, attackRCol), (attackRRow == finalrow));
                }
            }
            //attacks L
            if (isBound(attackLRow,attackLCol)) {
                if (isEnemy(board, attackLRow, attackLCol, myColor)) {
                    promotions(moves, myPosition, new ChessPosition(attackLRow, attackLCol), (attackLRow == finalrow));
                }
            }
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
}