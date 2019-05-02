
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame{

    private JTextField searchTextField;
    private JButton searchButton;
    private JTable recordTable;
    private JButton newRecordButton;
    private JButton newCustomerButton;
    private JComboBox statusComboBox;
    private JButton exitButton;
    private JButton settingsButton;
    private JPanel mainPanel;

    MainGUI() {
        this.setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
