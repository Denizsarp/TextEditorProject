import java.util.Stack;

public class History{
    private Stack<EditorState> editorStates = new Stack<EditorState>();

    public void push(EditorState state){
        editorStates.push(state);
    }

    public EditorState pop(){
        if(!editorStates.isEmpty()){
            EditorState targetState = editorStates.pop();
            return targetState;
        }
        else{
            return null;
        }
    }
    public boolean isEmpty(){
        if(editorStates.isEmpty()){
            return true;
        }
        else{
            return false;
        }


    }



}















