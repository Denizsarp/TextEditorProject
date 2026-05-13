public class EditorState {
    private final String text;

    public EditorState(String text){
        this.text = text;
    }
    public String getSavedContent(){
        return this.text;
    }

}
