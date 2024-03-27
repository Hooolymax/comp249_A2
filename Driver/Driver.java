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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import exceptions.*;
import movie.Movie;


public class Driver {

    
    final static int MAXNUMMOVIEINFILE = 200;

    // Directory to CSV files
    static String inputdirectoryPath ="inputfiles"; 
    static String outputdirectoryPath ="outputfiles"; 
    private static int currentGenre=0;
    private static int[]Position;
    static Scanner sc1=new Scanner(System.in);

    static String[] genres = {
        "musical", "comedy", "animation", "adventure", "drama", "crime", 
        "biography", "horror", "action", "documentary", "fantasy", "mystery",
        "sci-fi", "family", "western", "romance", "thriller"
    };



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

         
        Movie[][]all_movies=do_part3(part3_manifest); // deserialize and navigate

        navigateMovies(all_movies);




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


        } catch (FileNotFoundException e) {
            System.out.println("Manifest file not found: " + manifest1FilePath);
        } 


        createManifestFile("inputfiles\\part2_manifest.txt", 2);


        

        return "inputfiles\\part2_manifest.txt";  // returns name of part 2 manifest file

    }





   
    /**
     * creates 1st, 2nd or 3rd manifest file
     * @param manifestFilePath
     * @param manifestNumber
     */
    public static void createManifestFile(String manifestFilePath,int manifestNumber) {

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

            try (FileWriter writer = new FileWriter(manifestFilePath)) {
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

 
        String badMoviesoutputFilePath = outputdirectoryPath+"\\" + "bad_movie_records.txt"; 

        File movieFile = new File(filePath);
        

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
     * Processes a CSV file containing VALID movie records and partitions it into an array of Movie objects.
     *
     * @param filePath 
     * @return array of valid movie objects. Returns {@code null} if the file was not found or empty
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
                    

                
                } catch (SemanticException e2){
                    System.out.println("semantic error " + e2.getMessage() + " " + "[" + line + "]"+ " "  + filePath + " line " + count);
                    
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
        try(PrintWriter badMoviesClearFile=new PrintWriter(new FileOutputStream(path));){
            badMoviesClearFile.print("");
            badMoviesClearFile.flush();
        } catch (FileNotFoundException e) {}

        try(FileOutputStream fileOut = new FileOutputStream(serializedFile); 
            ObjectOutputStream out = new ObjectOutputStream(fileOut)){

            out.writeObject(movies);


        }catch(IOException e){
            System.out.println("Error occured during the serialization of movies of genre " + genre);
        }

    }


 


    /**
     * Deserializes the array of Movie objects from binary files
     * Each binary file contains a serialized array of Movie objects for a genre.
     * @param manifest3FilePath Path to the manifest file containing the list of binary files.
     * @return 2D array of Movie objects,row represents a genre.
     */
    public static Movie[][] do_part3(String manifest3FilePath) {

        int genreCount=countRows(manifest3FilePath);

        if(genreCount==0){
            return new Movie[0][0];     
        }

        Movie[][]all_movies=new Movie[genreCount][];
        
        try(Scanner scanner=new Scanner(new File(manifest3FilePath))){

            int index=0;

            while(scanner.hasNextLine()){
                String fileName=scanner.nextLine();
                String filePath=outputdirectoryPath+"\\"+fileName;

                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))){

                    all_movies[index++]=(Movie[]) in.readObject();

                }catch(IOException|ClassNotFoundException e){
                    System.out.println("Error deserializing "+ fileName);
                    e.printStackTrace();
                }
            }


        }catch(FileNotFoundException e){

            System.out.println("Manifest file not found: "+ manifest3FilePath);

        }


        return all_movies;
    }




    /**
     * Counts the number of rows in a file, as it corresponds to the number of genres.
     * @param filePath Path to the file.
     * @return The number of rows in the file.
     */
    private static int countRows(String filePath){

        int rows=0;
        try(Scanner scanner=new Scanner(new File(filePath))){
            while(scanner.hasNextLine()){
                scanner.nextLine();
                rows++;
            }
            }catch(FileNotFoundException e){
                System.out.println("File not found:"+ filePath);
            }
            return rows;


    }



    /**
     * Displays all movies from the 2D array of Movie objects.
     * @param all_movies The 2D array of Movie objects to display.
     */
    public static void displayAllMovies(Movie[][]all_movies){

        if(all_movies==null){
            System.out.println("Empty movie");
            return;
        }


        for (int i = 0; i < all_movies.length; i++) {

            System.out.println("Genre " + (i+1) + ":");

            if (all_movies[i] == null) {
                System.out.println("  No movies in this genre.");
                continue;
            }


            for (int j = 0; j < all_movies[i].length; j++) {
                if (all_movies[i][j] != null) {
                    System.out.println("  " + all_movies[i][j].toString());
                }
            }

            System.out.println();



        }





    }




    /**
     * Gets the genre name based on index.
     * @param index The index of the genre in the genres array.
     * @return The name of the genre.
     */
    public static String getGenre(int index) {
        if (index >= 0 && index < genres.length) {
            return genres[index];
        } else {
            return "Invalid Genre";
        }
    }





    /**
     * dispaly main menu and select a movie to navigate.
     * @param all_movies The 2D array of Movie objects where each row represents a genre.
     * 
     */
    public static void navigateMovies(Movie[][] all_movies){

        if(all_movies==null||all_movies.length==0){
            System.out.println("No movies to navigate.");
            return;

        }

        Position=new int[all_movies.length];

        
        String choice;

        do{
            System.out.println("----------------------------------------");
            System.out.println("                Main Menu               ");
            System.out.println("----------------------------------------");
            System.out.println("");
            System.out.println("s Select a movie array to navigate");
            System.out.println("n Navigate "+ getGenre(currentGenre)+ " movies (" + (all_movies[currentGenre]!=null?all_movies[currentGenre].length:0) + " records)");
            System.out.println("x Exit");
            System.out.println("");
            System.out.println("----------------------------------------");

            
            System.out.println("");
            System.out.println("Enter your Choice:");

            System.out.println("");

            

            choice=sc1.nextLine();
            choice=choice.toLowerCase();



            switch(choice){
                case "s":
                    selectGenre(all_movies);
                break;


                case"n":
                    navigateGenre(all_movies);
                break;

                case"x":
                    System.out.println("Exiting program, goodbye! ");
                break;

                default:
                    System.out.println("Invalid choice, please enter again. ");

            }

        }while(!choice.equalsIgnoreCase("x"));




    }




    /**
     * select a movie genre to navigate.
     * @param all_movies The 2D array of Movie objects where each row represents a genre.
     */
    public static void selectGenre(Movie[][] all_movies) {

       

        System.out.println("------------------------------");
        System.out.println("Genre Sub-Menu");
        System.out.println("------------------------------");

        for (int i = 0; i < genres.length; i++) {
            int movieCount = (all_movies[i] != null) ? all_movies[i].length : 0;
            System.out.println((i + 1) + " " + genres[i] + " (" + movieCount + " movies)");
        }

        System.out.println((genres.length + 1) + " Exit");
        System.out.println("------------------------------");
        System.out.print("Enter Your Choice: ");

        int choice = sc1.nextInt();

        // zero-based genre
        if (choice > 0 && choice <= genres.length) {
            currentGenre = choice - 1;  // Set the current genre to the user's choice
        } else if (choice == genres.length + 1) {
            System.out.println("Exiting to main menu...");
        } else {
            System.out.println("Invalid choice. Please select a valid option.");
        }
    }




    /**
    * navigate through the movies of a specific genre
    *
    * @param all_movies The 2D array containing arrays of Movie objects for each genre
    */
    private static void navigateGenre(Movie[][] all_movies) {
        
        System.out.println("Navigating " + getGenre(currentGenre) + " movies (" + (all_movies[currentGenre] != null ? all_movies[currentGenre].length : 0) + ")");
        System.out.print("Enter Your Choice: ");
        int n = sc1.nextInt();

        if (n == 0) {
            return;
        }

        int currentPosition = Position[currentGenre];
        if (n > 0) {
            displayMoviesBelow(all_movies[currentGenre], currentPosition, n);
        } else {
            displayMoviesAbove(all_movies[currentGenre], currentPosition, n);
        }
    }


    
    /**
    * Displays movies below the current position in the movie array.
    *
    * @param movies The array of Movie objects 
    * @param currentPosition The current position in the array 
    * @param n The number of steps to move downwards the start of the array
    */
    private static void displayMoviesBelow(Movie[] movies, int currentPosition, int n) {

        int endPosition = Math.min(movies.length, currentPosition + n);

        for (int i = currentPosition + 1; i < endPosition; i++) {
            if (movies[i] != null) {
                System.out.println(movies[i]);
            }
        }

        if (endPosition == movies.length) {
            System.out.println("EOF has been reached");
        }

        Position[currentGenre] = endPosition - 1;
    }




    /**
    * Displays movies above the current position in the movie array.
    *
    * @param movies The array of Movie objects 
    * @param currentPosition The current position in the array 
    * @param n The number of steps to move upwards towards the start of the array
    */
    private static void displayMoviesAbove(Movie[] movies, int currentPosition, int n) {

        int startPosition = Math.max(0, currentPosition + n); // n is negative

        for (int i = startPosition; i < currentPosition; i++) {
            if (movies[i] != null) {
                System.out.println(movies[i]);
            }
        }

        if (startPosition == 0) {
            System.out.println("BOF has been reached");
        }

    }


}



    




    


