package application;


import model.Position;
import positionevaluator.PositionEvaluator;
import uciservice.FenParser;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String blackEndingFen = "r1bqkbnr/ppppppBp/2n5/8/8/1P6/P1PPPPPP/RN1QKBNR b KQk - 0 4";
        String whiteEndingFen = "2bqkbnr/rppppppp/p1n5/P7/8/8/1PPPPPPP/RNBQKBNR w KQk - 1 4";

        Position blackPosition = FenParser.parseFen(blackEndingFen);
        Position whitePosition = FenParser.parseFen(whiteEndingFen);

        int blackValue = PositionEvaluator.evaluatePosition(blackPosition, false, 0);
        int whiteValue = PositionEvaluator.evaluatePosition(whitePosition, false, 0);

        System.out.println("black value: " + blackValue);
        System.out.println("white value: " + whiteValue);

        System.out.println("black > 0 (losing): " + (blackValue > 0));
        System.out.println("white = 0 (neutral): " + (whiteValue == 0));
    }
}
