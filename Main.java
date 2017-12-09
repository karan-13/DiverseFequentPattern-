import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
     
    public static void main(String[] args) {
        
        File file = new File("src/output.txt");
        StringBuffer stringBuffer = new StringBuffer(); 
        String line;
        ArrayList<ArrayList<Integer> > allPatterns = new ArrayList< ArrayList<Integer> >();
        try {  
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                  ArrayList<Integer> singlePattern = new ArrayList<Integer>();
                  String itemId="";
                  //System.out.println(line.length());
                  for(int i = 0; i < line.length(); i++){
                      if(line.charAt(i) != ' ')
                          itemId += line.charAt(i);
                      else if(line.charAt(i)== ' ' && itemId!= "") {
                          singlePattern.add(Integer.parseInt(itemId));
                          itemId = "";	
                      }
                      }
                  allPatterns.add(singlePattern);
           } // end while
        
      fileReader.close();
    } catch (IOException e) {
            e.printStackTrace();
                           }
  DBConnect connect = new DBConnect();
       System.out.println(connect.getCode(allPatterns));
    }
}
