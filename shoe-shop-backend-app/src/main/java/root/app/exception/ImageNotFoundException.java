package root.app.exception;

public class ImageNotFoundException extends RuntimeException{

    public ImageNotFoundException(String message) {
        super(message);
    }
}
