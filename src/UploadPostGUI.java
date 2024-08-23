/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.*;
import java.util.Calendar;
import java.text.ParseException;
import java.io.*;
import com.formdev.flatlaf.FlatLightLaf;


public class UploadPostGUI extends JFrame{
    private JTextField jobIdField, jTitleField, jTypeField, jLocationField, jSalaryField, jDurationField,jTagField;
    private JTextArea jDescriptionField;
    private JButton saveButton, homepageButton;
    private String currentCompanyId;
    private String profilePicPath;
    private String companyId;
    private String jobId;
    private RemoveExpiredJob removeExpiredJob;
    
    
    public UploadPostGUI(String profilePicPath, String currentCompanyId) {
        //added thia
        // Get the company ID from the Login session

        this.currentCompanyId = currentCompanyId;
        this.profilePicPath = profilePicPath; 
        
        System.out.println("UploadPostGUI created with Profile Picture Path: " + profilePicPath);
        createView(currentCompanyId);
        
        removeExpiredJob = new RemoveExpiredJob("JobPostInfo.csv");
        
        //ori
        

        setTitle("Upload New Job Post");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 576);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set the title background color to black and text color to white
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Upload New Job Post", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        getContentPane().add(titlePanel, BorderLayout.NORTH);
    }
    
    //private void createView() {
    private void createView(String companyId) {
        Font labelFont = new Font("Helvetica", Font.PLAIN, 12);
        Font inputFont = new Font("Helvetica", Font.PLAIN, 12);
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //ori is 15,15,15,15
        gbc.insets = new Insets(10, 10, 10, 10);  
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jobIdLabel = new JLabel("Job ID:");
        jobIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(jobIdLabel, gbc);
        
        //ori is 30, 1, 0, 3
        jobIdField = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(jobIdField, gbc);

        
        JLabel jTitleLabel = new JLabel("Job Title:");
        jTitleLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(jTitleLabel, gbc);

        jTitleField = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(jTitleField, gbc);

        
        JLabel jDescriptionLabel = new JLabel("Job Description:");
        jDescriptionLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(jDescriptionLabel, gbc);

        jDescriptionField = new JTextArea(4, 25);//ori 5,30
        jDescriptionField.setLineWrap(true);
        jDescriptionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(jDescriptionField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;//ori is 3
        gbc.gridheight = 2;
        panel.add(scrollPane, gbc);

        gbc.gridheight = 1; // Reset grid height after adding the JScrollPane

        
        JLabel jTypeLabel = new JLabel("Job Type:");
        jTypeLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(jTypeLabel, gbc);

        jTypeField = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(jTypeField, gbc);

        
        JLabel jLocationLabel = new JLabel("Job Location:");
        jLocationLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(jLocationLabel, gbc);

        jLocationField = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(jLocationField, gbc);

        
        JLabel jSalaryLabel = new JLabel("Job Salary:");
        jSalaryLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panel.add(jSalaryLabel, gbc);

        jSalaryField = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(jSalaryField, gbc);
        
        // New Job Tag field
        JLabel jTagLabel = new JLabel("Job Tags:");
        jTagLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 7; // Adjust based on your layout
        gbc.gridwidth = 1;
        panel.add(jTagLabel, gbc);

        jTagField = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(jTagField, gbc);


        // Save Button
        saveButton = new JButton("Save");
        saveButton.setBackground(Color.YELLOW);
        saveButton.setForeground(Color.BLACK);
        saveButton.setFont(inputFont); // Set button background to yellow
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;    
        panel.add(saveButton, gbc);

        // Homepage Button
        homepageButton = new JButton("Homepage");
        homepageButton.setBackground(Color.YELLOW);
        homepageButton.setForeground(Color.BLACK);
        homepageButton.setFont(inputFont);
// Set button background to yellow
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        homepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackToHomePage();
            }
        });
        panel.add(homepageButton, gbc);
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jobId = jobIdField.getText();
                String jTitle = jTitleField.getText();
                String jDescription = jDescriptionField.getText();
                String jType = jTypeField.getText();
                String jLocation = jLocationField.getText();
                String jSalary = jSalaryField.getText();
                //String jDuration = jDurationField.getText();
                String jTags = jTagField.getText(); // Get the Job Tags

                
                //1-Minute Countdown Version for Testing
                /*Calendar calendarTest = Calendar.getInstance();
                calendarTest.add(Calendar.MINUTE, 1);  // For testing, set to 1 minute
                String expirationTimeTest = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendarTest.getTime());*/
                
                //90-Day Countdown Version (Commented for Testing)
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 90);
                String expirationTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
                
                // Save job post to CSV with 90-day countdown
                //rmb change expirationTimeTest to expirationTime when change t0 90 days ver
                removeExpiredJob.saveJobPost(jobId, jTitle, jDescription, jType, jLocation, jSalary, expirationTime, companyId,jTags);

                // Show success message
                JOptionPane.showMessageDialog(null, "Job post saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear input after info is saved into CSV
                jobIdField.setText("");
                jTitleField.setText("");
                jDescriptionField.setText("");
                jTypeField.setText("");
                jLocationField.setText("");
                jSalaryField.setText("");
                jTagField.setText(""); // Clear the Job Tags field

            }
        });
    }         
   
    //added some stuff here
    private void goBackToHomePage(){
          dispose();
    if (profilePicPath != null && currentCompanyId != null) {
        // Create and display the new EMPProfile instance
        new EMPProfile(profilePicPath, currentCompanyId).setVisible(true);
    } else {
        // Handle the case where profilePicPath or currentCompanyId is null
        JOptionPane.showMessageDialog(null, "Profile picture path or Company ID is not set.");
    } // ltr change the name to actual homepage name
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new UploadPostGUI("path/to/profile/picture.jpg", "COMP123456").setVisible(true));      
    }

    public class RemoveExpiredJob {
        private String csvFilePath;


        public  RemoveExpiredJob(String filePath) {
            this.csvFilePath = filePath;
        }

        public void saveJobPost(String jobId, String jTitle, String jDescription, String jType, String jLocation, String jSalary, String jDuration, String companyId,String jTags) {
            File csvFile = new File(csvFilePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
                // Check if the file is empty or doesn't exist and write the header if needed
                if (!csvFile.exists() || csvFile.length() == 0) {
                    writer.write("Job ID,Job Title,Job Description,Job Type,Job Location,Job Salary,Expiration Date,Company_ID,Job_Tags");
                    writer.newLine();
                }
                
                // Split the tags by comma and trim any extra spaces
                String[] tagsArray = jTags.split(",");
                StringBuilder formattedTags = new StringBuilder();

                for (String tag : tagsArray) {
                    formattedTags.append(tag.trim()).append(";");
                }

                // Remove the last semicolon if exists
                if (formattedTags.length() > 0) {
                    formattedTags.setLength(formattedTags.length() - 1);
                }


                // Write the job post details
                writer.write(jobId + "," + jTitle + "," + jDescription + "," + jType + "," + jLocation + "," + jSalary + "," + jDuration+"," + companyId+ "," + formattedTags.toString());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void checkAndDeleteExpiredPosts() {
            List<String> updatedPosts = new ArrayList<>();
            long currentTime = System.currentTimeMillis();

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        updatedPosts.add(line); // Preserve the header
                        isFirstLine = false;
                        continue;
                    }

                    String[] columns = line.split(",");
                    if (columns.length >= 7) {
                        String expirationTime = columns[6];

                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date expirationDate = sdf.parse(expirationTime);

                            // Check if the job post has expired
                            if (expirationDate.getTime() > currentTime) {
                                updatedPosts.add(line); // Keep non-expired posts
                            }
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                            updatedPosts.add(line); // Keep the line if date parsing fails
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
                for (String post : updatedPosts) {
                    writer.write(post);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
