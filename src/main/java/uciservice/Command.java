package uciservice;

public class Command {
    private CommandType type;
    private String data;
    
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
    
}
