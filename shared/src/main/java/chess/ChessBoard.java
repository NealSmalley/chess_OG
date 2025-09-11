package chess;

//This is to import the PieceType enum
import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;
    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //create an empty board
        board = new ChessPiece[8][8];
        //creates a new chessboard
        //ChessBoard board = new ChessBoard();
        //for each piece enum
        //ChessPiece.PieceType King = ChessPiece.PieceType.KING;
        var King = ChessPiece.PieceType.KING;
        var Queen = ChessPiece.PieceType.QUEEN;
        var Bishop = ChessPiece.PieceType.BISHOP;
        var Knight = ChessPiece.PieceType.KNIGHT;
        var Rook = ChessPiece.PieceType.ROOK;
        var Pawn = ChessPiece.PieceType.PAWN;

        //map each piece to og position
        //list for black and white
        String[] colors = {"white", "black"};
        // for loop for both colors, black and white
        for (int i = 0; i < colors.length; i++) {
            String color;
            if (i == 0) {
                color = "white";
            }
            else{
                color = "black";
            }
            //Pawns for the first row
            Pawns(Pawn, color);
            // Rook 2
            Rooks(Rook,color);
            // Knight 2
            Knight(Knight,color);
            // Bishop 2
            Bishop(Bishop,color);
            // Queen 1
            Queen(Queen,color);
            // King 1
            King(King,color);
        }
        //return new boards
    }
    public void Pawns(ChessPiece.PieceType Pawn, String color) {
        for (int i = 1; i <= 8; i++) {
            ChessPosition OgPosition;
            ChessPiece PawnwithColor;
            if (Objects.equals(color, "white")) {
                OgPosition = new ChessPosition(2, i);
                PawnwithColor = new ChessPiece(ChessGame.TeamColor.WHITE, Pawn);
            }
            else {
                OgPosition = new ChessPosition(7, i);
                PawnwithColor = new ChessPiece(ChessGame.TeamColor.BLACK, Pawn);
            }
            addPiece(OgPosition, PawnwithColor);
        }
    }
    public void Rooks(ChessPiece.PieceType Rook, String color) {
            ChessPosition firstOgPosition;
            ChessPosition secondOgPosition;
            ChessPiece RookwithColor;
            if (Objects.equals(color, "white")) {
                RookwithColor = new ChessPiece(ChessGame.TeamColor.WHITE, Rook);
                firstOgPosition = new ChessPosition(1, 1);
                secondOgPosition = new ChessPosition(1, 8);
            }
            else {
                RookwithColor = new ChessPiece(ChessGame.TeamColor.BLACK, Rook);
                firstOgPosition = new ChessPosition(8, 1);
                secondOgPosition = new ChessPosition(8, 8);
            }
            addPiece(firstOgPosition, RookwithColor);
            addPiece(secondOgPosition, RookwithColor);
    }
    public void Knight(ChessPiece.PieceType Knight, String color) {
        ChessPosition firstOgPosition;
        ChessPosition secondOgPosition;
        ChessPiece KnightwithColor;
        if (Objects.equals(color, "white")) {
            KnightwithColor = new ChessPiece(ChessGame.TeamColor.WHITE, Knight);
            firstOgPosition = new ChessPosition(1, 2);
            secondOgPosition = new ChessPosition(1, 7);
        }
        else {
            KnightwithColor = new ChessPiece(ChessGame.TeamColor.BLACK, Knight);
            firstOgPosition = new ChessPosition(8, 2);
            secondOgPosition = new ChessPosition(8, 7);
        }
        addPiece(firstOgPosition, KnightwithColor);
        addPiece(secondOgPosition, KnightwithColor);
    }
    public void Bishop(ChessPiece.PieceType Bishop, String color) {
        ChessPosition firstOgPosition;
        ChessPosition secondOgPosition;
        ChessPiece bishopwithColor;
        if (Objects.equals(color, "white")) {
            bishopwithColor = new ChessPiece(ChessGame.TeamColor.WHITE, Bishop);
            firstOgPosition = new ChessPosition(1, 3);
            secondOgPosition = new ChessPosition(1, 6);
        }
        else {
            bishopwithColor = new ChessPiece(ChessGame.TeamColor.BLACK, Bishop);
            firstOgPosition = new ChessPosition(8, 3);
            secondOgPosition = new ChessPosition(8, 6);
        }
        addPiece(firstOgPosition, bishopwithColor);
        addPiece(secondOgPosition, bishopwithColor);
    }
    public void Queen(ChessPiece.PieceType Queen, String color) {
        ChessPosition OgPosition;
        ChessPiece QueenwithColor;
        if (Objects.equals(color, "white")) {
            QueenwithColor = new ChessPiece(ChessGame.TeamColor.WHITE, Queen);
            OgPosition = new ChessPosition(1, 4);
        }
        else {
            QueenwithColor = new ChessPiece(ChessGame.TeamColor.BLACK, Queen);
            OgPosition = new ChessPosition(8, 4);
        }
        addPiece(OgPosition, QueenwithColor);
    }
    public void King(ChessPiece.PieceType King, String color) {
        ChessPosition OgPosition;
        ChessPiece KingwithColor;
        if (Objects.equals(color, "white")) {
            KingwithColor = new ChessPiece(ChessGame.TeamColor.WHITE, King);
            OgPosition = new ChessPosition(1, 5);
        }
        else {
            KingwithColor = new ChessPiece(ChessGame.TeamColor.BLACK, King);
            OgPosition = new ChessPosition(8, 5);
        }
        addPiece(OgPosition, KingwithColor);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
