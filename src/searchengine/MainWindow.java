/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static searchengine.SearchEngine.beginPath;

/**
 *
 * @author Rodion
 */
public class MainWindow extends JFrame{
    public JTextField smallField;
    public MainWindow()
    {
        super("Поиск по содержимому");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        smallField = new JTextField(15);
        JButton searchButton = new JButton("Поиск");
        JButton indexButton = new JButton("Обработка директории");
//        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 SearchEngine.setArrayOfFiles(new ArrayList<Pair<String,Integer>>()); 
                try {
                    if(smallField.getText().equals(""))
                        JOptionPane.showMessageDialog(MainWindow.this, 
                 "Пустой запрос");
                    
                    else if(!SearchEngine.isNotStop(smallField.getText()))
                        JOptionPane.showMessageDialog(MainWindow.this, 
                 "Запрос не может состоять только из стоп-слов");
                    else
                    {
                    
                    
                  
                    int check=SearchEngine.indexingOrSearch(beginPath);
                    switch(check)
                    {
                    case -1: 
                        SearchBase.createHashTable();
                    
                    SearchEngine.findHistograms(smallField.getText(), beginPath);
                    if(SearchEngine.arrayOfFiles.size()>0)
                         
                     new ResultWindow();
                     else
                     {
                         smallField.setText("");
                         JOptionPane.showMessageDialog(MainWindow.this, 
                 "Файлов для данного запроса не найдено");
                     }
                    break;
                    
                    
                    
                        case 1:
                            while(true)
                            {
                             ProcessingWindow processingWindow = new ProcessingWindow();
                             
                             JOptionPane.showMessageDialog(MainWindow.this, 
                 "Началась обработка файлов");
                             SearchBase.createWordBase(beginPath);
                             SearchBase.createHashTable();
                             SearchBase.createAllHistograms(beginPath);
                             processingWindow.setVisible(false);
                             processingWindow.dispose();
                             if(SearchEngine.indexingOrSearch(beginPath)!=1)
                                 break;
                            }
                              JOptionPane.showMessageDialog(MainWindow.this, 
                 "Обработка завершена!");
                             
                     SearchEngine.findHistograms(smallField.getText(), beginPath);
                   if(SearchEngine.arrayOfFiles.size()>0)  
                     new ResultWindow();
                     else
                     {
                         smallField.setText("");
                         JOptionPane.showMessageDialog(MainWindow.this, 
                 "Файлов для данного запроса не найдено");
                     }
                    break;

                    
                    case 0:
                    
                  JOptionPane.showMessageDialog(MainWindow.this, 
                 "Тектовых файлов для поиска не существует");
                 break;
                    }
                    
                     
                }
                  
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        indexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                ProcessingWindow processingWindow = new ProcessingWindow();
                             
                             JOptionPane.showMessageDialog(MainWindow.this, 
                 "Началась обработка файлов");
                
                    SearchBase.createWordBase(beginPath);
                
                             SearchBase.createHashTable();
                             SearchBase.createAllHistograms(beginPath);
                             processingWindow.setVisible(false);
                             processingWindow.dispose();
                              JOptionPane.showMessageDialog(MainWindow.this, 
                 "Обработка завершена!");
                              } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel contents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contents.add(smallField);
        contents.setLayout(new FlowLayout(FlowLayout.CENTER));
        contents.add(searchButton);
        contents.add(indexButton);
        setContentPane(contents);
        setSize(200, 150);
        setVisible(true);
    }
    
}
