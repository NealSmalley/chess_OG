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
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */

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

        switch(pieceType) {
            //Pawn
            case PAWN -> pawnMoves(moves, myPosition, board);
            //Rooks
            case ROOK -> rookMoves(moves, myPosition, board);
            //Knight
            case KNIGHT -> knightMoves(moves, myPosition, board);
            //Bishop
            case BISHOP -> bishopMoves(moves, myPosition, board);
            //Queen
            case QUEEN -> queenMoves(moves, myPosition, board);
            //King
            case KING -> kingMoves(moves, myPosition, board);
        }
        return moves;
    }

    public HashSet<ChessMove> pawnMoves(HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {

        ChessPiece mypiece = board.getPiece(myPosition);
        ChessGame.TeamColor color = mypiece.getTeamColor();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //color-based variables
        int firstMove = 0;
        int standardForward = 0;
        int doubleFor = 0;
        int singleFor = 0;
        int lAttackRow = 0;
        int lAttackCol = 0;
        int rAttackRow = 0;
        int rAttackCol = 0;
        int promotionRow = 0;

        //White
        if (color == ChessGame.TeamColor.WHITE){
            firstMove = 2;
            standardForward = 1;
            doubleFor = 2;
            singleFor = 1;
            lAttackRow = 1;
            lAttackCol = -1;
            rAttackRow = 1;
            rAttackCol = 1;
            promotionRow = 8;
        }
        //Black
        if (color == ChessGame.TeamColor.BLACK) {
            firstMove = 7;
            standardForward = -1;
            doubleFor = -2;
            singleFor = -1;
            lAttackRow = -1;
            lAttackCol = -1;
            rAttackRow = -1;
            rAttackCol = 1;
            promotionRow = 1;
        }


        //error catch
        if (firstMove == 0) {
            throw new IllegalArgumentException("firstMove isn't updating");
        }
        if (standardForward == 0) {
            throw new IllegalArgumentException("standardForward isn't updating");
        }
        if (doubleFor == 0) {
            throw new IllegalArgumentException("doubleFor isn't updating");
        }
        if (singleFor == 0) {
            throw new IllegalArgumentException("singleFor isn't updating");
        }
        if (lAttackRow == 0) {
            throw new IllegalArgumentException("lAttackRow isn't updating");
        }
        if (lAttackCol == 0) {
            throw new IllegalArgumentException("lAttackCol isn't updating");
        }
        if (rAttackRow == 0) {
            throw new IllegalArgumentException("rAttackRow isn't updating");
        }
        if (rAttackCol == 0) {
            throw new IllegalArgumentException("rAttackCol isn't updating");
        }
        if (promotionRow == 0) {
            throw new IllegalArgumentException("promotionRow isn't updating");
        }



        //FirstMove
        if (row == firstMove){
            doubleForward(row, col, doubleFor, singleFor, myPosition, board, moves);
            standardMoves(row, col, promotionRow, lAttackRow, lAttackCol, rAttackRow, rAttackCol, standardForward, myPosition, board, moves);
        }
        else {
            standardMoves(row, col, promotionRow, lAttackRow, lAttackCol, rAttackRow, rAttackCol, standardForward, myPosition, board, moves);
        }

        return moves;
    }

    //Double Forward
    public void doubleForward(int row, int col, int doubleFor, int singleFor, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves) {
        int doubleNRow = row+doubleFor;
        int singleNRow = row+singleFor;

        ChessPosition singlenextPosition = new ChessPosition(singleNRow, col);
        ChessPosition doublenextPosition = new ChessPosition(doubleNRow, col);

        boolean singleavailable = available(myPosition, singlenextPosition, board);
        boolean doubleavailable = available(myPosition, doublenextPosition, board);

        boolean doubleisEmpty = isEmpty(board, doublenextPosition);
        boolean singleisEmpty = isEmpty(board, singlenextPosition);
        if (doubleavailable && doubleisEmpty && singleavailable && singleisEmpty){
            moves.add(new ChessMove(myPosition, doublenextPosition, null));
        }
    }
    //Standard Moves
    public void standardMoves(int row, int col, int promotionRow, int lAttackRow, int lAttackCol, int rAttackRow, int rAttackCol, int standardForward, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves) {
        forward(row, col, promotionRow, standardForward, myPosition, board, moves);
        attack(row, col, promotionRow, lAttackRow, lAttackCol, rAttackRow, rAttackCol, board, moves, myPosition);
    }

    public void forward(int row, int col, int promotionRow, int standForward, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves){
        int Nrow = row+standForward;
        ChessPosition nextPosition = new ChessPosition(Nrow, col);
        boolean available = available(myPosition, nextPosition, board);
        boolean isEmpty = isEmpty(board, nextPosition);
        if (available && isEmpty){
            if (isPromotion(Nrow, promotionRow)){
                allPromotions(moves, myPosition, nextPosition);
            }
            else {
                moves.add(new ChessMove(myPosition, nextPosition, null));
            }
        }
    }
    public boolean isPromotion(int row, int promotionRow){
        if (row == promotionRow){
            return true;
        }
        else {
            return false;
        }
    }

    public void allPromotions(HashSet<ChessMove> moves, ChessPosition myPosition, ChessPosition nextPosition){
        moves.add(new ChessMove(myPosition, nextPosition, PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, nextPosition, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, nextPosition, PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, nextPosition, PieceType.ROOK));
    }

    public void attack(int row, int col, int promotionRow, int lAttackRow, int lAttackCol, int rAttackRow, int rAttackCol, ChessBoard board, HashSet<ChessMove> moves, ChessPosition myPosition){
        ChessPosition Lattack = new ChessPosition(row + lAttackRow, col + lAttackCol);
        ChessPosition Rattack = new ChessPosition(row + rAttackRow, col + rAttackCol);

        //Left Attack
        if (isEnemy(board,myPosition,Lattack)){
            if (isPromotion(Lattack.getRow(), promotionRow)){
                allPromotions(moves, myPosition, Lattack);
            }
            else {
                moves.add(new ChessMove(myPosition, Lattack, null));
            }
        }
        //Right Attack
        if (isEnemy(board,myPosition,Rattack)){
            if (isPromotion(Rattack.getRow(), promotionRow)) {
                allPromotions(moves, myPosition, Rattack);
            }
            else {
                moves.add(new ChessMove(myPosition, Rattack, null));
            }
        }
    }


    public HashSet<ChessMove> rookMoves(HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        up(myRow, myCol, myPosition,board, moves);
        down(myRow, myCol, myPosition,board, moves);
        left(myRow, myCol, myPosition,board, moves);
        right(myRow, myCol, myPosition,board, moves);
        return moves;
    }
    public void up(int Row, int Col, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves){
        for(int Nrow = Row; Nrow < 8+Row;){
            Nrow++;
            ChessPosition nextPosition = new ChessPosition(Nrow,Col);
            boolean available = available(myPosition, nextPosition, board);
            boolean isFriend = !isEnemy(board, myPosition, nextPosition);
            if (available) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
                boolean isEmpty = isEmpty(board, nextPosition);
                if (!isEmpty) {
                    break;
                }
            }
            else if (isFriend){
                break;
            }
        }
    }
    public void down(int Row, int Col, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves){
        for(int Nrow = Row; Nrow > -8-Row;){
            Nrow--;
            ChessPosition nextPosition = new ChessPosition(Nrow,Col);
            boolean available = available(myPosition, nextPosition, board);
            boolean isFriend = !isEnemy(board, myPosition, nextPosition);
            if (available) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
                boolean isEmpty = isEmpty(board, nextPosition);
                if (!isEmpty) {
                    break;
                }
            }
            else if (isFriend){
                break;
            }
        }
    }
    public void right(int Row, int Col, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves){
        for(int Ncol = Col; Ncol < 8+Col;){
            Ncol++;
            ChessPosition nextPosition = new ChessPosition(Row,Ncol);
            boolean available = available(myPosition, nextPosition, board);
            boolean isFriend = !isEnemy(board, myPosition, nextPosition);
            if (available) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
                boolean isEmpty = isEmpty(board, nextPosition);
                if (!isEmpty) {
                    break;
                }
            }
            else if (isFriend){
                break;
            }
        }
    }
    public void left(int Row, int Col, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves){
        for(int Ncol = Col; Ncol > -8-Col;){
            Ncol--;
            ChessPosition nextPosition = new ChessPosition(Row,Ncol);
            boolean available = available(myPosition, nextPosition, board);
            boolean isFriend = !isEnemy(board, myPosition, nextPosition);
            if (available) {
                moves.add(new ChessMove(myPosition, nextPosition, null));
                boolean isEmpty = isEmpty(board, nextPosition);
                if (!isEmpty) {
                    break;
                }
            }
            else if (isFriend){
                break;
            }
        }
    }

    public HashSet<ChessMove> knightMoves(HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        topLs(myRow, myCol, myPosition, board, moves);
        rightLs(myRow, myCol, myPosition, board, moves);
        leftLs(myRow, myCol, myPosition, board, moves);
        bottomLs(myRow, myCol, myPosition, board, moves);
        return moves;
    }
    public void topLs(int row, int col, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> moves){
        for (int Ncol = col-1; Ncol <= col + 1; Ncol++){
            int Nrow = row+2;
            if (Ncol == col){
                continue;
            }
            else {
                ChessPosition nextPosition = new ChessPosition(Nrow, Ncol);
                boolean available = available(myPosition, nextPosition, board);
                if (available) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }
    }
    public void rightLs(int row, int col, ChessPosition myPosition,ChessBoard board, HashSet<ChessMove> moves){
        for (int Nrow = row-1; Nrow <= row + 1; Nrow++){
            int Ncol = col + 2;
            if (Nrow == row){
                continue;
            }
            else {
                ChessPosition nextPosition = new ChessPosition(Nrow, Ncol);
                boolean available = available(myPosition, nextPosition, board);
                if (available) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }
    }
    public void leftLs(int row, int col, ChessPosition myPosition,ChessBoard board, HashSet<ChessMove> moves){
        for (int Nrow = row-1; Nrow <= row + 1; Nrow++){
            int Ncol = col - 2;
            if (Nrow == row){
                continue;
            }
            else {
                ChessPosition nextPosition = new ChessPosition(Nrow, Ncol);
                boolean available = available(myPosition, nextPosition, board);
                if (available) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }
    }
    public void bottomLs(int row, int col, ChessPosition myPosition,ChessBoard board, HashSet<ChessMove> moves){
        for (int Ncol = col-1; Ncol <= col + 1; Ncol++){
            int Nrow = row-2;
            if (Ncol == col){
                continue;
            }
            else {
                ChessPosition nextPosition = new ChessPosition(Nrow, Ncol);
                boolean available = available(myPosition, nextPosition, board);
                if (available) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }
    }

    public HashSet<ChessMove> bishopMoves(HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {
        //bottom corners finding function
        ChessPosition bottomR = bottomRCornerFinder(myPosition, board);
        ChessPosition bottomL = bottomLCornerFinder(myPosition, board);
        //diagonals function
        diagonal(bottomR, bottomL, moves, myPosition, board);
        return moves;
    }

    public HashSet<ChessMove> queenMoves(HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {
        bishopMoves(moves, myPosition, board);
        rookMoves(moves, myPosition, board);
        return moves;
    }

    public HashSet<ChessMove> kingMoves(HashSet<ChessMove> moves, ChessPosition myPosition, ChessBoard board) {
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        int iter = 1;
        //Potential Moves
        potentialMoves(myRow, myCol, myPosition, moves, board, iter);
        return moves;
    }

    //kingMoves helper functions
    public void potentialMoves(int myRow, int myCol, ChessPosition myPosition, HashSet<ChessMove> moves, ChessBoard board, int iter) {
        for (int NRow = myRow - iter; NRow <= myRow+iter; NRow++) {
            for (int NCol = myCol - iter; NCol <= myCol+iter; NCol++) {
                ChessPosition nextPosition = new ChessPosition(NRow, NCol);
                boolean available = available(myPosition, nextPosition, board);
                if (available) {
                    moves.add(new ChessMove(myPosition, nextPosition, null));
                }
            }
        }
    }

    public boolean available(ChessPosition myPosition, ChessPosition nextPosition, ChessBoard board) {
        if (isBound(nextPosition)) {
            if (isEmpty(board, nextPosition)) {
                return true;
            }
            else if (isEnemy(board, myPosition, nextPosition)){
                return true;
            }

        }
        return false;
    }


    public boolean isEnemy(ChessBoard board, ChessPosition myPosition, ChessPosition nextPosition) {
        ChessPiece mypiece = board.getPiece(myPosition);
        if (isBound(nextPosition)) {
            boolean isEmpty = isEmpty(board, nextPosition);
            if (!isEmpty) {
                ChessPiece nextpiece = board.getPiece(nextPosition);
                ChessGame.TeamColor myPieceColor = mypiece.getTeamColor();
                ChessGame.TeamColor nextPieceColor = nextpiece.getTeamColor();
                //If is enemy color
                if (!(myPieceColor.equals(nextPieceColor))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEmpty(ChessBoard board, ChessPosition nextPosition) {
        //Find if there is already a piece on the board
        ChessPiece potentialpiece = board.getPiece(nextPosition);
        //if piece is not on board
        if (potentialpiece == null) {
            return true;
        }
        //if piece is on board
            return false;
    }

    public boolean isBound(ChessPosition nextPosition){
        int Row = nextPosition.getRow();
        int Col = nextPosition.getColumn();
        if (Row >= 1 && Row <= 8 && Col >= 1 && Col <= 8){
            return true;
        }
        return false;
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
    boolean hitPieceBR = false;
    boolean hitPieceBL = false;

    //While it hasn't hit bounds or the enemy
    while ((brRow < 8 && brCol > 1) && (!hitPieceBR)) {
        brRow = brRow + 1;
        brCol = brCol - 1;

        //is empty
        if (emptyposition(brRow, brCol, board, myPosition)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(brRow, brCol), null));
        }
        //is an enemy
        else if (isEnemy(brRow, brCol, board, myPosition)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(brRow, brCol), null));
            hitPieceBR = true;
        }
        //is a friend and not OG
        else if (!isEnemy(brRow, brCol, board, myPosition) && !(myPosition.equals(new ChessPosition(brRow,brCol)))) {
            hitPieceBR = true;
        }
        //is original
        else {
            continue;
        }

    }
    while (blRow < 8 && blCol < 8 && (!hitPieceBL)) {
        //bl
        //increment
        blRow = blRow + 1;
        blCol = blCol + 1;
        //is empty
        if (emptyposition(blRow, blCol, board, myPosition)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(blRow, blCol), null));
        }
        //is an enemy
        else if (isEnemy(blRow, blCol, board, myPosition)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(blRow, blCol), null));
            hitPieceBL = true;
        }
        //is a friend not OG
        else if (!isEnemy(blRow, blCol, board, myPosition) && !(myPosition.equals(new ChessPosition(blRow,blCol)))) {
            hitPieceBL = true;
        }
        //is original
        else {
            continue;
        }
    }
}




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