package uciservice;

import java.io.OutputStreamWriter;

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
        sendOff("id name " + name + " author " + author);
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

    public static void sendInfo(){
        // TODO: Discuss whether needed and if so with which arguments
    }

    public static void sendOption(){
        // TODO: Discuss which options - if any - can be set in the engine
    }

    private static void sendOff(String message){
        // TODO: write message output
    }
}
