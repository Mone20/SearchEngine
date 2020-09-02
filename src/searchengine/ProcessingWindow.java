/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Rodion
 */
public class ProcessingWindow extends JFrame {
    
    @SuppressWarnings("empty-statement")
    public ProcessingWindow()
    {
        JLabel label   = new JLabel(String.format("Обработка файлов..."));;
         // Создание окна с заголовком
        this.setTitle("Processing");
        
        // Не закрывать окно по нажатию на кнопку с крестиком 
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Подключение слушателя окна
    
        
       this.getContentPane().add(label);

       this.setPreferredSize(new Dimension(250, 80));
       this.pack();
        this.setVisible(true);
        
      
    }
    
}
