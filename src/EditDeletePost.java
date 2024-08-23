/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EditDeletePost {
    private File csvFile;

    public EditDeletePost(String csvFilePath) {
        this.csvFile = new File(csvFilePath);
    }
    
    public List<String[]> loadJobPosts() {
        List<String[]> jobPosts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                jobPosts.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobPosts;
    }
    
    public void deleteJobPost(String jobId) {
        List<String[]> jobPosts = loadJobPosts();
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile))) {
            pw.println("Job_ID,JTitle,JDescription,JType,JLocation,JSalary,JDuration");
            for (String[] jobPost : jobPosts) {
                if (!jobPost[0].equals(jobId)) {
                    pw.println(String.join(",", jobPost));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getJobPost(String jobId) {
        List<String[]> jobPosts = loadJobPosts();
        for (String[] jobPost : jobPosts) {
            if (jobPost[0].equals(jobId)) {
                return jobPost;
            }
        }
        return null;
    }
    
    public void updateJobPost(String[] updatedJobPost) {
        List<String[]> jobPosts = loadJobPosts();
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile))) {
            pw.println("Job_ID,JTitle,JDescription,JType,JLocation,JSalary,JDuration");
            for (String[] jobPost : jobPosts) {
                if (jobPost[0].equals(updatedJobPost[0])) {
                    pw.println(String.join(",", updatedJobPost));
                } else {
                    pw.println(String.join(",", jobPost));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
     }
}
}
