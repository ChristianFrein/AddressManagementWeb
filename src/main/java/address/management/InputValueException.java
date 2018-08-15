package address.management;

public class InputValueException extends Exception{

    public InputValueException(){
        super();
    }

    public InputValueException(String message){
        super(message);
    }

    public InputValueException(String message, Throwable cause){
        super(message,cause);
    }

    public InputValueException(Throwable cause){
        super(cause);
    }
}
