package uciservice;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private CommandType type;
    private String data;
    private List<Command> children;
    private Command parent;

    public List<Command> getChildren() {
        return this.children;
    }

    public void setChildren(List<Command> children) {
        this.children = children;
    }


    // Constructors
    public Command(CommandType type, String data, Command parent) {
        this.type = type;
        this.data = data;
        this.parent = parent;
        switch(type){
            case BINC:
            case BTIME:
            case CODE:
            case DEBUG:
            case DEPTH:
            case FEN:
            case GO:
            case MATE:
            case MOVES:
            case MOVESTOGO:
            case MOVETIME:
            case NAME:
            case NODES:
            case POSITION:
            case REGISTER:
            case SEARCHMOVES:
            case SETOPTION:
            case VALUE:
            case WINC:
            case WTIME:
                this.children = new ArrayList<>();
                break;
            case CONSTANT:
            case INFINITE:
            case ISREADY:
            case LATER:
            case PONDER:
            case PONDERHIT:
            case QUIT:
            case STARTPOS:
            case STOP:
            case UCI:
            case UCINEWGAME:
                this.children = null;
                break;
            default:
                break;
        }
    }

    public Command(CommandType type, Command parent) {
        this(type, "", parent);
    }


    // Functionality
    public void addChild(Command toAdd){
        switch(type){
            case BINC:
            case BTIME:
            case CODE:
            case DEBUG:
            case DEPTH:
            case FEN:
            case GO:
            case MATE:
            case MOVES:
            case MOVESTOGO:
            case MOVETIME:
            case NAME:
            case NODES:
            case POSITION:
            case REGISTER:
            case SEARCHMOVES:
            case SETOPTION:
            case VALUE:
            case WINC:
            case WTIME:
                break;
            case CONSTANT:
            case INFINITE:
            case ISREADY:
            case LATER:
            case PONDER:
            case PONDERHIT:
            case QUIT:
            case STARTPOS:
            case STOP:
            case UCI:
            case UCINEWGAME:
                throw new IllegalStateException("Error: Commands of the type "
                    + type + " are not allowed to have children");
            default:
                break;
        }
        children.add(toAdd);
    }

    public void addAll(List<Command> children){
        for (int i = 0; i < children.size(); i++){
            addChild(children.get(i));
        }
    }

    public void deleteChild(Command toDelete){
        children.remove(toDelete);
    }

    public void deleteChildAt(int index){
        children.remove(index);
    }

    public Command getChildAt(int index){
        return children.get(index);
    }

    public int getChildrenSize(){
        return children.size();
    }


    // Getter/Setter
    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Command getParent() {
        return this.parent;
    }

    public void setParent(Command parent) {
        this.parent = parent;
    }


    // Override-Methods
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Command)) {
            return false;
        }
        Command command = (Command) o;
        if (type != command.getType()) {
            return false;
        }
        if (type != CommandType.CONSTANT){
            if (children.size() != command.getChildrenSize()){
                return false;
            }
            for (int i = 0; i < children.size(); i++){
                if (!children.get(i).equals(command.getChildAt(i))){
                    return false;
                }
            }
        }
        return data.equals(command.getData());
    }
}