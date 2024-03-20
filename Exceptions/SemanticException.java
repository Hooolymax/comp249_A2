package Exceptions;


public abstract class SemanticException extends Exception{

    private SemanticException[] exceptions = new SemanticException[10];

    public SemanticException(){
        super();
    }

    public SemanticException(String message){
        super(message);
    }

    public SemanticException(SemanticException[] exceptions) {
        super("multiple semantic errors");
        this.exceptions = exceptions;
    }

    public SemanticException[] getExceptions() {
        return exceptions;
    }
    
}
