package additional.exceptions;

// todo replace all IllegalArgumentExceptions to NullableArgumentException
public class NullableArgumentException extends IllegalArgumentException {
    public NullableArgumentException() {
        super();
    }

    public NullableArgumentException(String s) {
        super(s);
    }
}
