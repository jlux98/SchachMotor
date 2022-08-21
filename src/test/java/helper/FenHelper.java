package helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import model.Position;
import uciservice.FenParser;

public class FenHelper {

    /**
     * Compares the positions denoted as fen strings against the list of positions.
     */
    public static void compareFenStringsToPosition(Collection<String> fenStrings, Collection<Position> positions) {
        //assure that the caluclated positions are stored in a sortable data structure
        List<Position> calculatedPositions = new ArrayList<Position>(positions);
        //translate the fen strings into boards
        List<Position> fenStringPositions = new ArrayList<Position>(fenStrings.size());
        for (String fen : fenStrings) {
            fenStringPositions.add(FenParser.parseFen(fen));

        }
        //sort expected and calculated boards
        Collections.sort(fenStringPositions);
        Collections.sort(calculatedPositions);
        //compare both lists
        assertEquals(fenStringPositions, calculatedPositions);
    }
}
