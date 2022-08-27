import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import uciservice.Command;
import uciservice.CommandType;
import uciservice.Tokenizer;
import uciservice.UCITokenizer;

import static java.lang.Math.max;

public class ParserTest {
    
    private static Tokenizer tokenizer;

    @BeforeAll
    public static void initialize(){
        tokenizer = new UCITokenizer();
    }

    @Test
    public void tokenizerTestValidStrings(){
        Command expected1 = new Command(CommandType.GO, null);
        Command expected11 = new Command(CommandType.BINC, expected1);
        expected1.addChild(expected11);
        Command expected12 = new Command(CommandType.CONSTANT, "1000", expected11);
        expected11.addChild(expected12);
        Command actual1 = tokenizer.tokenize("go binc 1000");
        assertTrue(expected1.equals(actual1));
        
        // Command[] expected2 = {
        //     new Command(CommandType.POSITION, "startpos"),
        //     new Command(CommandType.MOVES, "e2e4 d7d5")
        // };
        // Command[] actual2 = tokenizer.tokenize("position startpos moves e2e4 d7d5");
        // for (int i = 0; i < max(expected2.length, actual2.length); i++){
        //     assertEquals(expected2[i], actual2[i]);
        // }
    }
}
