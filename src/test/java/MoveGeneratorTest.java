import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
import movegenerator.MoveGenerator;
import uciservice.FenParser;

public class MoveGeneratorTest {
    
    Board startingPosition;

    @BeforeEach
    public void initialize(){
        String startingFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR " +
            "w KQkq - 0 1";
        FenParser startingFenParser = new FenParser(startingFen);
        startingPosition = startingFenParser.parseFen();
    }

    @Test
    public void parserTest(){
        String startingString = startingPosition.toString();
        System.out.println(startingPosition);
    }

    /**
     * Compares the positions denoted as fen strings against the list of boards.
     * @param fenStrings
     * @param boards
     * @return true if and only if 
     */
    private void compareFenStringsToBoard(List<String> fenStrings, List<Board> boards) {
        List<Board> fenStringBoards = new ArrayList<Board>(fenStrings.size());
        for (String fen : fenStrings) {
            fenStringBoards.add(FenParser.parseFen(fen));
            
        }
        Collections.sort(fenStringBoards);
        Collections.sort(boards);
        assertEquals(fenStringBoards, boards);
    }

    @Test
    public void moveBishopTest() {
        Board bishopTestBoard = FenParser.parseFen("7K/8/1n6/4P3/3b4/8/8/7k b - - 0 1"); //bishop starts at d4
        System.out.println(bishopTestBoard);
        Set<Board> followUpBoards = MoveGenerator.computeBishopMoves(bishopTestBoard,4,3);
        List<String> expectedfollowUpBoards = new ArrayList<String>(followUpBoards.size());
        expectedfollowUpBoards.add("7K/8/1n6/2b1P3/8/8/8/7k w - - 0 1"); //move one square to upper left
        expectedfollowUpBoards.add("7K/8/1n6/4b3/8/8/8/7k w - - 0 1"); //capture pawn to upper right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/2b5/8/7k w - - 0 1"); //move to c3 - one square to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/1b6/7k w - - 0 1"); //move to b2 - two squares to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/8/b6k w - - 0 1");  //move to a1 - three squares to bottom left
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/4b3/8/7k w - - 0 1"); //move to e3 - one square to bottom right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/5b2/7k w - - 0 1"); //move to f2 - two squares to bottom right
        expectedfollowUpBoards.add("7K/8/1n6/4P3/8/8/8/6bk w - - 0 1");  //move to g1 - three squares to bottom right


        compareFenStringsToBoard(expectedfollowUpBoards, new ArrayList<Board>(followUpBoards));
    }

    @Test
    public void moveRookTest() {
        
    }

    @Test
    public void moveQueenTest() {
        
    }
}
