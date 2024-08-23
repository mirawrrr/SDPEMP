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

public class CApplications {
    private String csvFilePath;

    public CApplications(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
    
    public List<String[]> getApplicationsForJob(String jobId) {
        List<String[]> applications = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] appData = line.split(",");
                if (appData[1].equals(jobId)) {
                    applications.add(appData);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return applications;
    }

    public void updateApplicationStatus(String applicationId, String status) {
        List<String[]> applications = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] appData = line.split(",");
                if (appData[0].equals(applicationId)) {
                    appData[4] = status;  
                }
                applications.add(appData);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFilePath))) {
            pw.println("Application ID,Job ID,User ID,Resume,Status");  // Rewrite header
            for (String[] appData : applications) {
                pw.println(String.join(",", appData));
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
 }
}
}
