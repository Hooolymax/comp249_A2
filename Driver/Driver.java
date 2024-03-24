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

    
    final static int MAXNUMMOVIEINFILE = 200;

    // Directory to CSV files
    static String inputdirectoryPath ="inputfiles"; 
    static String outputdirectoryPath ="outputfiles"; 

    public static void main(String[] args) {


        
        // Path to the manifest 
        String manifestFilePath = inputdirectoryPath+"\\part1_manifest.txt"; 
        

        //read the csv files and create the Manifest File
        createManifestFile(inputdirectoryPath, manifestFilePath);




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
        PrintWriter movieWriter = null;
        

        String[]Genres={
            "musical", "comedy", "animation", "adventure", "drama", "crime", 
            "biography", "horror", "action", "documentary", "fantasy", "mystery",
            "sci-fi", "family", "western", "romance", "thriller"
        };
        int genreCount=0;


        try{
            manifestScanner=new Scanner(new FileInputStream(manifestFile));
            
            
            // clear and create file
            PrintWriter badMoviesClearFile=new PrintWriter(new FileOutputStream("outputfiles\\bad_movie_records.txt"));
            badMoviesClearFile.print("");
            badMoviesClearFile.close();


            while (manifestScanner.hasNextLine()){
                String movieFileName = manifestScanner.nextLine().trim();

                if (!movieFileName.isEmpty()) {
                    
                    Movie[] validMovies = partiateFile(movieFileName);

                    for (Movie movie:validMovies){
                        if (movie== null){
                            break;

                        }

// need to clear all files

                        String genre=movie.getGenres();
                        String outputFileName = genre + ".csv";
                        String outputFilePath = outputdirectoryPath+"\\" + outputFileName; 


                        // Write movie record
                        try (PrintWriter writer=new PrintWriter(new FileOutputStream(outputFilePath,true))){

                            writer.println(movie.toString());
                            writer.flush();

                        }

                   

                        

                        
                        //System.out.println(validMovies[i].toString());
                        
                    }
                   
                }

            }



            try(PrintWriter manifestWriter=new PrintWriter(new FileOutputStream("outputfiles\\part2_manifest.txt"))){
                for (String genre:Genres) {
                    manifestWriter.println(genre + ".csv");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Manifest file not found: " + manifestFilePath);
        } finally {
            if (manifestScanner != null) {
                manifestScanner.close();
            }
            
        }


        

        return "outputfiles\\part2_manifest.txt";  // returns name of part 2 manifest file

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

        String movieFilePath = inputdirectoryPath+"\\"+fileName; 

        File movieFile = new File(movieFilePath);
        Scanner movieFileScanner = null;
        PrintWriter badMovieWriter = null;

        try{

            
            movieFileScanner = new Scanner(new FileInputStream(movieFile));
            String badMoviesoutputFilePath = outputdirectoryPath+"\\" + "bad_movie_records.txt"; 
            badMovieWriter = new PrintWriter(new FileOutputStream(badMoviesoutputFilePath, true));


            while(movieFileScanner.hasNextLine()){

                line = movieFileScanner.nextLine().trim();

// deal with comma inside quotes                
                try{
                    movieRecords[movieCount] = createMovieRecord(line);
                    movieCount++; // executed only if no exceptions

                } catch (SyntaxException e1){
                    badMovieWriter.println("syntax error " + e1.getMessage()+ " " + "[" + line + "]" + fileName+ " " + lineCount);
        
                } catch (MultipleSemanticExceptions es){
                    badMovieWriter.println("multiple semantic errors " + es.getMessages() + " " + "[" + line + "]" + fileName+ " " + lineCount);
                    
                } catch (SemanticException e2){
                    badMovieWriter.println("semantic error " + e2.getMessage() + " " + "[" + line + "]" + fileName + " " + lineCount);
                    
                }

                lineCount ++;
            }

            movieFileScanner.close();
            badMovieWriter.close();
            
            return movieRecords;


        } catch (FileNotFoundException e){
            System.out.println("File " + fileName + " was not found");
            return null;
        }

    }


    /**
     * parses the line into fields and validates the number of fields
     * @param line
     * @return array of String movie fields
     * @throws ExcessFieldsException
     * @throws MissingFieldsException
     */
    public static String[] parseLine(String line) throws ExcessFieldsException, MissingFieldsException{
        
        String[] movieRecordFields = new String[10];
        int fieldCount = 0;
        int quotesCount = 0;
        String stack = "";

        try{

            for (int i=0; i < line.length(); i++){
                // comma, can parse it since no enclosed quotes
                if (line.charAt(i) == ',' && quotesCount % 2 == 0){
                    movieRecordFields[fieldCount++] = stack.trim();
                    stack = ""; // empty stack for the next field
                    continue;
                }

                // if any other character
                stack += line.charAt(i);

                //if quotes 
                if (line.charAt(i) == '\"'){
                    quotesCount++;
                }
                
            }

            // last element
            movieRecordFields[fieldCount] = stack.trim();

            if (fieldCount < 9 ){
                throw new MissingFieldsException("missing fields");
            }

        // attempting to create 11th element
        } catch(IndexOutOfBoundsException e){
            throw new ExcessFieldsException("excess fields");
        }

        return movieRecordFields;
        
    }


    /**
     * partiates string into movie record fields and validates syntax and semantic of it, then creates a Movie object from them.
     * 
     * @param movieRecordFields - String array of all movie record fields
     * @return valid movie object
     * @throws SyntaxException - all types of syntax errors
     * @throws SemanticException - all types of semantic errors
     */
    public static Movie createMovieRecord(String line) throws SyntaxException, SemanticException{
        
        
        // checking that quotes are paired
        int count = 0;
        for (int i=0; i < line.length(); i++){
            if (line.charAt(i) == '\"'){
                count++;
            }
        }
        if (count % 2 != 0){
            throw new MissingQuotesException("missing quotes");
        }
        
        
        String[] movieRecordFields = parseLine(line);

        return new Movie(movieRecordFields);

    }


    //Checks if a genre has already been tracked

    private static boolean isGenreTracked(String[] encounteredGenres, String genre, int genreCount) {
        for (int i = 0; i < genreCount; i++) {
            if (genre.equals(encounteredGenres[i])) {
                return true;
            }
        }
        return false;
    }

    public static String do_part2(String s) {



















        return "";
    }

    public static void do_part3(String s) {
        return;
    }




    

}
