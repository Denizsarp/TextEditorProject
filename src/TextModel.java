import java.util.ArrayList;
import java.util.List;

public class TextModel {
    private String text = "";
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void setText(String newText){
        this.text = newText;
        notifyObservers();
    }
    private void notifyObservers(){
        for (Observer element : observers){
            element.update(text);
        }
    }
    public String getText(){
        return this.text;
    }
    public EditorState save(){
        return new EditorState(text);
    }

    public void restore(EditorState state){
        setText(state.getSavedContent());
    }
}
