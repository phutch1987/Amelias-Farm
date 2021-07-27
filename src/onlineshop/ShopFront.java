/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hutch
 */
public final class ShopFront extends javax.swing.JFrame {
    
    private Data data = new Data();
    private CustomerData customerData = new CustomerData();
        
    private boolean loggedIn;
    private String user;
    
    private String category;
    
    private String productName;
    private short weight;
    private String description;
    private String code;
    private BigDecimal priced;
    private short quantity;
    private String VEG;
    private String image;
    
    private ImageIcon imageChange;
    
    private static int page = 1;
    private static int groupBy = 0;    //number to get a group of products by for next and previous btns
    
    private javax.swing.ButtonGroup categoryButtons;
    private javax.swing.JButton[] buttons; 
    
    private final javax.swing.JPanel[] productPanels = new javax.swing.JPanel[6];
    private final JLabel[] productImagesLabels = new JLabel[6];
    private final JLabel[] productIdentityLabels = new JLabel[6];
    private final JLabel[] quantitysLabels = new JLabel[6];
    private final JLabel[] priceLabels = new JLabel[6];
    
    private final javax.swing.JButton[] addToBasketBTNS = new javax.swing.JButton[6];
    private final javax.swing.JButton[] detailsBTNS = new javax.swing.JButton[6];
    
    private final javax.swing.JSpinner[] quantitySpinners = new javax.swing.JSpinner[6];    
       
    private final String programPics = "C:\\AmeliasFarmData\\ShopPics\\";
    private final String imageFolder = "C:\\AmeliasFarmData\\ProductImages\\";
    private final String aboutFolder = "C:\\AmeliasFarmData\\AboutPics\\";
    
    private ArrayList<String> categories = new ArrayList<>();
    private final javax.swing.JLabel[] backgrounds = new javax.swing.JLabel[16];
    private final ArrayList<String> backgroundsArray = new ArrayList<>();
        
    private NumberFormat nf = NumberFormat.getCurrencyInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    MyActionListener  ma = new MyActionListener(); 
    MyActionListenerBasket mab = new MyActionListenerBasket();
    MyActionListenerDetails mad = new MyActionListenerDetails();
    MyActionListenerNextPrev manp = new MyActionListenerNextPrev();
    
  
    public ShopFront() {
        
        initComponents();
        
        menuPanel.setVisible(false);
        AdminPanel.setVisible(false);
        AboutUsAdminBTNS.setVisible(false);
        pagePanel.setVisible(false);
        
        titlePanel.setLocation(UserNameLabel.getX() + 100, UserNameLabel.getY());
        titlePanel.repaint();
    
        createDirectorys();
        createCategoryButtons();
        createArrays();
        backgroundCreate();
        loadSavedBackgrounds();
        loadSavedAboutUsPage();
    }
    
    /**
     * method to put all labels for products in arrays
     */
    
    public final void createArrays(){
                    
    //product panel aaray
        
        productPanels[0] = product;
        productPanels[1] = product1;
        productPanels[2] = product2;
        productPanels[3] = product3;
        productPanels[4] = product4;
        productPanels[5] = product5;
        
        for(javax.swing.JPanel p : productPanels){
            
            p.setVisible(false);
            p.setBackground(new Color(229,209,70,99));
            
        }
         
    //image file array
    
        productImagesLabels[0] = productImage;
        productImagesLabels[1] = productImage1;
        productImagesLabels[2] = productImage2;
        productImagesLabels[3] = productImage3;
        productImagesLabels[4] = productImage4;
        productImagesLabels[5] = productImage5;
        
    //product name array
        
        productIdentityLabels[0] = productIdentityLabel;
        productIdentityLabels[1] = productIdentityLabel1;
        productIdentityLabels[2] = productIdentityLabel2;
        productIdentityLabels[3] = productIdentityLabel3;
        productIdentityLabels[4] = productIdentityLabel4;
        productIdentityLabels[5] = productIdentityLabel5;
        
        for(javax.swing.JLabel l : productIdentityLabels){
            
            l.setOpaque(true);
            l.setBackground(Color.WHITE);
            l.repaint();
        }
        
    //product quantity array
        
        quantitysLabels[0] = inStockQuantityLabel;
        quantitysLabels[1] = inStockQuantityLabel1;
        quantitysLabels[2] = inStockQuantityLabel2;
        quantitysLabels[3] = inStockQuantityLabel3;
        quantitysLabels[4] = inStockQuantityLabel4;
        quantitysLabels[5] = inStockQuantityLabel5;
        
        for(javax.swing.JLabel l : quantitysLabels){
            
            l.setOpaque(true);
            l.setBackground(Color.WHITE);
            l.repaint();
        }
        
    //product price array
    
        priceLabels[0] = price;
        priceLabels[1] = price1;
        priceLabels[2] = price2;
        priceLabels[3] = price3;
        priceLabels[4] = price4;
        priceLabels[5] = price5;
        
        for(javax.swing.JLabel l : priceLabels){
            
            l.setOpaque(true);
            l.setBackground(Color.WHITE);
            l.repaint();
        }
        
    //product add to basket button array
    
        addToBasketBTNS[0] = addToBasketBTN;
        addToBasketBTNS[1] = addToBasketBTN1;
        addToBasketBTNS[2] = addToBasketBTN2;
        addToBasketBTNS[3] = addToBasketBTN3;
        addToBasketBTNS[4] = addToBasketBTN4;
        addToBasketBTNS[5] = addToBasketBTN5;
               
    //details buttons array
    
        detailsBTNS[0] = details;
        detailsBTNS[1] = details1;
        detailsBTNS[2] = details2;
        detailsBTNS[3] = details3;
        detailsBTNS[4] = details4;
        detailsBTNS[5] = details5;
        
        
    //quantity spinners array
    
        quantitySpinners[0] = productQuantitySpinner;
        quantitySpinners[1] = productQuantitySpinner1;
        quantitySpinners[2] = productQuantitySpinner2;
        quantitySpinners[3] = productQuantitySpinner3;
        quantitySpinners[4] = productQuantitySpinner4;
        quantitySpinners[5] = productQuantitySpinner5;
      
    
    //background label array 
    
        backgrounds[0] = AddCategoryBackground;
        backgrounds[1] = AddProductBackground;
        backgrounds[2] = RemoveCategoryBackground;
        backgrounds[3] = CustomerWindowBackground;
        backgrounds[4] = SalesWindowBackground;
        backgrounds[5] = LogInBackground;
        backgrounds[6] = SignUpBackground;
        backgrounds[7] = ContactUsBackground;
        backgrounds[8] = basketBackground;
        backgrounds[9] = AddAdminBackground;
        backgrounds[10] = ChangePictureBackground;
     
    //adding action listener to next and previous btns
    
        mainNextBTN.addActionListener(manp);
        mainPreviousBTN.addActionListener(manp);
    }
    
    /**
     * method to read the background variables text file and add it to an arrayList
     * 
     */
    
    public void backgroundCreate(){
        
        try(BufferedReader br = new BufferedReader(new FileReader(programPics + "background.txt"))){
            
            String b = "";
            
            while((b = br.readLine()) != null){
                
                backgroundsArray.add(b);
            }
            
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * method to load any saved background changes from the admin panel
     * 
     */
    
    public void loadSavedBackgrounds(){
        
        File sB = new File(programPics + "savedBackgrounds.txt");
        
        if(sB.exists()){
            
            try(BufferedReader br = new BufferedReader(new FileReader(sB))){
                
                String b = "";
                
                while((b = br.readLine()) != null){

                    int place = Integer.parseInt(b.trim());
                    b = br.readLine().trim();

                    ImageIcon saved = new ImageIcon(b.trim());
                    backgrounds[place].setIcon(changePicSize(saved, backgrounds[place]));
                    
                    Component comp =  (Component) backgrounds[place];
                    JFrame frame = (JFrame) SwingUtilities.getRoot(comp);
                    comp.getParent().repaint();
                    frame.repaint();

                }
                
            } catch (IOException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
    /**
     * loads the saved about us text
     */
    
    public void loadSavedAboutUsPage(){
        
        File about = new File(programPics + "aboutUs.txt");
        
        if(about.exists()){
            
            try(BufferedReader br = new BufferedReader(new FileReader(about))){
        
                String line = "";
                
                while((line = br.readLine()) != null){
                    
                    AboutUsTextArea.read(br, about);
                    
                }
            
            }catch (FileNotFoundException ex) {
        
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                
            } catch (IOException ex) {
                    
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     }
    
    /**
     * creates directory and folders
     */
    
    private void createDirectorys(){
        
        try {
            
            Path p1 = Paths.get("C:\\AmeliasFarmData");
            Path p2 = Paths.get("C:\\AmeliasFarmData\\ProductImages");
            Path p3 = Paths.get("C:\\AmeliasFarmData\\CustomerSales");
            Path p4 = Paths.get("C:\\AmeliasFarmData\\ShopPics");
            Path p5 = Paths.get("C:\\AmeliasFarmData\\AboutPics");
            
            Path[] paths = new Path[5];
            
            paths[0] = p1;
            paths[1] = p2;
            paths[2] = p3;
            paths[3] = p4;
            paths[4] = p5;
            
            for(Path p : paths){
                
                if(!p.toFile().exists()){
                    
                    Files.createDirectories(p);
                }
            }
        } catch (IOException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    /**
     * creates the buttons for the categories of products saved in the database
     */
    
    private void createCategoryButtons(){
        
        try {
            
            categories = data.getAllCategories();
            buttons = new javax.swing.JButton[categories.size()];
            
            int x = 10,  y = 10;
            
            for(int i = 0; i < buttons.length; i++){
                
                Font font = new Font("Georgia Pro Semibold",Font.BOLD,14);
                Border LoweredBevel = BorderFactory.createLoweredSoftBevelBorder();
                  
                buttons[i] = new javax.swing.JButton(categories.get(i));
                buttons[i].addActionListener(ma);
                buttons[i].setBackground(new Color(248,198,2));
                buttons[i].setFont(font);
                buttons[i].setBorder(LoweredBevel);
                
                menuPanel.add(buttons[i]).setBounds(x, y, 240, 30);
                
                y += 40;

            }
            
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    /**
     * removes category buttons
     */
    
    private void removeCategoryButtons(){
        
        for(int i = 0; i< buttons.length; i++){
            
            menuPanel.remove(buttons[i]);
            menuPanel.repaint();
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AddCategoryWindow = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ANCProductTable = new javax.swing.JTable();
        ANCCategoryLabel = new javax.swing.JLabel();
        ANCCategoryInput = new javax.swing.JTextField();
        ANCRemoveProductBTN = new javax.swing.JButton();
        ANCAddNewProductBTN = new javax.swing.JButton();
        ANCSaveBTN = new javax.swing.JButton();
        ANCSearchImage = new javax.swing.JButton();
        AddCategoryBackground = new javax.swing.JLabel();
        EditProductWindow = new javax.swing.JFrame();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        EPProductTable = new javax.swing.JTable();
        EPCategoryCombo = new javax.swing.JComboBox<>();
        EPCategoryLabel = new javax.swing.JLabel();
        EPUpdateBTN = new javax.swing.JButton();
        EPRemoveBTN = new javax.swing.JButton();
        EPAddImageBTN = new javax.swing.JButton();
        EPAddProductBTN = new javax.swing.JButton();
        AddProductBackground = new javax.swing.JLabel();
        RemoveCategoryWindow = new javax.swing.JFrame();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        RCCombo = new javax.swing.JComboBox<>();
        RCCategoryLabel = new javax.swing.JLabel();
        RCRemoveBTN = new javax.swing.JButton();
        RemoveCategoryBackground = new javax.swing.JLabel();
        CustomerWindow = new javax.swing.JFrame();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        CWCustomerTable = new javax.swing.JTable();
        CWGroupMailOutBTN = new javax.swing.JButton();
        CWContactBTN = new javax.swing.JButton();
        CWGetLastOrderBTN = new javax.swing.JButton();
        CustomerWindowBackground = new javax.swing.JLabel();
        CustomerOrderWindow = new javax.swing.JFrame();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        CustomerOrderTable = new javax.swing.JTable();
        COTotalOutput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        CustomerOrderBackground = new javax.swing.JLabel();
        SalesWindow = new javax.swing.JFrame();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        SWSalesTable = new javax.swing.JTable();
        SWPrintBTN = new javax.swing.JButton();
        SWDateChooser = new datechooser.beans.DateChooserCombo();
        SWProductRadio = new javax.swing.JRadioButton();
        SWProductCombo = new javax.swing.JComboBox<>();
        SWCategoryRadio = new javax.swing.JRadioButton();
        SWCategoryCombo = new javax.swing.JComboBox<>();
        SWSearchBTN = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        SalesWindowBackground = new javax.swing.JLabel();
        LogInWindow = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        LIUserNameInput = new javax.swing.JTextField();
        LIUsernameLabel = new javax.swing.JLabel();
        LIPasswordLabel = new javax.swing.JLabel();
        LICreateAccountBTN = new javax.swing.JButton();
        LILogInBTN = new javax.swing.JButton();
        LIPasswordInput = new javax.swing.JPasswordField();
        failedLogInLabel = new javax.swing.JLabel();
        LIAdminRadio = new javax.swing.JRadioButton();
        LogInBackground = new javax.swing.JLabel();
        SignUpWindow = new javax.swing.JFrame();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        SUEMailInput = new javax.swing.JTextField();
        SUNamelLabel = new javax.swing.JLabel();
        SUEMaillLabel = new javax.swing.JLabel();
        SUPasswordlLabel = new javax.swing.JLabel();
        SUReTypePasswordlLabel = new javax.swing.JLabel();
        SUPasswordInput = new javax.swing.JPasswordField();
        SUReTypePasswordInput = new javax.swing.JPasswordField();
        SUNameInput = new javax.swing.JTextField();
        SUEnterBTN = new javax.swing.JButton();
        SUUsernameInput = new javax.swing.JTextField();
        SUUsernameLabel = new javax.swing.JLabel();
        SignUpBackground = new javax.swing.JLabel();
        ContactUsWindow = new javax.swing.JFrame();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        CUTextArea = new javax.swing.JTextArea();
        CUSendBTN = new javax.swing.JButton();
        CUEMailLabel = new javax.swing.JLabel();
        CUNameLabel = new javax.swing.JLabel();
        CUSubjectLabel = new javax.swing.JLabel();
        CUNameInput = new javax.swing.JTextField();
        CUEMailInput = new javax.swing.JTextField();
        CUSubjectInput = new javax.swing.JTextField();
        ContactUsBackground = new javax.swing.JLabel();
        BasketWindow = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        BasketTable = new javax.swing.JTable();
        BWCheckoutBTN = new javax.swing.JButton();
        BWTotalOutput = new javax.swing.JLabel();
        BWTotalLabel = new javax.swing.JLabel();
        MBRemoveBTN = new javax.swing.JButton();
        MBLastOrderBTN = new javax.swing.JButton();
        basketBackground = new javax.swing.JLabel();
        AddAdminWindow = new javax.swing.JFrame();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        AAUsernameInput = new javax.swing.JTextField();
        AAPasswordInput = new javax.swing.JPasswordField();
        AAReTypePasswordInput = new javax.swing.JPasswordField();
        AAUsernameLabel = new javax.swing.JLabel();
        AAPasswordLabel = new javax.swing.JLabel();
        AAReTypePasswordLabel = new javax.swing.JLabel();
        AAEnterBTN = new javax.swing.JButton();
        AAFailedAtemptLabel = new javax.swing.JLabel();
        AddAdminBackground = new javax.swing.JLabel();
        AboutUsWindow = new javax.swing.JFrame();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        AboutUsAdminBTNS = new javax.swing.JPanel();
        AUEditBTN = new javax.swing.JButton();
        AUSaveBTN = new javax.swing.JButton();
        AUScroll = new javax.swing.JScrollPane();
        AboutUsTextArea = new javax.swing.JTextArea();
        AboutUsBackground = new javax.swing.JLabel();
        ChangePicturesWindow = new javax.swing.JFrame();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        CPAddImageBTN = new javax.swing.JButton();
        CPBackgroundLabel = new javax.swing.JLabel();
        CPApplyBTN = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        CPPreviewLabel = new javax.swing.JLabel();
        CPCombo = new javax.swing.JComboBox<>();
        ChangePictureBackground = new javax.swing.JLabel();
        mainBackPanel = new javax.swing.JPanel();
        mainFrontPanel = new javax.swing.JPanel();
        AdminPanel = new javax.swing.JPanel();
        AddCategoryWindowBTN = new javax.swing.JButton();
        EditProductWindowBTN = new javax.swing.JButton();
        CustomerWindowBTN = new javax.swing.JButton();
        SalesWindowBTN = new javax.swing.JButton();
        RemoveCategoryWindowBTN = new javax.swing.JButton();
        AddAdminWindowBTN = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        ShopPanel = new javax.swing.JPanel();
        product = new javax.swing.JPanel();
        productPanel = new javax.swing.JPanel();
        productImage = new javax.swing.JLabel();
        addToBasketBTN = new javax.swing.JButton();
        productLabel = new javax.swing.JLabel();
        inStockLabel = new javax.swing.JLabel();
        productQuantitySpinner = new javax.swing.JSpinner();
        buyLabel = new javax.swing.JLabel();
        productIdentityLabel = new javax.swing.JLabel();
        inStockQuantityLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        price = new javax.swing.JLabel();
        details = new javax.swing.JButton();
        product1 = new javax.swing.JPanel();
        productPanel1 = new javax.swing.JPanel();
        productImage1 = new javax.swing.JLabel();
        addToBasketBTN1 = new javax.swing.JButton();
        productLabel1 = new javax.swing.JLabel();
        inStockLabel1 = new javax.swing.JLabel();
        productQuantitySpinner1 = new javax.swing.JSpinner();
        buyLabel1 = new javax.swing.JLabel();
        productIdentityLabel1 = new javax.swing.JLabel();
        inStockQuantityLabel1 = new javax.swing.JLabel();
        priceLabel1 = new javax.swing.JLabel();
        price1 = new javax.swing.JLabel();
        details1 = new javax.swing.JButton();
        product3 = new javax.swing.JPanel();
        productPanel3 = new javax.swing.JPanel();
        productImage3 = new javax.swing.JLabel();
        addToBasketBTN3 = new javax.swing.JButton();
        productLabel3 = new javax.swing.JLabel();
        inStockLabel3 = new javax.swing.JLabel();
        productQuantitySpinner3 = new javax.swing.JSpinner();
        buyLabel3 = new javax.swing.JLabel();
        productIdentityLabel3 = new javax.swing.JLabel();
        inStockQuantityLabel3 = new javax.swing.JLabel();
        priceLabel3 = new javax.swing.JLabel();
        price3 = new javax.swing.JLabel();
        details3 = new javax.swing.JButton();
        product5 = new javax.swing.JPanel();
        productPanel5 = new javax.swing.JPanel();
        productImage5 = new javax.swing.JLabel();
        addToBasketBTN5 = new javax.swing.JButton();
        productLabel5 = new javax.swing.JLabel();
        inStockLabel5 = new javax.swing.JLabel();
        productQuantitySpinner5 = new javax.swing.JSpinner();
        buyLabel5 = new javax.swing.JLabel();
        productIdentityLabel5 = new javax.swing.JLabel();
        inStockQuantityLabel5 = new javax.swing.JLabel();
        priceLabel5 = new javax.swing.JLabel();
        price5 = new javax.swing.JLabel();
        details5 = new javax.swing.JButton();
        product2 = new javax.swing.JPanel();
        productPanel2 = new javax.swing.JPanel();
        productImage2 = new javax.swing.JLabel();
        addToBasketBTN2 = new javax.swing.JButton();
        productLabel2 = new javax.swing.JLabel();
        inStockLabel2 = new javax.swing.JLabel();
        productQuantitySpinner2 = new javax.swing.JSpinner();
        buyLabel2 = new javax.swing.JLabel();
        productIdentityLabel2 = new javax.swing.JLabel();
        inStockQuantityLabel2 = new javax.swing.JLabel();
        priceLabel2 = new javax.swing.JLabel();
        price2 = new javax.swing.JLabel();
        details2 = new javax.swing.JButton();
        product4 = new javax.swing.JPanel();
        productPanel4 = new javax.swing.JPanel();
        productImage4 = new javax.swing.JLabel();
        addToBasketBTN4 = new javax.swing.JButton();
        productLabel4 = new javax.swing.JLabel();
        inStockLabel4 = new javax.swing.JLabel();
        productQuantitySpinner4 = new javax.swing.JSpinner();
        buyLabel4 = new javax.swing.JLabel();
        productIdentityLabel4 = new javax.swing.JLabel();
        inStockQuantityLabel4 = new javax.swing.JLabel();
        priceLabel4 = new javax.swing.JLabel();
        price4 = new javax.swing.JLabel();
        details4 = new javax.swing.JButton();
        MainTitleBarPanel = new javax.swing.JPanel();
        SignUpWindowBTN = new javax.swing.JButton();
        ContactUsWindowBTN = new javax.swing.JButton();
        AboutUsWindowBTN = new javax.swing.JButton();
        MyBasketWindowBTN = new javax.swing.JButton();
        OpenShopBTN = new javax.swing.JButton();
        LogInWindowBTN = new javax.swing.JButton();
        menuPanel = new javax.swing.JPanel();
        UserNameLabel = new javax.swing.JLabel();
        titlePanel = new javax.swing.JPanel();
        mainTitleLabel = new javax.swing.JLabel();
        pagePanel = new javax.swing.JPanel();
        mainPreviousBTN = new javax.swing.JButton();
        pageLabel = new javax.swing.JLabel();
        mainNextBTN = new javax.swing.JButton();
        pageNumLabel = new javax.swing.JLabel();
        mainBackground = new javax.swing.JLabel();

        AddCategoryWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        AddCategoryWindow.setTitle("Add New Category");
        AddCategoryWindow.setMinimumSize(new java.awt.Dimension(876, 608));
        AddCategoryWindow.setResizable(false);

        jPanel2.setLayout(null);

        jPanel4.setOpaque(false);

        ANCProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Weight", "Description", "Price", "Quantity", "VEG", "Image Path"
            }
        ));
        jScrollPane1.setViewportView(ANCProductTable);

        ANCCategoryLabel.setText("Category Name:");

        ANCRemoveProductBTN.setText("REMOVE");
        ANCRemoveProductBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ANCRemoveProductBTNActionPerformed(evt);
            }
        });

        ANCAddNewProductBTN.setText("ADD NEW PRODUCT");
        ANCAddNewProductBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ANCAddNewProductBTNActionPerformed(evt);
            }
        });

        ANCSaveBTN.setText("SAVE");
        ANCSaveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ANCSaveBTNActionPerformed(evt);
            }
        });

        ANCSearchImage.setText("SEARCH IMAGE");
        ANCSearchImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ANCSearchImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(82, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(ANCCategoryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ANCCategoryInput, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(ANCAddNewProductBTN)
                            .addGap(18, 18, 18)
                            .addComponent(ANCSearchImage)
                            .addGap(53, 53, 53)
                            .addComponent(ANCRemoveProductBTN)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ANCSaveBTN))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(78, 78, 78))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ANCCategoryLabel)
                    .addComponent(ANCCategoryInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ANCRemoveProductBTN)
                    .addComponent(ANCAddNewProductBTN)
                    .addComponent(ANCSaveBTN)
                    .addComponent(ANCSearchImage))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4);
        jPanel4.setBounds(0, 0, 880, 610);

        AddCategoryBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AddCategoryBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        AddCategoryBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel2.add(AddCategoryBackground);
        AddCategoryBackground.setBounds(0, 0, 880, 610);

        javax.swing.GroupLayout AddCategoryWindowLayout = new javax.swing.GroupLayout(AddCategoryWindow.getContentPane());
        AddCategoryWindow.getContentPane().setLayout(AddCategoryWindowLayout);
        AddCategoryWindowLayout.setHorizontalGroup(
            AddCategoryWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddCategoryWindowLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 876, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        AddCategoryWindowLayout.setVerticalGroup(
            AddCategoryWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
        );

        EditProductWindow.setMinimumSize(new java.awt.Dimension(1259, 643));
        EditProductWindow.setResizable(false);

        jPanel7.setLayout(null);

        jPanel8.setOpaque(false);

        EPProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Weight", "Description", "Code", "Price", "Quantity", "VEG", "Image Path"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(EPProductTable);

        EPCategoryCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EPCategoryComboActionPerformed(evt);
            }
        });

        EPCategoryLabel.setText("Category:");

        EPUpdateBTN.setText("UPDATE");
        EPUpdateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EPUpdateBTNActionPerformed(evt);
            }
        });

        EPRemoveBTN.setText("REMOVE");
        EPRemoveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EPRemoveBTNActionPerformed(evt);
            }
        });

        EPAddImageBTN.setText("ADD IMAGE");
        EPAddImageBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EPAddImageBTNActionPerformed(evt);
            }
        });

        EPAddProductBTN.setText("ADD PRODUCT");
        EPAddProductBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EPAddProductBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EPCategoryLabel)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(EPCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(EPAddProductBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(EPRemoveBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(EPAddImageBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(EPUpdateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {EPAddImageBTN, EPAddProductBTN, EPRemoveBTN, EPUpdateBTN});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(EPCategoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EPCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EPUpdateBTN)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(EPAddProductBTN)
                        .addComponent(EPRemoveBTN)
                        .addComponent(EPAddImageBTN)))
                .addContainerGap(133, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel8);
        jPanel8.setBounds(0, 0, 1260, 650);

        AddProductBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AddProductBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        AddProductBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel7.add(AddProductBackground);
        AddProductBackground.setBounds(0, 0, 1000, 640);

        javax.swing.GroupLayout EditProductWindowLayout = new javax.swing.GroupLayout(EditProductWindow.getContentPane());
        EditProductWindow.getContentPane().setLayout(EditProductWindowLayout);
        EditProductWindowLayout.setHorizontalGroup(
            EditProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE)
        );
        EditProductWindowLayout.setVerticalGroup(
            EditProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
        );

        RemoveCategoryWindow.setMinimumSize(new java.awt.Dimension(465, 295));
        RemoveCategoryWindow.setResizable(false);

        jPanel13.setLayout(null);

        jPanel14.setOpaque(false);

        RCCategoryLabel.setText("Category:");

        RCRemoveBTN.setText("REMOVE");
        RCRemoveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RCRemoveBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RCCategoryLabel)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(RCCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RCRemoveBTN)))
                .addContainerGap(138, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(RCCategoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RCCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RCRemoveBTN))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel14);
        jPanel14.setBounds(0, 0, 470, 300);

        RemoveCategoryBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RemoveCategoryBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        RemoveCategoryBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel13.add(RemoveCategoryBackground);
        RemoveCategoryBackground.setBounds(0, 0, 470, 300);

        javax.swing.GroupLayout RemoveCategoryWindowLayout = new javax.swing.GroupLayout(RemoveCategoryWindow.getContentPane());
        RemoveCategoryWindow.getContentPane().setLayout(RemoveCategoryWindowLayout);
        RemoveCategoryWindowLayout.setHorizontalGroup(
            RemoveCategoryWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );
        RemoveCategoryWindowLayout.setVerticalGroup(
            RemoveCategoryWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        );

        CustomerWindow.setMinimumSize(new java.awt.Dimension(590, 586));
        CustomerWindow.setResizable(false);

        jPanel9.setLayout(null);

        jPanel10.setOpaque(false);

        CWCustomerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Username", "Name", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(CWCustomerTable);
        if (CWCustomerTable.getColumnModel().getColumnCount() > 0) {
            CWCustomerTable.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        CWGroupMailOutBTN.setText("GROUP MAILOUT");
        CWGroupMailOutBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CWGroupMailOutBTNActionPerformed(evt);
            }
        });

        CWContactBTN.setText("CONTACT");
        CWContactBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CWContactBTNActionPerformed(evt);
            }
        });

        CWGetLastOrderBTN.setText("GET LAST ORDER");
        CWGetLastOrderBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CWGetLastOrderBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(CWGetLastOrderBTN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CWContactBTN)
                        .addGap(18, 18, 18)
                        .addComponent(CWGroupMailOutBTN))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CWGroupMailOutBTN)
                    .addComponent(CWContactBTN)
                    .addComponent(CWGetLastOrderBTN))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel10);
        jPanel10.setBounds(0, 0, 590, 590);

        CustomerWindowBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CustomerWindowBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        CustomerWindowBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel9.add(CustomerWindowBackground);
        CustomerWindowBackground.setBounds(0, 0, 590, 590);

        javax.swing.GroupLayout CustomerWindowLayout = new javax.swing.GroupLayout(CustomerWindow.getContentPane());
        CustomerWindow.getContentPane().setLayout(CustomerWindowLayout);
        CustomerWindowLayout.setHorizontalGroup(
            CustomerWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        CustomerWindowLayout.setVerticalGroup(
            CustomerWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );

        CustomerOrderWindow.setMinimumSize(new java.awt.Dimension(733, 567));
        CustomerOrderWindow.setResizable(false);

        jPanel26.setLayout(null);

        CustomerOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Name", "VEG", "Quantity", "Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(CustomerOrderTable);

        jLabel1.setText("TOTAL:");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(COTotalOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(COTotalOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        jPanel26.add(jPanel27);
        jPanel27.setBounds(0, 0, 730, 570);
        jPanel26.add(CustomerOrderBackground);
        CustomerOrderBackground.setBounds(0, 0, 730, 570);

        javax.swing.GroupLayout CustomerOrderWindowLayout = new javax.swing.GroupLayout(CustomerOrderWindow.getContentPane());
        CustomerOrderWindow.getContentPane().setLayout(CustomerOrderWindowLayout);
        CustomerOrderWindowLayout.setHorizontalGroup(
            CustomerOrderWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
        );
        CustomerOrderWindowLayout.setVerticalGroup(
            CustomerOrderWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
        );

        SalesWindow.setMinimumSize(new java.awt.Dimension(1411, 601));
        SalesWindow.setResizable(false);

        jPanel11.setLayout(null);

        jPanel12.setOpaque(false);

        SWSalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Code", "Name", "Price", "Units Sold", "Total Sale"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(SWSalesTable);

        SWPrintBTN.setText("PRINT");
        SWPrintBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SWPrintBTNActionPerformed(evt);
            }
        });

        SWCategoryCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SWCategoryComboActionPerformed(evt);
            }
        });

        SWSearchBTN.setText("SEARCH");
        SWSearchBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SWSearchBTNActionPerformed(evt);
            }
        });

        jLabel2.setText("Date:");

        jLabel3.setText("Product:");

        jLabel4.setText("Category:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SWDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(40, 40, 40)
                        .addComponent(SWCategoryRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(SWCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(SWProductRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(SWProductCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(SWSearchBTN))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 582, Short.MAX_VALUE)
                        .addComponent(SWPrintBTN)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SWPrintBTN)
                            .addComponent(SWDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SWCategoryRadio)
                            .addComponent(SWCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(SWProductCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SWSearchBTN))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(SWProductRadio)))))
                .addContainerGap(100, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel12);
        jPanel12.setBounds(0, 0, 1400, 600);

        SalesWindowBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SalesWindowBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        SalesWindowBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel11.add(SalesWindowBackground);
        SalesWindowBackground.setBounds(0, 0, 1420, 600);

        javax.swing.GroupLayout SalesWindowLayout = new javax.swing.GroupLayout(SalesWindow.getContentPane());
        SalesWindow.getContentPane().setLayout(SalesWindowLayout);
        SalesWindowLayout.setHorizontalGroup(
            SalesWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 1411, Short.MAX_VALUE)
        );
        SalesWindowLayout.setVerticalGroup(
            SalesWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );

        LogInWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        LogInWindow.setTitle("Log In");
        LogInWindow.setMinimumSize(new java.awt.Dimension(442, 286));
        LogInWindow.setResizable(false);

        jPanel1.setLayout(null);

        jPanel3.setOpaque(false);

        LIUserNameInput.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LIUserNameInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        LIUsernameLabel.setText("USERNAME");

        LIPasswordLabel.setText("PASSWORD");

        LICreateAccountBTN.setText("CREATE ACCOUNT");
        LICreateAccountBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LICreateAccountBTNActionPerformed(evt);
            }
        });

        LILogInBTN.setText("LOG IN");
        LILogInBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LILogInBTNActionPerformed(evt);
            }
        });

        LIPasswordInput.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LIPasswordInput.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        failedLogInLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        failedLogInLabel.setForeground(new java.awt.Color(255, 51, 51));
        failedLogInLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        LIAdminRadio.setText("Admin");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(LIPasswordLabel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(LIUsernameLabel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(LICreateAccountBTN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LILogInBTN))
                            .addComponent(LIUserNameInput)
                            .addComponent(LIPasswordInput)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(LIAdminRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(failedLogInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(failedLogInLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LIAdminRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(LIUsernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LIUserNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LIPasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LIPasswordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LILogInBTN)
                    .addComponent(LICreateAccountBTN))
                .addGap(94, 94, 94))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {LIPasswordInput, LIUserNameInput});

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 450, 290);

        LogInBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LogInBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        LogInBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel1.add(LogInBackground);
        LogInBackground.setBounds(0, 0, 450, 310);

        javax.swing.GroupLayout LogInWindowLayout = new javax.swing.GroupLayout(LogInWindow.getContentPane());
        LogInWindow.getContentPane().setLayout(LogInWindowLayout);
        LogInWindowLayout.setHorizontalGroup(
            LogInWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LogInWindowLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        LogInWindowLayout.setVerticalGroup(
            LogInWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
        );

        SignUpWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        SignUpWindow.setMinimumSize(new java.awt.Dimension(569, 444));
        SignUpWindow.setResizable(false);

        jPanel15.setLayout(null);

        jPanel16.setOpaque(false);

        SUNamelLabel.setText("Name:");

        SUEMaillLabel.setText("E-Mail:");

        SUPasswordlLabel.setText("Password:");

        SUReTypePasswordlLabel.setText("Re-Type Password:");

        SUEnterBTN.setText("ENTER");
        SUEnterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SUEnterBTNActionPerformed(evt);
            }
        });

        SUUsernameLabel.setText("Username:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SUEnterBTN, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(SUNamelLabel)
                            .addComponent(SUEMaillLabel)
                            .addComponent(SUPasswordlLabel)
                            .addComponent(SUReTypePasswordlLabel)
                            .addComponent(SUUsernameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(SUUsernameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addComponent(SUPasswordInput)
                            .addComponent(SUReTypePasswordInput)
                            .addComponent(SUEMailInput)
                            .addComponent(SUNameInput)))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SUNamelLabel)
                    .addComponent(SUNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SUUsernameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SUUsernameLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SUEMailInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SUEMaillLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SUPasswordlLabel)
                    .addComponent(SUPasswordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SUReTypePasswordlLabel)
                    .addComponent(SUReTypePasswordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(SUEnterBTN)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel16);
        jPanel16.setBounds(0, 0, 570, 440);

        SignUpBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SignUpBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        SignUpBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel15.add(SignUpBackground);
        SignUpBackground.setBounds(0, 0, 570, 440);

        javax.swing.GroupLayout SignUpWindowLayout = new javax.swing.GroupLayout(SignUpWindow.getContentPane());
        SignUpWindow.getContentPane().setLayout(SignUpWindowLayout);
        SignUpWindowLayout.setHorizontalGroup(
            SignUpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
        );
        SignUpWindowLayout.setVerticalGroup(
            SignUpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SignUpWindowLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        ContactUsWindow.setMinimumSize(new java.awt.Dimension(942, 659));
        ContactUsWindow.setResizable(false);

        jPanel17.setLayout(null);

        jPanel18.setOpaque(false);

        CUTextArea.setColumns(20);
        CUTextArea.setRows(5);
        jScrollPane6.setViewportView(CUTextArea);

        CUSendBTN.setText("SEND");
        CUSendBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CUSendBTNActionPerformed(evt);
            }
        });

        CUEMailLabel.setText("E-Mail Address:");

        CUNameLabel.setText("Name:");

        CUSubjectLabel.setText("Subject:");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addGap(756, 756, 756)
                            .addComponent(CUSendBTN))
                        .addComponent(jScrollPane6))
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addComponent(CUSubjectLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(CUSubjectInput, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(CUNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CUNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel18Layout.createSequentialGroup()
                                .addComponent(CUEMailLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CUEMailInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jPanel18Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {CUEMailInput, CUNameInput, CUSubjectInput});

        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CUNameLabel)
                    .addComponent(CUNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CUEMailLabel)
                    .addComponent(CUEMailInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CUSubjectLabel)
                    .addComponent(CUSubjectInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CUSendBTN)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        jPanel17.add(jPanel18);
        jPanel18.setBounds(0, 0, 940, 660);

        ContactUsBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ContactUsBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ContactUsBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel17.add(ContactUsBackground);
        ContactUsBackground.setBounds(0, 0, 940, 660);

        javax.swing.GroupLayout ContactUsWindowLayout = new javax.swing.GroupLayout(ContactUsWindow.getContentPane());
        ContactUsWindow.getContentPane().setLayout(ContactUsWindowLayout);
        ContactUsWindowLayout.setHorizontalGroup(
            ContactUsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE)
        );
        ContactUsWindowLayout.setVerticalGroup(
            ContactUsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
        );

        BasketWindow.setMinimumSize(new java.awt.Dimension(673, 586));
        BasketWindow.setResizable(false);

        jPanel5.setLayout(null);

        jPanel6.setOpaque(false);

        BasketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Name", "VEG", "Quantity", "Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(BasketTable);

        BWCheckoutBTN.setText(" CHECKOUT");
        BWCheckoutBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BWCheckoutBTNActionPerformed(evt);
            }
        });

        BWTotalOutput.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        BWTotalLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        BWTotalLabel.setText("Total:");

        MBRemoveBTN.setText("Remove Item");
        MBRemoveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MBRemoveBTNActionPerformed(evt);
            }
        });

        MBLastOrderBTN.setText("LAST ORDER");
        MBLastOrderBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MBLastOrderBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(MBRemoveBTN)
                                .addGap(381, 381, 381)
                                .addComponent(BWTotalLabel))
                            .addComponent(MBLastOrderBTN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BWTotalOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BWCheckoutBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(MBRemoveBTN)
                        .addComponent(BWTotalLabel))
                    .addComponent(BWTotalOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BWCheckoutBTN)
                    .addComponent(MBLastOrderBTN))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6);
        jPanel6.setBounds(0, 0, 700, 590);

        basketBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        basketBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        basketBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel5.add(basketBackground);
        basketBackground.setBounds(0, 0, 700, 590);

        javax.swing.GroupLayout BasketWindowLayout = new javax.swing.GroupLayout(BasketWindow.getContentPane());
        BasketWindow.getContentPane().setLayout(BasketWindowLayout);
        BasketWindowLayout.setHorizontalGroup(
            BasketWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
        );
        BasketWindowLayout.setVerticalGroup(
            BasketWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );

        AddAdminWindow.setMinimumSize(new java.awt.Dimension(515, 316));
        AddAdminWindow.setResizable(false);

        jPanel19.setLayout(null);

        jPanel20.setOpaque(false);

        AAUsernameLabel.setText("Username:");

        AAPasswordLabel.setText("Password:");

        AAReTypePasswordLabel.setText("Re-Type Password:");

        AAEnterBTN.setText("ENTER");
        AAEnterBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AAEnterBTNActionPerformed(evt);
            }
        });

        AAFailedAtemptLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        AAFailedAtemptLabel.setForeground(new java.awt.Color(255, 0, 51));
        AAFailedAtemptLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(AAEnterBTN)
                    .addComponent(AAFailedAtemptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(AAPasswordLabel)
                            .addComponent(AAReTypePasswordLabel)
                            .addComponent(AAUsernameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AAUsernameInput)
                            .addComponent(AAPasswordInput)
                            .addComponent(AAReTypePasswordInput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(AAFailedAtemptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AAUsernameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AAUsernameLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AAPasswordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AAPasswordLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AAReTypePasswordInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AAReTypePasswordLabel))
                .addGap(18, 18, 18)
                .addComponent(AAEnterBTN)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        jPanel19.add(jPanel20);
        jPanel20.setBounds(0, 0, 520, 320);

        AddAdminBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AddAdminBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        AddAdminBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel19.add(AddAdminBackground);
        AddAdminBackground.setBounds(0, 0, 520, 320);

        javax.swing.GroupLayout AddAdminWindowLayout = new javax.swing.GroupLayout(AddAdminWindow.getContentPane());
        AddAdminWindow.getContentPane().setLayout(AddAdminWindowLayout);
        AddAdminWindowLayout.setHorizontalGroup(
            AddAdminWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        AddAdminWindowLayout.setVerticalGroup(
            AddAdminWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
        );

        AboutUsWindow.setMinimumSize(new java.awt.Dimension(1185, 725));
        AboutUsWindow.setResizable(false);

        jPanel21.setLayout(null);

        jPanel22.setOpaque(false);

        AboutUsAdminBTNS.setBackground(new java.awt.Color(0, 102, 153));

        AUEditBTN.setText("EDIT");
        AUEditBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AUEditBTNActionPerformed(evt);
            }
        });

        AUSaveBTN.setText("SAVE");
        AUSaveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AUSaveBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AboutUsAdminBTNSLayout = new javax.swing.GroupLayout(AboutUsAdminBTNS);
        AboutUsAdminBTNS.setLayout(AboutUsAdminBTNSLayout);
        AboutUsAdminBTNSLayout.setHorizontalGroup(
            AboutUsAdminBTNSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AboutUsAdminBTNSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AUEditBTN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(AUSaveBTN)
                .addContainerGap())
        );
        AboutUsAdminBTNSLayout.setVerticalGroup(
            AboutUsAdminBTNSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AboutUsAdminBTNSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AboutUsAdminBTNSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AUEditBTN)
                    .addComponent(AUSaveBTN))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AUScroll.setOpaque(false);

        AboutUsTextArea.setEditable(false);
        AboutUsTextArea.setBackground(new java.awt.Color(0, 0, 0));
        AboutUsTextArea.setColumns(20);
        AboutUsTextArea.setFont(new java.awt.Font("Edwardian Script ITC", 1, 30)); // NOI18N
        AboutUsTextArea.setForeground(new java.awt.Color(255, 255, 255));
        AboutUsTextArea.setRows(5);
        AboutUsTextArea.setBorder(null);
        AboutUsTextArea.setMaximumSize(new java.awt.Dimension(344, 119));
        AboutUsTextArea.setMinimumSize(new java.awt.Dimension(344, 119));
        AboutUsTextArea.setOpaque(false);
        AUScroll.setViewportView(AboutUsTextArea);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(AboutUsAdminBTNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AUScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(AUScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AboutUsAdminBTNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel22);
        jPanel22.setBounds(0, 0, 1190, 720);

        AboutUsBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AboutUsBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        AboutUsBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        AboutUsBackground.setMinimumSize(new java.awt.Dimension(1185, 725));
        AboutUsBackground.setPreferredSize(new java.awt.Dimension(1185, 725));
        jPanel21.add(AboutUsBackground);
        AboutUsBackground.setBounds(0, 0, 1190, 720);

        javax.swing.GroupLayout AboutUsWindowLayout = new javax.swing.GroupLayout(AboutUsWindow.getContentPane());
        AboutUsWindow.getContentPane().setLayout(AboutUsWindowLayout);
        AboutUsWindowLayout.setHorizontalGroup(
            AboutUsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 1185, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AboutUsWindowLayout.setVerticalGroup(
            AboutUsWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
        );

        ChangePicturesWindow.setMinimumSize(new java.awt.Dimension(695, 361));
        ChangePicturesWindow.setResizable(false);

        jPanel23.setLayout(null);

        jPanel24.setOpaque(false);

        CPAddImageBTN.setText("Add Image");
        CPAddImageBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CPAddImageBTNActionPerformed(evt);
            }
        });

        CPBackgroundLabel.setText("Background:");

        CPApplyBTN.setText("Apply");
        CPApplyBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CPApplyBTNActionPerformed(evt);
            }
        });

        jPanel25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel25.setMaximumSize(new java.awt.Dimension(431, 228));
        jPanel25.setMinimumSize(new java.awt.Dimension(431, 228));

        CPPreviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CPPreviewLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        CPPreviewLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        CPPreviewLabel.setMaximumSize(new java.awt.Dimension(431, 228));
        CPPreviewLabel.setMinimumSize(new java.awt.Dimension(431, 228));
        CPPreviewLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(CPPreviewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(CPPreviewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        CPCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CPComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CPApplyBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CPBackgroundLabel)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addComponent(CPCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CPAddImageBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(34, 34, 34)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(CPApplyBTN))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(CPBackgroundLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CPAddImageBTN)
                            .addComponent(CPCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel23.add(jPanel24);
        jPanel24.setBounds(0, 0, 700, 360);

        ChangePictureBackground.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChangePictureBackground.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        ChangePictureBackground.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel23.add(ChangePictureBackground);
        ChangePictureBackground.setBounds(0, 0, 700, 360);

        javax.swing.GroupLayout ChangePicturesWindowLayout = new javax.swing.GroupLayout(ChangePicturesWindow.getContentPane());
        ChangePicturesWindow.getContentPane().setLayout(ChangePicturesWindowLayout);
        ChangePicturesWindowLayout.setHorizontalGroup(
            ChangePicturesWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        ChangePicturesWindowLayout.setVerticalGroup(
            ChangePicturesWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChangePicturesWindowLayout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1388, 848));
        setResizable(false);

        mainBackPanel.setLayout(null);

        mainFrontPanel.setOpaque(false);

        AdminPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AdminPanel.setOpaque(false);

        AddCategoryWindowBTN.setBackground(new java.awt.Color(0, 102, 153));
        AddCategoryWindowBTN.setForeground(new java.awt.Color(255, 255, 255));
        AddCategoryWindowBTN.setText("Add Category");
        AddCategoryWindowBTN.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddCategoryWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddCategoryWindowBTNActionPerformed(evt);
            }
        });

        EditProductWindowBTN.setBackground(new java.awt.Color(0, 102, 153));
        EditProductWindowBTN.setForeground(new java.awt.Color(255, 255, 255));
        EditProductWindowBTN.setText("Edit Products");
        EditProductWindowBTN.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        EditProductWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditProductWindowBTNActionPerformed(evt);
            }
        });

        CustomerWindowBTN.setBackground(new java.awt.Color(0, 102, 153));
        CustomerWindowBTN.setForeground(new java.awt.Color(255, 255, 255));
        CustomerWindowBTN.setText("Customers");
        CustomerWindowBTN.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CustomerWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerWindowBTNActionPerformed(evt);
            }
        });

        SalesWindowBTN.setBackground(new java.awt.Color(0, 102, 153));
        SalesWindowBTN.setForeground(new java.awt.Color(255, 255, 255));
        SalesWindowBTN.setText("Sales");
        SalesWindowBTN.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SalesWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalesWindowBTNActionPerformed(evt);
            }
        });

        RemoveCategoryWindowBTN.setBackground(new java.awt.Color(0, 102, 153));
        RemoveCategoryWindowBTN.setForeground(new java.awt.Color(255, 255, 255));
        RemoveCategoryWindowBTN.setText("Remove Category");
        RemoveCategoryWindowBTN.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        RemoveCategoryWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveCategoryWindowBTNActionPerformed(evt);
            }
        });

        AddAdminWindowBTN.setBackground(new java.awt.Color(0, 102, 153));
        AddAdminWindowBTN.setForeground(new java.awt.Color(255, 255, 255));
        AddAdminWindowBTN.setText("Add Admin");
        AddAdminWindowBTN.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddAdminWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddAdminWindowBTNActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 153));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Change Pictures");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AdminPanelLayout = new javax.swing.GroupLayout(AdminPanel);
        AdminPanel.setLayout(AdminPanelLayout);
        AdminPanelLayout.setHorizontalGroup(
            AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddCategoryWindowBTN, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditProductWindowBTN, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RemoveCategoryWindowBTN, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CustomerWindowBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SalesWindowBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddAdminWindowBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        AdminPanelLayout.setVerticalGroup(
            AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CustomerWindowBTN)
                    .addComponent(AddCategoryWindowBTN, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SalesWindowBTN)
                    .addGroup(AdminPanelLayout.createSequentialGroup()
                        .addComponent(EditProductWindowBTN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(AdminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(RemoveCategoryWindowBTN)
                            .addComponent(AddAdminWindowBTN))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ShopPanel.setMinimumSize(new java.awt.Dimension(1370, 634));
        ShopPanel.setOpaque(false);

        product.setBackground(new Color(229,209,70,64));
        product.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        product.setMinimumSize(new java.awt.Dimension(349, 298));

        productPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        productPanel.setMinimumSize(new java.awt.Dimension(296, 178));

        productImage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productImage.setToolTipText("");
        productImage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        productImage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        productImage.setMaximumSize(new java.awt.Dimension(311, 174));
        productImage.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addToBasketBTN.setText("ADD TO BASKET");
        addToBasketBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToBasketBTNActionPerformed(evt);
            }
        });

        productLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productLabel.setText("PRODUCT:");

        inStockLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inStockLabel.setText("IN STOCK:");

        buyLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buyLabel.setText("BUY:");

        productIdentityLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inStockQuantityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inStockQuantityLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        priceLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        priceLabel.setText("PRICE:");

        price.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        details.setText("DETAILS");

        javax.swing.GroupLayout productLayout = new javax.swing.GroupLayout(product);
        product.setLayout(productLayout);
        productLayout.setHorizontalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(productLayout.createSequentialGroup()
                        .addComponent(inStockLabel)
                        .addGap(7, 7, 7)
                        .addComponent(inStockQuantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToBasketBTN))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productLayout.createSequentialGroup()
                        .addComponent(productLabel)
                        .addGap(6, 6, 6)
                        .addComponent(productIdentityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productQuantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productLayout.createSequentialGroup()
                        .addComponent(priceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(details))
                    .addComponent(productPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        productLayout.setVerticalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(details)
                    .addComponent(priceLabel))
                .addGap(11, 11, 11)
                .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIdentityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(productLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel)
                            .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buyLabel)
                                .addComponent(productQuantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(inStockLabel))
                    .addComponent(inStockQuantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBasketBTN))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        product1.setBackground(new java.awt.Color(211, 211, 172));
        product1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        product1.setMinimumSize(new java.awt.Dimension(349, 298));

        productPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        productPanel1.setMinimumSize(new java.awt.Dimension(296, 178));

        productImage1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productImage1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        productImage1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        productImage1.setMaximumSize(new java.awt.Dimension(311, 174));

        javax.swing.GroupLayout productPanel1Layout = new javax.swing.GroupLayout(productPanel1);
        productPanel1.setLayout(productPanel1Layout);
        productPanel1Layout.setHorizontalGroup(
            productPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        productPanel1Layout.setVerticalGroup(
            productPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addToBasketBTN1.setText("ADD TO BASKET");

        productLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productLabel1.setText("PRODUCT:");

        inStockLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inStockLabel1.setText("IN STOCK:");

        buyLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buyLabel1.setText("BUY:");

        productIdentityLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inStockQuantityLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inStockQuantityLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        priceLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        priceLabel1.setText("PRICE:");

        price1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        details1.setText("DETAILS");

        javax.swing.GroupLayout product1Layout = new javax.swing.GroupLayout(product1);
        product1.setLayout(product1Layout);
        product1Layout.setHorizontalGroup(
            product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(product1Layout.createSequentialGroup()
                        .addComponent(inStockLabel1)
                        .addGap(7, 7, 7)
                        .addComponent(inStockQuantityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToBasketBTN1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product1Layout.createSequentialGroup()
                        .addComponent(productLabel1)
                        .addGap(6, 6, 6)
                        .addComponent(productIdentityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productQuantitySpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product1Layout.createSequentialGroup()
                        .addComponent(priceLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(price1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(details1))
                    .addComponent(productPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        product1Layout.setVerticalGroup(
            product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(details1)
                    .addComponent(priceLabel1))
                .addGap(11, 11, 11)
                .addComponent(productPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIdentityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(product1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel1)
                            .addGroup(product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buyLabel1)
                                .addComponent(productQuantitySpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(product1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(product1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(inStockLabel1))
                    .addComponent(inStockQuantityLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBasketBTN1))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        product3.setBackground(new java.awt.Color(211, 211, 172));
        product3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        product3.setMinimumSize(new java.awt.Dimension(349, 298));

        productPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        productPanel3.setMinimumSize(new java.awt.Dimension(296, 178));

        productImage3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productImage3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        productImage3.setFocusable(false);
        productImage3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        productImage3.setMaximumSize(new java.awt.Dimension(311, 174));
        productImage3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout productPanel3Layout = new javax.swing.GroupLayout(productPanel3);
        productPanel3.setLayout(productPanel3Layout);
        productPanel3Layout.setHorizontalGroup(
            productPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        productPanel3Layout.setVerticalGroup(
            productPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addToBasketBTN3.setText("ADD TO BASKET");

        productLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productLabel3.setText("PRODUCT:");

        inStockLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inStockLabel3.setText("IN STOCK:");

        buyLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buyLabel3.setText("BUY:");

        productIdentityLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inStockQuantityLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inStockQuantityLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        priceLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        priceLabel3.setText("PRICE:");

        price3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        details3.setText("DETAILS");

        javax.swing.GroupLayout product3Layout = new javax.swing.GroupLayout(product3);
        product3.setLayout(product3Layout);
        product3Layout.setHorizontalGroup(
            product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(product3Layout.createSequentialGroup()
                        .addComponent(inStockLabel3)
                        .addGap(7, 7, 7)
                        .addComponent(inStockQuantityLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToBasketBTN3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product3Layout.createSequentialGroup()
                        .addComponent(productLabel3)
                        .addGap(6, 6, 6)
                        .addComponent(productIdentityLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productQuantitySpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product3Layout.createSequentialGroup()
                        .addComponent(priceLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(price3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(details3))
                    .addComponent(productPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        product3Layout.setVerticalGroup(
            product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(details3)
                    .addComponent(priceLabel3))
                .addGap(11, 11, 11)
                .addComponent(productPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIdentityLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(product3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel3)
                            .addGroup(product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buyLabel3)
                                .addComponent(productQuantitySpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(product3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(product3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(inStockLabel3))
                    .addComponent(inStockQuantityLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBasketBTN3))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        product5.setBackground(new java.awt.Color(211, 211, 172));
        product5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        product5.setMinimumSize(new java.awt.Dimension(349, 298));

        productPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        productPanel5.setMinimumSize(new java.awt.Dimension(296, 178));

        productImage5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productImage5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        productImage5.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        productImage5.setMaximumSize(new java.awt.Dimension(311, 174));

        javax.swing.GroupLayout productPanel5Layout = new javax.swing.GroupLayout(productPanel5);
        productPanel5.setLayout(productPanel5Layout);
        productPanel5Layout.setHorizontalGroup(
            productPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage5, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        productPanel5Layout.setVerticalGroup(
            productPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage5, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addToBasketBTN5.setText("ADD TO BASKET");

        productLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productLabel5.setText("PRODUCT:");

        inStockLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inStockLabel5.setText("IN STOCK:");

        buyLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buyLabel5.setText("BUY:");

        productIdentityLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inStockQuantityLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inStockQuantityLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        priceLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        priceLabel5.setText("PRICE:");

        price5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        details5.setText("DETAILS");

        javax.swing.GroupLayout product5Layout = new javax.swing.GroupLayout(product5);
        product5.setLayout(product5Layout);
        product5Layout.setHorizontalGroup(
            product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(product5Layout.createSequentialGroup()
                        .addComponent(inStockLabel5)
                        .addGap(7, 7, 7)
                        .addComponent(inStockQuantityLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToBasketBTN5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product5Layout.createSequentialGroup()
                        .addComponent(productLabel5)
                        .addGap(6, 6, 6)
                        .addComponent(productIdentityLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productQuantitySpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product5Layout.createSequentialGroup()
                        .addComponent(priceLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(price5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(details5))
                    .addComponent(productPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        product5Layout.setVerticalGroup(
            product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product5Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(details5)
                    .addComponent(priceLabel5))
                .addGap(11, 11, 11)
                .addComponent(productPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIdentityLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(product5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel5)
                            .addGroup(product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buyLabel5)
                                .addComponent(productQuantitySpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(product5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(product5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(inStockLabel5))
                    .addComponent(inStockQuantityLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBasketBTN5))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        product2.setBackground(new java.awt.Color(211, 211, 172));
        product2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        product2.setMinimumSize(new java.awt.Dimension(349, 298));

        productPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        productPanel2.setMinimumSize(new java.awt.Dimension(296, 178));

        productImage2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productImage2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        productImage2.setFocusable(false);
        productImage2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        productImage2.setMaximumSize(new java.awt.Dimension(311, 174));

        javax.swing.GroupLayout productPanel2Layout = new javax.swing.GroupLayout(productPanel2);
        productPanel2.setLayout(productPanel2Layout);
        productPanel2Layout.setHorizontalGroup(
            productPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        productPanel2Layout.setVerticalGroup(
            productPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addToBasketBTN2.setText("ADD TO BASKET");

        productLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productLabel2.setText("PRODUCT:");

        inStockLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inStockLabel2.setText("IN STOCK:");

        buyLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buyLabel2.setText("BUY:");

        productIdentityLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inStockQuantityLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inStockQuantityLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        priceLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        priceLabel2.setText("PRICE:");

        price2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        details2.setText("DETAILS");

        javax.swing.GroupLayout product2Layout = new javax.swing.GroupLayout(product2);
        product2.setLayout(product2Layout);
        product2Layout.setHorizontalGroup(
            product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(product2Layout.createSequentialGroup()
                        .addComponent(inStockLabel2)
                        .addGap(7, 7, 7)
                        .addComponent(inStockQuantityLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToBasketBTN2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product2Layout.createSequentialGroup()
                        .addComponent(productLabel2)
                        .addGap(6, 6, 6)
                        .addComponent(productIdentityLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productQuantitySpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product2Layout.createSequentialGroup()
                        .addComponent(priceLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(price2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(details2))
                    .addComponent(productPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        product2Layout.setVerticalGroup(
            product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(details2)
                    .addComponent(priceLabel2))
                .addGap(11, 11, 11)
                .addComponent(productPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIdentityLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(product2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel2)
                            .addGroup(product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buyLabel2)
                                .addComponent(productQuantitySpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(product2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(product2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(inStockLabel2))
                    .addComponent(inStockQuantityLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBasketBTN2))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        product4.setBackground(new java.awt.Color(211, 211, 172));
        product4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        product4.setMinimumSize(new java.awt.Dimension(349, 298));

        productPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        productPanel4.setMinimumSize(new java.awt.Dimension(296, 178));

        productImage4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        productImage4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        productImage4.setFocusable(false);
        productImage4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        productImage4.setMaximumSize(new java.awt.Dimension(311, 174));
        productImage4.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout productPanel4Layout = new javax.swing.GroupLayout(productPanel4);
        productPanel4.setLayout(productPanel4Layout);
        productPanel4Layout.setHorizontalGroup(
            productPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage4, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        productPanel4Layout.setVerticalGroup(
            productPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productImage4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addToBasketBTN4.setText("ADD TO BASKET");

        productLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        productLabel4.setText("PRODUCT:");

        inStockLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        inStockLabel4.setText("IN STOCK:");

        buyLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        buyLabel4.setText("BUY:");

        productIdentityLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        inStockQuantityLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inStockQuantityLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        priceLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        priceLabel4.setText("PRICE:");

        price4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        price4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        details4.setText("DETAILS");

        javax.swing.GroupLayout product4Layout = new javax.swing.GroupLayout(product4);
        product4.setLayout(product4Layout);
        product4Layout.setHorizontalGroup(
            product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(product4Layout.createSequentialGroup()
                        .addComponent(inStockLabel4)
                        .addGap(7, 7, 7)
                        .addComponent(inStockQuantityLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addToBasketBTN4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product4Layout.createSequentialGroup()
                        .addComponent(productLabel4)
                        .addGap(6, 6, 6)
                        .addComponent(productIdentityLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buyLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productQuantitySpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, product4Layout.createSequentialGroup()
                        .addComponent(priceLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(price4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(details4))
                    .addComponent(productPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        product4Layout.setVerticalGroup(
            product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(product4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(price4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(details4)
                    .addComponent(priceLabel4))
                .addGap(11, 11, 11)
                .addComponent(productPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productIdentityLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(product4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productLabel4)
                            .addGroup(product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buyLabel4)
                                .addComponent(productQuantitySpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addGroup(product4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(product4Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(inStockLabel4))
                    .addComponent(inStockQuantityLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBasketBTN4))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ShopPanelLayout = new javax.swing.GroupLayout(ShopPanel);
        ShopPanel.setLayout(ShopPanelLayout);
        ShopPanelLayout.setHorizontalGroup(
            ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ShopPanelLayout.createSequentialGroup()
                .addGroup(ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(product, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(product3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(product1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(product4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(product2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(product5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ShopPanelLayout.setVerticalGroup(
            ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(product, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addGroup(ShopPanelLayout.createSequentialGroup()
                    .addComponent(product1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(product4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ShopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(product3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ShopPanelLayout.createSequentialGroup()
                        .addComponent(product2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(product5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        MainTitleBarPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MainTitleBarPanel.setOpaque(false);

        SignUpWindowBTN.setBackground(new java.awt.Color(248, 198, 2));
        SignUpWindowBTN.setFont(new java.awt.Font("Georgia Pro Semibold", 1, 12)); // NOI18N
        SignUpWindowBTN.setText("SIGN UP");
        SignUpWindowBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SignUpWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignUpWindowBTNActionPerformed(evt);
            }
        });

        ContactUsWindowBTN.setBackground(new java.awt.Color(248, 198, 2));
        ContactUsWindowBTN.setFont(new java.awt.Font("Georgia Pro Semibold", 1, 12)); // NOI18N
        ContactUsWindowBTN.setText("CONTACT US");
        ContactUsWindowBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ContactUsWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContactUsWindowBTNActionPerformed(evt);
            }
        });

        AboutUsWindowBTN.setBackground(new java.awt.Color(248, 198, 2));
        AboutUsWindowBTN.setFont(new java.awt.Font("Georgia Pro Semibold", 1, 12)); // NOI18N
        AboutUsWindowBTN.setText("ABOUT US");
        AboutUsWindowBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AboutUsWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutUsWindowBTNActionPerformed(evt);
            }
        });

        MyBasketWindowBTN.setBackground(new java.awt.Color(248, 198, 2));
        MyBasketWindowBTN.setFont(new java.awt.Font("Georgia Pro Semibold", 1, 12)); // NOI18N
        MyBasketWindowBTN.setText("MY BASKET");
        MyBasketWindowBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MyBasketWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyBasketWindowBTNActionPerformed(evt);
            }
        });

        OpenShopBTN.setBackground(new java.awt.Color(248, 198, 2));
        OpenShopBTN.setFont(new java.awt.Font("Georgia Pro Semibold", 1, 12)); // NOI18N
        OpenShopBTN.setText("SHOP");
        OpenShopBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        OpenShopBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenShopBTNActionPerformed(evt);
            }
        });

        LogInWindowBTN.setBackground(new java.awt.Color(248, 198, 2));
        LogInWindowBTN.setFont(new java.awt.Font("Georgia Pro Semibold", 1, 12)); // NOI18N
        LogInWindowBTN.setText("LOG IN");
        LogInWindowBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        LogInWindowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogInWindowBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainTitleBarPanelLayout = new javax.swing.GroupLayout(MainTitleBarPanel);
        MainTitleBarPanel.setLayout(MainTitleBarPanelLayout);
        MainTitleBarPanelLayout.setHorizontalGroup(
            MainTitleBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainTitleBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OpenShopBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(LogInWindowBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(SignUpWindowBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(ContactUsWindowBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(AboutUsWindowBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(MyBasketWindowBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        MainTitleBarPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {AboutUsWindowBTN, ContactUsWindowBTN, LogInWindowBTN, MyBasketWindowBTN, SignUpWindowBTN});

        MainTitleBarPanelLayout.setVerticalGroup(
            MainTitleBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainTitleBarPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MainTitleBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SignUpWindowBTN)
                    .addComponent(ContactUsWindowBTN)
                    .addComponent(AboutUsWindowBTN)
                    .addComponent(MyBasketWindowBTN)
                    .addComponent(OpenShopBTN)
                    .addComponent(LogInWindowBTN))
                .addContainerGap())
        );

        menuPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        menuPanel.setMinimumSize(new java.awt.Dimension(259, 620));
        menuPanel.setOpaque(false);
        menuPanel.setPreferredSize(new java.awt.Dimension(259, 620));

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 255, Short.MAX_VALUE)
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        UserNameLabel.setFont(new java.awt.Font("Segoe Script", 1, 18)); // NOI18N
        UserNameLabel.setForeground(new java.awt.Color(51, 0, 51));

        titlePanel.setOpaque(false);

        mainTitleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/onlineshop/ameliasFarm.png"))); // NOI18N

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pagePanel.setMinimumSize(new java.awt.Dimension(1080, 34));
        pagePanel.setOpaque(false);

        mainPreviousBTN.setText("PREVIOUS");

        pageLabel.setBackground(new java.awt.Color(255, 204, 0));
        pageLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pageLabel.setForeground(new java.awt.Color(255, 255, 255));
        pageLabel.setText("Page:");

        mainNextBTN.setText("NEXT");

        pageNumLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pageNumLabel.setForeground(new java.awt.Color(255, 255, 255));
        pageNumLabel.setText("1");

        javax.swing.GroupLayout pagePanelLayout = new javax.swing.GroupLayout(pagePanel);
        pagePanel.setLayout(pagePanelLayout);
        pagePanelLayout.setHorizontalGroup(
            pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pagePanelLayout.createSequentialGroup()
                .addComponent(mainPreviousBTN)
                .addGap(412, 412, 412)
                .addComponent(pageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pageNumLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 461, Short.MAX_VALUE)
                .addComponent(mainNextBTN))
        );
        pagePanelLayout.setVerticalGroup(
            pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pagePanelLayout.createSequentialGroup()
                .addGroup(pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mainPreviousBTN)
                        .addComponent(pageLabel)
                        .addComponent(pageNumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(mainNextBTN))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainFrontPanelLayout = new javax.swing.GroupLayout(mainFrontPanel);
        mainFrontPanel.setLayout(mainFrontPanelLayout);
        mainFrontPanelLayout.setHorizontalGroup(
            mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainFrontPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(mainFrontPanelLayout.createSequentialGroup()
                            .addComponent(UserNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(99, 99, 99)
                            .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AdminPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(MainTitleBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainFrontPanelLayout.createSequentialGroup()
                        .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ShopPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        mainFrontPanelLayout.setVerticalGroup(
            mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainFrontPanelLayout.createSequentialGroup()
                .addGroup(mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AdminPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainFrontPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(MainTitleBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainFrontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainFrontPanelLayout.createSequentialGroup()
                        .addComponent(ShopPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        mainBackPanel.add(mainFrontPanel);
        mainFrontPanel.setBounds(0, 0, 1410, 900);

        mainBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/onlineshop/barn 2.jpg"))); // NOI18N
        mainBackPanel.add(mainBackground);
        mainBackground.setBounds(0, 0, 1410, 900);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainBackPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1410, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainBackPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * button to open the add new category window (ADMIN ONLY)
     * @param evt 
     */
    
    private void AddCategoryWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCategoryWindowBTNActionPerformed
        
        AddCategoryWindow.setVisible(true);
        AddCategoryWindow.setLocationRelativeTo(null);
        
    }//GEN-LAST:event_AddCategoryWindowBTNActionPerformed

    /**
     * button to add new row to the add new category product table
     * @param evt 
     */
    
    private void ANCAddNewProductBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANCAddNewProductBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel)ANCProductTable.getModel();
        
        tm.addRow(new Object[]{"","","","","",""});
        
    }//GEN-LAST:event_ANCAddNewProductBTNActionPerformed

    /**
     * button to remove row from the add new category product table
     * @param evt 
     */
    
    private void ANCRemoveProductBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANCRemoveProductBTNActionPerformed
       
        try{
            
            DefaultTableModel tm = (DefaultTableModel)ANCProductTable.getModel();

            int selected = ANCProductTable.getSelectedRow();

            tm.removeRow(selected);
            
        }catch(ArrayIndexOutOfBoundsException e){
            
            JOptionPane.showMessageDialog(null, "Please Select Product To Remove");
        }
    }//GEN-LAST:event_ANCRemoveProductBTNActionPerformed

    /**
     * button to save the new category details
     * @param evt 
     */
    
    private void ANCSaveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANCSaveBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel)ANCProductTable.getModel();
        
        int cols = tm.getColumnCount();
        int rows = tm.getRowCount();
        
        Object[][] products = new Object[rows][cols];
        
        for(int i = 0; i < rows; i++){
        
            for(int j = 0; j < cols; j++){
               
                products[i][j] = tm.getValueAt(i, j);

            }
        }
        
        category = ANCCategoryInput.getText().toUpperCase().trim();
 
        if(!category.isEmpty()){
                
            try {
                
                categories = data.getAllCategories();
                
                if(!categories.contains(category)){

                    data.createProductTable(category);

                    data.addToProductDatabase(category, products);

                    removeCategoryButtons();
                
                    createCategoryButtons();
                    
                    File f = new File(imageFolder + category);
                    f.mkdir();
                    
                    for(int i = 0; i < tm.getRowCount(); i++){
                        
                        String from = String.valueOf(tm.getValueAt(i, 6));
                        
                        Path A = Paths.get(imageFolder + from);
                        Path B = Paths.get(imageFolder + category + "\\" + from);
                        
                        try {
                            
                            Files.move(A, B, StandardCopyOption.REPLACE_EXISTING);
                            
                        } catch (IOException ex) {
                            
                            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    JOptionPane.showMessageDialog(null, category + " Added To Database");
                    
                    ANCCategoryInput.setText("");
                    
                    tm.setRowCount(0);
                
                }else{
                    
                    JOptionPane.showMessageDialog(null, "Category Name Has Already Been Taken");
                }
                
            } catch (SQLException ex) {

                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        }else{

            JOptionPane.showMessageDialog(null, "Please Enter Category Name");   
        }   
    }//GEN-LAST:event_ANCSaveBTNActionPerformed

    /**
     * button to search for image to add to the product
     * @param evt 
     */
    
    private void ANCSearchImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ANCSearchImageActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel)ANCProductTable.getModel();
        
        String image = "";
        
        JFileChooser fc = new JFileChooser(imageFolder);
        
        int choice = fc.showOpenDialog(null);
        
        if(choice == JFileChooser.APPROVE_OPTION){
            
            image = fc.getSelectedFile().getName();
        }
        
        int selected = ANCProductTable.getSelectedRow();
        
        if(selected != -1){
            
            tm.setValueAt(image, selected, 6);
            
        }else{
            
            JOptionPane.showMessageDialog(null, "Please Select product Row To Add Image To");
        }
        
    }//GEN-LAST:event_ANCSearchImageActionPerformed

    /**
     * opens up the my basket window
     * @param evt 
     */
    
    private void MyBasketWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyBasketWindowBTNActionPerformed
        
        BasketWindow.setVisible(true);
        BasketWindow.setLocationRelativeTo(null);
               
        float total = 0.0F;
        
        DefaultTableModel tm = (DefaultTableModel)BasketTable.getModel();
 
        for(int i = 0; i < tm.getRowCount(); i ++){
            
            Object t = tm.getValueAt(i, 5);
            total += Float.valueOf(t.toString().substring(1, t.toString().length()-1));

        }
        
        BWTotalOutput.setText(nf.format(total));
        
    }//GEN-LAST:event_MyBasketWindowBTNActionPerformed

    /**
     * button to open up the add new product window
     * @param evt 
     */
    
    private void EditProductWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditProductWindowBTNActionPerformed
       
        try {
            
            EditProductWindow.setVisible(true);
            EditProductWindow.setLocationRelativeTo(null);         
            
            categories = data.getAllCategories();
            
            EPCategoryCombo.removeAllItems();
            
            categories.forEach(c -> EPCategoryCombo.addItem(c));
            
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }//GEN-LAST:event_EditProductWindowBTNActionPerformed

    /**
    *add new product combo box
    */
    
    private void EPCategoryComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EPCategoryComboActionPerformed
        
        try {
            
            DefaultTableModel tm = (DefaultTableModel)EPProductTable.getModel();
            
            category = EPCategoryCombo.getSelectedItem().toString();
            
            Object[][] products = data.getProducts(category);
            
            tm.setRowCount(0);
            
            for(Object[] p: products){
                
                tm.addRow(new Object[]{p[0],p[1],p[2],p[3],p[4],p[5],p[6],p[7]});
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EPCategoryComboActionPerformed

    /**
     * removes Selected product
     * @param evt 
     */
    
    private void EPRemoveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EPRemoveBTNActionPerformed
       
        DefaultTableModel tm = (DefaultTableModel)EPProductTable.getModel();
        
        int selected = EPProductTable.getSelectedRow();
        
        if(selected != -1){
            
            int choice = JOptionPane.showConfirmDialog(null, "Remove Product From Databse?", "Remove Item", JOptionPane.YES_NO_OPTION);
            
            if(choice == 0){
                
                tm.removeRow(selected);
            }            
        }        
    }//GEN-LAST:event_EPRemoveBTNActionPerformed

    /**
     * adds image path to selected product
     * @param evt 
     */
    
    private void EPAddImageBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EPAddImageBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel)EPProductTable.getModel();
        
        category = EPCategoryCombo.getSelectedItem().toString();
        
        String image = "";
        
        JFileChooser fc = new JFileChooser(imageFolder + category);
        
        int choice = fc.showOpenDialog(null);
        
        if(choice == JFileChooser.APPROVE_OPTION){
            
            image = fc.getSelectedFile().getName();
        }
        
        int selected = EPProductTable.getSelectedRow();
        
        if(selected != -1){
            
            tm.setValueAt(image, selected, 7);
            
        }else{
            
            JOptionPane.showMessageDialog(null, "Please Select product Row To Add Image To");
        }             
    }//GEN-LAST:event_EPAddImageBTNActionPerformed

    /**
     * updates product List
     * @param evt 
     */
    
    private void EPUpdateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EPUpdateBTNActionPerformed

        try {
            
            DefaultTableModel tm = (DefaultTableModel)EPProductTable.getModel();
            
            category = EPCategoryCombo.getSelectedItem().toString();
            
            int row = tm.getRowCount();
            int col = tm.getColumnCount();
            
            Object[][] products = new Object[row][col];
            
            for(int i = 0; i < row; i++){
                for(int j = 0; j < col; j++){
                    
                    products[i][j] = tm.getValueAt(i,j);
                }
            }
            
            File f = new File(imageFolder + category);
            
            if(!f.exists()){
                
                f.mkdir();
            }
            
            for(int i = 0; i < tm.getRowCount(); i++){

                String from = String.valueOf(tm.getValueAt(i, 7));

                Path A = Paths.get(imageFolder + from);
                Path B = Paths.get(imageFolder + category + "\\" + from);

                try {

                    Files.move(A, B, StandardCopyOption.REPLACE_EXISTING);

                } catch (IOException ex) {

                    Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            data.upDateCategory(category, products);
            
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }//GEN-LAST:event_EPUpdateBTNActionPerformed

    /**
     * button to add a new product to category
     * @param evt 
     */
    
    private void EPAddProductBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EPAddProductBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel)EPProductTable.getModel();
        
        tm.addRow(new Object[]{"","","","","","","","",});
        
    }//GEN-LAST:event_EPAddProductBTNActionPerformed

    /**
     * opens the customer window
     * @param evt 
     */
    
    private void CustomerWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CustomerWindowBTNActionPerformed
        
        try {
            
            DefaultTableModel tm = (DefaultTableModel)CWCustomerTable.getModel();
            
            CustomerWindow.setVisible(true);
            CustomerWindow.setLocationRelativeTo(null);
            
            Object[][] customers = customerData.getCustomers();
            
            tm.setRowCount(0);
            
            for(Object[] c : customers){
                
                tm.addRow(new Object[]{c[0], c[3], c[1], c[2]});
            }
            
            
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }//GEN-LAST:event_CustomerWindowBTNActionPerformed

    /**
    *button to show the products and category menu
    */
    
    private void OpenShopBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenShopBTNActionPerformed
         
        menuPanel.setVisible(true); 

        menuPanel.setLocation(MainTitleBarPanel.getX(), MainTitleBarPanel.getY() + 48);
        menuPanel.repaint();

    }//GEN-LAST:event_OpenShopBTNActionPerformed

    /**
     * button to open the sale window
     * @param evt 
     */
    
    private void SalesWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalesWindowBTNActionPerformed
         
        try {
            
            SalesWindow.setVisible(true);
            SalesWindow.setLocationRelativeTo(null);

            String date = df.format(SWDateChooser.getSelectedDate().getTime());
            
            DefaultTableModel tm = (DefaultTableModel)SWSalesTable.getModel();
            
            Object[][] salesByDate = data.getSalesByDate(date);
            
            tm.setRowCount(0);
            
            for(Object[] s : salesByDate){
                
                tm.addRow(new Object[]{s[0], s[1], s[2], s[3], s[4], s[5]});
                
            }
            
            categories = data.getAllCategories();
            
            SWCategoryCombo.removeAllItems();
            
            categories.forEach(c -> SWCategoryCombo.addItem(c));
                    
        } catch (SQLException ex) {
                        
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_SalesWindowBTNActionPerformed

    /**
     * button to open the remove category window
     * @param evt 
     */
    
    private void RemoveCategoryWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveCategoryWindowBTNActionPerformed
        
        try {
            
            RemoveCategoryWindow.setVisible(true);
            RemoveCategoryWindow.setLocationRelativeTo(null);
            
            categories = data.getAllCategories();
            categories.forEach(c -> RCCombo.addItem(c));
            
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }//GEN-LAST:event_RemoveCategoryWindowBTNActionPerformed

    /**
     * button to remove chosen category
     * @param evt 
     */
    
    private void RCRemoveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RCRemoveBTNActionPerformed
        
        category = String.valueOf(RCCombo.getSelectedItem());
        
        int choice = JOptionPane.showConfirmDialog(null, "Do You Really Want To Remove \nCategory: '" + category + "'\nPermanetly From The DataBase?", "Remove Category", JOptionPane.OK_CANCEL_OPTION);
            
        if(choice == 0){
            
            try {
                
                data.RemoveCategory(category);
                categories = data.getAllCategories();
                
                RCCombo.removeAllItems();
                categories.forEach(c -> RCCombo.addItem(c));
                
                removeCategoryButtons();
                createCategoryButtons();
                
            } catch (SQLException ex) {
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_RCRemoveBTNActionPerformed

    /**
     * button to open window to contact customer
     * @param evt 
     */
    
    private void CWContactBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CWContactBTNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CWContactBTNActionPerformed

    /**
     * button to open up group mail out window
     * @param evt 
     */
    
    private void CWGroupMailOutBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CWGroupMailOutBTNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CWGroupMailOutBTNActionPerformed

    /**
     * button to search sales by type
     * @param evt 
     */
    
    private void SWSearchBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SWSearchBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel)SWSalesTable.getModel();
        
        try{
            
            if(SWCategoryRadio.isSelected()){
                
                String date = df.format(SWDateChooser.getSelectedDate().getTime());
                category = String.valueOf(SWCategoryCombo.getSelectedItem());
                
                Object[][] salesByCategory = data.getSalesByDateAndCategory(date, category);
                
                tm.setRowCount(0);
                
                for(Object[] s : salesByCategory){
                    
                    if(s[0] != null){
                        
                        tm.addRow(new Object[]{s[0], s[1], s[2], s[3], s[4], s[5]});
                    }
                }   
            }
            
            if(SWProductRadio.isSelected()){
                
                String date = df.format(SWDateChooser.getSelectedDate().getTime());
                productName = String.valueOf(SWProductCombo.getSelectedItem());
                
                Object[][] salesByProduct = data.getSalesByDateAndProduct(date, productName);
                
                tm.setRowCount(0);
                
                for(Object[] s : salesByProduct){
                    
                    if(s[0] != null){
                        
                        tm.addRow(new Object[]{s[0], s[1], s[2], s[3], s[4], s[5]});
                    }
                }
            }
  
        } catch (SQLException ex) {
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_SWSearchBTNActionPerformed

    /**
     * button to print the sales table
     * @param evt 
     */
    
    private void SWPrintBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SWPrintBTNActionPerformed
        
        try {
            
            SWSalesTable.print();
            
        } catch (PrinterException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SWPrintBTNActionPerformed

    /**
     * button to open the log in window
     * @param evt 
     */
    
    private void LogInWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogInWindowBTNActionPerformed
        
        LogInWindow.setVisible(true);
        LogInWindow.setLocationRelativeTo(null);
        
    }//GEN-LAST:event_LogInWindowBTNActionPerformed

    /**
     * login button for log in window
     * @param evt 
     */
    
    private void LILogInBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LILogInBTNActionPerformed
    
        if(!LIAdminRadio.isSelected()){
            
            try {

                String userName = LIUserNameInput.getText();
                char[] password = LIPasswordInput.getPassword();

                boolean admit = customerData.getLogin(userName, password);

                if(admit){

                    loggedIn = true;

                    user = userName;

                    UserNameLabel.setText("Welcome " + user);

                    LogInWindow.setVisible(false);
                    
                    AdminPanel.setVisible(false);
                    
                    AboutUsAdminBTNS.setVisible(false);

                }else{

                    failedLogInLabel.setText("Username Or Password Incorrect");
                }

            }catch (SQLException | NoSuchAlgorithmException ex) {

                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);

            }
            
        }else{
            
            try{
                
                String userName = LIUserNameInput.getText();
                char[] password = LIPasswordInput.getPassword();
                
                boolean admit = data.getAdminLogin(userName, password);
                
                if(admit){
                    
                    AdminPanel.setVisible(true);
                    
                    user = userName;
                    
                    UserNameLabel.setText("Welcome " + user);

                    LogInWindow.setVisible(false);
                    
                    AboutUsAdminBTNS.setVisible(true);
                    
                }else{

                    failedLogInLabel.setText("Username Or Password Incorrect");
                }
                
            } catch (NoSuchAlgorithmException | SQLException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_LILogInBTNActionPerformed

    /**
     * button to open up the sign up[ window from the log in window 
     * @param evt 
     */
    
    private void LICreateAccountBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LICreateAccountBTNActionPerformed
        
        LogInWindow.setVisible(false);
        
        SignUpWindow.setVisible(true);
        SignUpWindow.setLocationRelativeTo(null);
        
    }//GEN-LAST:event_LICreateAccountBTNActionPerformed

    /**
     * button to open up the sign up window
     * @param evt 
     */
    
    private void SignUpWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignUpWindowBTNActionPerformed
        
        SignUpWindow.setVisible(true);
        SignUpWindow.setLocationRelativeTo(null);
        
    }//GEN-LAST:event_SignUpWindowBTNActionPerformed

    /**
     * enter button for the sign up window
     * @param evt 
     */
    
    private void SUEnterBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SUEnterBTNActionPerformed
        
        String name = SUNameInput.getText().toUpperCase().trim();
        String userName = SUUsernameInput.getText().trim();
        String eMail = SUEMailInput.getText().trim();
        char[] password = SUPasswordInput.getPassword();
        char[] ReTypePassword = SUReTypePasswordInput.getPassword();
               
        if(!name.isEmpty() && !userName.isEmpty() && !eMail.isEmpty()){
                
            if(Arrays.equals(password, ReTypePassword)){
                
                    try {
                        
                        boolean taken = customerData.checkUsername(userName);
                        
                        if(taken == false){
                        
                            try {

                                customerData.addCustomerToDatabase(name, eMail, userName, password);
                                
                                user = userName;
                                UserNameLabel.setText("Welcome " + user);
                                
                                loggedIn = true;

                            }catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException ex) {

                                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            
                            JOptionPane.showMessageDialog(null, "Username Has Already Been Taken");
                        }
                    }catch (SQLException ex) {

                        Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                    }       
            }else{
                
                JOptionPane.showMessageDialog(null, "Passwords Do Not Match");
            }     
        }else{
            
            JOptionPane.showMessageDialog(null, "Please Fill In All Fields");
        }       
    }//GEN-LAST:event_SUEnterBTNActionPerformed

    /**
     * button to open up the contact us page
     * @param evt 
     */
    
    private void ContactUsWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContactUsWindowBTNActionPerformed
        
        ContactUsWindow.setVisible(true);
        ContactUsWindow.setLocationRelativeTo(null);
        
        if(loggedIn){
            
            try {
                
                Object[] customer = customerData.getLoggedInCustomer(user, loggedIn);
                
                CUNameInput.setText(String.valueOf(customer[1]));
                CUEMailInput.setText(String.valueOf(customer[2]));
                
            } catch (SQLException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }//GEN-LAST:event_ContactUsWindowBTNActionPerformed

    /**
     * send the message in the contact us window
     * @param evt 
     */
    
    private void CUSendBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CUSendBTNActionPerformed
        
        String name = CUNameInput.getText().trim();
        String eMail = CUEMailInput.getText().trim();
        String subject = CUSubjectInput.getText().trim();
        
        if(!name.isBlank() && !eMail.isBlank()){    
            
            File file = new File(subject + ".txt");

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){

                bw.write(CUNameInput.getText());
                bw.newLine();
                bw.write(subject);
                bw.newLine();
                CUTextArea.write(bw);
            
        //sends Email
        
                Mailing mail = new Mailing(eMail,subject,file);

            } catch (IOException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            
            JOptionPane.showMessageDialog(null, "Name And Email Must Be Completed");
        }
    }//GEN-LAST:event_CUSendBTNActionPerformed

    /**
     * opens the add admin window
     * @param evt 
     */
    
    private void AddAdminWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddAdminWindowBTNActionPerformed
        
        AddAdminWindow.setVisible(true);
        AddAdminWindow.setLocationRelativeTo(null);
        
    }//GEN-LAST:event_AddAdminWindowBTNActionPerformed

    /**
    *button to enter the new admin details into the database
    */
    
    private void AAEnterBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AAEnterBTNActionPerformed
        
        String userName = AAUsernameInput.getText().trim();
        char[] password = AAPasswordInput.getPassword();
        char[] ReTypePassword = AAReTypePasswordInput.getPassword();
        
        if(!userName.isBlank()){
            
            if(Arrays.equals(password, ReTypePassword)){
                
                try {
                    
                    data.addAdmin(userName, password);
                    
                    AddAdminWindow.setVisible(false);
                    
                } catch (SQLException ex) {
                    
                    Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                }               
            }else{
            
            AAFailedAtemptLabel.setText("Passwords Do Not Match!");
        }    
        }else{
            
            AAFailedAtemptLabel.setText("Username Cannot Be Blank!");
        }       
    }//GEN-LAST:event_AAEnterBTNActionPerformed

    private void addToBasketBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToBasketBTNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addToBasketBTNActionPerformed

    /**
     * button to open the about us window
     * @param evt 
     */
    
    private void AboutUsWindowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutUsWindowBTNActionPerformed
       
        AUScroll.setOpaque(false);
        AUScroll.getViewport().setOpaque(false);
        AUScroll.setBorder(null);
        AUScroll.setViewportBorder(null);
        
        AboutUsTextArea.setBorder(null);
        AboutUsTextArea.setBackground(new Color(0,0,0,64));
        
        Timer t;
        
        AboutUsBackground.setSize(1185, 715);
        
        AboutUsWindow.setVisible(true);
        AboutUsWindow.setLocationRelativeTo(null);
        
        File AboutPics = new File(aboutFolder);
        
        String[] pics = AboutPics.list();
        
        ImageIcon pic = new ImageIcon(aboutFolder + pics[0]);
        AboutUsBackground.setIcon(changePicSize(pic, AboutUsBackground));
             
        t = new Timer(5000, new ActionListener () {
                
            int i = 1;
                
            @Override
            public void actionPerformed(ActionEvent e) {

                ImageIcon pic = new ImageIcon(aboutFolder + pics[i++]);
                AboutUsBackground.setIcon(changePicSize(pic, AboutUsBackground));
                
                AboutUsBackground.getParent().repaint();

                if(i == pics.length){

                    i = 0;
                }
            }
        });
            
        t.start();
    }//GEN-LAST:event_AboutUsWindowBTNActionPerformed

    /**
     * opens the change picture window and populates the combo box with background labels
     * @param evt 
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        ChangePicturesWindow.setVisible(true);
        ChangePicturesWindow.setLocationRelativeTo(null);
        
        CPCombo.removeAllItems();
        backgroundsArray.forEach(b -> CPCombo.addItem(b));
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * button to preview the picture int he preview label
     * @param evt 
     */
    private void CPAddImageBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CPAddImageBTNActionPerformed
        
        JFileChooser jf = new JFileChooser(programPics);
        
        int choice = jf.showOpenDialog(null);
        
        if(choice == JFileChooser.APPROVE_OPTION){
            
            imageChange = new ImageIcon(jf.getSelectedFile().getPath());
             
            CPPreviewLabel.setIcon(changePicSize(imageChange, CPPreviewLabel));
            CPPreviewLabel.revalidate();
        }
    }//GEN-LAST:event_CPAddImageBTNActionPerformed

    /**
     * button to apply background pic to chosen background and save choice to file
     * @param evt 
     */
    
    private void CPApplyBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CPApplyBTNActionPerformed
              
        int label = CPCombo.getSelectedIndex();
            
        backgrounds[label].setIcon(changePicSize(imageChange, backgrounds[label]));
        Component comp =  (Component) backgrounds[label];
        JFrame frame = (JFrame) SwingUtilities.getRoot(comp);
        frame.repaint();
        
        JOptionPane.showMessageDialog(null, "Background Picture Changed");
        
        File sB = new File(programPics + "savedBackgrounds.txt");
        
        if(sB.exists()){
            
            try(FileWriter fw = new FileWriter(sB, true)){
                
                fw.write("" + label);
                fw.write("\n");
                fw.write(" " + imageChange);
                fw.write("\n");
                
            } catch (FileNotFoundException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                
            } catch (IOException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            
            try(FileWriter fw = new FileWriter(sB)){
                            
                fw.write("" + label);
                fw.write("\n");
                fw.write(" " + imageChange);
                fw.write("\n");
                
            } catch (FileNotFoundException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                
            } catch (IOException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }  
    }//GEN-LAST:event_CPApplyBTNActionPerformed

    private void CPComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CPComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CPComboActionPerformed

    /**
     * button to remove a chosen item t=from the my basket table
     * @param evt 
     */
    
    private void MBRemoveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MBRemoveBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel) BasketTable.getModel();
        
        int selected = BasketTable.getSelectedRow();
        
        if(selected != -1){
         
            try {
                
                code = String.valueOf(tm.getValueAt(selected, 0));
                String prodName = String.valueOf(tm.getValueAt(selected, 1));
                
                int amount = Integer.parseInt(tm.getValueAt(selected,3).toString());
                int stock  = data.getQuantity(code);   
                int updateAmount = stock + amount;
                
                category = data.findProductCategory(code);
                
                data.updateQuantity(updateAmount, prodName, category);
                
                tm.removeRow(selected);
                
            } catch (SQLException ex) {
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }      
        }else{
            
            JOptionPane.showMessageDialog(null, "Please Select An Item To Remove");
        }
    }//GEN-LAST:event_MBRemoveBTNActionPerformed

    /**
     * button to go to check out with chosen items
     * @param evt 
     */
    
    private void BWCheckoutBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BWCheckoutBTNActionPerformed
        
        DefaultTableModel tm = (DefaultTableModel) BasketTable.getModel();
        int row = tm.getRowCount();
        int col = tm.getColumnCount();
        
        Object[][]items = new Object[row][col];
        
        for(int i = 0 ; i < row; i++){
            for(int j = 0 ; j < col; j++){
                
                items[i][j] = tm.getValueAt(i, j);
                
            }
        }     
               
        try {

            customerData.saveCustomerOrder(items, user);
            data.addToSales(items, LocalDate.now());
            
            JOptionPane.showMessageDialog(null, "Thank You For Your Purchase");
            
            tm.setRowCount(0);
            
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BWCheckoutBTNActionPerformed

    /**
     * button to get customers last order
     * @param evt 
     */
    private void CWGetLastOrderBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CWGetLastOrderBTNActionPerformed
        
        try {
            
            DefaultTableModel tm = (DefaultTableModel)CWCustomerTable.getModel();
            DefaultTableModel tm2 = (DefaultTableModel)CustomerOrderTable.getModel();
            
            int selected = CWCustomerTable.getSelectedRow();
            
            if(selected != -1){
                
                CustomerOrderWindow.setVisible(true);
                CustomerOrderWindow.setLocationRelativeTo(null);
            
                String username = String.valueOf(tm.getValueAt(selected, 1));

                Object[][]items = customerData.getCustomerOrder(username);

                float total = 0.0F;

                tm2.setRowCount(0);

                for(Object[] i : items){

                    tm2.addRow(new Object[]{i[0], i[1], i[2], i[3], i[4], i[5]});

                    Object t = i[5];
                    total += Float.valueOf(t.toString().substring(1));
                }

                String grandTotal = nf.format(total);

                COTotalOutput.setText(grandTotal);
                
            }else{
                
                JOptionPane.showMessageDialog(null, "Please Select Customer");
            }
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }//GEN-LAST:event_CWGetLastOrderBTNActionPerformed

    /**
     * sales window category combo box 
     * @param evt 
     */
    
    private void SWCategoryComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SWCategoryComboActionPerformed
        
        try {
            category = String.valueOf(SWCategoryCombo.getSelectedItem());
            
            Object[][] products = data.getProducts(category);
            
            SWProductCombo.removeAllItems();
            
            for(Object[] p : products){
                
                productName = String.valueOf(p[0]);
                
                SWProductCombo.addItem(productName);
            }
        } catch (SQLException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SWCategoryComboActionPerformed

    /**
     * but to enable the about us text area to be editable
     * @param evt 
     */
    
    private void AUEditBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AUEditBTNActionPerformed
       
       AboutUsTextArea.setEditable(true);
       
       AboutUsTextArea.repaint();
       AboutUsWindow.repaint();
       
    }//GEN-LAST:event_AUEditBTNActionPerformed

    /**
     *save button for about us info 
     * @param evt 
     */
    
    private void AUSaveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AUSaveBTNActionPerformed
        
        File aboutUs = new File(programPics + "AboutUs.txt");
        
        try(FileWriter fw = new FileWriter(aboutUs)){
            
            AboutUsTextArea.write(fw);
            
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            
            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JOptionPane.showMessageDialog(null, "Message Saved");
        
    }//GEN-LAST:event_AUSaveBTNActionPerformed

    /**
     * button to get the last order of the customer and populate the my basket table
     * @param evt 
     */
    
    private void MBLastOrderBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MBLastOrderBTNActionPerformed

        DefaultTableModel tm = (DefaultTableModel)BasketTable.getModel();
        
        int rowCount = tm.getRowCount();
        
        if(rowCount > 0){
            
            Object[] options = {"Add To Order", "Replace Order", "Cancel"};
            
            int choice = JOptionPane.showOptionDialog(null, "How Would You Like To Change Your Order?", "Adding Last Order", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
            
            switch (choice){
                
                case 0: 
                    
                    try {

                        Object[][] lastOrder = customerData.getCustomerOrder(user);

                        for(Object[] o : lastOrder){

                            tm.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5]});
                        }

                        float total = 0.0F;

                        for(int i = 0; i < tm.getRowCount(); i++){

                           total += Float.parseFloat(String.valueOf(tm.getValueAt(i, 5)).substring(1));
                        }

                       BWTotalOutput.setText(nf.format(total));

                       break;

                    } catch (SQLException ex) {

                    Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                    
                    break;
                    
                    }  
                
                case 1: 
                    
                    try {

                        Object[][] lastOrder = customerData.getCustomerOrder(user);

                        tm.setRowCount(0);

                        for(Object[] o : lastOrder){

                            tm.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5]});
                        }

                        float total = 0.0F;

                        for(int i = 0; i < tm.getRowCount(); i++){

                           total += Float.parseFloat(String.valueOf(tm.getValueAt(i, 5)).substring(1));
                        }

                       BWTotalOutput.setText(nf.format(total));
                       
                       break;

                    } catch (SQLException ex) {

                        Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                        
                        break;
                    }       
            }

        }else{
        
            try {

                Object[][] lastOrder = customerData.getCustomerOrder(user);

                tm.setRowCount(0);

                for(Object[] o : lastOrder){

                    tm.addRow(new Object[]{o[0], o[1], o[2], o[3], o[4], o[5]});
                }

                float total = 0.0F;

                for(int i = 0; i < tm.getRowCount(); i++){

                   total += Float.parseFloat(String.valueOf(tm.getValueAt(i, 5)).substring(1));
                }

               BWTotalOutput.setText(nf.format(total));

            } catch (SQLException ex) {

                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }
    }//GEN-LAST:event_MBLastOrderBTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ShopFront.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShopFront.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShopFront.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShopFront.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShopFront().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AAEnterBTN;
    private javax.swing.JLabel AAFailedAtemptLabel;
    private javax.swing.JPasswordField AAPasswordInput;
    private javax.swing.JLabel AAPasswordLabel;
    private javax.swing.JPasswordField AAReTypePasswordInput;
    private javax.swing.JLabel AAReTypePasswordLabel;
    private javax.swing.JTextField AAUsernameInput;
    private javax.swing.JLabel AAUsernameLabel;
    private javax.swing.JButton ANCAddNewProductBTN;
    private javax.swing.JTextField ANCCategoryInput;
    private javax.swing.JLabel ANCCategoryLabel;
    private javax.swing.JTable ANCProductTable;
    private javax.swing.JButton ANCRemoveProductBTN;
    private javax.swing.JButton ANCSaveBTN;
    private javax.swing.JButton ANCSearchImage;
    private javax.swing.JButton AUEditBTN;
    private javax.swing.JButton AUSaveBTN;
    private javax.swing.JScrollPane AUScroll;
    private javax.swing.JPanel AboutUsAdminBTNS;
    private javax.swing.JLabel AboutUsBackground;
    private javax.swing.JTextArea AboutUsTextArea;
    private javax.swing.JFrame AboutUsWindow;
    private javax.swing.JButton AboutUsWindowBTN;
    private javax.swing.JLabel AddAdminBackground;
    private javax.swing.JFrame AddAdminWindow;
    private javax.swing.JButton AddAdminWindowBTN;
    private javax.swing.JLabel AddCategoryBackground;
    private javax.swing.JFrame AddCategoryWindow;
    private javax.swing.JButton AddCategoryWindowBTN;
    private javax.swing.JLabel AddProductBackground;
    private javax.swing.JPanel AdminPanel;
    private javax.swing.JButton BWCheckoutBTN;
    private javax.swing.JLabel BWTotalLabel;
    private javax.swing.JLabel BWTotalOutput;
    private javax.swing.JTable BasketTable;
    private javax.swing.JFrame BasketWindow;
    private javax.swing.JTextField COTotalOutput;
    private javax.swing.JButton CPAddImageBTN;
    private javax.swing.JButton CPApplyBTN;
    private javax.swing.JLabel CPBackgroundLabel;
    private javax.swing.JComboBox<String> CPCombo;
    private javax.swing.JLabel CPPreviewLabel;
    private javax.swing.JTextField CUEMailInput;
    private javax.swing.JLabel CUEMailLabel;
    private javax.swing.JTextField CUNameInput;
    private javax.swing.JLabel CUNameLabel;
    private javax.swing.JButton CUSendBTN;
    private javax.swing.JTextField CUSubjectInput;
    private javax.swing.JLabel CUSubjectLabel;
    private javax.swing.JTextArea CUTextArea;
    private javax.swing.JButton CWContactBTN;
    private javax.swing.JTable CWCustomerTable;
    private javax.swing.JButton CWGetLastOrderBTN;
    private javax.swing.JButton CWGroupMailOutBTN;
    private javax.swing.JLabel ChangePictureBackground;
    private javax.swing.JFrame ChangePicturesWindow;
    private javax.swing.JLabel ContactUsBackground;
    private javax.swing.JFrame ContactUsWindow;
    private javax.swing.JButton ContactUsWindowBTN;
    private javax.swing.JLabel CustomerOrderBackground;
    private javax.swing.JTable CustomerOrderTable;
    private javax.swing.JFrame CustomerOrderWindow;
    private javax.swing.JFrame CustomerWindow;
    private javax.swing.JButton CustomerWindowBTN;
    private javax.swing.JLabel CustomerWindowBackground;
    private javax.swing.JButton EPAddImageBTN;
    private javax.swing.JButton EPAddProductBTN;
    private javax.swing.JComboBox<String> EPCategoryCombo;
    private javax.swing.JLabel EPCategoryLabel;
    private javax.swing.JTable EPProductTable;
    private javax.swing.JButton EPRemoveBTN;
    private javax.swing.JButton EPUpdateBTN;
    private javax.swing.JFrame EditProductWindow;
    private javax.swing.JButton EditProductWindowBTN;
    private javax.swing.JRadioButton LIAdminRadio;
    private javax.swing.JButton LICreateAccountBTN;
    private javax.swing.JButton LILogInBTN;
    private javax.swing.JPasswordField LIPasswordInput;
    private javax.swing.JLabel LIPasswordLabel;
    private javax.swing.JTextField LIUserNameInput;
    private javax.swing.JLabel LIUsernameLabel;
    private javax.swing.JLabel LogInBackground;
    private javax.swing.JFrame LogInWindow;
    private javax.swing.JButton LogInWindowBTN;
    private javax.swing.JButton MBLastOrderBTN;
    private javax.swing.JButton MBRemoveBTN;
    private javax.swing.JPanel MainTitleBarPanel;
    private javax.swing.JButton MyBasketWindowBTN;
    private javax.swing.JButton OpenShopBTN;
    private javax.swing.JLabel RCCategoryLabel;
    private javax.swing.JComboBox<String> RCCombo;
    private javax.swing.JButton RCRemoveBTN;
    private javax.swing.JLabel RemoveCategoryBackground;
    private javax.swing.JFrame RemoveCategoryWindow;
    private javax.swing.JButton RemoveCategoryWindowBTN;
    private javax.swing.JTextField SUEMailInput;
    private javax.swing.JLabel SUEMaillLabel;
    private javax.swing.JButton SUEnterBTN;
    private javax.swing.JTextField SUNameInput;
    private javax.swing.JLabel SUNamelLabel;
    private javax.swing.JPasswordField SUPasswordInput;
    private javax.swing.JLabel SUPasswordlLabel;
    private javax.swing.JPasswordField SUReTypePasswordInput;
    private javax.swing.JLabel SUReTypePasswordlLabel;
    private javax.swing.JTextField SUUsernameInput;
    private javax.swing.JLabel SUUsernameLabel;
    private javax.swing.JComboBox<String> SWCategoryCombo;
    private javax.swing.JRadioButton SWCategoryRadio;
    private datechooser.beans.DateChooserCombo SWDateChooser;
    private javax.swing.JButton SWPrintBTN;
    private javax.swing.JComboBox<String> SWProductCombo;
    private javax.swing.JRadioButton SWProductRadio;
    private javax.swing.JTable SWSalesTable;
    private javax.swing.JButton SWSearchBTN;
    private javax.swing.JFrame SalesWindow;
    private javax.swing.JButton SalesWindowBTN;
    private javax.swing.JLabel SalesWindowBackground;
    private javax.swing.JPanel ShopPanel;
    private javax.swing.JLabel SignUpBackground;
    private javax.swing.JFrame SignUpWindow;
    private javax.swing.JButton SignUpWindowBTN;
    private javax.swing.JLabel UserNameLabel;
    private javax.swing.JButton addToBasketBTN;
    private javax.swing.JButton addToBasketBTN1;
    private javax.swing.JButton addToBasketBTN2;
    private javax.swing.JButton addToBasketBTN3;
    private javax.swing.JButton addToBasketBTN4;
    private javax.swing.JButton addToBasketBTN5;
    private javax.swing.JLabel basketBackground;
    private javax.swing.JLabel buyLabel;
    private javax.swing.JLabel buyLabel1;
    private javax.swing.JLabel buyLabel2;
    private javax.swing.JLabel buyLabel3;
    private javax.swing.JLabel buyLabel4;
    private javax.swing.JLabel buyLabel5;
    private javax.swing.JButton details;
    private javax.swing.JButton details1;
    private javax.swing.JButton details2;
    private javax.swing.JButton details3;
    private javax.swing.JButton details4;
    private javax.swing.JButton details5;
    private javax.swing.JLabel failedLogInLabel;
    private javax.swing.JLabel inStockLabel;
    private javax.swing.JLabel inStockLabel1;
    private javax.swing.JLabel inStockLabel2;
    private javax.swing.JLabel inStockLabel3;
    private javax.swing.JLabel inStockLabel4;
    private javax.swing.JLabel inStockLabel5;
    private javax.swing.JLabel inStockQuantityLabel;
    private javax.swing.JLabel inStockQuantityLabel1;
    private javax.swing.JLabel inStockQuantityLabel2;
    private javax.swing.JLabel inStockQuantityLabel3;
    private javax.swing.JLabel inStockQuantityLabel4;
    private javax.swing.JLabel inStockQuantityLabel5;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPanel mainBackPanel;
    private javax.swing.JLabel mainBackground;
    private javax.swing.JPanel mainFrontPanel;
    private javax.swing.JButton mainNextBTN;
    private javax.swing.JButton mainPreviousBTN;
    private javax.swing.JLabel mainTitleLabel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JLabel pageLabel;
    private javax.swing.JLabel pageNumLabel;
    private javax.swing.JPanel pagePanel;
    private javax.swing.JLabel price;
    private javax.swing.JLabel price1;
    private javax.swing.JLabel price2;
    private javax.swing.JLabel price3;
    private javax.swing.JLabel price4;
    private javax.swing.JLabel price5;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JLabel priceLabel1;
    private javax.swing.JLabel priceLabel2;
    private javax.swing.JLabel priceLabel3;
    private javax.swing.JLabel priceLabel4;
    private javax.swing.JLabel priceLabel5;
    private javax.swing.JPanel product;
    private javax.swing.JPanel product1;
    private javax.swing.JPanel product2;
    private javax.swing.JPanel product3;
    private javax.swing.JPanel product4;
    private javax.swing.JPanel product5;
    private javax.swing.JLabel productIdentityLabel;
    private javax.swing.JLabel productIdentityLabel1;
    private javax.swing.JLabel productIdentityLabel2;
    private javax.swing.JLabel productIdentityLabel3;
    private javax.swing.JLabel productIdentityLabel4;
    private javax.swing.JLabel productIdentityLabel5;
    private javax.swing.JLabel productImage;
    private javax.swing.JLabel productImage1;
    private javax.swing.JLabel productImage2;
    private javax.swing.JLabel productImage3;
    private javax.swing.JLabel productImage4;
    private javax.swing.JLabel productImage5;
    private javax.swing.JLabel productLabel;
    private javax.swing.JLabel productLabel1;
    private javax.swing.JLabel productLabel2;
    private javax.swing.JLabel productLabel3;
    private javax.swing.JLabel productLabel4;
    private javax.swing.JLabel productLabel5;
    private javax.swing.JPanel productPanel;
    private javax.swing.JPanel productPanel1;
    private javax.swing.JPanel productPanel2;
    private javax.swing.JPanel productPanel3;
    private javax.swing.JPanel productPanel4;
    private javax.swing.JPanel productPanel5;
    private javax.swing.JSpinner productQuantitySpinner;
    private javax.swing.JSpinner productQuantitySpinner1;
    private javax.swing.JSpinner productQuantitySpinner2;
    private javax.swing.JSpinner productQuantitySpinner3;
    private javax.swing.JSpinner productQuantitySpinner4;
    private javax.swing.JSpinner productQuantitySpinner5;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables

    /**
     * method to change the size of imageIcon to chosen label
     * @param ii
     * @param picLabel
     * @return 
     */
    
    public ImageIcon changePicSize(ImageIcon ii, JLabel picLabel) {
        
        Image img = ii.getImage();
        
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        
        Graphics g = bi.createGraphics();
        
        g.drawImage(img, 0, 0, picLabel.getWidth(), picLabel.getHeight(), null);
      
        return new ImageIcon(bi);
    }
    
    /**
     * resets the product displays ready for new category
     */
    
    public void resetProductDisplay(){
        
        for(int i = 0; i < productIdentityLabels.length; i++){
            
            productIdentityLabels[i].setText("");
            quantitysLabels[i].setText(""); 
            priceLabels[i].setText("");
            
            addToBasketBTNS[i].removeActionListener(mab);
            detailsBTNS[i].removeActionListener(mad);

            addToBasketBTNS[i].repaint();
            detailsBTNS[i].repaint();
            
            ImageIcon img = new ImageIcon(imageFolder + "comingSoon.JPG");
            
            productImagesLabels[i].setIcon(changePicSize(img, productImagesLabels[i]));
            
            productPanels[i].revalidate();
            
        }
    }

    /**
     * actionListener for category buttons to display products
     */
    
     class MyActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            try {
                
                 for(javax.swing.JPanel p : productPanels){
            
                    p.setVisible(true);
                }
                 
                pagePanel.setVisible(true);
                
                page = 1;
                String pageNum = String.valueOf(page);
                pageNumLabel.setText(pageNum);
                
                resetProductDisplay();                               
                
                javax.swing.JButton button = (javax.swing.JButton)e.getSource();
                category = button.getText();
                
                Object[][] products = data.getProducts(category);
/*
    productName = products[0][0];
    weight = products[0][1];
    description = products[0][2];
    code = products[0][3];
    priced = products[0][4];
    Quantity = products[0][5];
    VEG = products[0][6];
    image = products[0][7];
 */              
   
                for(int i = 0 ; i < productImagesLabels.length; i++) {
                    
                    if(i < products.length)  {  
                        
                        ImageIcon productPic = new ImageIcon(imageFolder + category + "\\" + products[i][7].toString());    

                        productImagesLabels[i].setIcon(changePicSize(productPic, productImagesLabels[i]));

                        productIdentityLabels[i].setText(products[i][0].toString());

                        quantitysLabels[i].setText(products[i][5].toString());

                        priceLabels[i].setText(nf.format(products[i][4]));
                        
                        addToBasketBTNS[i].addActionListener(mab);
                        detailsBTNS[i].addActionListener(mad);
                        
                        addToBasketBTNS[i].repaint();
                        detailsBTNS[i].repaint();
                         
                    } 
                }

            } catch (SQLException ex) {
                
                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
     
     /**
      * action listener to add product to customers basket
      */
     
     class MyActionListenerBasket implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           
           Component found;
            
           Component c = (Component) e.getSource();

            if(loggedIn == true) {
  
                for(int i = 0; i < productIdentityLabels.length; i++){

                    found = (Component) productIdentityLabels[i];

                    if(c.getParent().equals(found.getParent())){

                        productName = productIdentityLabels[i].getText(); 
                           
                        short quantityToAdd = Short.parseShort(quantitySpinners[i].getValue().toString());

                        short quantity = Short.parseShort(quantitysLabels[i].getText());

                        if(quantityToAdd > quantity || quantityToAdd <= 0) {

                            JOptionPane.showMessageDialog(null, "Amount Chosen Cannot Exceed Stock Or Be A negative Amount");

                        }else{

                            int taken = quantity - quantityToAdd;

                            try {
                                 
                                DefaultTableModel tm = (DefaultTableModel)BasketTable.getModel();

                                float cost = data.getProductPrice(productName, category);

                                VEG = data.getProductVeg(productName, category);

                                code = data.getProductCode(productName, category);

                                data.updateQuantity(taken, productName, category);

                                quantitysLabels[i].setText(String.valueOf(taken));

                                float totalCost = cost * quantityToAdd;

                                Object[] item = new Object[]{code, productName, VEG, quantityToAdd, nf.format(cost), nf.format(totalCost)};

                                tm.addRow(item);

                                quantitySpinners[i].setValue(0);

                                JOptionPane.showMessageDialog(null, quantityToAdd + " x " + productName + " Added To Basket.");
                               

                            }catch (SQLException ex) {

                                Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }  
                }
            }else{
                
                int choice =  JOptionPane.showConfirmDialog(null, "You Must Be Logged In To Add Goods To Your Basket \n Log In Now?", "Log In", JOptionPane.YES_NO_OPTION);

                if(choice == 0){
                   
                    LogInWindow.setVisible(true);
                    LogInWindow.setLocationRelativeTo(null);
                   
                }
            }
        }        
    }  
     
    /**
    * action listener for the detail buttons in the product frames
    */
     
     class MyActionListenerDetails implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            Component found;
            
            Component c = (Component) e.getSource();
            
            for(int i = 0 ; i < detailsBTNS.length; i++){
                
                found = (Component) detailsBTNS[i];
                
                if(c.getParent().equals(found.getParent())){
                    
                    if(detailsBTNS[i].getText().matches("DETAILS")){
                        
                        try {

                            productName = String.valueOf(productIdentityLabels[i].getText());
                            Object[] details = data.getProductDetails(productName, category);

                            code = String.valueOf(details[0]);
                            String description = String.valueOf(details[1]);
                            String productWeight = String.valueOf(details[2]);
                            
                            Font font = new Font("Serif", Font.BOLD, 15);
                            productImagesLabels[i].setFont(font);
                            
                            productImagesLabels[i].setIcon(null);
                            productImagesLabels[i].setOpaque(true);
                            productImagesLabels[i].setBackground(Color.WHITE);
                            
                            productImagesLabels[i].setText("<html>Code: '" + code + "'<br/>Description: '" + description + "'<br/>Weight: '" + productWeight + "'<br/></html>");

                            detailsBTNS[i].setText("IMAGE");
                            
                            detailsBTNS[i].revalidate();
                            productImagesLabels[i].revalidate();
                            productPanels[i].getParent().repaint();

                        } catch (SQLException ex) {

                            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else{
                        
                        try {
                            
                            productName = String.valueOf(productIdentityLabels[i].getText());
                            
                            ImageIcon findImage = new ImageIcon(imageFolder + category + "\\" + data.getProductImage(productName, category));
                            
                            productImagesLabels[i].setText("");
                            
                            productImagesLabels[i].setIcon(changePicSize(findImage, productImagesLabels[i]));
                            
                            detailsBTNS[i].setText("DETAILS");
                            
                            detailsBTNS[i].repaint();
                            productImagesLabels[i].repaint();
                            productPanels[i].getParent().repaint();
                            
                        } catch (SQLException ex) {
                            
                            Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } 
            }
        }   
    }
     
     /**
      * action listener for the next and previous buttons
      */
     
     class MyActionListenerNextPrev implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            JButton button = (JButton) e.getSource();
             
            if(button.getText().matches("NEXT")){
                
                try {
                      
                    Object[][] products = data.getProducts(category);
                    
                    if(groupBy < products.length){
                        
                        resetProductDisplay(); 
                        
                        groupBy += 6;
                        
                        int prod = groupBy;   //to cycle through product without changing the groupBy variable
                        
                        page ++;
                        String pageNum = String.valueOf(page);
                        pageNumLabel.setText(pageNum);

                        for(int i = 0; i < productIdentityLabels.length; i++){

                            if(i < products.length - groupBy)  {  

                                ImageIcon productPic = new ImageIcon(imageFolder + category + "\\" + products[prod][7].toString());    

                                productImagesLabels[i].setIcon(changePicSize(productPic, productImagesLabels[i]));

                                productIdentityLabels[i].setText(products[prod][0].toString());

                                quantitysLabels[i].setText(products[prod][5].toString());

                                priceLabels[i].setText(nf.format(products[prod][4]));

                                prod++;

                                addToBasketBTNS[i].addActionListener(mab);
                                detailsBTNS[i].addActionListener(mad);
                                
                                addToBasketBTNS[i].revalidate();
                                detailsBTNS[i].revalidate();
                                productPanels[i].getParent().repaint();   
                                
                            }  
                        } 
                        
                    }
                } catch (SQLException ex) {
                    
                    Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }  
            
            if(button.getText().matches("PREVIOUS")){
                
                try {
                    
                    resetProductDisplay();
                    
                    if(groupBy != 0){
                        
                        groupBy -= 6;
                    }
                    
                    int prod = groupBy;
                    
                    Object[][] products = data.getProducts(category);
                    
                    if(groupBy < products.length){
                        
                        if(page != 1){
                            
                            page --;
                            String pageNum = String.valueOf(page);
                            pageNumLabel.setText(pageNum);
                        }

                        for(int i = 0; i < productIdentityLabels.length; i++){

                            if(i < products.length - groupBy)  {  

                                ImageIcon productPic = new ImageIcon(imageFolder + category + "\\" + products[prod][7].toString());    

                                productImagesLabels[i].setIcon(changePicSize(productPic, productImagesLabels[i]));

                                productIdentityLabels[i].setText(products[prod][0].toString());

                                quantitysLabels[i].setText(products[prod][5].toString());

                                priceLabels[i].setText(nf.format(products[prod][4]));

                                prod++;

                                addToBasketBTNS[i].addActionListener(mab);
                                detailsBTNS[i].addActionListener(mad);
                                
                                addToBasketBTNS[i].revalidate();
                                detailsBTNS[i].revalidate();
                                productPanels[i].getParent().repaint();
                                
                            } 
                        }   
                    }
                } catch (SQLException ex) {
                    
                    Logger.getLogger(ShopFront.class.getName()).log(Level.SEVERE, null, ex);
                }     
            } 
        }
    }
}
