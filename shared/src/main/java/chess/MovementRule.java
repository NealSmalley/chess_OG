package chess;

public interface MovementRule {
    java.util.Collection<ChessMove> getMoves(ChessBoard board, ChessPosition position);
}