
package project3;

import static java.awt.Color.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
/**
 *
 * @author Carlos Diaz
 */
public class Console {
    static final String DEFAULT_QUERY ="";
    private static String jdbcDriver = "com.mysql.jdbc.Driver";
    private static String dbURL = "jdbc:mysql://localhost:3306/project3?useSSL=true";
    private static String dbDisplay = "jdbc:mysql://localhost:3306/project3";
    private static GUI gui = new GUI();
    private static Connection connection = null;
    private static boolean conStatus;
    private boolean working;
    private ResultSetTableModel tableModel;
    
    
    
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
           JOptionPane.showMessageDialog(null, ex.getMessage(),"Database Error", JOptionPane.ERROR_MESSAGE);
       }
       conStatus = connection.isClosed();
       working = true;
    
           gui.getdbConLabel().setBackground(green);
           gui.getdbConLabel().setForeground(black);
           gui.getdbConButt().setText("Connected to Database");
           System.out.println(conStatus);
      
    }
    
    public void ResultTable() throws SQLException, ClassNotFoundException{
        String query = gui.getqueryField().getText();
        
        if(working == true){
            if(query.isEmpty()){
                JOptionPane.showMessageDialog(null, "The query field is empty. Please enter a query");
            }else{
                try{
                    tableModel = new ResultSetTableModel(connection, query);
                }catch(SQLException e){
                    JOptionPane.showMessageDialog( null, 
                    e.getMessage(), "Database error", 
                    JOptionPane.ERROR_MESSAGE );
                    gui.getqueryField().setText("");
                }
                
                gui.getguiResultTable().setModel(tableModel);
                
            }
        }else{
            JOptionPane.showMessageDialog(null, "Make sure you are connected.");
        }
      
    }
    
    public void removeResult() throws SQLException{
        tableModel.setValueAt("", 0, 0);
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
