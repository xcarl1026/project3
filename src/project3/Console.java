
package project3;

import static java.awt.Color.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Carlos Diaz
 */
public class Console {
    private static String jdbcDriver = "com.mysql.jdbc.Driver";
    private static String dbURL = "jdbc:mysql://localhost:3306/project3?useSSL=true";
    private static String dbDisplay = "jdbc:mysql://localhost:3306/project3";
    private static GUI gui = new GUI();
    private static Connection connection = null;
    private static boolean conStatus;
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    
    
    public void prepareGui() throws ClassNotFoundException{
        gui.getJDBCDropDown().addItem(jdbcDriver); 
        gui.getdbDropDown().addItem(dbDisplay);
        
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded");
    }
    
    public void connectDB() throws SQLException{
       String userName = gui.getuserTextField().getText();
       String pass = gui.getpassTextField().getText();
       System.out.println(userName);
       System.out.println(pass);
       try{
           connection = DriverManager.getConnection(dbURL, userName, pass);
       }catch(SQLException ex){
           System.out.print("Cannot connect to db");
       }
       conStatus = connection.isClosed();
    
           gui.getdbConLabel().setBackground(green);
           gui.getdbConLabel().setForeground(black);
           gui.getdbConButt().setText("Connected to Database");
           System.out.println(conStatus);
      
    }
    
    
    public static void main(String[] args)throws SQLException, ClassNotFoundException {
        Console console = new Console();
        console.prepareGui();
        gui.setVisible(true);
        
        gui.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
               if(conStatus !=false){
                   try {
                       connection.close();
                   } catch (SQLException ex) {
                       Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   System.exit(0);
               }else{
                   System.exit(0);
               }
            
            }
        });
      
    }
    
}
