import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends Frame
{
	Button insert,update,delete;
	TextField usidtxt,usernametxt, contacttxt,gendertxt;
	TextArea errtxt;
	Connection connection;
	Statement statement;
	Button upduserbtn;
	List USIDList;
	ResultSet rs;
	Button dltuserbtn;

	public User()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
	    catch (Exception e)
	    {
	    	System.err.println("Unable to find and load driver");
	        System.exit(1);
	    }
	    connectToDB();
	    addWindowListener(new WindowAdapter()
	    {
	    	public void windowClosing(WindowEvent e)
	    	{
	    		dispose();
	    	}
	    });
	    buildGUI();
	}
	public void connectToDB()
    {
		try
		{
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","mdeepthi","deepthi");
			statement = connection.createStatement();
			}
		catch (SQLException connectException)
	    {
			System.out.println(connectException.getMessage());
	    
		    System.out.println(connectException.getSQLState());
		    System.out.println(connectException.getErrorCode());
		    System.exit(1);
	    }
    }
    private void loadUsers()
    {  
    	try
    	{
    		rs = statement.executeQuery("SELECT userid FROM users");
    		while (rs.next())
    		{
    			USIDList.add(rs.getString("userid"));
    		}
		}
	    catch (SQLException e)
	    {
		   displaySQLErrors(e);
	    }
    }
    public void buildGUI()
    {
    	insert = new Button("Insert");
    	upduserbtn = new Button("Update");
    	dltuserbtn=new Button("Delete");
    	USIDList = new List(10);
        loadUsers();
        add(USIDList);
        USIDList.setBounds(450,80,100,200);
        USIDList.addItemListener(new ItemListener()
        {
        public void itemStateChanged(ItemEvent e)
        {
        try
        {
        rs = statement.executeQuery("SELECT * FROM users where userid ="+USIDList.getSelectedItem());
        rs.next();
        usidtxt.setText(rs.getString("userid"));
        usernametxt.setText(rs.getString("username"));
        contacttxt.setText(rs.getString("contact"));
        gendertxt.setText(rs.getString("gender"));

        }
        catch (SQLException selectException)
        {
        displaySQLErrors(selectException);
        }
        }
        });
        
        insert.addActionListener(new ActionListener()
	    {
		    public void actionPerformed(ActionEvent e)
		    {
		    	
		    	Button clickedButton = (Button) e.getSource();
	            String buttonText = clickedButton.getLabel();
	            System.out.println("Button clicked: " + buttonText);
	            if (buttonText.equals("Insert"))
	            {
	            	try
	            	{
				        String query= "INSERT INTO users VALUES(" + usidtxt.getText() + ", " + "'" + usernametxt.getText() + "'"+"," +"'"+ contacttxt.getText() +"'"+ "," +"'"+ gendertxt.getText() + "'"+")";
				        int i = statement.executeUpdate(query);
				        errtxt.append("\nInserted " + i + " rows successfully");
				       
			        }
			        catch (SQLException insertException)
			        {
			    	    displaySQLErrors(insertException);
			        }
			   }
		    }
	    });
        

        upduserbtn.addActionListener(new ActionListener()
	    {
		    public void actionPerformed(ActionEvent e)
		    {
		    	
		    	Button clickedButton = (Button) e.getSource();
	            String buttonText = clickedButton.getLabel();
	            System.out.println("Button clicked: " + buttonText);
	            if(buttonText.equals("Update"))
	            		{ 
	            	     try
	          		      {
	          		    	  Statement statement = connection.createStatement();
	          		      
	          			      int i = statement.executeUpdate("UPDATE users "
	          			      + "SET username='" + usernametxt.getText() + "', "
	          					+ "contact='" + contacttxt.getText() + "' , "
	          			       + "gender ='"+ gendertxt.getText() + "' WHERE userid = "
	          					+ USIDList.getSelectedItem());
	          			       errtxt.append("\nUpdated " + i+ " rows successfully");
	          			       
	          			       USIDList.removeAll();
	          			       loadUsers();
	          			   }
	          		       catch (SQLException insertException)
	          		       {
	          		        	displaySQLErrors(insertException);
	          		        }
	          	       
	            	
	            		}
		    }
		 });
        
        dltuserbtn.addActionListener(new ActionListener()
	    {
		    public void actionPerformed(ActionEvent e)
		    {
		    	
		    	Button clickedButton = (Button) e.getSource();
	            String buttonText = clickedButton.getLabel();
	            System.out.println("Button clicked: " + buttonText);
	            if(buttonText.equals("Delete"))
	            {
	            	try
	            	{
	            		Statement statement = connection.createStatement();
	            	
	            		int i = statement.executeUpdate("DELETE FROM users WHERE userid = "
	            	    + USIDList.getSelectedItem());
	            	    errtxt.append("\nDeleted " + i + " rows successfully");
	            	    usidtxt.setText(null);
	            	    usernametxt.setText(null);
	            	    gendertxt.setText(null);
	            	    contacttxt.setText(null);
	            	    USIDList.removeAll();
	            	     loadUsers();
	            	}
	            	catch (SQLException insertException)
	            	{
	            		displaySQLErrors(insertException);
	            	}
	            }
	        }
	    });
	            
	            	
	            
	            	
	            	
	            	
	            	
	            	
	     usidtxt = new TextField(15);
	     usernametxt = new TextField(15);
	     contacttxt = new TextField(15);
	     gendertxt = new TextField(15);
	     errtxt = new TextArea(10,40);
	     errtxt.setEditable(false);
	     Panel first = new Panel();
	     first.setLayout(new GridLayout(4, 2));
	     first.add(new Label("User ID:"));
	     first.add(usidtxt);
	     first.add(new Label("User name:"));
	     first.add(usernametxt);
	     first.add(new Label("contact:"));
	     first.add(contacttxt);
	     first.add(new Label("gender:"));
	     first.add(gendertxt);
	     first.setBounds(125,70,200,100);
	     
	     GridLayout lay=new GridLayout(1, 3);
	     lay.setHgap(10);
	     Panel second = new Panel(lay);
	     
	     second.add(insert);
	     second.add(upduserbtn);
	     second.add(dltuserbtn);
	     
         second.setBounds(125,320,300,40);        
         Panel third = new Panel();
         third.add(errtxt);
         third.setBounds(125,450,600,600);
         setLayout(null);
    
         add(first);
    
         add(second);
         add(third);
   
         setTitle(" user");
         setSize(700, 800);
         setVisible(true);
    
        
         
           
    }
    private void displaySQLErrors(SQLException e) {
    errtxt.append("\nSQLException: " + e.getMessage() + "\n");
    errtxt.append("SQLState:     " + e.getSQLState() + "\n");
    errtxt.append("VendorError:  " + e.getErrorCode() + "\n");
    }
}

