package data;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorData {
    /**
     * used to test {@link MoveGenerator#generatePossibleMoves(Position)}
     * and {@link GameNode#queryChildren()}
     */
    public static String allBlackPiecesFen = "8/6K1/2Rp4/3np3/2r2p2/5qp1/1kb4p/8 b - - 0 1";

 
    /**
     * @return the follow-up moves to {@link #allBlackPiecesFen}
     */
    public static List<String> getExpectedAllBlackPiecesFenFollowUpMoves() {

        List<String> expectedFollowUpPositions = new ArrayList<String>(50);

        //king moves
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/k4qp1/2b4p/8 w - - 1 2"); //move to a3 - one square to upper left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/1k3qp1/2b4p/8 w - - 1 2"); //move to b3 - one square up
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/2k2qp1/2b4p/8 w - - 1 2"); //move to c3 - one square to upper right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/k1b4p/8 w - - 1 2"); //move to a2 - one square left
        //king can't move to the right (blocked by bishop)
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/2b4p/k7 w - - 1 2"); //move to a1 - one square to bottom left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/2b4p/1k6 w - - 1 2"); //move to b1 - one square down
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/2b4p/2k5 w - - 1 2"); //move to c1 - one square to bottom right

        //rook moves
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/1r3p2/5qp1/1kb4p/8 w - - 1 2"); //move to b4 - one square left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/r4p2/5qp1/1kb4p/8 w - - 1 2"); //move to a4 - two squares left
        expectedFollowUpPositions.add("8/6K1/2Rp4/2rnp3/5p2/5qp1/1kb4p/8 w - - 1 2"); //move to c5 - one square up
        expectedFollowUpPositions.add("8/6K1/2rp4/3np3/5p2/5qp1/1kb4p/8 w - - 0 2"); //capture white rook on c6 - two squares up
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/3r1p2/5qp1/1kb4p/8 w - - 1 2"); //move to d4 - one square right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/4rp2/5qp1/1kb4p/8 w - - 1 2"); //move to e4 - two squares right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/5p2/2r2qp1/1kb4p/8 w - - 1 2"); //move to c3 - one square down

        //bishop moves
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/1b3qp1/1k5p/8 w - - 1 2"); //move to b3 - one square to upper left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/b1r2p2/5qp1/1k5p/8 w - - 1 2"); //move to a4 - two squares to upper left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/3b1qp1/1k5p/8 w - - 1 2"); //move to d3 - one square to upper right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r1bp2/5qp1/1k5p/8 w - - 1 2"); //move to e4 - two square to upper right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3npb2/2r2p2/5qp1/1k5p/8 w - - 1 2"); //move to f5 - three squares to upper right
        expectedFollowUpPositions.add("8/6K1/2Rp2b1/3np3/2r2p2/5qp1/1k5p/8 w - - 1 2"); //move to g6 - four squares to upper right
        expectedFollowUpPositions.add("8/6Kb/2Rp4/3np3/2r2p2/5qp1/1k5p/8 w - - 1 2"); // move to h7 - five squares to upper right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/1k5p/1b6 w - - 1 2"); //move to b1 - one square to bottom left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/1k5p/3b4 w - - 1 2"); //move to d1 - one square to bottom right

        //knight moves
        expectedFollowUpPositions.add("8/6K1/1nRp4/4p3/2r2p2/5qp1/1kb4p/8 w - - 1 2"); //move to b6 - left, then up
        expectedFollowUpPositions.add("8/2n3K1/2Rp4/4p3/2r2p2/5qp1/1kb4p/8 w - - 1 2"); //move to c7 - up, then left
        expectedFollowUpPositions.add("8/4n1K1/2Rp4/4p3/2r2p2/5qp1/1kb4p/8 w - - 1 2"); //move to e7 - up, then right
        expectedFollowUpPositions.add("8/6K1/2Rp1n2/4p3/2r2p2/5qp1/1kb4p/8 w - - 1 2"); //move to f6 - right, then up
        //knight cannot move to f4 - right, then down (blocked by own black pawn)
        expectedFollowUpPositions.add("8/6K1/2Rp4/4p3/2r2p2/4nqp1/1kb4p/8 w - - 1 2"); //move to e3 - down, then right
        expectedFollowUpPositions.add("8/6K1/2Rp4/4p3/2r2p2/2n2qp1/1kb4p/8 w - - 1 2"); //move to c3 - down, then left
        expectedFollowUpPositions.add("8/6K1/2Rp4/4p3/1nr2p2/5qp1/1kb4p/8 w - - 1 2"); //move to b4 - left, then down

        //queen moves
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/4q1p1/1kb4p/8 w - - 1 2"); //move to e3 - one square left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/3q2p1/1kb4p/8 w - - 1 2"); //move to d3 - two squares left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/2q3p1/1kb4p/8 w - - 1 2"); //move to c3 - three squares left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/1q4p1/1kb4p/8 w - - 1 2"); //move to b3 - four squares left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/q5p1/1kb4p/8 w - - 1 2"); //move to a3 - five squares left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r1qp2/6p1/1kb4p/8 w - - 1 2"); //move to d5 - one square to upper left
        //queen can't move up (blocked by own black pawn)
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2pq1/6p1/1kb4p/8 w - - 1 2"); //move to g4 - one square to upper right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np2q/2r2p2/6p1/1kb4p/8 w - - 1 2"); //move to h5 - two squares to upper right
        //queen can't move right (blocked by own black pawn)
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/6p1/1kb3qp/8 w - - 1 2"); //move to g2 - one square to bottom right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/6p1/1kb4p/7q w - - 1 2"); //move to  h1 - two squares to bottom right
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/6p1/1kb2q1p/8 w - - 1 2"); //move to f2 - one square down
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/6p1/1kb4p/5q2 w - - 1 2"); //move to f1 - two squares down
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/6p1/1kb1q2p/8 w - - 1 2"); //move to e2 - one square to bottom left
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/6p1/1kb4p/3q4 w - - 1 2"); //move to d1 - two squares to bottom left

        //pawn moves
        //pawn on d6 is blocked by own black knight 
        expectedFollowUpPositions.add("8/6K1/2Rp4/3n4/2r1pp2/5qp1/1kb4p/8 w - - 0 2"); //move pawn on e5 to e4
        //pawn on f4 is blocked by own black queen 
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5q2/1kb3pp/8 w - - 0 2"); //move pawn on g3 to g2 
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/1kb5/7q w - - 0 2"); //promote pawn on h2 to queen
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/1kb5/7r w - - 0 2"); //promote pawn on h2 to rook
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/1kb5/7b w - - 0 2"); //promote pawn on h2 to bishop
        expectedFollowUpPositions.add("8/6K1/2Rp4/3np3/2r2p2/5qp1/1kb5/7n w - - 0 2"); //promote pawn on h2 to knight

        return expectedFollowUpPositions;
    }
}
