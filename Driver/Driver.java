// -----------------------------------------------------
// Assignment 2
// Written by: Alisa Ignatina 40267100 and Jinghao Lai 40041316 
// -----------------------------------------------------

package driver;

import java.util.Scanner;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import exceptions.*;
import movie.Movie;


public class Driver {

    
    final static int MAXNUMMOVIEINFILE = 1000;

    // Directory to CSV files
    static String directoryPath ="inputFiles"; 

    public static void main(String[] args) {


        
        // Path to the manifest 
        String manifestFilePath = directoryPath+"\\part1_manifest.txt"; 
        

        //read the csv files and create the Manifest File
        createManifestFile(directoryPath, manifestFilePath);




        System.out.println();
        System.out.println("Welcome to Alisa's and Max's program");
        System.out.println();

        // part 1’s manifest file
        String part1_manifest = manifestFilePath;

        // part 2’s manifest file
        String part2_manifest = do_part1(part1_manifest /* , ... */); // partition

        // part 3’s manifest file
        String part3_manifest = do_part2(part2_manifest /* , ... */); // serialize
        do_part3(part3_manifest /* , ... */); // deserialize

        // and navigate


        System.out.println("Goodbuy!");


        return;



    }

    public static String do_part1(String manifestFilePath) {

        
        File manifestFile = new File(manifestFilePath);
        Scanner manifestScanner = null;
        //PrintWriter badMoviesClearFile = null;
        


        try{
            manifestScanner=new Scanner(new FileInputStream(manifestFile));
            
            // clear and create file
            PrintWriter badMoviesClearFile=new PrintWriter(new FileOutputStream("bad_movie_records.txt"));
            badMoviesClearFile.print("");
            badMoviesClearFile.close();


            while (manifestScanner.hasNextLine()){
                String movieFileName = manifestScanner.nextLine().trim();

                if (!movieFileName.isEmpty()) {
                    
                    Movie[] movies = partiateFile(movieFileName);
                   
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("Manifest file not found: " + manifestFilePath);
        } finally {
            if (manifestScanner != null) {
                manifestScanner.close();
            }
            
            /* 
            if (badMoviesClearFile != null) {
                badMoviesClearFile.close();
            }
            */
            
        }
    
        return "part2_manifest.txt";  // returns name of part 2 manifest file

    }



    //Create the manifest File
    
    public static void createManifestFile(String directoryPath, String manifestFileName) {

        File dir = new File(directoryPath);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".csv"));

            try (FileWriter writer = new FileWriter(manifestFileName)) {
                if(files!=null){
                    for(File file:files){
                        writer.write(file.getName()+"\n");
                    }
                }

            }catch(IOException e){
                System.out.println("Can't create manifest file");
            }
    

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

        String movieFilePath = directoryPath+"\\"+fileName; 

        File movieFile = new File(movieFilePath);
        Scanner movieFileScanner = null;
        PrintWriter BadMovieWriter = null;

        try{

            
            movieFileScanner = new Scanner(new FileInputStream(movieFile));
            
            // might move to parent method if it will rewrite itsef with a new file call
            BadMovieWriter = new PrintWriter(new FileOutputStream("bad_movie_records.txt", true));


            while(movieFileScanner.hasNextLine()){

                line = movieFileScanner.nextLine().trim();

// deal with comma inside quotes                
                try{
                    movieRecords[movieCount] = createMovieRecord(line.split(","));
                    movieCount++; // executed only if no exceptions

                } catch (SyntaxException e1){
                    BadMovieWriter.println("syntax error " + e1.getMessage()+ " " + "[" + line + "]" + fileName+ " " + lineCount);
        
                } catch (MultipleSemanticExceptions es){
                    BadMovieWriter.println("multiple semantic errors " + es.getMessages() + " " + "[" + line + "]" + fileName+ " " + lineCount);
                    
                } catch (SemanticException e2){
                    BadMovieWriter.println("semantic error " + e2.getMessage() + " " + "[" + line + "]" + fileName + " " + lineCount);
                    
                }

                lineCount ++;
            }

            movieFileScanner.close();
            BadMovieWriter.close();
            
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
    public static Movie createMovieRecord(String[] movieRecordFields) throws SyntaxException, SemanticException{
        
        if (movieRecordFields.length > 10){
            throw new ExcessFieldsException("excess fields");
        }

        if (movieRecordFields.length < 10){
            throw new MissingFieldsException("missing fields");
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
                    throw new MissingQuotesException("missing quotes");
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
