/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.util.EventObject;
import java.io.FileWriter;
import java.util.Vector;
import java.io.File;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.util.Set;
import java.util.HashSet;

public class CApplicationsGUI extends JFrame {
    private JTable applicationTable;
    private DefaultTableModel tableModel;
    private CApplications cApplications;
    private String jobId;
    private String jobTitle;
    private String companyId;
    private String userId;  // New field to store the specific user ID
    //ltr rmb to change the file path
    //private static final String RESUME_FOLDER_PATH = "C:\\Users\\4hana\\OneDrive - Asia Pacific University\\( SDP ) Software Development Project\\Assignment Code\\SDPAssignment\\Resume\\";
    private static final String RESUME_FOLDER_PATH = "C:\\Users\\user\\OneDrive - Asia Pacific University\\work\\SDP2\\RESUME";
    
    public CApplicationsGUI(String jobId, String jobTitle, String companyId, String userId) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyId = companyId;
        setTitle("Applicants for " + jobTitle);
        setSize(1024, 576);  
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        cApplications = new CApplications("JobApplication.csv");
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Job ID: " + jobId + " - " + jobTitle, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Adding space between the main header and the table (not working dk y)
        panel.add(Box.createVerticalStrut(15), BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new Object[]{"User ID", "Resume", "Status", "Action"}, 0);
        applicationTable = new JTable(tableModel){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row == 1) { // User ID and Status columns
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        
        applicationTable.setRowHeight(40); 
        
       
        loadApplications();
        
        TableColumnModel columnModel = applicationTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);  // User ID column
        columnModel.getColumn(1).setPreferredWidth(500);  // Resume column
        columnModel.getColumn(2).setPreferredWidth(150);  // Status column
        columnModel.getColumn(3).setPreferredWidth(224);  // Action column
        
        // Set the table header background to grey
        applicationTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        applicationTable.getTableHeader().setOpaque(true);
        
        //for pdf thing
        applicationTable.getColumn("Resume").setCellRenderer(new ResumeRenderer());
        applicationTable.getColumn("Resume").setCellEditor(new ResumeEditor(new JCheckBox()));
        
        
        applicationTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        applicationTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));       

        JScrollPane scrollPane = new JScrollPane(applicationTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.YELLOW);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new CJobListGUI(companyId).setVisible(true); // Reopen JobListGUI page
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }
    
    //original     //for the pdf version 
        private void loadApplications() {
        tableModel.setRowCount(0);
        
        // Declare the uniqueUserIds set
        Set<String> uniqueUserIds = new HashSet<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("JobApplication.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] applicationData = line.split(",");
                
                //added
                for (int i = 0; i < applicationData.length; i++) {
                applicationData[i] = applicationData[i].trim();
            }
                
                System.out.println("Loaded line: " + line);
                System.out.println("Application Data: " + String.join(", ", applicationData));
            
            
                if (applicationData.length >= 5 && applicationData[1].equals(jobId)) {
                    uniqueUserIds.add(applicationData[2]); // Add User ID to set
                   
                    Vector<Object> row = new Vector<>();
                    row.add(applicationData[2]); // User ID
                                        
                    // Check if pdf exists in specified folder
                    String resumeFileName = applicationData[2] + ".pdf";
                    String resumePath = RESUME_FOLDER_PATH + resumeFileName;
                    System.out.println("Looking for resume at: " + resumePath); // Debugging statement
                    
                    File resumeFile = new File(resumePath);
                    if (resumeFile.exists()) {
                        row.add(resumeFileName);
                    } else {
                        row.add("Resume not found");
                    } 

                    row.add(applicationData[4]); // Status
                    row.add("Status"); // Placeholder for the accept/reject button

                    tableModel.addRow(row);
                }
            }
            // Print the count of unique User IDs associated with the job ID
            System.out.println("Total number of unique User IDs for Job ID " + jobId + ": " + uniqueUserIds.size());
        } catch (IOException e) {
            System.err.println("Error reading JobApplication.csv: " + e.getMessage());
            e.printStackTrace();
        }
    }
        

             
    
    private void updateApplicationStatus(String userId, String status) {
         
        try (BufferedReader br = new BufferedReader(new FileReader("JobApplication.csv"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            boolean updated = false;
            while ((line = br.readLine()) != null) {
                String[] applicationData = line.split(",");
                // Debugging print statements
                System.out.println("Checking User ID: " + applicationData[2]);
                System.out.println("Comparing with: " + userId);
                
                if (applicationData[2].equals(userId)) {
                    //debug one line below
                    System.out.println("Match found. Updating status to " + status);
                    applicationData[4] = status;
                    line = String.join(",", applicationData);
                    updated = true;
                }
                fileContent.append(line).append("\n");
            }           

            // Write updated content back to the file
            try (FileWriter fw = new FileWriter("JobApplication.csv")) {
                fw.write(fileContent.toString());
            }
            
            if (updated) {
                JOptionPane.showMessageDialog(null, "Status updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update status.");
            }

            loadApplications(); // Refresh table to reflect changes

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating status.");
        }
    }  
    
    // Renderer for resume column (pdf thing)
    class ResumeRenderer extends JLabel implements TableCellRenderer {
        public ResumeRenderer() {
            setOpaque(true);
        }
        
         @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 1) { // Only apply to the Resume column
                setText((value == null) ? "" : value.toString());
                setForeground(Color.BLUE);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setText((value == null) ? "" : value.toString());
                setForeground(Color.BLACK); // Set to default color for other columns
                setCursor(Cursor.getDefaultCursor());
            }
            return this;
        }
    }
    
    // Editor for resume column (pdf thing)
    class ResumeEditor extends DefaultCellEditor {
        private JLabel label;
        private String resumeFilePath;

        public ResumeEditor(JCheckBox checkBox) {
            super(checkBox);
            label = new JLabel();
            label.setOpaque(true);
            label.setForeground(Color.BLUE);
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (column == 1) { // Only apply to the resume column
                String userId = (String) table.getValueAt(row, 0); // Get User ID from first column
                resumeFilePath = RESUME_FOLDER_PATH + userId + ".pdf";
                label.setText((value == null) ? "" : value.toString());
                return label;
            }
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            return label.getText();
        }

        @Override
        public boolean stopCellEditing() {
            if (!label.getText().equals("Resume not found")) {
                try {
                    File pdfFile = new File(resumeFilePath);
                    if (pdfFile.exists()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        JOptionPane.showMessageDialog(null, "Resume not found.");
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to open the resume: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return super.stopCellEditing();
        }
    }

    
    // Renderer is used to change code to interactive button 
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton acceptButton = new JButton("Accept");
        private JButton rejectButton = new JButton("Reject");

        public ButtonRenderer() {
            setOpaque(true);
            setLayout(new FlowLayout());
            acceptButton.setBackground(Color.YELLOW);
            rejectButton.setBackground(Color.YELLOW);
            setLayout(new GridLayout(1, 2, 10, 10)); //added
            add(acceptButton);
            add(rejectButton);
            
            acceptButton.setForeground(Color.BLACK); //added
            acceptButton.setBackground(Color.GREEN); 
            rejectButton.setForeground(Color.BLACK);
            rejectButton.setBackground(Color.RED);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel = new JPanel();
        private JButton acceptButton = new JButton("Accept");
        private JButton rejectButton = new JButton("Reject");
        private String userId; //added
        
         public ButtonEditor(JCheckBox checkBox) {
            //panel.setLayout(new GridLayout(1, 2, 10, 10));
            panel.add(acceptButton);
            panel.add(rejectButton);
            
            acceptButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateApplicationStatus(userId, "Accepted");
                    fireEditingStopped();
                }
            });
            
            rejectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateApplicationStatus(userId, "Rejected");
                    fireEditingStopped();
                }
            });
        }

        /*public ButtonEditor(JCheckBox checkBox) {
            panel.setLayout(new FlowLayout());
            acceptButton.setBackground(Color.YELLOW);
            rejectButton.setBackground(Color.YELLOW);
            panel.add(acceptButton);
            panel.add(rejectButton);

            acceptButton.addActionListener(e -> {
                fireEditingStopped();
                int row = applicationTable.getSelectedRow();
                String applicationId = (String) applicationTable.getValueAt(row, 0);
                updateApplicationStatus(applicationId, "Accepted");
            });

            rejectButton.addActionListener(e -> {
                fireEditingStopped();
                int row = applicationTable.getSelectedRow();
                String applicationId = (String) applicationTable.getValueAt(row, 0);
                updateApplicationStatus(applicationId, "Rejected");
            });
        }*/

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            userId = (String) table.getValueAt(row, 0); //added
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
                    //panel;
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }
    }

     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CApplicationsGUI("jobId", "jobTitle", "companyId", "userId").setVisible(true));
    }
}




