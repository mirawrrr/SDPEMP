/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class EditingPost {
    private EditDeletePost editDeletePost;
    private String jobId;

    public EditingPost(String jobId) {
        this.editDeletePost = new EditDeletePost("JobPostInfo.csv");
        this.jobId = jobId;
    }
    
    public String[] getJobPost() {
        return editDeletePost.getJobPost(jobId);
    }

    public void updateJobPost(String[] updatedJobPost) {
        editDeletePost.updateJobPost(updatedJobPost);
}
    
    
}
