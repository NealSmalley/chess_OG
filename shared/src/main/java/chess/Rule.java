package chess;

import java.util.Arrays;
import java.util.HashSet;


public class Rule {
    private int repeats;
    private int[][] coordinates;

    public Rule(int repeats, int[][] coordinates) {
        //making these variables accessible to the whole class
        this.repeats = repeats;
        this.coordinates = coordinates;
    }

    public HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        //moves set
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
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
            if (isEmpty(board, newforwardRow,myCol, myColor)) {
                //doubleforward
                if (firstrow == myRow) {
                    //empty
                    if (isEmpty(board,newDForwardRow,myCol, myColor)){
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
                ChessPiece potentialPiece = board.getPiece(new ChessPosition(attackRRow, attackRCol));
                if (isEnemy(board, attackRRow, attackRCol, myColor)) {
                    promotions(moves, myPosition, new ChessPosition(attackRRow, attackRCol), (attackRRow == finalrow));
                }
            }
            //attacks L
            if (isBound(attackLRow,attackLCol)) {
                ChessPiece potentialPiece = board.getPiece(new ChessPosition(attackLRow, attackLCol));
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
                    ChessPiece potentialPiece = board.getPiece(new ChessPosition(newRow, newCol));
                    //Empty
                    if (isEmpty(board,newRow, newCol, myColor)) {
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
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8){
            return true;
        }
        return false;
    }
    public boolean isAvailable(ChessBoard board, int newRow, int newCol, ChessGame.TeamColor myColor){
        if (isEmpty(board, newRow, newCol, myColor)){
            return true;
        }
        else if (isEnemy(board, newRow, newCol, myColor)){
            return true;
        }
        return false;
    }
    public boolean isEnemy(ChessBoard board, int Row, int Col, ChessGame.TeamColor myColor) {
        ChessPiece potentialPiece = board.getPiece(new ChessPosition(Row,Col));
        if (!isEmpty(board,Row, Col, myColor)) {
            if (potentialPiece.getTeamColor() != myColor) {
                return true;
            }
        }
        return false;
    }
    public boolean isEmpty(ChessBoard board,int Row,int Col, ChessGame.TeamColor myColor){
        if (isBound(Row,Col)) {
            ChessPiece potentialPiece = board.getPiece(new ChessPosition(Row, Col));
            if (potentialPiece== null) {
                return true;
            }
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