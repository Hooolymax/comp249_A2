package driver;



import java.util.Scanner;

public class Driver {

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


        // read p1 manifest file line by line and handle the exception if it does not exist
        // check if it s empty 
        // if nor read it line by line and open the files containing movies (separate function)
        
        // handle the exceptions and go to the next file - 
        // comment what is missing in case of exception(s) and put it into bad files
        // based on a genre write it to a file 


        // throws syntax and semantic exceptions
        // function takes a file name, than open it and read line by line
        // each line is checked for syntax errors and a movie object is created (and returned) if no errors else exception

        // create manifest file

        return ""; // returns name of part 2 manifest file

    }

    public static String do_part2(String s) {
        return "";
    }

    public static void do_part3(String s) {
        return;
    }

}
