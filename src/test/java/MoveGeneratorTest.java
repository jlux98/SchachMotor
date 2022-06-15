import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
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
}
