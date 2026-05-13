import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextModelTest {

    @Test
    void testSetAndGetText() {
        TextModel model = new TextModel();
        model.setText("Hello World");

        // Is the text set correctly?
        assertEquals("Hello World", model.getText(), "The text in the model should match what is expected.");
    }

    @Test
    void testObserverNotification() {
        TextModel model = new TextModel();
        // We test whether the observer is working by using a flag inside lambda.
        final boolean[] isNotified = {false};

        model.addObserver(text -> isNotified[0] = true);
        model.setText("New Text");

        // Did the Observer triggered?
        assertTrue(isNotified[0], "The observer should be triggered when the text changes..");
    }
}
