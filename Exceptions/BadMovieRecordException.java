// alisa

package exceptions;

public class BadMovieRecordException extends Exception {
    
    public BadMovieRecordException(){
        super();
    }

    public BadMovieRecordException(String message){
        super(message);
    }

}