/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.Timer;


public class CJobListGUI extends JFrame {
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private UploadPostGUI upload;
    private RemoveExpiredJob removeExpiredJob;
    private UploadPost uploadPost;
    private String currentCompanyId = "COMP123456";  // Add a variable to hold the current company ID
    private String companyId;
    private String csvCompanyId; 
    private String jobId;
    private String jobTitle;
    private String profilePicPath = "path/to/profile/picture.jpg";
    private String userId;
    
    //originally
    //public CJobListGUI() {
    public CJobListGUI(String companyId) {  // Accept company ID as a parameter
        //added this
        this.currentCompanyId = companyId; 
        this.companyId = companyId;
        this.profilePicPath = profilePicPath; 
        
        System.out.println("CJobListGUI created with Profile Picture Path: " + profilePicPath);
        
        initializeUI();
        tableModel = (DefaultTableModel) jobTable.getModel();
        loadJobPosts();
        
        setTitle("Job List");
        setSize(1024, 576);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);     
        
        // Initialize the RemoveExpiredJob object
        upload = new UploadPostGUI(profilePicPath,currentCompanyId) ;
        //added thia
        removeExpiredJob = new RemoveExpiredJob ("JobPostInfo.csv");
        
        // Check and remove expired jobs on initialization
        removeExpiredJob.checkAndDeleteExpiredPosts();
      
        
        
        //refresh and check list per minute
        Timer refreshTimer = new Timer(1000 * 60, new ActionListener() {  // Check every 1 minute
            @Override
            public void actionPerformed(ActionEvent e) {
                removeExpiredJob.checkAndDeleteExpiredPosts();  // Remove expired jobs
                loadJobPosts();  // Reload the job posts
            }
        });
       refreshTimer.start();
       
    }
    
    private void initializeUI() {
        JPanel panel = new JPanel(new BorderLayout());
        
         // Adding space between the main header and the table header (not working dk y)
        //panel.add(Box.createVerticalStrut(15), BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Job List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        //titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add spacing above and below the header
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        
        tableModel = new DefaultTableModel(new Object[]{"Job ID", "Job Title", "View"}, 0);     
        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(40);  // Set row height for better visibility
        
         // Adding space between the main header and the table
        panel.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
        
        loadJobPosts();
        
        jobTable.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 14));

        
        // Adjusting the column widths to fit the new panel size
        TableColumnModel columnModel = jobTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Job ID column
        columnModel.getColumn(1).setPreferredWidth(276);  // Job Title column
        columnModel.getColumn(2).setPreferredWidth(150);  // Action column
        
          // Set the table header background to grey
        jobTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        jobTable.getTableHeader().setOpaque(true);

        jobTable.getColumn("View").setCellRenderer(new ButtonRenderer());
        jobTable.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox()));       

        JScrollPane scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Homepage"); //some changes here n below this line
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                
                System.out.println("Profile Picture Path: " + profilePicPath);
                System.out.println("Company ID: " + currentCompanyId);
        
                if (profilePicPath != null && !profilePicPath.isEmpty() && currentCompanyId != null && !currentCompanyId.isEmpty()) {
            
        } else {
            JOptionPane.showMessageDialog(null, "Profile picture path or Company ID is not set.");
        } // change name to actual one ltr
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        backButton.setBackground(Color.YELLOW);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);

}

 //added some stuff in here
private void loadJobPosts() {
        tableModel.setRowCount(0);
        String csvFilePath = "JobPostInfo.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header line
                }
                String[] jobData = line.split(",");
                if (jobData.length >= 2) { 
                    jobId = jobData[0]; // Job ID
                    jobTitle = jobData[1]; // Job Title
                    csvCompanyId = jobData[7];
                    Vector<Object> row = new Vector<>();  //around here got some changes i forgot what
                    
                    // Filter by company ID
                    if (csvCompanyId.equals(companyId)) {
                        row.add(jobId); // Job ID
                        row.add(jobTitle); // Job Title
                        row.add("View"); // Placeholder for action

                        tableModel.addRow(row);
                        
                        // Debug: Confirm that a row has been added
                        System.out.println("Added row: " + row);
                    }
                } else {
                    // Debug: Print a warning if the data length is not sufficient
                    System.out.println("Warning: Skipping line due to insufficient data length");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CJobListGUI("Company_ID").setVisible(true));
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setBackground(Color.YELLOW);  
        setFont(new Font("Arial", Font.PLAIN, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "View" : value.toString());
        
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  

        return this;
    }
}

    // ButtonEditor is for handle button clicks in the JTable
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.YELLOW);  
            button.setFont(new Font("Arial", Font.PLAIN, 12)); 
            button.addActionListener(e -> {
                fireEditingStopped();
                int selectedRow = jobTable.getSelectedRow();
                String jobId = (String) jobTable.getValueAt(selectedRow, 0);
                String jobTitle = (String) jobTable.getValueAt(selectedRow, 1);
                dispose();
                new CApplicationsGUI(jobId, jobTitle, companyId,userId).setVisible(true);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "View" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    
    public class RemoveExpiredJob {
        private String csvFilePath;


        public  RemoveExpiredJob(String filePath) {
            this.csvFilePath = filePath;
        }

        public void saveJobPost(String jobId, String jTitle, String jDescription, String jType, String jLocation, String jSalary, String jDuration, String companyId) {
            File csvFile = new File(csvFilePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
                // Check if the file is empty or doesn't exist and write the header if needed
                if (!csvFile.exists() || csvFile.length() == 0) {
                    writer.write("Job ID,Job Title,Job Description,Job Type,Job Location,Job Salary,Expiration Date,Company_ID");
                    writer.newLine();
                }

                // Write the job post details
                writer.write(jobId + "," + jTitle + "," + jDescription + "," + jType + "," + jLocation + "," + jSalary + "," + jDuration+"," + companyId);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void checkAndDeleteExpiredPosts() {
            java.util.List<String> updatedPosts = new ArrayList<>();
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
                            //yyyy-MM-dd HH:mm:ss

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

