/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UploadPost {
    private String csvFilePath;
    private File csvFile;
    
    public UploadPost(String csvFilePath){
        this.csvFilePath = csvFilePath;
        this.csvFile = new File(csvFilePath);
        createCSVIfNotExists();
    }
    
    private void createCSVIfNotExists() {
        if (!csvFile.exists()) {
            try (FileWriter fw = new FileWriter(csvFilePath, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("Job_ID,JTitle,JDescription,JType,JLocation,JSalary,JDuration");
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
            }
        }
    }
    
    public void saveJobPost(String jobId, String jTitle, String jDescription, String jType, String jLocation, String jSalary, String jDuration,String jTags) {

        List<String[]> jobPosts = readAllJobPosts();
        boolean updated = false;

        for (String[] jobPost : jobPosts) {
            if (jobPost[0].equals(jobId)) {
                jobPost[1] = jTitle;
                jobPost[2] = jDescription;
                jobPost[3] = jType;
                jobPost[4] = jLocation;
                jobPost[5] = jSalary;
                jobPost[6] = jDuration;
                jobPost[7] = jTags; // Update jTags field
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAllJobPosts(jobPosts);
            JOptionPane.showMessageDialog(null, "Job Post Updated Successfully!");
        } else {
            try (FileWriter fw = new FileWriter(csvFilePath, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println(jobId + "," + jTitle + "," + jDescription + "," + jType + "," + jLocation + "," + jSalary + "," + jDuration + "," + jTags);
                pw.flush();
                JOptionPane.showMessageDialog(null, "Job Post Saved Successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving job post: " + e.getMessage());
            }
        }
    }
    
    public void deleteJobPost(String jobId) {
        List<String[]> jobPosts = readAllJobPosts();
        boolean deleted = jobPosts.removeIf(jobPost -> jobPost[0].equals(jobId));

        if (deleted) {
            writeAllJobPosts(jobPosts);
            JOptionPane.showMessageDialog(null, "Job Post Deleted Successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Job Post with ID " + jobId + " not found.");
        }
    }
    
    public String[] getJobPost(String jobId) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(jobId)) {
                    return values;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading job post: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private List<String[]> readAllJobPosts() {
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

    private void writeAllJobPosts(List<String[]> jobPosts) {
        try (FileWriter fw = new FileWriter(csvFilePath, false);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("Job_ID,JTitle,JDescription,JType,JLocation,JSalary,JDuration,JTags");
            for (String[] jobPost : jobPosts) {
                pw.println(String.join(",", jobPost));
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}

    


