package movie;

import java.io.Serializable;
import java.util.Optional;

import exceptions.*;

public class Movie implements Serializable {

    private int year;
    private String title;
    private Double duration;
    private String genres;
    private int rating;
    private double score;
    private String director;
    private String actor1;
    private String actor2;
    private String actor3;

    public Movie(){
        this.year = 0;
        this.title = "n/a";
        this.duration = 0.0;
        this.genres = "n/a";
        this.rating = 0;
        this.score = 0;
        this.director = "n/a";
        this.actor1 = "n/a";
        this.actor2 = "n/a";
        this.actor3 = "n/a";
    }

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
        

        // max
        this.duration = movieRecordFields[2];

        // max
        this.genres = movieRecordFields[3];

        // max
        this.rating = movieRecordFields[4];

        // alisa
        try{
            this.score = validateScore(movieRecordFields[5]);
        }  catch (BadScoreException e) {
            exceptions[exceptionCount++] = e;
        }
        

        // max 
        this.director = movieRecordFields[6];

        // alisa
        this.actor1 = validateActor(movieRecordFields[7]);

        // max
        this.actor2 = movieRecordFields[8]; 

        // max
        this.actor3 = movieRecordFields[9];

        // there were exceptions
        if (exceptionCount > 0) {
            if (exceptionCount == 1){
                throw exceptions[0]; // 1 semantic exception
            } else{
                throw new MultipleSemanticExceptions(exceptions, exceptionCount); // many semantic exceptions
            }
        }
    }

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

    /**
     * Checks if given String is an integer and converts it to integer
     * @param str - String to be checked and converted
     * @return string converted to int if integer, empty otherwise
     */
    public static Optional<Integer> toInteger(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static boolean notInteger(String str) {
        try {
            Integer.parseInt(str);
            return true; // String is an integer
        } catch (NumberFormatException e) {
            return false; // String is not an integer
        }
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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getScore() {
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
                if (year > 1990 && year <= 2024){
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
     * Validates the movie score which must be positive
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
                if (score > 0){
                    return score;
                } else {
                    throw new BadScoreException("invalid score");
                }
            } catch (NumberFormatException e) {
                throw new BadScoreException("invalid score");
            }

        }
    }


    // max
    public static String validateActor(String candidateActor){
        return candidateActor;
    }

    
    public String toString() {
        return "Movie [year=" + year + ", title=" + title + ", duration=" + duration + ", genres=" + genres
                + ", rating=" + rating + ", score=" + score + ", director=" + director + ", actor1=" + actor1
                + ", actor2=" + actor2 + ", actor3=" + actor3 + "]";
    }

    
    

}
