import java.awt.*;
import java.awt.event.*;

public class BiometricMetroRailSystem extends Frame {
    public BiometricMetroRailSystem() {
        setTitle("Biometric Metro Rail Automation System");
        setSize(700, 500);
        setBackground(Color.WHITE);

        // Create menu bar
        MenuBar menuBar = new MenuBar();

        // Create main menu
        Menu mainMenu = new Menu("Menu");

        // Create menu items
        MenuItem userMenuItem = new MenuItem("User");
        MenuItem ticketMenuItem = new MenuItem("Ticket");
        MenuItem walletMenuItem = new MenuItem("Wallet");
        MenuItem biometricMenuItem = new MenuItem("Biometric");

        // Create action listeners for menu items
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch (command) {
                    case "User":
                        new User();
                        break;
                    case "Ticket":
                        new Ticket();
                        break;
                    case "Wallet":
                        new Wallet();
                        break;
                    case "Biometric":
                        new Biometric();
                        break;
                    default:
                        break;
                }
            }
        };

        // Add action listeners to menu items
        userMenuItem.addActionListener(menuListener);
        ticketMenuItem.addActionListener(menuListener);
        walletMenuItem.addActionListener(menuListener);
        biometricMenuItem.addActionListener(menuListener);

        // Add menu items to the main menu
        mainMenu.add(userMenuItem);
        mainMenu.add(ticketMenuItem);
        mainMenu.add(walletMenuItem);
        mainMenu.add(biometricMenuItem);

        // Add main menu to the menu bar
        menuBar.add(mainMenu);

        // Set the menu bar for the frame
        setMenuBar(menuBar);

        // Add window listener to close the program when the window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    public void paint(Graphics g) {
        // Draw the main heading
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.BLACK);
        g.drawString("Biometric Metro Rail Automation System", 50, 100);
    }

    public static void main(String[] args) {
        BiometricMetroRailSystem frame = new BiometricMetroRailSystem();
        frame.setVisible(true);
    }
}


