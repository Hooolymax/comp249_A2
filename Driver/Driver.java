package driver;

import java.util.Scanner;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import exceptions.*;
import movie.Movie;


public class Driver {

    
    final static int MAXNUMMOVIEINFILE = 50;

    public static void main(String[] args) {

        // part 1’s manifest file
        String part1_manifest = "part1_manifest.txt";

        // part 2’s manifest file
        String part2_manifest = do_part1(part1_manifest /* , ... */); // partition

        // part 3’s manifest file
        String part3_manifest = do_part2(part2_manifest /* , ... */); // serialize
        do_part3(part3_manifest /* , ... */); // deserialize

        // and navigate
        return;

    }

    public static String do_part1(String s) {

        // max
        // read p1 manifest file line by line and handle the exception if it does not exist
        // check if it s empty 
        // if nor read it line by line and open the files containing movies (separate function)
        
        // handle the exceptions and go to the next file - 
        // comment what is missing in case of exception(s) and put it into bad files
        // based on a genre write it to a file 

        // alisa 
        // throws syntax and semantic exceptions
        // function takes a file name, than open it and read line by line
        // each line is checked for syntax errors and a movie object is created (and returned) if no errors else exception

        // create manifest file


        partiateFile(fileName);

        return ""; // returns name of part 2 manifest file

    }


    /**
     * Processes a file containing movie records and partitions it into an array of Movie objects.
     * Invalid records are written to a separate file "bad_movie_records.txt".
     *
     * @param fileName 
     * @return array of valid movie objects. Returns {@code null} if the file was not found.
     */
    public static Movie[] partiateFile(String fileName){

        
        String line;
        Movie[] movieRecords = new Movie[MAXNUMMOVIEINFILE];
        int lineCount = 0;
        int movieCount = 0;

        Scanner inputStream = null;
        PrintWriter outputStream = null;

        try{

            
            inputStream = new Scanner(new FileInputStream(fileName));
            
            // might move to parent method if it will rewrite itsef with a new file call
            outputStream = new PrintWriter(new FileOutputStream("bad_movie_records.txt", true));


            while(inputStream.hasNextLine()){

                line = inputStream.nextLine();

                try{
                    movieRecords[movieCount] = createMovieRecord(line.split(" , "));
                    movieCount++; // executed only if no exceptions

                } catch (SyntaxException e1){
                    outputStream.println("syntax error" + e1.getMessage() + line + fileName + lineCount);
                } catch (MultipleSemanticExceptions es){
                    outputStream.println("multiple semantic errors" + es.getMessages() + line + fileName + lineCount);
                } catch (SemanticException e2){
                    outputStream.println("semantic error" + e2.getMessage() + line + fileName + lineCount);
                }

                lineCount ++;
            }

            inputStream.close();
            outputStream.close();
            
            return movieRecords;


        } catch (FileNotFoundException e){
            System.out.println("File " + fileName + " was not found");
            return null;
        }

    }

    /**
     * Validates syntax and semantic of movie record fields and creates a Movie object from them.
     * 
     * @param movieRecordFields - String array of all movie record fields
     * @return valid movie object
     * @throws SyntaxException - all types of syntax errors
     * @throws SemanticException - all types of semantic errors
     */
    public static Movie createMovieRecord(String[] movieRecordFields) throws SyntaxException, MultipleSemanticExceptions{
        
        if (movieRecordFields.length > 10){
            throw new ExcessFieldsException();
        }

        if (movieRecordFields.length < 10){
            throw new MissingFieldsException();
        }

        for (int i = 0; i<10; i++){
            if (movieRecordFields[i].contains("\"")){
                int count= 0;
                for (int j=0; j < movieRecordFields[i].length(); j++){
                    if (movieRecordFields[i].charAt(j) == '\"'){
                        count++;
                    }
                }
                if (count % 2 != 0){
                    throw new MissingQuotesException();
                }
            }
        }

        return new Movie(movieRecordFields);

    }


    public static String do_part2(String s) {
        return "";
    }

    public static void do_part3(String s) {
        return;
    }

}
