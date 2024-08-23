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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.EventObject;
import javax.swing.table.JTableHeader;

public class EditDeletePostGUI extends JFrame{
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private UploadPost uploadPost;
    private String profilePicPath;
    private String currentCompanyId;
    private String companyId;

    public EditDeletePostGUI(String companyId, String profilePicPath) {
        this.currentCompanyId = companyId; 
        this.companyId = companyId;
        this.profilePicPath = profilePicPath; 
        setTitle("Edit / Delete Job Posts");
        setSize(1024, 576);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        uploadPost = new UploadPost("JobPostInfo.csv");
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Edit / Delete Job Posts", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Main header font size
        titlePanel.add(titleLabel);
        getContentPane().add(titlePanel, BorderLayout.NORTH);
        
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Add space between the main header and the table
        panel.add(Box.createVerticalStrut(15), BorderLayout.NORTH); 
        
        tableModel = new DefaultTableModel(new Object[]{"Job ID", "Title", "Edit or Delete"}, 0);
        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(40);
        loadJobPosts();
        
        // column header font size
        JTableHeader header = jobTable.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14)); 

        // table content font size
        jobTable.setFont(new Font("Arial", Font.PLAIN, 12)); 
        
        TableColumnModel columnModel = jobTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);  // Job ID column width
        columnModel.getColumn(1).setPreferredWidth(400);  // Title column width
        columnModel.getColumn(2).setPreferredWidth(300);  // Button column width
        
        // Set the table header background to grey
        jobTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
        jobTable.getTableHeader().setOpaque(true);

        jobTable.getColumn("Edit or Delete").setCellRenderer(new ButtonRenderer());
        jobTable.getColumn("Edit or Delete").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Homepage");
        backButton.setBackground(Color.YELLOW); 
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        dispose();
        if (profilePicPath != null && !profilePicPath.isEmpty() && currentCompanyId != null && !currentCompanyId.isEmpty()) {
            EMPProfile empProfile = new EMPProfile(profilePicPath,currentCompanyId);
            empProfile.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Profile picture path or Company ID is not set.");
        }
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadJobPosts() {
        tableModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("JobPostInfo.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if(isFirstLine){
                    isFirstLine = false;
                    continue;
                }
                String[] jobData = line.split(",");
                if (jobData.length >= 3 && jobData[7].trim().equals(currentCompanyId.trim())) {
                    Vector<Object> row = new Vector<>();
                    row.add(jobData[0]);
                    row.add(jobData[1]);

                    JButton editButton = new JButton("Edit");
                    editButton.setBackground(Color.YELLOW); 
                    editButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                            new EditingPostGUI(uploadPost, jobData[0]).setVisible(true);
                        }
                    });

                    JButton deleteButton = new JButton("Delete");
                    deleteButton.setBackground(Color.YELLOW); 
                    deleteButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            uploadPost.deleteJobPost(jobData[0]);
                            loadJobPosts();
                        }
                    });

                    JPanel panel = new JPanel();
                    panel.add(editButton);
                    panel.add(deleteButton);
                    row.add(panel);

                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditDeletePostGUI("companyid","profile.jpg").setVisible(true));
    }
    
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton = new JButton("Edit");
        private JButton deleteButton = new JButton("Delete");

        public ButtonRenderer() {
            setOpaque(true);
            setLayout(new FlowLayout());
            editButton.setBackground(Color.YELLOW); 
            deleteButton.setBackground(Color.YELLOW); 
            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel = new JPanel();
        private JButton editButton = new JButton("Edit");
        private JButton deleteButton = new JButton("Delete");

        public ButtonEditor(JCheckBox checkBox) {
            panel.setLayout(new FlowLayout());
            editButton.setBackground(Color.YELLOW); 
            deleteButton.setBackground(Color.YELLOW); 
            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int row = jobTable.getSelectedRow();
                    String jobId = (String) jobTable.getValueAt(row, 0);
                    // Initialize UploadPost and pass it to EditingPostGUI
                    UploadPost uploadPostInstance = new UploadPost("JobPostInfo.csv");
                    EditingPostGUI editingPostGUI = new EditingPostGUI(uploadPostInstance, jobId); 
                    //dispose();
                    new EditingPostGUI(uploadPost, jobId).setVisible(true);
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int selectedRow = jobTable.getSelectedRow();
                    String jobId = (String) jobTable.getValueAt(selectedRow, 0);
                    uploadPost.deleteJobPost(jobId);
                    loadJobPosts(); // Reload data after deletion
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return panel;
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
      }
}
}
