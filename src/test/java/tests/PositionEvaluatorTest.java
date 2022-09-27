package tests;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import positionevaluator.PositionEvaluator;
import uciservice.FenParser;

public class PositionEvaluatorTest {
    // @Test
    // public void positionEvaluatorTest(){
    //     assertEquals(0, PositionEvaluator.evaluatePosition(
    //         FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
    //         "w KQkq - 0 1"),false, 1));
    //     assertEquals(-4300,PositionEvaluator.evaluatePosition(
    //         FenParser.parseFen("rnbqkbnr/pppppppp/8/8/8/8/8/7K b kq - 1 1"),false, 1));
    //     assertEquals(4300,PositionEvaluator.evaluatePosition(
    //         FenParser.parseFen("k7/8/8/8/8/8/PPPPPPPP/RNBQKBNR b kq - 1 1"),false, 1));
    //     assertEquals(-3775,PositionEvaluator.evaluatePosition(
    //         FenParser.parseFen("rnbqk1nr/pppppppp/8/8/8/8/8/7K b kq - 1 1"),false, 1));
    //     assertEquals(3775,PositionEvaluator.evaluatePosition(
    //         FenParser.parseFen("k7/8/8/8/8/8/PPPPPPPP/RN1QKBNR b kq - 1 1"),false, 1));
    // }
}
