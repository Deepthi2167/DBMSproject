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
public class Biometric extends Frame{
	Button insert,update,delete;
	TextField bidText, useridText;
	TextArea errorText;
	Connection connection;
	Statement statement;
	List scannerIDList;
	ResultSet rs;
	
	public Biometric()
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
	private void loadScanners() 
	{	   
		try 
		{
		  rs = statement.executeQuery("SELECT bid FROM biometric");
		  while (rs.next()) 
		  {
			scannerIDList.add(rs.getString("BID"));
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
    	update = new Button("Update");
    	delete=new Button("Delete");
    	scannerIDList = new List(10);
    	loadScanners();
        add(scannerIDList);
        scannerIDList.setBounds(450,80,100,200);
        scannerIDList.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) 
			{
				try 
				{
					rs = statement.executeQuery("SELECT * FROM biometric where BID ="+scannerIDList.getSelectedItem());
					rs.next();
					bidText.setText(rs.getString("bid"));
					useridText.setText(rs.getString("userid"));
					
					
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
				  			  
				      String query= "INSERT INTO biometric VALUES(" + bidText.getText() + ", "  + useridText.getText() + ")";
				      int i = statement.executeUpdate(query);
				      errorText.append("\nInserted " + i + " rows successfully");
				    } 
				    catch (SQLException insertException) 
				    { 
				        displaySQLErrors(insertException);
				    }
			    }
		    }
		});
        update.addActionListener(new ActionListener()
	    {
		    public void actionPerformed(ActionEvent e)
		    {
		    	
		    	Button clickedButton = (Button) e.getSource();
	            String buttonText = clickedButton.getLabel();
	            System.out.println("Button clicked: " + buttonText);
	            if(buttonText.equals("Update"))
	            		{ 
	            	     try {

	 					Statement statement = connection.createStatement();
	 					int i = statement.executeUpdate("UPDATE biometric "
	 					+ "SET userid=" + useridText.getText() + " WHERE bid = "
	 					+ scannerIDList.getSelectedItem());
	 					errorText.append("\nUpdated " + i + " rows successfully");
	 					scannerIDList.removeAll();
	 					loadScanners();
	 				} 
	 				catch (SQLException insertException) 
	 				{
	 					displaySQLErrors(insertException);
	 				}
	 			}
		    }
		  
	    });
        delete.addActionListener(new ActionListener()
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
	            		rs = statement.executeQuery("SELECT * FROM biometric");
						while (rs.next()) 
						{
							if (rs.getString("BID").equals(scannerIDList.getSelectedItem()))
							break;
						}
						if (!rs.isAfterLast()) 
						{
							bidText.setText(rs.getString("BID"));
							useridText.setText(rs.getString("userid"));
							
						}
					} 
					catch (SQLException selectException) 
					{
						displaySQLErrors(selectException);
					}
				}
		    }
		 
	    });		
        
        bidText = new TextField(15);
		useridText = new TextField(15);
		

		
		errorText = new TextArea(10, 60);
		errorText.setEditable(false);

		Panel first = new Panel();
		first.setLayout(new GridLayout(2, 2));
		first.add(new Label("Biometric ID:"));
		first.add(bidText);
		first.add(new Label("UserID:"));
		first.add(useridText);
		
		first.setBounds(125,70,200,100);
		
		GridLayout lay=new GridLayout(1, 3);
	     lay.setHgap(10);
	     Panel second = new Panel(lay);
	     second.add(insert);
	     second.add(update);
	     second.add(delete);
		
        second.setBounds(125,320,300,40);         
		
		Panel third = new Panel();
		third.add(errorText);
		third.setBounds(125,450,600,600);
		
		setLayout(null);

		add(first);
		add(second);
		add(third);
	    
		setTitle("BIOMETRIC");
		setSize(700, 800);
		setVisible(true);
	}
   
	private void displaySQLErrors(SQLException e) {
errorText.append("\nSQLException: " + e.getMessage() + "\n");
errorText.append("SQLState:     " + e.getSQLState() + "\n");
errorText.append("VendorError:  " + e.getErrorCode() + "\n");
}
}
	
	            	
	            		
	            	
	            	  

	
	
