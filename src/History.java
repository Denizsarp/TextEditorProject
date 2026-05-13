
import java.util.Stack;
public class History {
    private Stack<EditorState> states = new Stack<>();

    public void push(EditorState state){
        states.push(state);
    }
    public EditorState pop(){
        if(!states.isEmpty()){

            EditorState targetState = states.pop();
            return targetState;
        }
        else{
            return null;
        }
    }

    public boolean isEmpty(){
        if(states.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
}
