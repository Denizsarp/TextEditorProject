import java.io.FileReader;
import java.io.IOException;

// OpenCommand usage: we declared this function for opening the previous file that user has written.
public class OpenCommand implements Command{
    private TextModel textModel;
    private String path;
    private String oldText;


    public OpenCommand(TextModel textModel, String path){
        setTextModel(textModel);
        setPath(path);
    }

    public void setTextModel(TextModel textModel){
        this.textModel = textModel;
    }
    public void setPath(String path){
        this.path = path;
    }

    @Override
    public void execute(){
        oldText = this.textModel.getText();
        StringBuilder builder = new StringBuilder();

        try(FileReader reader = new FileReader(this.path)){
            int character;

            while((character = reader.read()) != -1){
                builder.append((char) character);

            }

            textModel.setText(builder.toString());

        }
        catch (IOException error){
            System.out.println("error while reading file!" + error.getMessage());
        }

    }

    @Override
    public void undo(){
        StringBuilder builder = new StringBuilder();
        if(!oldText.isEmpty()){
            for(int i = 0; i < oldText.length(); i++){
                char character = oldText.charAt(i);
                builder.append(character);
            }
        }
        textModel.setText(builder.toString());

    }

}
