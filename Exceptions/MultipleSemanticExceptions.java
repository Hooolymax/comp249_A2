package Exceptions;

/**
 * An exception that collects multiple {@link SemanticException} instances.
 * 
 */
public class MultipleSemanticExceptions extends SemanticException{

    
    private SemanticException[] exceptions;
    private int exceptionCount;

    // takes an array of semantic exceptions to handle them later all in ones
    public MultipleSemanticExceptions(SemanticException[] exceptions, int exceptionCount) {
        this.exceptions = exceptions;
        this.exceptionCount = exceptionCount;
    }

    public SemanticException[] getExceptions() {
        return exceptions;
    }

    public int getExceptionCount() {
        return exceptionCount;
    }

    public String getMessages(){
        String messages = "";
        for (int i = 0; i < exceptionCount; i++) {
            messages += exceptions[i].getMessage();
            if (i < exceptionCount - 1) {
                messages += ", "; // Separate messages by comma and space
            }
        }
        return messages;
    }

       
    
}
