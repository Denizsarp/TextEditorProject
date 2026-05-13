public class EditorConfig {

    private static EditorConfig editorConfigObject;
    private int fontSize = 15;
    private String theme = "Light";
    private String fontName = "Arial";


    private EditorConfig(){}
    //constructor
    public static EditorConfig getEditorConfigObject(){
        if (editorConfigObject == null){
            editorConfigObject = new EditorConfig();
        }
        return editorConfigObject;
    }
    //setter methods
    public void setFontName(String newFontName){
        this.fontName = newFontName;
        System.out.println("Font has been changed: " + newFontName);
    }
    public void setTheme(String newTheme){
        if (!"Light".equals(newTheme) && !"Dark".equals(newTheme)){
            throw new IllegalArgumentException("Invalid theme!");
        }
        else{
            this.theme = newTheme;
        }
    }
    public void setFontSize(int newFontSize){
        this.fontSize = newFontSize;
    }


    //getter methods

    public String getFontName(){
        return this.fontName;
    }
    public int getFontSize(){
        return this.fontSize;
    }
    public String getTheme(){
        return this.theme;
    }
}
