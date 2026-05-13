import javax.swing.*;

// FindCommand usage: Take an input from user and highlight the FIRST one that appears in text.
public class FindCommand implements Command {

    private JTextArea textArea;
    private String target;

    // Constructor
    public FindCommand(JTextArea textArea, String target){
        this.textArea = textArea;
        this.setTarget(target);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public void execute(){

        if(target == null || target.isEmpty()){
            return;
        }

        String targetText = textArea.getText();

        int startPosition = textArea.getCaretPosition();
        int index = targetText.indexOf(target, startPosition);


        if (index == -1){
            index = targetText.indexOf(target, 0);
        }

        if(index != -1){

            textArea.setSelectionStart(index);
            textArea.setSelectionEnd(index + target.length());
            textArea.requestFocusInWindow();
        }
        else{
            JOptionPane.showMessageDialog(textArea, "'" + target + "' is not found in text!");
        }
    }

    @Override
    public void undo(){
        // Arama işlemini geri almak, yapılan seçimi (vurgulamayı) kaldırmaktır
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(0);
    }
}