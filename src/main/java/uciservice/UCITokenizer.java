package uciservice;

import java.util.ArrayList;
import java.util.List;

public class UCITokenizer implements Tokenizer {

    @Override
    public Command[] tokenize(String input) {
        // remove leading and trailing spaces, make multiple spaces into singles 
        input = input.trim().replaceAll(" +", " ");
        // split the string into its word
        String[] splitInput = input.split(" ");                                        
        List<Command> result = new ArrayList<>();
        for (int i = 0; i+1<splitInput.length; i++) {
            String currentWord = splitInput[i];
            String nextWord = splitInput[i+1];
            switch (currentWord){
                case "binc":
                    result.add(new Command(CommandType.BINC, nextWord));
                    i++;
                break;
                
                case "btime":
                    result.add(new Command(CommandType.BTIME, nextWord));
                    i++;
                break;
                
                case "depth":
                    result.add(new Command(CommandType.DEPTH, nextWord));
                    i++;
                break;

                case "go":
                    result.add(new Command(CommandType.GO));
                break;
                    
                case "infinite":
                    result.add(new Command(CommandType.INFINITE));
                break;

                case "isready":
                    result.add(new Command(CommandType.BTIME));
                break;

                case "mate":
                    result.add(new Command(CommandType.MATE, nextWord));
                    i++;
                break;

                case "moves":
                    result.add(new Command(
                        CommandType.MOVES, glueTogetherFromIndex(i, splitInput)));
                    i++;
                break;

                case "movestogo":
                    result.add(new Command(CommandType.MOVESTOGO, nextWord));
                    i++;
                break;

                case "movetime":
                    result.add(new Command(CommandType.MOVETIME, nextWord));
                    i++;
                break;

                case "name":
                    result.add(new Command(CommandType.NAME, nextWord));
                    i++;
                break;

                case "nodes":
                    result.add(new Command(CommandType.NODES, nextWord));
                    i++;
                break;

                case "ponder":
                    result.add(new Command(CommandType.PONDER));
                break;

                case "ponderhit":
                    result.add(new Command(CommandType.PONDERHIT));
                break;

                case "position":
                    result.add(new Command(CommandType.POSITION, nextWord));
                    i++;
                break;

                case "quit":
                    result.add(new Command(CommandType.QUIT));
                break;

                // case "setoption":
                //     result.add(new Command(CommandType.SETOPTION, nextWord));
                //     i++;
                //     break;

                case "stop":
                    result.add(new Command(CommandType.STOP));
                break;

                case "uci":
                    result.add(new Command(CommandType.UCI, nextWord));
                    i++;
                break;

                case "ucinewgame":
                    result.add(new Command(CommandType.UCINEWGAME));
                break;

                // case "value":
                //     result.add(new Command(CommandType.VALUE, nextWord));
                //     i++;
                // break;

                case "winc":
                    result.add(new Command(CommandType.WINC, nextWord));
                    i++;
                break;

                case "wtime":
                    result.add(new Command(CommandType.WTIME, nextWord));
                    i++;
                break;

                default:
                break;
            }
        }
        return result.toArray(new Command[0]);
    }

    
    private String glueTogetherFromIndex(int i, String[] splitInput) {
        String result = "";
        i++;
        while (i < splitInput.length){
            result = result + splitInput[i] + " ";
            i++;
        }
        return result.trim();
    }
}
