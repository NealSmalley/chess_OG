package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

//import static sun.jvm.hotspot.debugger.win32.coff.DebugVC50X86RegisterEnums.BL;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
//    public enum TeamColor {
//        WHITE,
//        BLACK
//    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        var moves = new HashSet<ChessMove>();
        //Simplified pieceType
        var pieceType = piece.getPieceType();
        //Simplified enums
        var Pawn = PieceType.PAWN;
        var Rook = PieceType.ROOK;
        var Knight = PieceType.KNIGHT;
        var Bishop = PieceType.BISHOP;
        var Queen = PieceType.QUEEN;
        var King = PieceType.KING;

        //Pawn
        if ((pieceType == Pawn)){
            pawnMoves(moves);
        }
        //Rooks
        else if (pieceType == Rook){
            rookMoves(moves);
        }
        //Knight
        else if (pieceType == Knight){
            knightMoves(moves);
        }
        //Bishop
        else if (pieceType == Bishop){
            bishopMoves(moves,myPosition,board);
        }
        //Queen
        else if (pieceType == Queen){
            queenMoves(moves);
        }
        //King
        else if (pieceType == King){
            kingMoves(moves);
        }
        return moves;
    }

    public HashSet<ChessMove> pawnMoves(HashSet<ChessMove> moves){
        moves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,5), null));
        return moves;
    }
    public HashSet<ChessMove> rookMoves(HashSet<ChessMove> moves){
        moves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,5), null));
        return moves;
    }
    public HashSet<ChessMove> knightMoves(HashSet<ChessMove> moves){
        moves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,5), null));
        return moves;
    }
    public HashSet<ChessMove> bishopMoves(HashSet<ChessMove> moves,ChessPosition myPosition, ChessBoard board){
        //bottom corners finding function
        ChessPosition bottomR = bottomRCornerFinder(myPosition, board);
        ChessPosition bottomL = bottomLCornerFinder(myPosition, board);
        //diagonals function
        diagonal(bottomR,bottomL,moves,myPosition,board);
        return moves;
    }
    public HashSet<ChessMove> queenMoves(HashSet<ChessMove> moves){
        moves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,5), null));
        return moves;
    }
    public HashSet<ChessMove> kingMoves(HashSet<ChessMove> moves){
        moves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,5), null));
        return moves;
    }

    //bishopMoves helper functions
    //Corner Functions
    public ChessPosition bottomRCornerFinder(ChessPosition myPosition, ChessBoard board) {
        //Bottom Right rows and columns
        int brrow = myPosition.getRow();
        int brcol = myPosition.getColumn();

        //Finds Bottom Right corner
        while ((brrow > 1 && brcol < 8)) {
            //Checks the diagonal below
            brrow = brrow - 1;
            brcol = brcol + 1;
            //checks if there is a piece in that position
            boolean empty = emptyposition(brrow,brcol,board,myPosition);
            //not empty but a friend
            if (!empty && !isEnemy(brrow,brcol,board,myPosition)){
                break;
            }
            //not empty but an enemy
            if (!empty && isEnemy(brrow,brcol,board,myPosition)){
                break;
            }
            //empty

        }

        ChessPosition BottomRight = new ChessPosition(brrow,brcol);
        return BottomRight;
    }
    public ChessPosition bottomLCornerFinder(ChessPosition myPosition, ChessBoard board) {
        //Bottom Left rows and columns
        int blrow = myPosition.getRow();
        int blcol = myPosition.getColumn();

        //Finds Bottom Left corner
        while ((blrow > 1 && blcol > 1)) {
            //checks if there is a piece in that position
            blrow--;
            blcol--;
            //is empty
            boolean empty = emptyposition(blrow,blcol,board,myPosition);

            //not empty but a friend
            if (!empty && !isEnemy(blrow,blcol,board,myPosition)){
                blrow++;
                blcol++;
                //not empty and the original
                if (myPosition.equals(new ChessPosition(blrow,blcol))){
                    blrow++;
                    blcol++;
                    break;
                }
            }
            //not empty but an enemy
            if (!empty && isEnemy(blrow,blcol,board,myPosition)){
                break;
            }
            //empty


        }
        ChessPosition BottomLeft = new ChessPosition(blrow,blcol);
        return BottomLeft;
    }
    public boolean emptyposition(int brrow, int brcol, ChessBoard board, ChessPosition myPosition){
        //Find if there is already a piece on the board
        ChessPiece potentialpiece = board.getPiece(new ChessPosition(brrow,brcol));
        //if piece is not on board
        if (potentialpiece == null) {
            return true;
        }
        //if piece is on board
        else {
            return false;
        }
    }
    public boolean isEnemy(int row, int col, ChessBoard board, ChessPosition myPosition){
        //If occupid position
        if (!emptyposition(row,col,board,myPosition)){
            ChessPiece potentialpiece = board.getPiece(new ChessPosition(row,col));
            ChessPiece mypiece = board.getPiece(myPosition);
            ChessGame.TeamColor myPieceColor = mypiece.getTeamColor();

            //If is enemy color
            if (!(myPieceColor.equals(potentialpiece.getTeamColor()))){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    //Diagonal
    public void diagonal(ChessPosition br, ChessPosition bl, HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {
        //Row and Col values
        int brRow = br.getRow();
        int brCol = br.getColumn();
        int blRow = bl.getRow();
        int blCol = bl.getColumn();

        //adding corner chess positions
        if (((myPosition.getRow() != brRow) && (myPosition.getColumn() != brCol))) {
            moves.add(new ChessMove(myPosition, br, null));
        }
        if (((myPosition.getRow() != blRow) && (myPosition.getColumn() != blCol))) {
            moves.add(new ChessMove(myPosition, bl, null));
        }
        //if enemy hit
        boolean enemyHitBR = false;
        boolean enemyHitBL = false;

        //in bounds
        while ((brRow < 8 && brCol > 1) || (blRow < 8 && blCol < 8)) {
            //br
            //increment
            //if enemy hit bottom right
            if (!enemyHitBR) {
                brRow = brRow + 1;
                brCol = brCol - 1;
                //if not the OG position
                //is empty
                if (emptyposition(brRow, brCol, board, myPosition)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(brRow, brCol), null));
                }
                //is an enemy
                else if (isEnemy(blRow, blCol, board, myPosition)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(blRow, blCol), null));
                    enemyHitBR = true;
                }
                //is a friend/OG position continue
            }

            //bl
            if (!enemyHitBL) {
                //increment
                blRow = blRow + 1;
                blCol = blCol + 1;
                //is empty
                if (emptyposition(blRow, blCol, board, myPosition)) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(blRow, blCol), null));
                }
                //is an enemy
                else if (isEnemy(blRow, blCol, board, myPosition)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(blRow, blCol), null));
                    enemyHitBL = true;
                }
                //is a friend/OG position
            }
            else if (enemyHitBR && enemyHitBL){
                break;
            }
        }
    }
//    public void diagonalBottomLeft(){
//
//    }
//    public void diagonalBottomLeft(){
//
//    }




    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
