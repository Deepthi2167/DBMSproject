import java.awt.Button;
import java.awt.FlowLayout;
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
public class Ticket extends Frame 
{
	
	List transactionIDList;
	Button update,insert,delete;
	TextField tidtext, costtext, sourcetext,desttext;
	TextArea errorText;
	Connection connection;
	Statement statement;
	ResultSet rs;
	public Ticket() 
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
	private void loadTransaction() 
	{	   
		try 
		{
		  rs = statement.executeQuery("SELECT TID FROM ticket");
		  while (rs.next()) 
		  {
			transactionIDList.add(rs.getString("TID"));
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
    	transactionIDList = new List(10);
    	loadTransaction();
		add(transactionIDList);
		transactionIDList.setBounds(450,80,100,200);
		transactionIDList.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) 
			{
				try 
				{
					rs = statement.executeQuery("SELECT * FROM ticket where TID ="+transactionIDList.getSelectedItem());
					rs.next();
					tidtext.setText(rs.getString("TID"));
					costtext.setText(rs.getString("cost"));
					sourcetext.setText(rs.getString("source"));
					desttext.setText(rs.getString("destination"));
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
	            		  
	  				  String query= "INSERT INTO ticket VALUES(" + tidtext.getText() + ", " + costtext.getText() +" ,"+"'" + sourcetext.getText()+ "'" +","+"'"+desttext.getText()+"'"+")";
	  				  int i = statement.executeUpdate(query);
	  				  errorText.append("\nInserted " + i + " rows scessfully");
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
		     					int i = statement.executeUpdate("UPDATE ticket SET cost=" + costtext.getText() + ",source='" + sourcetext.getText() +"',destination='"+desttext.getText()+"' WHERE tid = "
		     					+ transactionIDList.getSelectedItem());
		     					errorText.append("\nUpdated " + i + " rows successfully");
		     					transactionIDList.removeAll();
		     					loadTransaction();
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
					int i = statement.executeUpdate("DELETE FROM ticket WHERE TID = "
							+ transactionIDList.getSelectedItem());
					errorText.append("\nDeleted " + i + " rows successfully");
					tidtext.setText(null);
					costtext.setText(null);
					sourcetext.setText(null);
					desttext.setText(null);
					transactionIDList.removeAll();
					loadTransaction();
				} 
				catch (SQLException insertException) 
				{
					displaySQLErrors(insertException);
				}
			}
	    }
	 });
    
    tidtext = new TextField(15);
	tidtext.setEditable(false);
	costtext = new TextField(15);
	sourcetext = new TextField(15);
	desttext = new TextField(15);
	
	errorText = new TextArea(10, 40);
	errorText.setEditable(false);

	Panel first = new Panel();
	first.setLayout(new GridLayout(4, 2));
	first.add(new Label("Ticket ID:"));
	first.add(tidtext);
	first.add(new Label("Amount Deducted:"));
	first.add(costtext);
	first.add(new Label("Source:"));
	first.add(sourcetext);
	first.add(new Label("Destination:"));
	first.add(desttext);
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
	
	add(first);
	add(second);
	add(third);
    
	setTitle(" TICKET");
	setSize(700, 800);
	setLayout(new FlowLayout());
	setVisible(true);
	
}

private void displaySQLErrors(SQLException e) 
{
	errorText.append("\nSQLException: " + e.getMessage() + "\n");
	errorText.append("SQLState:     " + e.getSQLState() + "\n");
	errorText.append("VendorError:  " + e.getErrorCode() + "\n");
}
}

            	
            		
            	
            		
            	

					
	            		
					
		
		
    	
    	
	
	
	
	
	
	