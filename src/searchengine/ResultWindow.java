/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import javafx.util.Pair;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Rodion
 */
public class ResultWindow extends JFrame {
    public ResultWindow()
    {
         
        super("Result");
        Collections.sort(SearchEngine.arrayOfFiles, (Pair<Pair<String, Double>,Integer> first, Pair<Pair<String, Double>,Integer> second) -> {
                            if (first.getKey().getValue()< second.getKey().getValue())
                                return 1;
                            if (first.getKey().getValue()>second.getKey().getValue())
                                return -1;
                            else return 0;
                        });
       
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE );
       
       
       for(int i=0;i<SearchEngine.arrayOfFiles.size();i++)
                {
                   String buttonText=SearchEngine.arrayOfFiles.get(i).getKey().getKey();
                   JButton button= new JButton(buttonText);
                   button.setContentAreaFilled(false);
                   button.setAlignmentX(Component.CENTER_ALIGNMENT); 
                   panel.add(button);
                   JLabel label=new JLabel("Количество совпадений: "+Integer.toString(SearchEngine.arrayOfFiles.get(i).getValue()));
                   label.setAlignmentX(Component.CENTER_ALIGNMENT);
                   panel.add(label);
                   
                    final File file=new File(SearchEngine.arrayOfFiles.get(i).getKey().getKey());
                    if(file.exists())
                    {
                    button.addActionListener(new ActionListener() {
                        
              
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop= Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (IOException ex) {
                   
                }
 
            }
        });
                  
                    }
                }   
       
       
       JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       
        // Определение свойств панели прокрутки
        scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.yellow));
        scrollPane.setWheelScrollingEnabled(true);
        // Вывод окна на экран
        getContentPane().add(scrollPane);
        setSize(300, 150);
        setVisible(true);
    }
}

