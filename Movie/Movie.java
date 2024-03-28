// -----------------------------------------------------
// COMP249 Assignment 2 due 27.03.2024
// Written by: Alisa Ignatina 40267100 and Jinghao Lai 40041316 
// -----------------------------------------------------

package movie;

import java.io.Serializable;

import exceptions.*;

/**
 * Represents a movie record with 10 attributes 
 * (year, title, duration, genres, rating, score, director, and actors). 
 * The class is designed to handle semantic validation of movie attributes and throw 
 * appropriate exceptions for invalid data.
 *
 * @author Alisa Ignatina 40267100 and Jinghao Lai 40041316 
 * @since 2024-03-27
 */
public class Movie implements Serializable {

    private int year;
    private String title;
    private int duration;
    private String genres;
    private String rating;
    private double score;
    private String director;
    private String actor1;
    private String actor2;
    private String actor3;


    /**
     * default constructor
     */
    public Movie(){
        this.year = 0;
        this.title = "n/a";
        this.duration = 0;
        this.genres = "n/a";
        this.rating = "n/a";
        this.score = 0;
        this.director = "n/a";
        this.actor1 = "n/a";
        this.actor2 = "n/a";
        this.actor3 = "n/a";
    }


    /**
     * constructor which validates movie fields before creating Movie object
     * @param movieRecordFields - array of 10 candidate fields all in format String
     * @throws SemanticException if only 1 exception occured during movie object construction
     * @throws MultipleSemanticExceptions if more than 1 exception occured - throws an array of exceptions
     */
    public Movie(String[] movieRecordFields) throws SemanticException, MultipleSemanticExceptions {
        
        SemanticException[] exceptions = new SemanticException[10];
        int exceptionCount = 0;


        try{
            this.year = validateYear(movieRecordFields[0]);
        }  catch (BadYearException e) {
            exceptions[exceptionCount++] = e;
        }

        try{
            this.title = validateTitle(movieRecordFields[1]);
        }  catch (BadTitleException e) {
            exceptions[exceptionCount++] = e;
        }
        
       
        try {
            this.duration = validateDuration(movieRecordFields[2]);
        } catch (BadDurationException e) {
            exceptions[exceptionCount++] = e;
        }


        try {
            this.genres = validateGenres(movieRecordFields[3]);
        } catch (BadGenreException e) {
            exceptions[exceptionCount++] = e;
        }

       
        try {
            this.rating = validateRating(movieRecordFields[4]);
        } catch (BadRatingException e) {
            exceptions[exceptionCount++] = e;
        }

        
        try{
            this.score = validateScore(movieRecordFields[5]);
        }  catch (BadScoreException e) {
            exceptions[exceptionCount++] = e;
        }
        

        try{
            this.director = validateDirector(movieRecordFields[6]);
        } catch (BadDirectorException e) {
            exceptions[exceptionCount++] = e;
        }

        try{
            this.actor1 = validateActor(movieRecordFields[7]);
        } catch (BadActorException e) {
            exceptions[exceptionCount++] = e;
        }
        

        try{
            this.actor1 = validateActor(movieRecordFields[7]);
        } catch (BadActorException e) {
            exceptions[exceptionCount++] = e;
        }

        try{
            this.actor2 = validateActor(movieRecordFields[8]); 
        } catch (BadActorException e) {
            exceptions[exceptionCount++] = e;
        }
        

        try{
            this.actor3 = validateActor(movieRecordFields[9]); 
        } catch (BadActorException e) {
            exceptions[exceptionCount++] = e;
        }
        
        

        // there were exceptions
        if (exceptionCount > 0) {
            if (exceptionCount == 1){
                throw exceptions[0]; // 1 semantic exception
            } else{
                throw new MultipleSemanticExceptions(exceptions, exceptionCount); // many semantic exceptions
            }
        }
    }

    

    /**
     * copy constructor
     * */
    public Movie(Movie aMovie) {
        this.year = aMovie.year;
        this.title = aMovie.title;
        this.duration = aMovie.duration;
        this.genres = aMovie.genres;
        this.rating = aMovie.rating;
        this.score = aMovie.score;
        this.director = aMovie.director;
        this.actor1 = aMovie.actor1;
        this.actor2 = aMovie.actor2;
        this.actor3 = aMovie.actor3;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor1() {
        return actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public String getActor3() {
        return actor3;
    }

    public void setActor3(String actor3) {
        this.actor3 = actor3;
    }


    /**
     * Validates the movie year which must be between 1990 and 2024
     * @param candidateYear
     * @return valid year
     * @throws BadYearException in case of missing year or invalid year
     */
    public static int validateYear(String candidateYear) throws BadYearException{

        if (candidateYear.isEmpty()){
            throw new BadYearException("missing year");
        
        } else {

            try{
                int year = Integer.parseInt(candidateYear);
                if (year >= 1990 && year <= 1999){
                    return year;
                } else {
                    throw new BadYearException("invalid year");
                }
            } catch (NumberFormatException e) {
                throw new BadYearException("invalid year");
            }

        }
    }

    /**
     * Validates the movie title
     * @param candidateTitle
     * @return valid title
     * @throws BadTitleException if the title is missing
     */
    public static String validateTitle(String candidateTitle) throws BadTitleException{
        if (candidateTitle.isEmpty()){
            throw new BadTitleException("missing title");
        } else {
            return candidateTitle;
        }
    }


    /**
     * Validates the movie score which must be positive and double
     * @param candidateScore
     * @return valid score
     * @throws BadScoreException in case of missing score or invalid score
     */
    public static double validateScore(String candidateScore) throws BadScoreException{

        if (candidateScore.isEmpty()){
            throw new BadScoreException("missing score");
        
        } else {

            try{

                double score = Double.parseDouble(candidateScore);

                if (score > 0 && score <=10){
                    return score;
                } else {
                    throw new BadScoreException("invalid score");
                }
                
            } catch (NumberFormatException e) {
                throw new BadScoreException("invalid score");
            }

        }
    }


    /**
     * validates director
     * @param candidateDirector
     * @return validated director
     * @throws BadDirectorException if missing director
     */
    public static String validateDirector (String candidateDirector) throws BadDirectorException{
        if (candidateDirector.isEmpty()){
            throw new BadDirectorException("missing director");
        } else {
            return candidateDirector;
        }
    }


    /**
     * validates actor
     * @param candidateActor
     * @return validated director
     * @throws BadActorException if missing actor
     */
    public static String validateActor (String candidateActor) throws BadActorException{
        if (candidateActor.isEmpty()){
            throw new BadActorException("missing actor");
        } else {
            return candidateActor;
        }
    }




   
    /**
     * validates the movie duration: an integer from 30 through 300 minutes.
     * @param candidateDuration
     * @return valid duration
     * @throws BadDurationException if invalid duration
     */
    public static int validateDuration(String candidateDuration)throws BadDurationException{


        if (candidateDuration.isEmpty()){
            throw new BadDurationException("missing duration");
        
        }

        else{


            try{
                int duration=Integer.parseInt(candidateDuration);
                if(duration>=30&&duration<=300){
                    return duration;
                }else{

                    throw new BadDurationException("Invalid duration");
                }
            }catch (NumberFormatException e){
                throw new BadDurationException("Invalid duration");
            }


        }

    }





    
    /**
     * validates the movie genres
     * @param candidateGenres
     * @return valid genre
     * @throws BadGenreException
     */
    public static String validateGenres(String candidateGenres)throws BadGenreException{

        String validGenres[]={"musical", "comedy", "animation", "adventure", "drama", "crime", "biography",
        "horror", "action", "documentary", "fantasy", "mystery", "sci-fi", "family",
        "romance", "thriller", "western"};



        if(candidateGenres.isEmpty()){

            throw new BadGenreException("missing genre");

        } else{

            boolean isValidGenre = false;

            for (String validGenre : validGenres) {

                if (candidateGenres.trim().equalsIgnoreCase(validGenre)){
                    isValidGenre = true;
                    break;
                }

            }

            if (!isValidGenre) {
                throw new BadGenreException("invalid genre:" + candidateGenres);
            }

            
            return candidateGenres;
            
        }

    }




    /**
     * validates the movie rating
     * @param candidateRating
     * @return valid rating
     * @throws BadRatingException
     */
    public static String validateRating(String candidateRating)throws BadRatingException{

        String validRatings[]={"PG", "Unrated", "G", "R", "PG-13", "NC-17"};

        if(candidateRating.isEmpty()){
            throw new BadRatingException("missing rating");
        }


        try{

            boolean isValidRating=false;

            for(String validRating:validRatings){

                if(candidateRating.trim().equalsIgnoreCase(validRating)){
                    isValidRating=true;
                }
            }

            if(!isValidRating){
                throw new BadRatingException("Invalid Rating");
            }


            

            return candidateRating;


        }catch(SemanticException e){

            throw new BadRatingException("Invalid Rating");
        }


    }

    /**
     * converts movie object to string
     */
    public String toString() {
        return "Movie [year=" + year + ", title=" + title + ", duration=" + duration + ", genres=" + genres
                + ", rating=" + rating + ", score=" + score + ", director=" + director + ", actor1=" + actor1
                + ", actor2=" + actor2 + ", actor3=" + actor3 + "]";
    }

    
    

}
