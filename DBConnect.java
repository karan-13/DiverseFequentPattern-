import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DBConnect {
    private Connection con;
    private PreparedStatement st;
    private ResultSet resultSet, rs;
    
    public DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/major","root","");
            //st= con.prepareStatement();
            
        }catch(Exception ex){
            System.out.println("Error: "+ex);
        }
    }
    
    public HashMap<Integer,String> getCode(ArrayList<ArrayList<Integer> > allPatterns){
        
        HashMap<Integer, String> listOfCodes = new HashMap<Integer, String>();//creates a Hashmap
	
        try{
           	
             for(ArrayList<Integer> singlePattern : allPatterns){
                  //System.out.println("sp: "+singlePattern);
                    for(int itemId : singlePattern){
                         int currentItemId = itemId;   
                         String code = "";

                            while(itemId != 0) {
                                    //System.out.println("itemId:"+ itemId);
                                // follows latest edit
                                String queryPos= "select pos from patterns where `id` = ?"; // gets pos
                                PreparedStatement ps1= con.prepareStatement(queryPos);
                                ps1.setInt(1,itemId);
                                rs = ps1.executeQuery();
                                    System.out.println("pos: "+rs.getInt("pos"));
                                 // end edit
                                    code = code+itemId;	//saves the pattern
                                    String queryParent = "select idparent from patterns where `id` = ?"; //gets parent id
                                    PreparedStatement ps = con.prepareStatement(queryParent);
                                    ps.setInt(1,itemId);
                                    resultSet = ps.executeQuery();
                                    while(resultSet.next()){
                                    itemId = resultSet.getInt("idparent");    
                                    }
                                    
                            }
                            //System.out.println("itemID: "+itemId+ "code: "+ code);
                            listOfCodes.put(currentItemId, code);
                            }
                } // outer for
        }catch(Exception ex){
            System.out.println(ex);
        }
        return listOfCodes;
    }
    
    public void diverseFP( ArrayList< ArrayList< Integer> > allPatterns, HashMap<Integer, String > listofCodes){
        
       // HashMap< ArrayList<Integer>, Double > DR= new HashMap< ArrayList<Integer>, Double >();
        
        int height=5; // height pata honi chaie !! how? may be max length of the code!?
        
        double PLF[]= new double[height];
        
         for(int level=height-1 ; level >=1 ; level--)
        {
       	 	PLF[level]=(2.0*(height-level))/((height-1)*height);
        }
         // Now for every Pattern ....
         
         for(ArrayList < Integer> singlePattern : allPatterns){
             // intialise DR to 0;
             //DR.put(singlePattern, 0.0);
             double dr=0.0;
             int level = height-1;
             int GFPa=singlePattern.size();
             
             while(true){
                 int GFPb = GFPgen(singlePattern,listofCodes,level,height);
                 if(GFPb==1)
                     break;
                 
                 double MF=(GFPb-1)/(GFPa-1);
                 dr=dr+(MF*PLF[level]);
                 level=level-1;
                 GFPa=GFPb;
                 } // end of while
           //  DR.put(singlePattern, 0.0);
             System.out.println("Enter minDiv");
             // manually taking right now !!
             Double minDiv=2.0;
             if(dr> minDiv)
                 System.out.println("pattern: "+singlePattern+"  DR: "+dr);
         }
    } 
    
 public int GFPgen(ArrayList<Integer> singlePattern, HashMap<Integer, String> listofCodes ,int level,int height){
       
       ArrayList<String> GFP = new ArrayList<String>();
       for(Integer itemId : singlePattern){
           String parent="";
           String code=(String)listofCodes.get(itemId);
           //int length=
           for(int j=0;j<level;j++){
             parent+=code.charAt(j);
       }
         for(int k=1; k <=(height-level);k++)  
         parent+='*';
         
         if(GFP.indexOf(parent)== -1)
         GFP.add(parent);
   }
       return  GFP.size();
}
}