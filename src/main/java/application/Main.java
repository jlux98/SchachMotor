package application;


import model.Position;
import movegenerator.MoveGenerator;
import positionevaluator.PositionEvaluator;
import uciservice.FenParser;

public class Main {
    /* public static void main(String[] args) throws InterruptedException {
        String blackEndingFen = "r1bqkbnr/ppppppBp/2n5/8/8/1P6/P1PPPPPP/RN1QKBNR b KQk - 0 4";
        String whiteEndingFen = "2bqkbnr/rppppppp/p1n5/P7/8/8/1PPPPPPP/RNBQKBNR w KQk - 1 4";

        Position blackPosition = FenParser.parseFen(blackEndingFen);
        Position whitePosition = FenParser.parseFen(whiteEndingFen);

        int blackValue = PositionEvaluator.evaluatePosition(blackPosition);
        int whiteValue = PositionEvaluator.evaluatePosition(whitePosition);

        System.out.println("black value: " + blackValue);
        System.out.println("white value: " + whiteValue);

        System.out.println("black > 0 (losing): " + (blackValue > 0));
        System.out.println("white = 0 (neutral): " + (whiteValue == 0));
    } */

    public static void main(String[] args) {
        Position position = FenParser.parseFen("8/8/R2k2k1/8/8/8/8/K7 b - - 0 1");
        Position[] followups = MoveGenerator.generatePossibleMoves(position);
        for (Position followUp : followups) {
            System.out.println(followUp);
        }
    }
}
