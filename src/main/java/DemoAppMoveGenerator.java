import model.Board;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class DemoAppMoveGenerator {
    public static void main(String[] args) {
        if (args.length != 1){
            throw new IllegalArgumentException("Please use this program with" +
            "one argument.");
        }
        Board inputBoard = FenParser.parseFen(args[0]);
        System.out.println("Input Board:\n" + inputBoard);
        Board[] followUpBoards = MoveGenerator.generatePossibleMoves(inputBoard);
        System.out.println("\nFollowUp-Boards:\n");
        for (int i = 0; i < followUpBoards.length; i++){
            System.out.println(followUpBoards[i] + "\n");
        }
    }
}