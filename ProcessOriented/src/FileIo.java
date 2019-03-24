/**
 * FileIo.java
 * @author Group 41: Chong Ye, Dongmin Han, Shan Xiong
 * Georgia Institute of Technology, Spring 2019
 *
 * Helper function for reading data and writing results
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileIo {

    public static void readFile(String filepath) throws IOException {
        // Open the file and read information about the vehicles
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String currLine = "";
    }
}
