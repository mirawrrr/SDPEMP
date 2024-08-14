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

public class CSVValidator {
    
    public static void validateCSVFile(String validate){
        String line;
        String csvSplitBy = ",";
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            while ((line = br.readLine()) !=null){
                String[] details = line.split(csvSplitBy);
                if (details.length >=11){
                    System.out.println("CompanyID: " + details[0] + ", CompanyName: " + details[1] +
                                       ", CEmail: " + details[2] + ", CPassword: " + details[3] +
                            ", CPhoneno: "+ details[4] + ", CDescription: " + details[5] + ", CLocation: " + details[6] +
                            ", V_Document" + details[7] + ", CProfilePic: " + details[8] +", CStatus: "+ details[9]+ ", Job_ID: " + details[10]);
                } else {
                    System.out.println("Row with sufficient columns: "+ line);
                }
        }
    } catch (IOException e){
        e.printStackTrace();
    }
    
}
}
