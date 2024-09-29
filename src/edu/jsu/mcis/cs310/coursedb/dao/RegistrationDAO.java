package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class RegistrationDAO {
    
    private final static String QUERY_CREATE = "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?) "; 
    
    private final DAOFactory daoFactory;
    
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public boolean create(int studentid, int termid, int crn) {
        
        boolean result = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
            // create query for duplicate check + store fields
            String QUERY_CHECK = "SELECT COUNT(*) FROM registration WHERE studentid = ? AND termid = ? AND crn = ?";
            ps = conn.prepareStatement(QUERY_CHECK);
            ps.setInt(1, studentid);
            ps.setInt(2, termid);
            ps.setInt(3, crn);
            
            // perform query / checking for duplicates and tracking
            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            // If no duplicate exists, insert the new registration
            if (count == 0) {
                ps.close(); // Close previous Prepared Statement for new one
                
                // load create query
                ps = conn.prepareStatement(QUERY_CREATE);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);
                
                // add those effected if there are any
                int rowsAffected = ps.executeUpdate();
                result = (rowsAffected > 0);
                
            } else {
                // Duplicate found, remove it if required
                System.out.println("Duplicate registration detected. Removing duplicate...");
                delete(studentid, termid, crn); // Assuming you have a delete method
            }
            
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }

    public boolean delete(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // Create Delete Query 
                String QUERY_DELETE = "DELETE FROM registration WHERE studentid = ? AND termid = ? AND crn = ?";
                
                // Load Delete statement into prepared statement
                // set fields
                ps = conn.prepareStatement(QUERY_DELETE);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);
                
                // execute the deletion process + track # deleted
                int rowsDeleted = ps.executeUpdate();
                result = (rowsDeleted > 0); // assertion for result
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    public boolean delete(int studentid, int termid) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // Same as other deletion but removed crn
                String QUERY_DELETE = "DELETE FROM registration WHERE studentid = ? AND termid = ?";
                ps = conn.prepareStatement(QUERY_DELETE);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                int rowsDeleted = ps.executeUpdate();
                result = (rowsDeleted > 0);
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }

    public String list(int studentid, int termid) {
        
        String result = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // Prepared Query statement
                String QUERY_LIST = "SELECT * FROM registration WHERE studentid = ? AND termid = ?";
                
                // Load and get fields
                ps = conn.prepareStatement(QUERY_LIST);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                rs = ps.executeQuery();
                
                // store metadata
                rsmd = rs.getMetaData();
                
                // return metadata as String
                result = rsmd.toString();
   
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
}
