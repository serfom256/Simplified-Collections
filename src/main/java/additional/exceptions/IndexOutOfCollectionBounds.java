package additional.exceptions;

public class IndexOutOfCollectionBounds extends ArrayIndexOutOfBoundsException{
    public IndexOutOfCollectionBounds() {
        super();
    }

    public IndexOutOfCollectionBounds(String s) {
        super(s);
    }
}
