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

public class Wallet extends Frame
{
	Button update,delete,insert;
	List cardIDList;
	TextField balText,widText;
	TextArea errorText;
	Connection connection;
	Statement statement;
	ResultSet rs;
	
	public Wallet() 
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
		addWindowListener(new WindowAdapter(){
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
	
	private void loadCard() 
	{	   
		try 
		{
		  rs = statement.executeQuery("SELECT * FROM wallet");
		  while (rs.next()) 
		  {
			cardIDList.add(rs.getString("WID"));
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
	    	cardIDList = new List(10);
	        loadCard();
	        add(cardIDList);
	        cardIDList.setBounds(450,80,100,200);
	        cardIDList.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e) 
				{
					try 
					{
						rs = statement.executeQuery("SELECT * FROM wallet");
						while (rs.next()) 
						{
							if (rs.getString("wid").equals(cardIDList.getSelectedItem()))
							break;
						}
						if (!rs.isAfterLast()) 
						{
							balText.setText(rs.getString("BALANCE"));
							widText.setText(rs.getString("wid"));
							
						}
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
						  				  
						  String query= "INSERT INTO wallet VALUES(" + widText.getText() + ", "+ balText.getText() + ")";
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
		            	try 
						{
							Statement statement = connection.createStatement();
							int i = statement.executeUpdate("UPDATE wallet "
							+ "SET balance=" + balText.getText() + " WHERE wid = "
							+ cardIDList.getSelectedItem());
							errorText.append("\nUpdated " + i + " rows successfully");
							cardIDList.removeAll();
							loadCard();
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
							Statement statement = connection.createStatement();
							int i = statement.executeUpdate("DELETE FROM wallet WHERE WID = "
									+ cardIDList.getSelectedItem());
							errorText.append("\nDeleted " + i + " rows successfully");
							balText.setText(null);
							widText.setText(null);
							
							cardIDList.removeAll();
							loadCard();
						} 
						catch (SQLException insertException) 
						{
							displaySQLErrors(insertException);
						}
					}
			    }
				});
	        balText = new TextField(15);
			widText = new TextField(15);
			

			
			errorText = new TextArea(10, 40);
			errorText.setEditable(false);

			Panel first = new Panel();
			first.setLayout(new GridLayout(4, 2));
			first.add(new Label("Wallet ID:"));
			first.add(widText);
			first.add(new Label("Balance:"));
			first.add(balText);
			
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
		    
			setTitle("INSERT WALLET:");
			setSize(700, 800);
			setVisible(true);
		}

		

		private void displaySQLErrors(SQLException e) 
		{
			errorText.append("\nSQLException: " + e.getMessage() + "\n");
			errorText.append("SQLState:     " + e.getSQLState() + "\n");
			errorText.append("VendorError:  " + e.getErrorCode() + "\n");
		}
}
		            		
		            	
		            
		            
	        
		            
