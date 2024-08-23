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
import java.io.FileReader;
import java.io.IOException;

public class EditingPostGUI extends JFrame {
    private JTextField jobIdField, jTitleField, jTypeField, jLocationField, jSalaryField, jDurationField, jTagField;
    private JTextArea jDescriptionField;
    private JButton saveButton;
    private UploadPost uploadPost;
    private String jobId;
    private String companyId;
    private String profilePicPath;

    public EditingPostGUI(UploadPost uploadPost,String jobId) {
        this.jobId = jobId;
        this.uploadPost = uploadPost;
        this.uploadPost = new UploadPost("JobPostInfo.csv");
        setTitle("Edit Job Post");
        setSize(1024, 576);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set title bar background to black and text to white
        UIManager.put("InternalFrame.activeTitleBackground", Color.BLACK);
        UIManager.put("InternalFrame.activeTitleForeground", Color.WHITE);
        
        initializeUI();
        
        loadJobPostData(jobId);
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Arial", Font.BOLD, 16);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Job ID:"), gbc);
        
        jobIdField = new JTextField(30);
        jobIdField.setEditable(false);
        gbc.gridx = 1;
        panel.add(jobIdField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Title:"), gbc);

        jTitleField = new JTextField(30);
        gbc.gridx = 1;
        panel.add(jTitleField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Description:"), gbc);

        jDescriptionField = new JTextArea(5, 30);
        jDescriptionField.setLineWrap(true);
        jDescriptionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(jDescriptionField);
        gbc.gridx = 1;
        gbc.gridheight = 3;
        panel.add(scrollPane, gbc);
        gbc.gridheight = 1;
        

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Type:"), gbc);

        jTypeField = new JTextField(30);
        gbc.gridx = 1;
        panel.add(jTypeField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Location:"), gbc);

        jLocationField = new JTextField(30);
        gbc.gridx = 1;
        panel.add(jLocationField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Salary:"), gbc);

        jSalaryField = new JTextField(30);
        gbc.gridx = 1;
        panel.add(jSalaryField, gbc);
        

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Duration:"), gbc);

        jDurationField = new JTextField(30);
        jDurationField.setEditable(false);
        gbc.gridx = 1;
        panel.add(jDurationField, gbc);
        

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.YELLOW); 
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveJobPost();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(saveButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.YELLOW); 
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EditDeletePostGUI(companyId,profilePicPath).setVisible(true);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(backButton, gbc);

        add(panel);
    }     

    private void loadJobPostData(String jobId) {        
        try (BufferedReader br = new BufferedReader(new FileReader("JobPostInfo.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] jobData = line.split(",");
                if (jobData[0].equals(jobId)) {
                    jobIdField.setText(jobData[0]);
                    jTitleField.setText(jobData[1]);
                    jDescriptionField.setText(jobData[2]);
                    jTypeField.setText(jobData[3]);
                    jLocationField.setText(jobData[4]);
                    jSalaryField.setText(jobData[5]);
                    jDurationField.setText(jobData[6]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }         
    
    private void saveJobPost() {
        String jobId = jobIdField.getText();
        String jTitle = jTitleField.getText();
        String jDescription = jDescriptionField.getText();
        String jType = jTypeField.getText();
        String jLocation = jLocationField.getText();
        String jSalary = jSalaryField.getText();
        String jDuration = jDurationField.getText();
        String jTags = jTagField.getText();
        
        // Debugging: Print collected data to console
        System.out.println("Attempting to save job post with the following data:");
        System.out.println("Job ID: " + jobId);
        System.out.println("Title: " + jTitle);
        System.out.println("Description: " + jDescription);
        System.out.println("Type: " + jType);
        System.out.println("Location: " + jLocation);
        System.out.println("Salary: " + jSalary);
        System.out.println("Duration: " + jDuration);
        System.out.println("Tags: " + jTags);
        
        //debug
        if (uploadPost != null) {
            try {
                // Attempt to save the job post
                uploadPost.saveJobPost(jobId, jTitle, jDescription, jType, jLocation, jSalary, jDuration, jTags);
            } catch (Exception e) {
                // Debugging: Catch any exceptions during saving
                System.out.println("Error while saving job post: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Debugging: UploadPost instance is null
            System.out.println("Error: uploadPost is null. Unable to save job post data.");
            JOptionPane.showMessageDialog(this, "Error: Unable to save job post data.");
        }

        // Debugging: Confirm disposal and transition to the EditDeletePostGUI
        System.out.println("Disposing current GUI and opening EditDeletePostGUI.");
        dispose();
        new EditDeletePostGUI(companyId,profilePicPath).setVisible(true);
    }  
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UploadPost uploadPostInstance = new UploadPost("JobPostInfo.csv");
            new EditingPostGUI(uploadPostInstance, "jobID").setVisible(true); 
   });
    }
}
