import javax.swing.*;

/**
 * Metin içinde arama yapma işlemini yöneten komut sınıfı.
 * Metni değiştirmediği için geri alma (undo) işlemi sadece seçimi temizler.
 */
public class FindCommand implements Command {

    private JTextArea textArea; // Üzerinde arama yapılacak metin alanı
    private String target;      // Aranacak kelime veya cümle

    // Constructor: Hangi alanda neyi arayacağımızı belirtiyoruz
    public FindCommand(JTextArea textArea, String target){
        this.textArea = textArea;
        this.setTarget(target);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public void execute(){
        // Aranan metin boşsa işlem yapmadan çık
        if(target == null || target.isEmpty()){
            return;
        }

        String targetText = textArea.getText(); // Tüm metni al

        // Aramaya imlecin (caret) o anki konumundan başla (Sonrakini Bul özelliği)
        int startPosition = textArea.getCaretPosition();
        int index = targetText.indexOf(target, startPosition);

        // Eğer imleçten sonra kelimeyi bulamazsa, metnin en başına dönüp tekrar bak (Wrap search)
        if (index == -1){
            index = targetText.indexOf(target, 0);
        }

        // Kelime bulunduysa
        if(index != -1){
            // Kelimeyi maviyle seçili (highlight) hale getir
            textArea.setSelectionStart(index);
            textArea.setSelectionEnd(index + target.length());
            // Odaklanmayı metin alanına ver ki seçim kullanıcıya görünsün
            textArea.requestFocusInWindow();
        }
        else{
            // Kelime metnin hiçbir yerinde yoksa kullanıcıya uyarı ver
            JOptionPane.showMessageDialog(textArea, "'" + target + "' bulunamadı!");
        }
    }

    @Override
    public void undo(){
        // Arama işlemini geri almak, yapılan seçimi (vurgulamayı) kaldırmaktır
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(0);
    }
}