// text counter class
public class CharacterCounter implements Observer{

    @Override
    public void update(String text){
        System.out.println("there are " + text.length() + "characters in editor.");
    }

}
