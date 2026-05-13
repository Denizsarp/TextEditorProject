import javax.swing.*;
import java.awt.*;

// ChangeFontCommand: using for changing the font size
public class ChangeFontCommand implements Command {

    private int newSize;
    private int oldSize;      // Store old size for undo.
    private JTextArea textArea; // the area that is going to be updated.

    public ChangeFontCommand(JTextArea textArea, int newSize) {
        this.textArea = textArea;
        setNewSize(newSize);
    }

    public void setNewSize(int newSize) {
        this.newSize = newSize;
    }

    @Override
    public void execute() {

        // Take the current size and store
        this.oldSize = EditorConfig.getEditorConfigObject().getFontSize();

        // 1. Update Singleton configuration
        EditorConfig.getEditorConfigObject().setFontSize(newSize);

        // 2. Updating ui with new size
        updateUI(newSize);
    }

    @Override
    public void undo() {
        // Load the old size to configuration.
        EditorConfig.getEditorConfigObject().setFontSize(oldSize);
        updateUI(oldSize);
    }

    // Apply the change in font size to JavaTextArea
    private void updateUI(int size) {
        Font currentFont = textArea.getFont();

        textArea.setFont(
                new Font(
                        currentFont.getName(),
                        currentFont.getStyle(),
                        size
                ));
    }
}