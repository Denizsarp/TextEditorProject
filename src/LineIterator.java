import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator<String>{
    private List<String> lines;
    private int position = 0;

    public LineIterator(List<String> lines){
        this.lines = lines;
    }

    @Override
    public boolean hasNext(){
        if(position < lines.size()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String next(){
        if (!hasNext()){
            throw new NoSuchElementException();
        }
        return lines.get(position++);
    }
}
