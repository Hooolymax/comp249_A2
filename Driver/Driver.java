// -----------------------------------------------------
// Assignment 2
// Written by: Alisa Ignatina 40267100 and Jinghao Lai 40041316 
// -----------------------------------------------------


// to fix:


// javadoc documentation








package driver;


import java.util.Scanner;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        createManifestFile(manifestFilePath, 1);




        System.out.println();
        System.out.println("Welcome to Alisa's and Max's program");
        System.out.println();



        // part 1’s manifest file
        String part1_manifest = manifestFilePath;

        // part 2’s manifest file
        String part2_manifest = do_part1(part1_manifest); // partition

        // part 3’s manifest file
        String part3_manifest = do_part2(part2_manifest); // serialize

        do_part3(part3_manifest); // deserialize and navigate


        System.out.println("Goodbuy!");


        return;



    }


    /**
     * Partitions movie records in all the input files into CSV files, 
     * each storing valid movie records from the same genre 
     * separates movie records with errors into separate file
     * produces manifest2 file and 17 CSV files containing valid movie records + error file
     * 
     * @param manifest1FilePath
     * @return manifest2FileName
     */
    public static String do_part1(String manifest1FilePath) {

        
        File manifest1File = new File(manifest1FilePath);
        
        

        String[]genres={
            "musical", "comedy", "animation", "adventure", "drama", "crime", 
            "biography", "horror", "action", "documentary", "fantasy", "mystery",
            "sci-fi", "family", "western", "romance", "thriller"
        };

        // create empty csv genre files or clear them after restarting the program
        for (int i= 0; i < genres.length; i++){
            try(PrintWriter csvFileWriter = new PrintWriter(new FileOutputStream( outputdirectoryPath+"\\" + genres[i] + ".csv"))){
                csvFileWriter.print("");
                csvFileWriter.flush();
            } catch (FileNotFoundException e) {
                System.out.println("An error occured in " + genres[i] + " file creation");
            }

        }



        try(Scanner manifest1Scanner=new Scanner(new FileInputStream(manifest1File));){
            
            
            // clear and create bad movies file
            PrintWriter badMoviesClearFile=new PrintWriter(new FileOutputStream("outputfiles\\bad_movie_records.txt"));
            badMoviesClearFile.print("");
            badMoviesClearFile.close();

            // go through each file to create genre file
            while (manifest1Scanner.hasNextLine()){
                String movieFileName = manifest1Scanner.nextLine().trim();

                if (!movieFileName.isEmpty()) {
                    
                    Movie[] validMovies = partiateFile(inputdirectoryPath+"\\"+movieFileName);

                    for (Movie movie:validMovies){
                        if (movie== null){
                            break;

                        }

                        String genre=movie.getGenres();
                        String outputFileName = genre + ".csv";
                        String outputFilePath = outputdirectoryPath+"\\" + outputFileName; 


                        // Write movie record
                        try (PrintWriter writer=new PrintWriter(new FileOutputStream(outputFilePath,true))){

                            writer.println(movie.toString());
                            writer.flush();

                        }

                        
                    }
                   
                }

            }


            /* 
            // creating 2nd manifest file
            try(PrintWriter manifestWriter=new PrintWriter(new FileOutputStream("inputfiles\\part2_manifest.txt"))){
                for (String genre:genres) {
                    manifestWriter.println(genre + ".csv");
                }
            }
            */

        } catch (FileNotFoundException e) {
            System.out.println("Manifest file not found: " + manifest1FilePath);
        } 


        createManifestFile("part2_manifest.txt", 2);


        

        return "inputfiles\\part2_manifest.txt";  // returns name of part 2 manifest file

    }





   
    /**
     * creates 1st, 2nd or 3rd manifest file
     * @param manifestFileName
     * @param manifestNumber
     */
    public static void createManifestFile(String manifestFileName,int manifestNumber) {

        String suffix;
        String directoryPath;


        switch(manifestNumber){
            case 1:
                suffix = ".csv";
                directoryPath = inputdirectoryPath;
            break;

            case 2:
                suffix = ".csv";
                directoryPath = outputdirectoryPath;
            break;

            case 3:
                suffix = ".ser";
                directoryPath = outputdirectoryPath;
            break;

            // should not happen
            default:
                suffix = "";
                directoryPath = "";
                break;
        }


        File dir = new File(directoryPath);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(suffix));

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
     * @param filePath 
     * @return array of valid movie objects. Returns {@code null} if the file was not found.
     */
    public static Movie[] partiateFile(String filePath){

        
        String line;
        Movie[] movieRecords = new Movie[MAXNUMMOVIEINFILE];
        int lineCount = 0;
        int movieCount = 0;

        //String movieFilePath = inputdirectoryPath+"\\"+fileName; 
        String badMoviesoutputFilePath = outputdirectoryPath+"\\" + "bad_movie_records.txt"; 

        File movieFile = new File(filePath);
        //Scanner movieFileScanner = null;
        //PrintWriter badMovieWriter = null;

        try(Scanner movieFileScanner = new Scanner(new FileInputStream(movieFile)); 
        PrintWriter  badMovieWriter = new PrintWriter(new FileOutputStream(badMoviesoutputFilePath, true))){

        
            // file is empty
            if (!movieFileScanner.hasNextLine()){
                return null;
            }

            while(movieFileScanner.hasNextLine()){

                line = movieFileScanner.nextLine().trim();
            
                try{
                    movieRecords[movieCount] = createMovieRecord(line);
                    movieCount++; // executed only if no exceptions

                } catch (SyntaxException e1){
                    badMovieWriter.println("syntax error " + e1.getMessage()+ " " + "[" + line + "]" + " " + filePath+ " line " + lineCount);
        
                } catch (MultipleSemanticExceptions es){
                    badMovieWriter.println("multiple semantic errors " + es.getMessages() + " " + "[" + line + "]"+ " "  + filePath+ " line " + lineCount);
                    
                } catch (SemanticException e2){
                    badMovieWriter.println("semantic error " + e2.getMessage() + " " + "[" + line + "]"+ " "  + filePath + " line " + lineCount);
                    
                }

                lineCount ++;
            }

            
            badMovieWriter.flush();
            
            return movieRecords;


        } catch (FileNotFoundException e){
            System.out.println("File " + filePath + " was not found");
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

    /**
     * for all genre files loads a Movie array with records from the file, 
     * and then serializes the resulting array into a binary file
     * produces manifest3 file and 17 binary files containing serialized movie arrays
     * 
     * @param manifest2FilePath
     * @return manifest3FileName
     */
    public static String do_part2(String manifest2FilePath) {

        


        try(Scanner manifest2Scanner = new Scanner(new FileInputStream(manifest2FilePath));){

            

            // go through each csv file 
            while(manifest2Scanner.hasNextLine()){

                // load movies from genre.csv file into array of movie object
                String fileName = manifest2Scanner.nextLine();
                String genreOfFile = fileName.substring(0 , fileName.length() - 4);
                Movie[] moviesInFile = partiateCSVFile(outputdirectoryPath+"\\"+fileName);






                if (moviesInFile != null){
                    // serialize them and create genre.ser
                    serialize(moviesInFile, genreOfFile);
                }

            }
        

        

            // produce part3_manifest.txt 
            createManifestFile("inputfiles\\part3_manifest.txt", 3);




        } catch (FileNotFoundException e){
            System.out.println("Manifest file not found: " + manifest2FilePath);
        } 
        
        return "inputfiles\\part3_manifest.txt"; 
    }


    /**
     * Processes a csv file containing valid modified movie records 
     * and partitions it into an array of Movie objects.
     *
     * @param filePath 
     * @return array of valid movie objects. Returns {@code null} if the file was not found.
     */
    public static Movie[] partiateCSVFile(String filePath){

        
        String line;
        Movie[] movieRecords = new Movie[MAXNUMMOVIEINFILE];
        int count = 0;
        

        try(Scanner movieFileScanner = new Scanner(new FileInputStream(filePath))){

        
            // file is empty
            if (!movieFileScanner.hasNextLine()){
                return null;
            }

            while(movieFileScanner.hasNextLine()){

                line = movieFileScanner.nextLine().trim();

                //Movie [year=1994, title=Hoop Dreams, duration=170, genres=Documentary, 
                //rating=PG-13, score=8.3, director=Steve James, actor1=William Gates, actor2=Arthur Agee, 
                //actor3=Isiah Thomas]

                String[] lineParsed = line.split("=");
                String[] movieFields = new String[10];
                for (int i = 1; i < lineParsed.length; i++){
                    movieFields[i-1] = lineParsed[i].split(",")[0];
                }
                
            
                try{
                    movieRecords[count] = new Movie(movieFields);
                
                } catch (SemanticException e){
                    System.out.println("error in creating movie from " + line);
                    
                }

                count ++;
            }

            
            
            return movieRecords;


        } catch (FileNotFoundException e){
            System.out.println("File " + filePath + " was not found");
            return null;
        }

    }



    /**
     * serializes the array of movie objects into a binary file named genre.ser
     * @param movies
     */
    public static void serialize(Movie[] movies, String genre){

    
        String path = outputdirectoryPath+"\\"+genre+".ser"; 
        File serializedFile = new File(path);

        // clear file
        try(PrintWriter clearFile=new PrintWriter(new FileOutputStream(path));){
            clearFile.print("");
            clearFile.flush();
        } catch (FileNotFoundException e) {}

        try(FileOutputStream fileOut = new FileOutputStream(serializedFile); 
            ObjectOutputStream out = new ObjectOutputStream(fileOut)){

            out.writeObject(movies);


        }catch(IOException e){
            System.out.println("Error occured during the serialization of movies of genre " + genre);
        }

    }

 
    /**
     * deserializes a binary file into the array of movie objects 
     * @param filePath to .ser file to be deserialized
     * @return array of Movie objects
     */
    
    public static Movie[] deserializeFile(String filePath){

        Movie[] movies = null;

        try(FileInputStream serFileIn = new FileInputStream(filePath); 
        ObjectInputStream in = new ObjectInputStream(serFileIn)){



            movies = (Movie[]) in.readObject();


        } catch(FileNotFoundException e){
            System.out.println(filePath + " file was not found");
        } catch(IOException e1){
            System.out.println("an error occured during the deserialization of " + filePath);
        } catch (ClassNotFoundException e2){
            System.out.println("Class not found");
        } 
            
        return movies;
        

        

    }





    /**
     * reconstructs the serialized array objects from the binary files and 
     * creates a 2D array of all movie objects
     * @param manifest3FilePath
     * @return a 2D object of type Movie[][]
     */
    public static Movie[][] do_part3(String manifest3FilePath) {

        Movie[][] allMovies = null;

        // deserealize each file back into movie arrays
        try(Scanner manifest3Scanner = new Scanner(new FileInputStream(manifest3FilePath));){

            

            // go through each ser file 
            while(manifest3Scanner.hasNextLine()){

                
                String fileName = manifest3Scanner.nextLine();
                
                Movie[] movies = deserializeFile(outputdirectoryPath+"\\"+fileName);

                if (movies == null){
                    System.out.println(fileName + " is empty");
                } else{

                    System.out.println(fileName + movies[0].toString());

                    /* 
                    for(int i = 0; i<movies.length; i++){



                        if (movies[i] == null){
                            break;    
                        }

                        System.out.println(movies[i].toString());

                    }
                    */

                }

            } 


        }catch (FileNotFoundException e){
            System.out.println("Manifest3 file was not found");
        }

        // create Movie[][] from all these arrays

        return allMovies;

        
    }




    

}
