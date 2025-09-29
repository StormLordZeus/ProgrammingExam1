package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor m_teamTurn;
    private ChessBoard m_board;

    public ChessGame() {
        m_teamTurn = TeamColor.WHITE;
        m_board = new ChessBoard();
        m_board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return m_teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        m_teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        ChessPiece myPiece = m_board.getPiece(startPosition);
        if (myPiece == null) {
            return null;
        }
        Collection<ChessMove> myMoves = myPiece.pieceMoves(m_board, startPosition);
        if (isInCheck(m_teamTurn))
        {
            for (ChessMove move : myMoves)
            {
                ChessPosition endPos = move.getEndPosition();
                ChessPiece enemyPiece = m_board.getPiece(endPos);
                m_board.addPiece(endPos, myPiece);
                if(isInCheck(m_teamTurn))
                {
                    myMoves.remove(move);
                }
                m_board.addPiece(endPos, enemyPiece);
                m_board.addPiece(move.getStartPosition(), myPiece);
            }
        }
        return myMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        ChessPosition start_pos = move.getStartPosition();
        ChessPiece myPiece = m_board.getPiece(start_pos);
        if (myPiece != null) {
            TeamColor myColor = myPiece.getTeamColor();
            if (this.validMoves(start_pos).contains(move) && myColor == m_teamTurn) {
                if (move.getPromotionPiece() != null)
                {
                    m_board.addPiece(move.getEndPosition(), new ChessPiece(myColor, move.getPromotionPiece()));
                }
                else
                {
                    m_board.addPiece(move.getEndPosition(), myPiece);
                }
                m_board.addPiece(start_pos, null);
                if (m_teamTurn == TeamColor.WHITE)
                {
                    m_teamTurn = TeamColor.BLACK;
                }
                else
                {
                    m_teamTurn = TeamColor.WHITE;
                }
            } else {
                throw new InvalidMoveException();
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        ChessPosition kingPos = null;
        for (int y = 1; y < 9; y ++) {
            for (int x = 1; x < 9; x++)
            {
                ChessPiece myPiece = m_board.getPiece(new ChessPosition(y,x));
                if (myPiece == null)
                {
                    continue;
                }
                if (myPiece.getPieceType() == ChessPiece.PieceType.KING &&
                    myPiece.getTeamColor() == teamColor)
                {
                    kingPos = new ChessPosition(y,x);
                    break;
                }
            }
            if (kingPos != null)
            {
                break;
            }
        }

        for (int y = 1; y < 9; y ++) {
            for (int x = 1; x < 9; x++)
            {
                ChessPiece myPiece = m_board.getPiece(new ChessPosition(y,x));
                if (myPiece != null && myPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> pieceMoves = myPiece.pieceMoves(m_board, new ChessPosition(y, x));
                    if (pieceMoves.contains(new ChessMove(new ChessPosition(y, x), kingPos, null))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor)
    {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        m_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        return m_board;
    }
}
