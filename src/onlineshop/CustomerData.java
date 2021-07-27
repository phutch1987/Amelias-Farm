/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshop;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author hutch
 */
public class CustomerData {
    
    private Data data = new Data();
    
    private String dba;
    
    private PreparedStatement ps;
    private ResultSet rs;
    private DatabaseMetaData dbmd;
    
    /**
     * creates the customer database
     */
    
    public CustomerData(){
        
        try{
               
            data.DbConCustomer();
            dba = "CREATE TABLE IF NOT EXISTS customers("
                    + "ID INT (6),"
                    + "Name STRING (20),"
                    + "Email STRING (50),"
                    + "Username STRING (10),"
                    + "Salt BLOB (20),"
                    + "Hash STRING (10));";
            
            ps = data.DbConCustomer().prepareStatement(dba);
            
            ps.executeUpdate();
        
        }catch (SQLException ex) {
       
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            try {
                
                data.DbConCustomer().close();
                
            } catch (SQLException ex) {
                
                Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    /**
     * adds a new customer to the database
     * @param name
     * @param eMail
     * @param userName
     * @param passWord 
     * @throws java.sql.SQLException 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws java.security.NoSuchProviderException 
     */
    
    public synchronized void addCustomerToDatabase(String name, String eMail, String userName, char[] passWord) throws SQLException, NoSuchAlgorithmException, NoSuchProviderException{
        
        int id = createID();
        String securePassword = "";
        byte[] salt = getSalt();
        String hash = generateSecurePassword(Arrays.toString(passWord), salt);
        
        try{
            
            data.DbConCustomer();
            dba = "INSERT INTO customers (id, name, email, username, salt, hash) VALUES (?,?,?,?,?,?)";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, eMail);
            ps.setString(4, userName);
            ps.setBytes(5, salt);
            ps.setString(6, hash);
            
            ps.executeUpdate();
            
            createCustomerTable(userName);
            
            JOptionPane.showMessageDialog(null, "Welcome To Amelias Farm " + userName);
            
        } catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            data.DbConCustomer().close();
        }        
    }
    
     /**
     * generates secure password
     * @param password
     * @param salt
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     */
    
    public synchronized String generateSecurePassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        
        String securePassword = null;
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        md.update(salt);
        
        byte[] bytes = md.digest(password.getBytes());
        
        StringBuilder sb = new StringBuilder();
        
        for(int i =0; i < bytes.length; i++){
            
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            
        }
        
        securePassword = sb.toString();
        
        
        return securePassword;
    }
    
    /**
     * generates salt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException 
     */
    
    public synchronized byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        
        byte[] Salt = new byte[16];
        
        sr.nextBytes(Salt);
        
        return Salt;
        
    }
    
    /**
     * checks login details for login page
     * @param username
     * @param password
     * @return
     * @throws SQLException
     * @throws NoSuchAlgorithmException 
     */
    
    public synchronized boolean getLogin(String username, char[]password) throws SQLException, NoSuchAlgorithmException{
        
        boolean admit = false;
        
        try{
            
            data.DbConCustomer();
            dba = "SELECT * FROM customers WHERE username = ?";
            ps = data.DbConCustomer().prepareStatement(dba);
            ps.setString(1, username);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                if(username.matches(rs.getString("username"))){
                    
                    byte[] salt = rs.getBytes("salt");
                    
                    String securePassword = generateSecurePassword(Arrays.toString(password), salt);
                    
                    if(securePassword.matches(rs.getString("hash"))){
                        
                        admit = true;
                    }   
                }
            }           
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        } 
        
        return admit;
    }
    
    /**
     * creates the id number
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public synchronized int createID() throws SQLException{
        
        String identity = "";       
        boolean unique = false;
        
        while(unique == false){
            
            for(int i = 0; i < 6; i++){

                int digit = (int) (Math.random() * 10);
               identity += digit;
            }
            
            unique = checkID(Integer.parseInt(identity));
        }
            
        return Integer.parseInt(identity);
    }
    
    /**
     * creates the table to store the customers last order
     * @param username
     * @throws SQLException 
     */
    
     public void createCustomerTable(String username) throws SQLException{
         
         try{
             
             data.DbConCustomer();
             dba ="CREATE TABLE IF NOT EXISTS '" + username +"'("
                     + "Code STRING (10),"
                     + "Name STRING (20),"
                     + "VEG STRING (2),"
                     + "Quantity INT (5),"
                     + "Price DECIMAL (10),"
                     + "Total DECIMAL (10));";
             
             ps = data.DbConCustomer().prepareStatement(dba);
             
             ps.executeUpdate();
             
         }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{

            data.DbConCustomer().close();
        }
     }
     
     /**
      * drops customer last orders
      * @param username 
     * @throws java.sql.SQLException 
      */
     
     public synchronized void updateCustomerOrder(String username) throws SQLException{
         
         try{
             
             data.DbConCustomer();
             dba = "DROP TABLE '" + username + "'";
             ps = data.DbConCustomer().prepareStatement(dba);
             
             ps.executeUpdate();
             
         }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{

            data.DbConCustomer().close();
        }
         
     }
     
     /**
      * adds the customers last order to their table
      * @param items
      * @param username
      * @throws SQLException 
      */
     
     public synchronized void saveCustomerOrder(Object[][] items, String username) throws SQLException{
         
         updateCustomerOrder(username);
         createCustomerTable(username);
         
         for(Object[] i: items){
                
            try{
                  
                data.DbConCustomer();
                dba = "INSERT INTO '" + username + "'(code, name, veg, quantity, price, total) VALUES(?,?,?,?,?,?)";
                ps = data.DbConCustomer().prepareStatement(dba);

                ps.setObject(1, i[0]);
                ps.setObject(2, i[1]);
                ps.setObject(3, i[2]);
                ps.setObject(4, i[3]);
                ps.setObject(5, i[4]);
                ps.setObject(6, i[5]);

                ps.executeUpdate();
                  
            }catch (SQLException ex) {

               Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);

            }finally{

               data.DbConCustomer().close();
            }  
        }   
    }
     
     /**
      * gets the number of items from the customers last orders
      * @param username
      * @return
      * @throws SQLException 
      */
     
    public synchronized int getNumOfItemsLastOrder(String username) throws SQLException{
        
        int items = 0;
        
        try{
            
            data.DbConCustomer();
            dba = "SELECT COUNT (*) AS items FROM '" + username + "'";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                items = rs.getInt("items");
              
            }
            
        }catch (SQLException ex) {

            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);

        }finally{
            
            rs.close();
           data.DbConCustomer().close();
        } 
        
       return items; 
    }
     
    /**
     * gets all the items from the last orders of the customer
     * @param username
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public synchronized Object[][] getCustomerOrder(String username) throws SQLException{
         
        int item = 0; 
        Object[][] items = new Object[getNumOfItemsLastOrder(username)][6];
         
        try{
             
            data.DbConCustomer();
            dba = "SELECT * FROM '" + username + "' ";
            ps = data.DbConCustomer().prepareStatement(dba);

            rs = ps.executeQuery();
            
            while(rs.next()){
                
               items[item][0] = rs.getObject("code");
               items[item][1] = rs.getObject("name");
               items[item][2] = rs.getObject("veg");
               items[item][3] = rs.getObject("quantity");
               items[item][4] = rs.getObject("price");
               items[item][5] = rs.getObject("total");
               
               item ++;
                
            }
             
        }catch (SQLException ex) {

            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);

        }finally{
            
            rs.close();
           data.DbConCustomer().close();
        } 
        
        return items; 
    }        
    
    
    /**
     * gets all customers ids
     * @return 
     * @throws java.sql.SQLException 
     */
      
    public synchronized int[] getIds() throws SQLException{
        
        int[] ids = new int[getNumberOfCustomers()];
        int place = 0;
        
        try{
            
            data.DbConCustomer();
            dba = "SELECT id FROM customers";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                ids[place++] = rs.getInt("id");
                
            }
            
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        }     
        
        return ids;
    }
    
    /**
     * checks if the id has already been created
     * @param id
     * @return
     * @throws SQLException 
     */
    
    public boolean checkID(int id) throws SQLException{
   
        boolean unique = true;
        
        try{
            
            data.DbConCustomer();
            dba = "SELECT id FROM customers";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                if(id == rs.getInt("id")){
                    
                    unique = false;
                }
                
            }
            
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        }     
        
        return unique;
    }
    
    /**
     * gets the number of customers in the database
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public int getNumberOfCustomers() throws SQLException{
        
        int number = 0;
        
        try{
            
            data.DbCon();
            dba = "SELECT COUNT(*) AS number FROM customers";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
               number = rs.getInt("number");
                
            }
            
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        } 
        
        return number;
    }
    
    /**
     * retrieves all customers details form database
     * @return
     * @throws SQLException 
     */
    
    public Object[][] getCustomers() throws SQLException{
        
        Object[][] customers = new Object[getNumberOfCustomers()][6];
        int num = 0;
        
        try{
            
            data.DbConCustomer();
            dba = "SELECT * FROM customers ORDER BY name ASC";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                customers[num][0] = rs.getObject("id");
                customers[num][1] = rs.getObject("name");
                customers[num][2] = rs.getObject("email");
                customers[num][3] = rs.getObject("username");
                customers[num][4] = rs.getObject("salt");
                customers[num][5] = rs.getObject("hash");
                
                num++;
            }
            
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        } 
        
        return customers;
    }
    
    /**
     * checks if username is already taken
     * @param username
     * @return
     * @throws SQLException 
     */
    
    public synchronized boolean checkUsername(String username) throws SQLException{
        
        boolean taken = false;
        
        try{
            data.DbConCustomer();
            dba = "SELECT username FROM customers";
            ps = data.DbConCustomer().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                if(username.matches(rs.getString("username"))){
                    
                    taken = true;     
                    break;
                    
                }        
            }
            
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        }
        
        return taken;
    }
    
    /**
     * gets the details from the logged in customer
     * @param userName
     * @param loggedIn
     * @return
     * @throws SQLException 
     */
    
    public Object[] getLoggedInCustomer(String userName, boolean loggedIn) throws SQLException{
        
        Object[] customer = new Object[6];
        
        if(loggedIn){
            
            try{
                
                data.DbConCustomer();
                dba = "SELECT * FROM customers WHERE username = ?";
                ps = data.DbConCustomer().prepareStatement(dba);
                ps.setString(1, userName);
                
                rs = ps.executeQuery();
                
                while(rs.next()){
                    
                    customer = new Object[]{rs.getObject("id"),rs.getObject("name"),rs.getObject("email"),rs.getObject("username")};
                    
                }
                
            }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            data.DbConCustomer().close();
        }
            
            return customer;
    }
        
        return customer;
    }
}
