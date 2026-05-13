import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Defining TextModel for text storage
        TextModel textModel = new TextModel();
        // Defining command manager for undo and redo commands
        CommandManager commandManager = new CommandManager();
        // Defining config for editor settings
        EditorConfig config = EditorConfig.getEditorConfigObject();

        // Defining Frame
        JFrame frame = new JFrame("AOOP Project Text Editor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //close operation
        frame.setSize(800, 500);

        // Defining text area
        JTextArea textArea = new JTextArea();
        // Getting font settings from Singleton object (config)
        textArea.setFont(new Font(config.getFontName(), Font.PLAIN, config.getFontSize()));

        // Defining status label for giving feedback to user
        JLabel statusLabel = new JLabel("Character number: 0");
        // Observer Pattern: Change the character count according to text length.
        textModel.addObserver(text -> statusLabel.setText("Character number: " + text.length()));

        // Little welcoming screen.
        JOptionPane.showMessageDialog(frame,"welcome to the notepad 2.0 :)", "WELCOME", JOptionPane.PLAIN_MESSAGE);

        // --- DOSYA YOLU TAKİBİ ---
        // Lambda ifadeleri içinden erişebilmek için tek elemanlı dizi
        final String[] currentFilePath = {null};

        /*
        Class declarations
        fontAdjuster: for adjusting the size of text, we declared a class named ChangeFontCommand and fontAdjuster is a object of it.
        finder: Same logic behind the declaration of finder. For preventing the repeat at process, we declared a object named finder from class named FindCommand
        saver: Same logic behind the declaration of saver. For preventing the repeat at process, we declared a object named saver from class named SaveCommand
        cmd: update the text synchronously
        */
        ChangeFontCommand fontAdjuster = new ChangeFontCommand(textArea, 18);
        FindCommand finder = new FindCommand(textArea, "");
        SaveCommand saver = new SaveCommand();
        TypeCommand cmd = new TypeCommand(textModel);

       //Confirmation message while exiting application with JOptionPane library
        frame.addWindowListener(new java.awt.event.WindowAdapter(){
                                    @Override
                                    public void windowClosing(java.awt.event.WindowEvent event) {
                                        int result = JOptionPane.showConfirmDialog(
                                                frame,
                                                "Are you sure you want to exit?",
                                                "CONFIRM",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE);


                                        if (result == JOptionPane.YES_OPTION) {

                                            if (saver.getSaveNumber()) {
                                                System.exit(0);
                                            } else {

                                                int sureness = JOptionPane.showConfirmDialog(frame,"you hasn't saved your progress!", "WARNING", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                                                if(sureness == JOptionPane.OK_OPTION){
                                                    System.exit(0);
                                                }

                                            }


                                        }


                                    }

                                }
        );

        // Defining the buttons and adjusting the layout by using GridLayout(row, column, horizontal gap, vertical gap)
        JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 5, 5));

        JButton btnType = new JButton("Confirm Text");
        JButton btnUndo = new JButton("Undo");
        JButton increaseFont = new JButton("Size +");
        JButton decreaseFont = new JButton("Size -");
        JButton btnIterate = new JButton("List Rows");
        JButton btnSave = new JButton("Save");
        JButton btnSaveAs = new JButton("Save As (.txt)");
        JButton btnFind = new JButton("Find");
        JButton btnOpen = new JButton("Open");

        // Adding buttons to panel
        buttonPanel.add(btnType);
        buttonPanel.add(btnUndo);
        buttonPanel.add(increaseFont);
        buttonPanel.add(decreaseFont);
        buttonPanel.add(btnIterate);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnSaveAs);
        buttonPanel.add(btnFind);
        buttonPanel.add(btnOpen);

        // Metni onaylama ve Model'e gönderme (TypeCommand)
        btnType.addActionListener(e -> {
            cmd.setNewText(textArea.getText());
            commandManager.executeCommand(cmd);
            System.out.println("Text has been updated!");
        });

        // Son işlemi geri alma (Undo)
        btnUndo.addActionListener(e -> {
            commandManager.undo();
            textArea.setText(textModel.getText());
            System.out.println("Last operation has been reversed!");
        });

        // Increasing the font by 2 with every click
        increaseFont.addActionListener(e -> {

            int currentSize = EditorConfig.getEditorConfigObject().getFontSize();
            int nextSize = currentSize + 2;
            fontAdjuster.setNewSize(nextSize);
            commandManager.executeCommand(fontAdjuster);

            System.out.println("Font size increased to: " + nextSize);
        });

        // Decreasing the font by 2 with every click
        decreaseFont.addActionListener(e -> {
            int currentSize = EditorConfig.getEditorConfigObject().getFontSize();
            int nextSize = currentSize - 2;

            fontAdjuster.setNewSize(nextSize);
            commandManager.executeCommand(fontAdjuster);

            System.out.println("Font size decreased to: " + nextSize);
        });

        btnIterate.addActionListener(e -> {
            String[] textLines = textArea.getText().split("\n");
            LineIterator iterator = new LineIterator(Arrays.asList(textLines));

            int lines = 0;
            while(iterator.hasNext()){
                if(!iterator.next().trim().isEmpty()){
                    lines++;
                }
            }
            JOptionPane.showMessageDialog(frame, "Row number is: " + lines);

        });


        // Save as: ask for the file path to save and save to that location.
        btnSaveAs.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select location to save");

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // .txt uzantısı yoksa otomatik ekle
                if (!filePath.toLowerCase().endsWith(".txt")) {
                    filePath += ".txt";
                }

                // Save file path to array declared before.
                currentFilePath[0] = filePath;

                saver.setPath(filePath);
                saver.setContent(textArea.getText());

                commandManager.executeCommand(saver);

                JOptionPane.showMessageDialog(frame, "File saved successfully!");
            }
        });

        // Just save
        btnSave.addActionListener(e -> {

            if (currentFilePath[0] == null) { // If no save has been done before, act like 'save as'
                btnSaveAs.doClick();
            } else {
                saver.setPath(currentFilePath[0]);
                saver.setContent(textArea.getText());

                commandManager.executeCommand(saver);
                System.out.println("Updated existing file: " + currentFilePath[0]);
                JOptionPane.showMessageDialog(frame, "Changes saved!");
            }
        });

        // Find command: Find the given input, highlight the upfront one.
        btnFind.addActionListener(e -> {
            String word = JOptionPane.showInputDialog(frame, "Enter text to search:");

            if (word != null && !word.trim().isEmpty()){

                finder.setTarget(word);
                commandManager.executeCommand(finder);
            }
        });

        // Opening the existing file.
        btnOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Bir dosya seç");

            int userSelection = fileChooser.showOpenDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();

                OpenCommand openCmd = new OpenCommand(textModel, path);
                commandManager.executeCommand(openCmd);

                textArea.setText(textModel.getText());
                currentFilePath[0] = path;

                System.out.println("Dosya açıldı: " + path);
            }
        });

        // Finally, adjusting frame
        frame.setLayout(new BorderLayout());

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER); // Scroll for long texts
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}