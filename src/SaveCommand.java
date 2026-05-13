// SaveCommand usage: saving
public class SaveCommand implements Command {
    private String content;
    private String path;
    private boolean saved = false;

    public SaveCommand() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void execute() {
        try (java.io.FileWriter writer = new java.io.FileWriter(path)) {
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        saved = true;
    }

    @Override
    public void undo() {
        return;
    }

    public boolean getSaveNumber(){
        return this.saved;
    }
}