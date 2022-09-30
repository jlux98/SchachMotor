package uciservice;

import model.Move;

public abstract class UCIOperator {
    
    public static void sendId(String argument, boolean isAuthor){
        if (isAuthor) {
            sendOff("id author " +  argument);
        } else {        
            sendOff("id name " + argument);
        }
    }

    public static void sendId(String name, String author){
        sendOff("id name " + name);
        sendOff("id author " + author);
    }

    public static void sendUciOk() {
        sendOff("uciok");
    }

    public static void sendReadyOk() {
        sendOff("readyok");
    }

    public static void sendBestmove(Move bestmove){
        sendOff("bestmove " + bestmove.toStringAlgebraic());
    }

    public static void sendBestmove(Move bestMove, Move ponderMove){
        sendOff(
            "bestmove " + bestMove.toStringAlgebraic() +  
            " ponder "  + ponderMove.toStringAlgebraic());
    }

    private static void sendOff(String message){
        System.out.println(message);
    }
}
