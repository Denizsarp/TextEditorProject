import java.util.Stack;

// Manages the execution of commands and maintains a history stack to support undo operations.
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
