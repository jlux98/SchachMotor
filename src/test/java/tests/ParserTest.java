package tests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Move;
import uciservice.Command;
import uciservice.CommandType;
import uciservice.Tokenizer;
import uciservice.UCIParserAlphaBetaPruning;
import uciservice.UCITokenizer;

import static java.lang.Math.max;

public class ParserTest {
    
    private static Tokenizer tokenizer;

    @BeforeAll
    public static void initialize(){
        tokenizer = new UCITokenizer();
    }

    // @Test
    // public void tokenizerTestValidStrings(){
    //     Command expected1 = new Command(CommandType.GO, null);
    //     Command expected11 = new Command(CommandType.BINC, expected1);
    //     expected1.addChild(expected11);
    //     Command expected12 = new Command(CommandType.CONSTANT, "1000", expected11);
    //     expected11.addChild(expected12);
    //     Command actual1 = tokenizer.tokenize("go binc 1000");
    //     assertTrue(expected1.equals(actual1));
        
    //     // Command[] expected2 = {
    //     //     new Command(CommandType.POSITION, "startpos"),
    //     //     new Command(CommandType.MOVES, "e2e4 d7d5")
    //     // };
    //     // Command[] actual2 = tokenizer.tokenize("position startpos moves e2e4 d7d5");
    //     // for (int i = 0; i < max(expected2.length, actual2.length); i++){
    //     //     assertEquals(expected2[i], actual2[i]);
    //     // }
    // }

    @Test
    public void sameGameTestTrue(){
        Tokenizer tokenizer = new UCITokenizer();
        Command parent = tokenizer.tokenize("position startpos moves a2a4 a7a5 d2d4 d7d5");
        String startingPosition = "startpos";
        List<Move> oldTurn = new ArrayList<Move>();
        oldTurn.add(new Move("a2a4"));
        oldTurn.add(new Move("a7a5"));
        oldTurn.add(new Move("d2d4"));
        assertTrue(UCIParserAlphaBetaPruning.isTheSameGame(startingPosition, oldTurn, parent.getChildren()));
        String startingPosition2 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertTrue(UCIParserAlphaBetaPruning.isTheSameGame(startingPosition2, oldTurn, parent.getChildren()));
    }
    @Test
    public void sameGameTestFalse(){
        Tokenizer tokenizer = new UCITokenizer();
        Command parent1 = tokenizer.tokenize("position startpos moves a2a4 a7a5 d2d4 d7d5 e2e4");
        String startingPosition = "startpos";
        List<Move> oldTurn = new ArrayList<Move>();
        oldTurn.add(new Move("a2a4"));
        oldTurn.add(new Move("a7a5"));
        assertFalse(UCIParserAlphaBetaPruning.isTheSameGame(startingPosition, oldTurn, parent1.getChildren()));
        Command parent2 = tokenizer.tokenize("position startpos moves a2a1 a7a5 d2d4 d7d5");
        assertFalse(UCIParserAlphaBetaPruning.isTheSameGame(startingPosition, oldTurn, parent2.getChildren()));
        String startingPosition2 = "rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 0 1"; 
        Command parent3 = tokenizer.tokenize("position startpos moves a2a4 a7a5 d2d4 d7d5");
        assertFalse(UCIParserAlphaBetaPruning.isTheSameGame(startingPosition2, oldTurn, parent3.getChildren()));
    }
}
