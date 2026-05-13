
// Setting typed input.
public class TypeCommand implements Command{
    private TextModel content;
    private String newText;
    private EditorState backupState;


    public TypeCommand(TextModel content){
        setContent(content);
    }

    public void setContent(TextModel content){
        this.content = content;
    }

    public void setNewText(String text) {
        this.newText = text;
    }

    @Override
    public void execute(){
        backupState = content.save();
        content.setText(newText);
    }

    @Override
    public void undo(){
        content.restore(backupState);
    }
}
