import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextModelTest {

    @Test
    void testSetAndGetText() {
        TextModel model = new TextModel();
        model.setText("Merhaba Dünya");

        // Metin doğru set edildi mi?
        assertEquals("Merhaba Dünya", model.getText(), "Modeldeki metin beklenenle aynı olmalı.");
    }

    @Test
    void testObserverNotification() {
        TextModel model = new TextModel();
        // Lambda içinde bir bayrak (flag) kullanarak observer'ın çalışıp çalışmadığını test ediyoruz
        final boolean[] isNotified = {false};

        model.addObserver(text -> isNotified[0] = true);
        model.setText("Yeni Metin");

        // Observer tetiklendi mi?
        assertTrue(isNotified[0], "Metin değiştiğinde observer tetiklenmeli.");
    }
}
