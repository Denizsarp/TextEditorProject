import javax.swing.*;
import java.awt.*;

/**
 * Font boyutunu değiştirmek için kullanılan komut sınıfı.
 * Hem konfigürasyonu günceller hem de UI (arayüz) elemanına değişikliği yansıtır.
 */
public class ChangeFontCommand implements Command {
    private int newSize;
    private int oldSize;      // Geri alma işlemi için eski boyut
    private JTextArea textArea; // Değişikliğin yansıtılacağı bileşen

    // Constructor: Yeni boyut ve hedef metin alanını alır
    public ChangeFontCommand(JTextArea textArea, int newSize) {
        this.textArea = textArea;
        setNewSize(newSize);
    }

    public void setNewSize(int newSize) {
        this.newSize = newSize;
    }

    @Override
    public void execute() {
        // Mevcut boyutu geri alma işlemi için sakla
        this.oldSize = EditorConfig.getEditorConfigObject().getFontSize();

        // 1. Singleton konfigürasyonunu güncelle
        EditorConfig.getEditorConfigObject().setFontSize(newSize);

        // 2. Değişikliği JTextArea üzerinde uygula (UI Güncelleme)
        updateUI(newSize);
    }

    @Override
    public void undo() {
        // Eski boyutu konfigürasyona geri yükle
        EditorConfig.getEditorConfigObject().setFontSize(oldSize);

        // UI'ı eski boyuta geri döndür
        updateUI(oldSize);
    }

    // Yardımcı metod: Fontu fiziksel olarak JTextArea'ya uygular
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