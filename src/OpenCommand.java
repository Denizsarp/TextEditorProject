import java.io.FileReader;
import java.io.IOException;

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
        StringBuilder builder = new StringBuilder();
        // reader.read() metodu dosyanın sonuna gelene kadar karakterin sayısal değerini döndürür
        // Dosya bittiğinde -1 döndürür.

        try(FileReader reader = new FileReader(this.path)){
            int character;

            while((character = reader.read()) != -1){
                // Sayısal değeri karaktere (char) çevirip StringBuilder'a ekliyoruz
                builder.append((char) character);

            }

            // Okuma bitince modeldeki metni güncelle
            textModel.setText(builder.toString());

        }
        catch (IOException error){
            System.out.println("error while reading file!" + error.getMessage());
        }

    }

    @Override
    public void undo(){
        return;

    }

}
