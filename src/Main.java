import javax.swing.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // --- MODEL VE YÖNETİCİ BAŞLATMA ---
        TextModel model = new TextModel(); // Metin verisini tutan model
        CommandManager commandManager = new CommandManager(); // Komutları (Undo/Redo) yöneten sınıf
        EditorConfig config = EditorConfig.getEditorConfigObject(); // Singleton: Editör ayarları

        // --- ANA PENCERE (FRAME) AYARLARI ---
        JFrame frame = new JFrame("AOOP Project Text Editor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//close operation
        frame.setSize(800, 500);


        // --- METİN ALANI (TEXT AREA) AYARLARI ---
        JTextArea textArea = new JTextArea();
        // Font ayarını Singleton objesinden alıyoruz
        textArea.setFont(new Font(config.getFontName(), Font.PLAIN, config.getFontSize()));



        // --- DURUM ÇUBUĞU (STATUS LABEL) ---
        JLabel statusLabel = new JLabel("Character number: 0");
        // Observer Pattern: Model değiştikçe karakter sayısını otomatik güncelle
        model.addObserver(text -> statusLabel.setText("Character number: " + text.length()));

        JOptionPane.showMessageDialog(frame,"welcome to the notepad 2.0 :)", "WELCOME", JOptionPane.PLAIN_MESSAGE);

        // --- DOSYA YOLU TAKİBİ ---
        // Lambda ifadeleri içinden erişebilmek için tek elemanlı dizi
        final String[] currentFilePath = {null};



        ChangeFontCommand fontCmd = new ChangeFontCommand(textArea, 18);
        FindCommand findcmd = new FindCommand(textArea, "");
        SaveCommand saveCmd = new SaveCommand();
        TypeCommand cmd = new TypeCommand(model);


        frame.addWindowListener(new java.awt.event.WindowAdapter(){
                                    @Override
                                    public void windowClosing(java.awt.event.WindowEvent event) {
                                        int result = JOptionPane.showConfirmDialog(
                                                frame,
                                                "Are you sure you want to exit?",
                                                "CONFIRM",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE);

                                        if (result == JOptionPane.YES_OPTION && saveCmd.getSaveNumber()) {
                                            System.exit(0);
                                        }
                                        if(result == JOptionPane.YES_OPTION && !saveCmd.getSaveNumber()){
                                            int sureness = JOptionPane.showConfirmDialog(frame,"you hasn't saved your progress!", "WARNING", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                                            if(sureness == JOptionPane.OK_OPTION){
                                                System.exit(0);
                                            }
                                        }


                                    }

                                }
        );

        // --- BUTON PANELİ VE YERLEŞİM (LAYOUT) ---
        // GridLayout(satır, sütun, yatay boşluk, dikey boşluk)
        JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 5, 5));

        JButton btnType = new JButton("Confirm Text");
        JButton btnUndo = new JButton("Undo");
        JButton increaseFont = new JButton("Size +");
        JButton decreaseFont = new JButton("Size -");
        JButton btnIterate = new JButton("List Rows");
        JButton btnSave = new JButton("Save"); // Mevcut dosyanın üzerine yazar
        JButton btnSaveAs = new JButton("Save As (.txt)"); // Her zaman yol sorar
        JButton btnFind = new JButton("Find");
        JButton btnOpen = new JButton("Open");

        // Butonları panele ekle
        buttonPanel.add(btnType);
        buttonPanel.add(btnUndo);
        buttonPanel.add(increaseFont);
        buttonPanel.add(decreaseFont);
        buttonPanel.add(btnIterate);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnSaveAs);
        buttonPanel.add(btnFind);
        buttonPanel.add(btnOpen);

        // --- BUTON AKSİYONLARI (ACTION LISTENERS) ---

        // Metni onaylama ve Model'e gönderme (TypeCommand)
        btnType.addActionListener(e -> {
            cmd.setNewText(textArea.getText());
            commandManager.executeCommand(cmd);
            System.out.println("Text has been updated!");
        });

        // Son işlemi geri alma (Undo)
        btnUndo.addActionListener(e -> {
            commandManager.undo();
            textArea.setText(model.getText());
            System.out.println("Last operation has been reversed!");
        });

        increaseFont.addActionListener(e -> {
            // Mevcut font boyutunu al ve 2 birim arttır
            int currentSize = EditorConfig.getEditorConfigObject().getFontSize();
            int nextSize = currentSize + 2;

            // Komutu oluştur ve CommandManager üzerinden çalıştır
            fontCmd.setNewSize(nextSize);
            commandManager.executeCommand(fontCmd);

            System.out.println("Font size increased to: " + nextSize);
        });

        decreaseFont.addActionListener(e -> {
            int currentSize = EditorConfig.getEditorConfigObject().getFontSize();
            int nextSize = currentSize - 2;

            // Komutu oluştur ve CommandManager üzerinden çalıştır
            fontCmd.setNewSize(nextSize);
            commandManager.executeCommand(fontCmd);

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


        // FARKLI KAYDET (Save As) MANTIĞI
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

                // Dosya yolunu hafızaya kaydet
                currentFilePath[0] = filePath;

                saveCmd.setPath(filePath);
                saveCmd.setContent(textArea.getText());

                commandManager.executeCommand(saveCmd);

                JOptionPane.showMessageDialog(frame, "File saved successfully!");
            }
        });

        // KAYDET (Save) MANTIĞI
        btnSave.addActionListener(e -> {
            // Eğer daha önce hiç kaydedilmediyse (yol null ise) Save As penceresini aç
            if (currentFilePath[0] == null) {
                btnSaveAs.doClick();
            } else {
                // Yol zaten belliyse direkt üzerine yaz

                saveCmd.setPath(currentFilePath[0]);
                saveCmd.setContent(textArea.getText());

                commandManager.executeCommand(saveCmd);
                System.out.println("Updated existing file: " + currentFilePath[0]);
                JOptionPane.showMessageDialog(frame, "Changes saved!");
            }
        });

        // METİN ARAMA (FindCommand)
        btnFind.addActionListener(e -> {
            String word = JOptionPane.showInputDialog(frame, "Enter text to search:");

            if (word != null && !word.trim().isEmpty()){

                findcmd.setTarget(word);

                commandManager.executeCommand(findcmd);
            }
        });

        // 2. Butonun aksiyonunu yaz
        btnOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Bir dosya seç");

            // Sadece mevcut dosyaları açmak için OpenDialog kullanıyoruz
            int userSelection = fileChooser.showOpenDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();

                // --- COMMAND PATTERN KULLANIMI ---
                // Komutu oluştur ve CommandManager üzerinden çalıştır
                OpenCommand openCmd = new OpenCommand(model, path);
                commandManager.executeCommand(openCmd);

                // --- UI GÜNCELLEME ---
                // Modeldeki yeni metni textArea'ya yansıt
                textArea.setText(model.getText());

                // --- ÖNEMLİ: Dosya yolunu güncelle ---
                // 'Save' butonuna basınca direkt bu dosyanın üzerine yazması için yolu kaydediyoruz
                currentFilePath[0] = path;

                System.out.println("Dosya açıldı: " + path);
            }
        });




        // --- PENCERE BİLEŞENLERİNİ YERLEŞTİRME ---
        frame.setLayout(new BorderLayout());
        // JScrollPane: Metin uzarsa kaydırma çubuğu çıkmasını sağlar
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.NORTH); // Butonlar üstte
        frame.add(statusLabel, BorderLayout.SOUTH); // Durum çubuğu altta

        // Pencereyi görünür yap
        frame.setVisible(true);
    }
}