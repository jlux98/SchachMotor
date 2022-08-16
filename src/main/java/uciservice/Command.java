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
    

    public Command(CommandType type, String data) {
        this.type = type;
        this.data = data;
    }

    public Command(CommandType type) {
        this.type = type;
        this.data = "";
    }
    

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Command)) {
            return false;
        }
        Command command = (Command) o;
        return type == command.type && data.equals(command.data);
    }
}
