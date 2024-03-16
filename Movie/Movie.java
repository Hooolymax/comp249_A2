package Movie;

import java.io.Serializable;

public class Movie implements Serializable {

    private int year;
    private String title;
    private Double duration;
    private String genres;
    private int rating;
    private int score;
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

    public Movie(int year, String title, Double duration, String genres, int rating, int score, String director,
            String actor1, String actor2, String actor3) {
        this.year = year;
        this.title = title;
        this.duration = duration;
        this.genres = genres;
        this.rating = rating;
        this.score = score;
        this.director = director;
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.actor3 = actor3;
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

    
    public String toString() {
        return "Movie [year=" + year + ", title=" + title + ", duration=" + duration + ", genres=" + genres
                + ", rating=" + rating + ", score=" + score + ", director=" + director + ", actor1=" + actor1
                + ", actor2=" + actor2 + ", actor3=" + actor3 + "]";
    }

    
    

}
