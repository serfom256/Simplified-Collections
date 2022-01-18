package additional.exceptions;

public class NullableArgumentException extends IllegalArgumentException {
    public NullableArgumentException() {
        super("Specified argument is null");
    }

    public NullableArgumentException(String s) {
        super(s);
    }
}
