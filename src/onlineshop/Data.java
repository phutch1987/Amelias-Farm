/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshop;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author hutch
 */
public class Data {
       
    private Connection con; 
    private String URL;
    private String dba;
    private PreparedStatement ps;
    private ResultSet rs;
    private DatabaseMetaData dbmd;

    public Connection DbCon() throws SQLException{
                
        try {

            URL = "jdbc:sqlite:C:\\AmeliasFarmData\\Products.db";
            con = DriverManager.getConnection(URL);
            
            
        }catch(SQLException e) {
            
           JOptionPane.showInputDialog(e.getMessage());
        }
        
        return con;
    }
    
    /**
     * creates customer Database
     * @return
     * @throws SQLException 
     */
    
    public Connection DbConCustomer() throws SQLException{
                
        try {

            URL = "jdbc:sqlite:C:\\AmeliasFarmData\\Customer.db";
            con = DriverManager.getConnection(URL);
            
            
        }catch(SQLException e) {
            
           JOptionPane.showInputDialog(e.getMessage());
        }
        
        return con;
    }
    
    /**
     * creates admin Database
     * @return
     * @throws SQLException 
     */
    
    public Connection DbConAdmin() throws SQLException{
                
        try {

            URL = "jdbc:sqlite:C:\\AmeliasFarmData\\Admin.db";
            con = DriverManager.getConnection(URL);
  
        }catch(SQLException e) {
            
           JOptionPane.showInputDialog(e.getMessage());
        }
        
        return con;
    }
    
    /**
    * creates sales Database
    * @return
    * @throws SQLException 
    */
    
    public Connection DbConSales() throws SQLException{
                
        try {

            URL = "jdbc:sqlite:C:\\AmeliasFarmData\\Sales.db";
            con = DriverManager.getConnection(URL);
  
        }catch(SQLException e) {
            
           JOptionPane.showInputDialog(e.getMessage());
        }
        
        return con;
    }
    
    /**
     * creates admin table
     * @throws SQLException 
     */
    
    public void createAdminTable() throws SQLException{
        
        try{
            
            DbConAdmin();
            dba ="CREATE TABLE IF NOT EXISTS admin("
                    + "userName STRING (10),"
                    + "Salt BLOB (16),"
                    + "Hash STRING (20));";
            
            ps = DbConAdmin().prepareStatement(dba);
            ps.executeUpdate();
            
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbCon().close();
        }        
    }
    
    /**
     * adds an admin to the database
     * @param userName
     * @param password
     * @throws SQLException 
     */
    
    public void addAdmin(String userName, char[] password) throws SQLException{
        
        CustomerData customerData = new CustomerData();
        createAdminTable();
        
        try {
            
            byte[] Salt = customerData.getSalt();
            String securePassword = customerData.generateSecurePassword(Arrays.toString(password), Salt);
            
            DbConAdmin();
            dba = "INSERT INTO admin (username, salt, hash)  VALUES (?,?,?)";
            ps = DbConAdmin().prepareStatement(dba);
            
            ps.setString(1, userName);
            ps.setBytes(2, Salt);
            ps.setString(3, securePassword);
            
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, userName + " Added To Admin Database");
              
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    /**
     * checks the admin log in details
     * @param userName
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SQLException 
     */
    
    public synchronized boolean getAdminLogin(String userName, char[]password) throws NoSuchAlgorithmException, SQLException{
        
        CustomerData customerData = new CustomerData();
        boolean admit = false;
        
        try{
            
            DbConAdmin();
            dba = "SELECT * FROM admin WHERE username = ?";
            ps = DbConAdmin().prepareStatement(dba);
            ps.setString(1, userName);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                if(userName.matches(rs.getString("username"))){
                    
                    byte[] salt = rs.getBytes("salt");
                    
                    String securePassword = customerData.generateSecurePassword(Arrays.toString(password), salt);
                    
                    if(securePassword.matches(rs.getString("hash"))){
                        
                        admit = true;
                        break;
                    }      
                }
            }      
        }catch (SQLException ex) {
            
            Logger.getLogger(CustomerData.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbConAdmin().close();
        } 
        
        return admit;    
    }
    
    /**
     * creates the sales table
     * @param ld
     * @throws SQLException 
     */
    
    public void createSalesTable(LocalDate ld) throws SQLException{
               
        String date = ld.toString();
        
        try {

        DbConSales();

        dba = "CREATE TABLE IF NOT EXISTS '" + ld + "'("
                + "Category STRING (10),"
                + "code STRING (20),"
                + "name STRING (10),"
                + "UnitsSold INT (5),"
                + "Price DECIMAL (5),"
                + "Total STRING (3));";

        ps = DbConSales().prepareStatement(dba);

        ps.executeUpdate();
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbConSales().close();
        }
    }
    
    /**
     * adds items sold into the days sales 
     * @param sales
     * @param ld
     * @throws SQLException 
     */
    
    public synchronized void addToSales(Object[][] sales, LocalDate ld) throws SQLException{
      
        createSalesTable(ld);
        String date = ld.toString();
        
        try{
            
           DbConSales();
           
            for(Object[] s : sales) {
                
                String category = findProductCategory(String.valueOf(s[0]));

                dba = "INSERT INTO '" + date + "' (Category, code, name, unitsSold, price, total) VALUES(?,?,?,?,?,?)";
                ps = DbConSales().prepareStatement(dba);
             
                ps.setObject(1, category);
                ps.setObject(2, s[0]);
                ps.setObject(3, s[1]);
                ps.setObject(4, s[3]);
                ps.setObject(5, s[4]);
                ps.setObject(6, s[5]);
                
                ps.executeUpdate();
                
            }
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbConSales().close();
        } 
    }
    
    /**
     * gets number of rows from sales table
     * @param date
     * @return
     * @throws SQLException 
     */
    
    public synchronized int getNumberOfSales(String date) throws SQLException{
        
        int number = 0;
        
        try{
            DbConSales();
            dba = "SELECT COUNT(*) AS number FROM '" + date + "'";
            ps = DbConSales().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                number = rs.getInt("number");
            }
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbConSales().close();
        }
        
        return number;
    }
    
    /**
     * gets all sales by date
     * @param date
     * @return
     * @throws SQLException 
     */
    
    public synchronized Object[][] getSalesByDate(String date) throws SQLException{
        
        Object[][] sales = new Object[getNumberOfSales(date)][6];
        int row = 0;
        
        try{
            
            DbConSales();
            dba = "SELECT * FROM '" + date + "'";
            ps = DbConSales().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                sales[row][0] = rs.getObject("Category");
                sales[row][1] = rs.getObject("name");
                sales[row][2] = rs.getObject("code");
                sales[row][3] = rs.getObject("price");
                sales[row][4] = rs.getObject("unitsSold");
                sales[row][5] = rs.getObject("total");
                
                row++;
                
            }
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            rs.close();
            DbConSales().close();
        }
        
        return sales;
    }
 
    /**
     * gets all sales by date and category
     * @param date
     * @param category
     * @return
     * @throws SQLException 
     */
    
    public synchronized Object[][] getSalesByDateAndCategory(String date, String category) throws SQLException{
        
        Object[][] sales = new Object[getNumberOfSales(date)][6];
        int row = 0;
        
        try{
            
            DbConSales();
            dba = "SELECT * FROM '" + date + "' WHERE category = ?";
            ps = DbConSales().prepareStatement(dba);
            
            ps.setString(1, category);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                sales[row][0] = rs.getObject("Category");
                sales[row][1] = rs.getObject("name");
                sales[row][2] = rs.getObject("code");
                sales[row][3] = rs.getObject("price");
                sales[row][4] = rs.getObject("unitsSold");
                sales[row][5] = rs.getObject("total");
                
                row++;
                
            }
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            rs.close();
            DbConSales().close();
        }
        
        return sales;
    }
    
    /**
     * gets all sales by date and category
     * @param date
     * @param product
     * @return
     * @throws SQLException 
     */
    
    public synchronized Object[][] getSalesByDateAndProduct(String date, String product) throws SQLException{
        
        Object[][] sales = new Object[getNumberOfSales(date)][6];
        int row = 0;
        
        try{
            
            DbConSales();
            dba = "SELECT * FROM '" + date + "' WHERE name = ?";
            ps = DbConSales().prepareStatement(dba);
            
            ps.setString(1, product);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                sales[row][0] = rs.getObject("Category");
                sales[row][1] = rs.getObject("name");
                sales[row][2] = rs.getObject("code");
                sales[row][3] = rs.getObject("price");
                sales[row][4] = rs.getObject("unitsSold");
                sales[row][5] = rs.getObject("total");
                
                row++;
                
            }
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbConSales().close();
        }
        
        return sales;
    }
    
    /**
     * create a customer product table
     * @param Category
     * @throws SQLException 
     */
    
    public synchronized void createProductTable(String Category) throws SQLException{
        
        try {
            
            DbCon();
            
            dba = "CREATE TABLE IF NOT EXISTS '" + Category + "'("
                    + "Name STRING (30),"
                    + "Weight STRING (10),"
                    + "Description STRING (50),"
                    + "Code STRING (5),"
                    + "Price DECIMAL (5),"
                    + "Quantity INT (5),"
                    + "VEG STRING (3),"
                    + "Image STRING (60));";
            
            ps = DbCon().prepareStatement(dba);
            
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbCon().close();
        }
    }
    
    /**
     * inserts data into chosen product database
     * @param Category 
     * @param newProduct 
     * @throws java.sql.SQLException 
     */
    
    public synchronized void addToProductDatabase(String Category, Object[][] newProduct) throws SQLException{
        
        try{
            
            DbCon();
            
            for(Object[] p : newProduct){
                
               String code = p[0].toString().substring(0,3) + p[1] + p[6];
            
                dba = "INSERT INTO '" + Category + "'(name, weight,  description, code, price, quantity, veg, image) VALUES(?,?,?,?,?,?,?,?)";
                ps = DbCon().prepareStatement(dba);

                ps.setObject(1, p[0]);
                ps.setObject(2, p[1]);
                ps.setObject(3, p[2]);
                ps.setObject(4, code);
                ps.setObject(5, p[3]);
                ps.setObject(6, p[4]);
                ps.setObject(7, p[5]);
                ps.setObject(8, p[6]);
                
                ps.executeUpdate();
            
            }
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbCon().close();
        }
    }
    
    /**
     * deletes table dat then updates with new information
     * @param category 
     * @param products 
     * @throws java.sql.SQLException 
     */
    
    public synchronized void upDateCategory(String category, Object[][] products) throws SQLException{
        
        try{
            
            DbCon();
            dba = "DROP TABLE'" + category + "'";
            ps = DbCon().prepareStatement(dba);
            
            ps.executeUpdate();
            
            createProductTable(category);
            
            for(Object[] p : products){
                
               String code = p[0].toString().substring(0,3) + p[1] + p[6];
            
                dba = "INSERT INTO '" + category + "'(name, weight,  description, code, price, quantity, veg, image) VALUES(?,?,?,?,?,?,?,?)";
                ps = DbCon().prepareStatement(dba);

                ps.setObject(1, p[0]);
                ps.setObject(2, p[1]);
                ps.setObject(3, p[2]);
                ps.setObject(4, code); //p[3] empty String
                ps.setObject(5, p[4]);
                ps.setObject(6, p[5]);
                ps.setObject(7, p[6]);
                ps.setObject(8, p[7]);
                
                ps.executeUpdate();

            }
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbCon().close();
        }
        
        JOptionPane.showMessageDialog(null, category + " Has Been Updated");
    }
    
    /**
     * gets number of products in category
     * @param Category
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public synchronized int getNumOfProducts(String Category) throws SQLException{
        
        int numberOfProducts = 0;
        
        try{
            DbCon();
            
            dba = "SELECT COUNT(*) AS numberOfProducts FROM '" + Category + "'";
            ps = DbCon().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
               numberOfProducts = rs.getInt("numberOfProducts");
                
            }
   
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbCon().close();
        }
        
        return numberOfProducts;   
    }
    
    /**
     * gets all products from chosen category
     * @param Category
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public synchronized Object[][] getProducts(String Category) throws SQLException{
        
        Object[][] products = new Object[getNumOfProducts(Category)][8];
        int row = 0;
        
        try{
            
            DbCon();
            
            dba = "SELECT * FROM '" + Category + "'";
            ps = DbCon().prepareStatement(dba);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                products[row][0] = rs.getString("name");
                products[row][1] = rs.getString("weight");
                products[row][2] = rs.getString("description");
                products[row][3] = rs.getString("code");
                products[row][4] = rs.getFloat("price");
                products[row][5] = rs.getInt("quantity");
                products[row][6] = rs.getString("veg");
                products[row][7] = rs.getString("image");
                
                row++;
                
            }
   
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbCon().close();
        }
        
        return products;
    }
    
    /**
     * gets all categories of products from database
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public synchronized ArrayList<String> getAllCategories() throws SQLException{
        
        ArrayList<String> categories = new ArrayList<>();
        
        try{
            DbCon();
            
            dbmd = DbCon().getMetaData();
            
            rs = dbmd.getTables(null, null, "%", null);
            
            while(rs.next()){
                
                categories.add(rs.getString(3));
            }
            
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbCon().close();
        }
             
        return categories;
        
    }
    
    /**
     * updates chosen amount of stock from product in database
     * @param amount
     * @param product
     * @param category 
     * @throws java.sql.SQLException 
     */
    
    public synchronized void updateQuantity(int amount, String product, String category) throws SQLException{
        
        try{
            
            DbCon();
            dba = "UPDATE '" + category + "' SET quantity = ? WHERE name = ?";
            ps = DbCon().prepareStatement(dba);
            ps.setInt(1, amount);
            ps.setString(2, product);
             
            ps.executeUpdate();
                    
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbCon().close();
        }
    }
    
    /**
     * gets the quantity of stock of chosen item
     * @param code
     * @return
     * @throws SQLException 
     */
    
    public synchronized int getQuantity(String code) throws SQLException{
        
        int quantity = 0;
        ArrayList<String> categories = getAllCategories();
        
        try{
            
        cat:for(String c : categories) {
               
                DbCon();
                dba = "SELECT quantity FROM '" + c + "' WHERE code = ?";
                ps = DbCon().prepareStatement(dba);
                ps.setString(1, code);

                rs = ps.executeQuery();

                while(rs.next()){

                    quantity = rs.getInt("quantity");
                    break cat;
                }
            }  
        } catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbCon().close();
        }
        
        return quantity;
    }
       
    /**
     * gets product code
     * @param product
     * @param category
     * @return
     * @throws SQLException 
     */
    
    public synchronized String getProductCode(String product, String category) throws SQLException{
        
        String code = "";
        
        try{
            
            DbCon();
            dba = "SELECT code FROM '" + category + "' WHERE name = ? ";
            ps = DbCon().prepareStatement(dba);
            ps.setString(1, product);
            
            rs = ps.executeQuery();
            
            while(rs.next()){
                
                code = rs.getString("code");
                break;
            }
        
        }catch (SQLException ex) {
            
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbCon().close();
        }
        
        return code;
    }
    
    /**
     * gets products vegetarian status
     * @param product
     * @param category
     * @return
     * @throws SQLException 
     */
    
    public synchronized String getProductVeg(String product, String category) throws SQLException{

        String veg = "";

        try{

            DbCon();
            dba = "SELECT veg FROM '" + category + "' WHERE name = ? ";
            ps = DbCon().prepareStatement(dba);
            ps.setString(1, product);

            rs = ps.executeQuery();

            while(rs.next()){

                veg = rs.getString("veg");
                break;
            }

        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);

        }finally{

            rs.close();
            DbCon().close();
        }

        return veg;
    }
    
    /**
    * gets products vegetarian status
    * @param product
    * @param category
    * @return
    * @throws SQLException 
    */
    
    public synchronized String getProductDescription(String product, String category) throws SQLException{

        String description = "";

        try{

            DbCon();
            dba = "SELECT description FROM '" + category + "' WHERE name = ? ";
            ps = DbCon().prepareStatement(dba);
            ps.setString(1, product);

            rs = ps.executeQuery();

            while(rs.next()){

                description = rs.getString("description");
                break;
            }

        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);

        }finally{

            rs.close();
            DbCon().close();
        }

        return description;
    }
    
    /**
    * gets products weight
    * @param product
    * @param category
    * @return
    * @throws SQLException 
    */
    
    public synchronized String getProductWeight(String product, String category) throws SQLException{

        String weight = "";

        try{

            DbCon();
            dba = "SELECT weight FROM '" + category + "' WHERE name = ? ";
            ps = DbCon().prepareStatement(dba);
            ps.setString(1, product);

            rs = ps.executeQuery();

            while(rs.next()){

                weight= rs.getString("weight");
                break;
            }

        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);

        }finally{

            rs.close();
            DbCon().close();
        }

        return weight;
    }
    
        
    /**
    * gets products image
    * @param product
    * @param category
    * @return
    * @throws SQLException 
    */
    
    public synchronized String getProductImage(String product, String category) throws SQLException{

        String image = "";

        try{

            DbCon();
            dba = "SELECT image FROM '" + category + "' WHERE name = ? ";
            ps = DbCon().prepareStatement(dba);
            ps.setString(1, product);

            rs = ps.executeQuery();

            while(rs.next()){

                image = rs.getString("image");
                break;
            }

        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);

        }finally{

            rs.close();
            DbCon().close();
        }

        return image;
    }
    
    /**
     * gets the price of the product
     * @param product
     * @param category
     * @return
     * @throws SQLException 
     */
    
    public synchronized float getProductPrice(String product, String category) throws SQLException{

        float price = 0.0F;

        try{

            DbCon();
            dba = "SELECT price FROM '" + category + "' WHERE name = ? ";
            ps = DbCon().prepareStatement(dba);
            ps.setString(1, product);

            rs = ps.executeQuery();

            while(rs.next()){

                price = rs.getFloat("price");
                break;
            }

        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);

        }finally{

            rs.close();
            DbCon().close();
        }

        return price;
    }
    
    /**
     * removes chosen category from database
     * @param category 
     * @throws java.sql.SQLException 
     */
    
    public synchronized void RemoveCategory(String category) throws SQLException{
        
        try{
            
            DbCon();
            dba ="DROP TABLE'" + category + "'";
            ps = DbCon().prepareStatement(dba);
            
            ps.executeUpdate();
            
        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            DbCon().close();
        }
    }
    
    /**
     * finds the category a product is in
     * @param code
     * @return 
     * @throws java.sql.SQLException 
     */
    
    public synchronized String findProductCategory(String code) throws SQLException{
        
        ArrayList<String> categories = getAllCategories();
        String category = "";
        
        try{
            
        cat:    for(String c : categories){
                
                DbCon();
                dba = "SELECT * FROM '" + c + "' WHERE code = ?";
                ps = DbCon().prepareStatement(dba);
                ps.setString(1, code);
                
                rs = ps.executeQuery();
                
                while(rs.next()){
                    
                    category = c;
                    
                    break cat;
                    
                }  
            }
        }catch (SQLException ex) {

            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            
        }finally{
            
            rs.close();
            DbCon().close();
        }

        return category;
    }
    
    /**
     * gets product details
     * @param product
     * @param category
     * @return
     * @throws SQLException 
     */
    
    public synchronized Object[] getProductDetails(String product, String category) throws SQLException{
        
        Object[] details = new Object[3];
        
        details[0] = getProductCode(product, category);
        details[1] = getProductDescription(product, category);
        details[2] = getProductWeight(product, category);
        
        return details;
    }
}
