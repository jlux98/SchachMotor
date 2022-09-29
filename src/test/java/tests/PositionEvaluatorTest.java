package tests;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import positionevaluator.PositionEvaluator;
import uciservice.FenParser;

public class PositionEvaluatorTest {

    // FIXME: Recalculate Values

//     @Test
//     public void equalBoardTest(){
//         assertEquals(0, PositionEvaluator.evaluatePosition(
//             FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
//             "w KQkq - 0 1")));
//     }
    
//     /*
//          * Material value of the board is -4150.
//          * The pair of black Bishops subtracts 150 points.
//          * The positioning of the black Rooks has no influence.
//          * The positioning of the black Knights adds 40 points each.
//          * The positioning of the black Bishops adds 10 points each.
//          * The positioning of the black Queen adds 5 points
//          * The positioning of the black Pawns subtracts 5+10+10-20-20+10+10+5 = 10 points
//          * The positioning of the Kings has no influence.
//          * Thus the total comes to -4205.
//         */
//     @Test
//     public void onlyWhiteKingVsFullBlackStartingBoardTest(){
//         assertEquals(-4205,PositionEvaluator.evaluatePosition(FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/7K b kq - 1 1")));
//     }

//     /*
//          * Material value of the board is 4150.
//          * The pair of white Bishops adds 150 points.
//          * The positioning of the white Rooks has no influence.
//          * The positioning of the white Knights subtracts 40 points each.
//          * The positioning of the white Bishops subtracts 10 points each.
//          * The positioning of the white Queen subtracts 5 points
//          * The positioning of the white Pawns adds 5+10+10-20-20+10+10+5 = 10 points
//          * The positioning of the Kings has no influence.
//          * Thus the total comes to 4205.
//         */
//     @Test
//     public void onlyBlackKingVsFullWhiteStartingBoardTest(){
//         assertEquals(4205,PositionEvaluator.evaluatePosition(
//             FenParser.parseFen("k7/8/8/8/8/8/PPPPPPPP/RNBQKBNR b kq - 1 1")));
//     }

//     /*
//          * Material value of the board is -3775.
//          * The positioning of the black Rooks has no influence.
//          * The positioning of the black Knights adds 40 points each.
//          * The positioning of the black Bishop adds 10 points.
//          * The positioning of the black Queen adds 5 points
//          * The positioning of the black Pawns subtracts 5+10+10-20-20+10+10+5 = 10 points
//          * The positioning of the Kings has no influence.
//          * Thus the total comes to -3690.
//         */
//     @Test
//     public void onlyWhiteKingVsBlackStartingBoardWithoutBishopTest(){
//         assertEquals(-3690,PositionEvaluator.evaluatePosition(
//             FenParser.parseFen("rnbqk1nr/pppppppp/8/8/8/8/8/7K b kq - 1 1")));
//     }

// /*
//          * Material value of the board is 3775.
//          * The positioning of the white Rooks has no influence.
//          * The positioning of the white Knights subtracts 40 points each.
//          * The positioning of the white Bishop subtracts 10 points.
//          * The positioning of the white Queen subtracts 5 points
//          * The positioning of the white Pawns adds 5+10+10-20-20+10+10+5 = 10 points
//          * The positioning of the Kings has no influence.
//          * Thus the total comes to 3690.
//         */
//     @Test
//     public void onlyWhiteKingVsFullBlackStartingBoardWithoutBishopTest(){
//         assertEquals(3690,PositionEvaluator.evaluatePosition(
//             FenParser.parseFen("k7/8/8/8/8/8/PPPPPPPP/RN1QKBNR b kq - 1 1")));
//     }
}
