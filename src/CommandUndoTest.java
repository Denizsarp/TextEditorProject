import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandUndoTest {

    @Test
    void testTypeCommandAndUndo() {
        TextModel model = new TextModel();
        CommandManager manager = new CommandManager();

        // 1. Durum: İlk metni yaz
        model.setText("İlk hali");

        // 2. Durum: Yeni metni komutla yaz
        TypeCommand cmd = new TypeCommand(model);
        cmd.setNewText("Güncel hali");
        manager.executeCommand(cmd);

        assertEquals("Güncel hali", model.getText());

        // 3. Durum: Geri al
        manager.undo();

        // Metin "İlk hali"ne geri döndü mü?
        assertEquals("İlk hali", model.getText(), "Undo işleminden sonra metin eski haline dönmeli.");
    }
}