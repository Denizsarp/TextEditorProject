import java.util.Stack;

public class CommandManager {
    private Stack<Command> history = new Stack<>();

    public void executeCommand(Command targetCommand){
        targetCommand.execute();
        history.push(targetCommand);
    }

    public void undo(){
        if (!history.isEmpty()){
            Command command = history.pop();
            command.undo();
        }
    }
}
