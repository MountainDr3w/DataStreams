import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamGUI extends JFrame {
    private JFileChooser fileChooser = new JFileChooser();

    /*
    The top panel will just have a title
     */
    //Top Panel
    JPanel topPanel;
    JLabel title;

    /*
    the middle panel will have 2 JScroll panes with 2 JText areas in them
     */
    //Middle Panel
    JPanel midPanel;
    JTextArea origFileDisplay;
    JTextArea filteredFileDisplay;
    JTextField targetField;


    /*
    THe bottom panel will include all 3 buttons
    Load a file
    Search the file
    Quit
     */
    //Botton Panel
    JPanel botPanel;
    JButton loadButton, searchButton, quitButton;



    public DataStreamGUI() {
        setTitle("Data Stream");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        // center frame in screen
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        createTopPanel();
        createMiddlePanel();
        createBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);
        mainPanel.add(botPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void createTopPanel(){
        topPanel = new JPanel();

        title = new JLabel("Java Data Stream Processor");
        title.setFont(new Font("Times New Roman", Font.BOLD, 48));

        topPanel.add(title);
    }

    private void createMiddlePanel(){
        midPanel = new JPanel();
        midPanel.setLayout(new BorderLayout(10, 10));

        origFileDisplay = new JTextArea(10,35);
        origFileDisplay.setEditable(false);

        JPanel targetPanel = new JPanel(new BorderLayout(5,5));
        JLabel targetTitle = new JLabel("Search String");
        targetField = new JTextField(20);
        targetPanel.add(targetTitle, BorderLayout.NORTH);
        targetPanel.add(targetField, BorderLayout.CENTER);


        filteredFileDisplay = new JTextArea(10,35);
        filteredFileDisplay.setEditable(false);

        JScrollPane origScrollPane = new JScrollPane(origFileDisplay);
        JScrollPane filteredScrollPane = new JScrollPane(filteredFileDisplay);

        midPanel.add(origScrollPane, BorderLayout.WEST);
        midPanel.add(targetPanel, BorderLayout.CENTER);
        midPanel.add(filteredScrollPane, BorderLayout.EAST);
    }

    private void createBottomPanel(){
        botPanel = new JPanel();
        botPanel.setLayout(new BorderLayout(10,10));

        loadButton = new JButton("Load File");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(DataStreamGUI.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    try(Stream<String> lines = Files.lines(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()))) {
                        origFileDisplay.setText(lines.collect(Collectors.joining("\n")));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        searchButton = new JButton("Search File");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetString = targetField.getText();
                try (Stream<String> lines = Files.lines(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()))) {
                    List<String> filteredLines = lines.filter(line ->line.contains(targetString)).collect(Collectors.toList());
                    filteredFileDisplay.setText(String.join("\n", filteredLines));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent ae) -> System.exit(0));

        botPanel.add(loadButton, BorderLayout.WEST);
        botPanel.add(searchButton, BorderLayout.CENTER);
        botPanel.add(quitButton, BorderLayout.EAST);
    }

}
