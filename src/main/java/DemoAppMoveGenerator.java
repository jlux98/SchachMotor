import model.Position;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class DemoAppMoveGenerator {
    public static void main(String[] args) {
        if (args.length != 1){
            throw new IllegalArgumentException("Please use this program with" +
            "one argument.");
        }
        Position inputPosition = FenParser.parseFen(args[0]);
        System.out.println("Input Position:\n" + inputPosition);
        Position[] followUpPositions = MoveGenerator.generatePossibleMoves(inputPosition);
        System.out.println("\nFollowUp-Positions:\n");
        for (int i = 0; i < followUpPositions.length; i++){
            System.out.println(followUpPositions[i] + "\n");
        }
    }
}