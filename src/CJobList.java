/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CJobList {
    private String csvFilePath;

    public CJobList(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
    
    public List<String[]> getAllJobPosts() {
        List<String[]> jobPosts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] jobData = line.split(",");
                jobPosts.add(jobData);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return jobPosts;
    }
}
