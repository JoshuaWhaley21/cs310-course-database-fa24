package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.*;
import com.github.cliftonlabs.json_simple.*;
import java.util.ArrayList;

public class DAOUtility {
    
    public static final int TERMID_FA24 = 1;
    
    public static String getResultSetAsJson(ResultSet rs) {
        
        JsonArray records = new JsonArray();
        
        try {
        
            if (rs != null) {
                
                // create metadata for storage
                ResultSetMetaData metadata = rs.getMetaData();
                int colnum = metadata.getColumnCount();
                
                // result set 
                while(rs.next()){
                    JsonObject JsonResult = new JsonObject();
                    
                    for(int i=1; i<=colnum; i++){
                        String colName = metadata.getColumnLabel(i);
                        Object colContent = rs.getObject(i);
                        
                        // time to string
                        if (colContent instanceof Time){
                            colContent = colContent.toString();
                        }
                        
                        // return info as JsonObject
                        JsonResult.put(colName, colContent);
                    }
                    
                    // add JsonObject to the array
                    records.add(JsonResult);
                    
                }

            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return Jsoner.serialize(records);
        
    }
    
}
