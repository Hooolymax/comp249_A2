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
        return "";

    }

    public static String do_part2(String s) {
        return "";
    }

    public static void do_part3(String s) {
        return;
    }

}
