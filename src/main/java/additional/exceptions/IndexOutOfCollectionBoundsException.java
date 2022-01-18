package additional.exceptions;

public class IndexOutOfCollectionBoundsException extends IndexOutOfBoundsException {
    public IndexOutOfCollectionBoundsException() {
        super("Specified index(-es) out of collection bounds");
    }
}
