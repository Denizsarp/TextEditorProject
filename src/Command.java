// interface for all commands that includes execute() and undo()
public interface Command {
    void execute();
    void undo();
}
