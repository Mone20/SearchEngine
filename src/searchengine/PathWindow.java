/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Rodion
 */
public class PathWindow extends JFrame{
    public JTextField smallField;
    public PathWindow()
    {
        super("Введите путь");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        smallField = new JTextField(15); 
        JButton button = new JButton("Ввести");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path path=Paths.get(smallField.getText());
                if(Files.exists(path))
                {
                    if(Files.isDirectory(path))
                    {
                        SearchEngine.beginPath=smallField.getText();
                        setVisible(false);
                        new MainWindow();
                        
                        
                    }else
                    {
                        JOptionPane.showMessageDialog(PathWindow.this, 
                 "Путь не является директорией");
                    }
                }
                else
                JOptionPane.showMessageDialog(PathWindow.this, 
                 "Пути не существует!");
            }
        });
  
        JPanel contents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contents.add(new JLabel("Введите входной путь поиска"));
        contents.add(smallField);
        contents.setLayout(new FlowLayout(FlowLayout.CENTER));
        contents.add(button);
        setContentPane(contents);
        setSize(200, 150);
        setVisible(true);
    }
    
}
