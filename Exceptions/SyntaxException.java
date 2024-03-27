// -----------------------------------------------------
// Assignment 2
// Written by: Alisa Ignatina 40267100 and Jinghao Lai 40041316 
// -----------------------------------------------------

package exceptions;

/**
 * Abstract exception class for handling all types of syntax errors in movie records
 */
public abstract class SyntaxException extends Exception{

    public SyntaxException(){
        super();
    }

    public SyntaxException(String message){
        super(message);
    }

    
}
