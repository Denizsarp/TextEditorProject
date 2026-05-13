// Örnek bir SaveCommand taslağı
public class SaveCommand implements Command {
    private String content;
    private String path;

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
    }

    @Override
    public void undo() {
        return;
    }
}