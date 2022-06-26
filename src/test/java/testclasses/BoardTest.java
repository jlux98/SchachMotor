package testclasses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.Board;
import model.Piece;

public class BoardTest {
    
    @Test
    public void copySpacesTest() {
        Piece[][] spaces = {
            {new Piece('p'),new Piece('B'),new Piece('r'),new Piece('n'),new Piece('Q'),new Piece('k'),new Piece('n'),new Piece('q')},
        {new Piece('Q'),new Piece('b'),new Piece('P'),new Piece('p'),new Piece('B'),new Piece('P'),new Piece('r'),new Piece('R')},
        {new Piece('b'),new Piece('B'),new Piece('r'),new Piece('R'),new Piece('n'),new Piece('N'),new Piece('p'),new Piece('P')},
        {new Piece('p'),new Piece('p'),new Piece('b'),new Piece('b'),null,new Piece('r'),new Piece('n'),new Piece('P')},
        {null, new Piece('q'),new Piece('K'),new Piece('b'),null,new Piece('P'),new Piece('N'),new Piece('p')},
        {new Piece('n'), null,null,new Piece('N'),null,new Piece('b'),new Piece('q'),null},
        {new Piece('r'), new Piece('N'),new Piece('P'),new Piece('P'),null,new Piece('R'),new Piece('r'),new Piece('Q')},
        {new Piece('N'), new Piece('R'),null,new Piece('b'),null,new Piece('p'),new Piece('r'),null},
    };
        Board board = new Board(false, false, spaces, false, false, false, false, false, 0, 0, 0, 1);
        Piece[][] copy = board.copySpaces();
        //verify that a new array was created
        //pieces need not be new instances
        assertTrue(spaces != copy);
        for (int i = 0; i < 8; i++) {
            assertTrue(spaces[i] != copy[i] );
            for (int j = 0; j < 8; j++) {
                assertEquals(spaces[i][j], copy[i][j]);
            }
        }


    }
}
