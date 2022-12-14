package uciservice;

import java.util.LinkedList;
import java.util.List;

public class UCITokenizer implements Tokenizer {

    @Override
    public Command tokenize(String input) {
        // remove leading and trailing squares, make multiple squares into singles 
        input = input.trim().replaceAll(" +", " ");
        // split the string into its word
        String[] splitInput = input.split(" ");
        int i = 0;
        Command result = tokenizeWord(splitInput, i, null);
        while (result != null && result.getType() == CommandType.CONSTANT){
            i++;
            result = tokenizeWord(splitInput, i, null);
        }
        return result;
    }

    private Command tokenizeWord(String[] sentence, int i, Command parent){
        Command result = null;
        // if (sentence.length == 1){
        //     return null;
        // }
        String word = sentence[i];
        switch (word){
            case "binc":
                result = new Command(CommandType.BINC, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;
            
            case "btime":
                result = new Command(CommandType.BTIME, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;
            
            case "code":
                result = new Command(CommandType.CODE, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "debug":
                result = new Command(CommandType.DEBUG, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "depth":
                result = new Command(CommandType.DEPTH, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "fen":
                result = new Command(CommandType.FEN, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "go":
                result = new Command(CommandType.GO, parent);
                result.addAll(grabChildren(sentence, i+1, parent));
            break;
                
            case "infinite":
                result = new Command(CommandType.INFINITE, parent);
            break;

            case "isready":
                result = new Command(CommandType.ISREADY, parent);
            break;

            case "later":
                result = new Command(CommandType.LATER, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "mate":
                result = new Command(CommandType.MATE, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "moves":
                result = new Command(CommandType.MOVES, parent);
                // result.addAll(grabChildren(sentence, i+1, parent));
            break;

            case "movestogo":
                result = new Command(CommandType.MOVESTOGO, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "movetime":
                result = new Command(CommandType.MOVETIME, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "name":
                result = new Command(CommandType.NAME, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "nodes":
                result = new Command(CommandType.NODES, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "ponder":
                result = new Command(CommandType.PONDER, parent);
            break;

            case "ponderhit":
                result = new Command(CommandType.PONDERHIT, parent);
            break;

            case "position":
                result = new Command(CommandType.POSITION, parent);
                result.addAll(grabChildren(sentence, i+1, result));
            break;

            case "quit":
                result = new Command(CommandType.QUIT, parent);
            break;

            case "register":            
                result = new Command(CommandType.REGISTER, parent);
                result.addAll(grabChildren(sentence, i+1, parent));
            break;
            
            case "searchmoves":
                result = new Command(CommandType.SEARCHMOVES, parent);
                result.addAll(grabChildren(sentence, i+1, parent));
            break;

            case "setoption":
                result = new Command(CommandType.SETOPTION, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "startpos":
                result = new Command(CommandType.STARTPOS, parent);
            break;

            case "stop":
                result = new Command(CommandType.STOP, parent);
            break;

            case "uci":
                result = new Command(CommandType.UCI, parent);
            break;

            case "ucinewgame":
                result = new Command(CommandType.UCINEWGAME, parent);
            break;

            case "value":
                result = new Command(CommandType.VALUE, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "winc":
                result = new Command(CommandType.WINC, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            case "wtime":
                result = new Command(CommandType.WTIME, parent);
                result.addChild(tokenizeWord(sentence, i+1, result));
            break;

            default:
                result = new Command(CommandType.CONSTANT, word, parent);
            break;
        }
        return result;
    }

    private List<Command> grabChildren(String[] sentence, int substring, Command parent){
        List<Command> results = new LinkedList<Command>();
        for (int i = substring; i < sentence.length; i++){
            Command tempResult = tokenizeWord(sentence, i, parent);
            // if ((tempResult.getType() == CommandType.CONSTANT) == lookingForConstants){
                results.add(tempResult);
            // } else {
                // break;
            // }
        }
        if (checkForFenString(results)){
            results = glueFenTogether(results);
        }
        return results;
    }
    
    private boolean checkForFenString(List<Command> children){
        if (children.size() > 5){
            if (children.get(0).getParent() != null &&
                children.get(0).getParent().getType() == CommandType.POSITION &&
                children.get(0).getType() != CommandType.STARTPOS){
                return true;
            }
        }
        return false;
    }

    private List<Command> glueFenTogether(List<Command> children){
        String fenString = children.get(0).getData();
        for (int i = 1; i < 6; i++){
            fenString = fenString + " " + children.get(i).getData();
        }
        for (int i = 5; i > 0; i--){
            children.remove(i);
        }
        children.get(0).setData(fenString);
        return children;
    }
}