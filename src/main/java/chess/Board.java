package chess;

import chess.calculator.OrderBy;
import chess.pieces.Color;
import chess.pieces.Piece;
import chess.pieces.Representation;
import chess.calculator.ScoreUtils;

import chess.pieces.Representation.Type;
import java.util.ArrayList;
import java.util.List;

import static utils.RankMaker.*;
import static utils.StringUtils.*;

public class Board {
    List<Rank> ranks = new ArrayList<>();

    public void initialize() {
        ranks.add(getGoodPiecesRank(Color.BLACK));
        ranks.add(getPawnsRank(Color.BLACK));
        ranks.add(getEmptyRank());
        ranks.add(getEmptyRank());
        ranks.add(getEmptyRank());
        ranks.add(getEmptyRank());
        ranks.add(getPawnsRank(Color.WHITE));
        ranks.add(getGoodPiecesRank(Color.WHITE));
    }

    public void initializeEmpty() {
        for (int i = 0; i < 8; i++) {
            ranks.add(getEmptyRank());
        }
    }

    public double getScore(Color color) {
        return ScoreUtils.calc(ranks, color);
    }

    public List<Piece> sortByScore(Color color, OrderBy orderBy) {
        return ScoreUtils.sort(ranks, color, orderBy);
    }

    public void move(String sourcePosition, String targetPosition) {
        Position source = Position.from(sourcePosition);
        Position target = Position.from(targetPosition);

        final Piece sourcePiece = findPiece(source);

        if (sourcePiece.isPieceOf(Representation.BLANK)) {
            throw new IllegalArgumentException("빈칸을 움직일 수 없습니다");
        }
        setPiece(source, Piece.create(Type.NO_PIECE, Color.NOCOLOR));
        setPiece(target, sourcePiece);
    }

    public void setPiece(Position position, Piece piece) {
        ranks.get(position.getRank())
                .set(position.getFile(), piece);
    }

    public void setPiece(String position, Piece piece) {
        Position p = Position.from(position);
        setPiece(p, piece);
    }

    public String print() {
        StringBuilder sb = new StringBuilder();

        for (Rank rank : ranks) {
            sb.append(appendNewLine(rank.toString()));
        }

        return sb.toString();
    }

    public long pieceCount() {
        return ranks.stream()
                .mapToLong(Rank::count)
                .sum();
    }

    public long pieceCount(Representation representation) {
        return ranks.stream()
                .mapToLong(rank -> rank.count(representation))
                .sum();
    }

    public Piece findPiece(Position position) {
        Rank rank = ranks.get(position.getRank());
        return rank.get(position.getFile());
    }

    public Piece findPiece(String rankFile) {
        Position position = Position.from(rankFile);
        return findPiece(position);
    }
}
