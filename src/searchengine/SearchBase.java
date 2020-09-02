/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static searchengine.SearchEngine.beginPath;



public class SearchBase {
     
     public static String wordBasePath="wordBase.txt";
      public static String stopWordsPath="stopWords.txt";
     public static String wordBaseSer="wordBase.ser";
      public static ArrayList<String> arrayOfStopWords;
      private static final int MAX_NUMBER_WORDS=5000;
      private static int numWordsInBase;
    public static void partitionFile(String filePath,String wordBasePath) throws IOException {
        
        Path path=Paths.get(filePath);
        try{
        List<String> allLinesFromFile = Files.readAllLines(path, Charset.forName("Cp1251"));
        for(int i = 0; i < allLinesFromFile.size(); i++) {
            String[] words = allLinesFromFile.get(i).split("[\\p{Punct}\\s]+");
            for (String word : words) {
                if(!arrayOfStopWords.contains(word))
                {
                 if(numWordsInBase<MAX_NUMBER_WORDS)
                 {
                appendFileWriter(wordBasePath,word); 
                numWordsInBase++;
                 }
                 else
                     return;
                }
            }
        }
        }catch(UnmappableCharacterException ignore)
        {}
    }
    public static void createStopWords() throws IOException
    {
        List<String> allLinesFromFile = Files.readAllLines(Paths.get(stopWordsPath), Charset.forName("Cp1251"));
        
       arrayOfStopWords=new ArrayList<String>();
        for(int i = 0; i < allLinesFromFile.size(); i++) {
            String[] words = allLinesFromFile.get(i).split("[\\p{Punct}\\s]+");
            for (String word : words) {
                arrayOfStopWords.add(word);   
            }
        }
    }
    public static void createHashTable() throws IOException, ClassNotFoundException
    {
        Hashtable<String,Integer> hTable;
        File serFile=new File(wordBaseSer);
         if(!serFile.exists())
       {
        
        Path path= Paths.get(SearchBase.wordBasePath);
        if(!Files.exists(path))
        {
            SearchBase.createWordBase(beginPath);
        }
        
        hTable = new Hashtable<String,Integer>();
        List<String> allLinesFromFile = Files.readAllLines(path, Charset.forName("Cp1251"));
        int k=0;
        for(int i = 0; i < allLinesFromFile.size(); i++) {
            String[] words = allLinesFromFile.get(i).split("[\\p{Punct}\\s]+");
            for (String word : words) {
               
                hTable.put(word.toLowerCase(), k);
                k++;
            }
            
        }
        System.out.println(hTable.toString());
       
      
       if(serFile.createNewFile())
       {
       FileOutputStream outputStream = new FileOutputStream(wordBaseSer);
       
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            
            objectOutputStream.writeObject(hTable);
             objectOutputStream.close();
        }
       }
       else
       {
           System.out.println("create wordBase error");
           System.exit(0);
       }
       }
       else
       {
        FileInputStream fileInputStream = new FileInputStream(wordBaseSer);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

       hTable = (Hashtable) objectInputStream.readObject();

       
       }
       SearchEngine.setHashTable(hTable);
    }
    
    public static void createAllHistograms(String path) throws IOException
    {
       
        File dir = new File(path);
        if(dir.isDirectory())
        {
            File[] list=dir.listFiles();
            if(list!=null)
            {
            for(File item : list){
                if(!isServiceFile(item.getAbsolutePath()))
                {
              
                 if(item.isDirectory()){
                      
                     createAllHistograms(item.getAbsolutePath());
                 }
                 else{
                      
                     if(SearchEngine.getFileExtension(item).equals("txt"))
                     {
                         System.out.println(item.getName());
                         createHistogram(Paths.get(item.getAbsolutePath()));
                         
                     }
             }
            }
        }
        }
        }
}
    public static boolean isServiceFile(String path)
    {
         String s =new File(path).getName();
         if(s.contains("$"))
             return true;
         else
             return false;
    }
    public static void createWordBase(String path) throws IOException
    {
        numWordsInBase=0;
        if(Files.exists(Paths.get(wordBaseSer)))
                {
                    Files.delete(Paths.get(wordBaseSer));
                }
        if(Files.exists(Paths.get(wordBasePath)))
        {
            Files.delete(Paths.get(wordBasePath));
            Files.createFile(Paths.get(wordBasePath));
        }
        else
        {
             Files.createFile(Paths.get(wordBasePath));
        }  
        File dir = new File(path);
        if(dir.isDirectory())
        {
            File[] list=dir.listFiles();
            if(list!=null)
            {
            for(File item : list){
                if(!isServiceFile(item.getAbsolutePath()))
                {
                 if(item.isDirectory()){
                      
                     createWordBase(item.getAbsolutePath());
                 }
                 else{
                      
                     if(SearchEngine.getFileExtension(item).equals("txt"))
                     {
                         System.out.println(item.getName());
                         partitionFile(item.getAbsolutePath(),wordBasePath);
                          if(numWordsInBase>MAX_NUMBER_WORDS)
                              return;
                         
                     }
             }
            }
        }
        }
        }
        
    }
     public static void createHistogram(Path path) throws IOException
    {
        Hashtable ht=SearchEngine.getHashTable();
        List<String> allLinesFromFile = Files.readAllLines(path, Charset.forName("Cp1251"));
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < allLinesFromFile.size(); i++) {
            String[] words = allLinesFromFile.get(i).split("[\\p{Punct}\\s]+");
            for (String word : words) {
                if(!arrayOfStopWords.contains(word))
                {
                if (ht.containsKey(word)) {
                    if(map.containsKey(ht.get(word)))
                        map.put((Integer)ht.get(word),1+map.get(ht.get(word)));
                    else
                        map.put((Integer)ht.get(word),1);
                    
                    
                }
                }
            } 
        }
    
       String filePath = path.toString();
       String serPath;
       serPath=filePath.substring(0,filePath.lastIndexOf("."));
       serPath=serPath + ".ser";
       if(!Files.exists(Paths.get(serPath)))
       {
           Files.createFile(Paths.get(serPath));
       }
       else
       {
           Files.delete(Paths.get(serPath));
           Files.createFile(Paths.get(serPath));
       }
       
       FileOutputStream outputStream = new FileOutputStream(serPath);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(map);
             objectOutputStream.close();
        }
       
    }
    private static void appendFileWriter(String filePath, String text) throws IOException {
        File file = new File(filePath);
        BufferedWriter fr = null;
        fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
        fr.write(text+"\n");
        fr.close();
    }
    
}
