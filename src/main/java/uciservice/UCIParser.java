package uciservice;

public abstract class UCIParser{
    public static void executeCommand(Command command){
        switch(command.getType()){
            case BINC:
                break;
            case BTIME:
                break;
            case CODE:
                break;
            case CONSTANT:
                break;
            case DEBUG:
                break;
            case DEPTH:
                break;
            case FEN:
                break;
            case GO:
                /*
                 * Supported arguments:
                 * - searchmoves
                 * - ponder (if there is time to implement this)
                 * - depth
                 * - nodes?
                 * - movetime
                 * - infinite
                 */
                // List<Command> children = command.getChildren();                break;
            case INFINITE:
                break;
            case ISREADY:
                System.out.println("readyok");
                break;
            case LATER:
                break;
            case MATE:
                break;
            case MOVES:
                break;
            case MOVESTOGO:
                break;
            case MOVETIME:
                break;
            case NAME:
                break;
            case NODES:
                break;
            case PONDER:
                break;
            case PONDERHIT:
                break;
            case POSITION:
                break;
            case QUIT:
                break;
            case REGISTER:
                break;
            case SEARCHMOVES:
                break;
            case SETOPTION:
                break;
            case STARTPOS:
                break;
            case STOP:
                break;
            case UCI:
                System.out.println("uciok");
                
                break;
            case UCINEWGAME:
                break;
            case VALUE:
                break;
            case WINC:
                break;
            case WTIME:
                break;
            default:
                break;

        }
    }
}