import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DBConnect {
    private Connection con;
    private PreparedStatement st;
    private ResultSet rs;
    
    public DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/major","root","");
           
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }
    
    public HashMap<Integer,String> getCode(ArrayList<ArrayList<Integer>> allPatterns){
        
        HashMap<Integer, String> listOfCodes = new HashMap<Integer, String>();//creates a Hashmap
	
        try{
       	
         for(ArrayList<Integer> singlePattern : allPatterns){
            for(int itemId : singlePattern){ 
	            String code = "";
	            int currentItemId = itemId;
	            int pos = 0;
                while(itemId != 0) {
	                String query= "select * from patterns where `id` = ?"; 
	                st= con.prepareStatement(query);
	                st.setInt(1,itemId);
	                rs = st.executeQuery();
	                while(rs.next()) {
		                pos = rs.getInt("pos");
		                //Now item Id becomes the id of the parent, so
		                itemId = rs.getInt("idparent");
	                }
                    code = pos+code;	//saves the pattern
                }
                
                listOfCodes.put(currentItemId, code);
                }
            } // outer for
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        System.out.println(listOfCodes);
        return listOfCodes;
    }
    
    public void diverseFP( ArrayList< ArrayList< Integer> > allPatterns, HashMap<Integer, String > listofCodes){
        
       // HashMap< ArrayList<Integer>, Double > DR= new HashMap< ArrayList<Integer>, Double >();
        
        int height = 3; // height pata honi chaie !! how? may be max length of the code!?
        
        double PLF[]= new double[height];
        
         for(int level = height-1; level >= 1; level--)
         	PLF[level]=(2.0*(height-level))/((height-1)*height);
        
         System.out.println("allPatterns: "+ allPatterns);
         // Now for every Pattern ....
         //System.out.println("Enter minDiv");
         for(ArrayList<Integer> singlePattern : allPatterns){
             // intialise DR to 0;
             //DR.put(singlePattern, 0.0);
             System.out.println("pattern: "+ singlePattern);
             double dr = 0.0;
             int level = height-1;
             int GFPa = singlePattern.size();
             
             while(true){
                 int GFPb = GFPgen(singlePattern,listofCodes,level,height);
                 System.out.println("GFPb for level "+level+" is "+GFPb);
                 if(GFPb == 1)
                     break;
                 
                 double MF = (GFPb-1)/(GFPa-1);
                 dr = dr+(MF*PLF[level]);
                 level = level-1;
                 GFPa = GFPb;
                 } // end of while
           //  DR.put(singlePattern, 0.0);
             
             // manually taking right now !!
             Double minDiv = 0.0;
             if(dr >= minDiv)
                 System.out.println("pattern: "+singlePattern+"  DR: "+dr);
         }
    } 
    
 public int GFPgen(ArrayList<Integer> singlePattern, HashMap<Integer, String> listofCodes, int level, int height) {
       
       ArrayList<String> GFP = new ArrayList<String>();
       for(Integer itemId : singlePattern){
           String parent="";
           String code=(String)listofCodes.get(itemId);
          // System.out.println(" GFPgen fxn code: "+code);
           //int length=
           for(int j=0; j <= level; j++){
             parent+=code.charAt(j);
       }
         for(int k=1; k <= (height-level); k++)  
         parent+='*';
         
         if(GFP.indexOf(parent)== -1)
         GFP.add(parent);
   }
       //System.out.println("GFPb: "+GFP);
       //System.out.println("GFPb size(): "+GFP.size());
       return  GFP.size();
}
}