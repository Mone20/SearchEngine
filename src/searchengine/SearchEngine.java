/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair ;
import static searchengine.SearchBase.isServiceFile;

public class SearchEngine {

   
    public static String beginPath;
    private static  Hashtable ht;
    private static int numOfTexts;
    private static int numOfHistograms;
    public static  ArrayList<Pair<Pair<String,Double>,Integer>> arrayOfFiles;  
  
  
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
       
        return fileName.substring(fileName.lastIndexOf(".")+1);
      
        else return "";
    }
    public static void setHashTable(Hashtable table)
    {
        ht=table;
    }
    public static Hashtable getHashTable()
    {
        return ht;
    }
    public static void countTextsAndHistograms(String path) throws IOException
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
                      
                     countTextsAndHistograms(item.getAbsolutePath());
                 }
                 else{
                      
                     if(getFileExtension(item).equals("ser"))
                     {
                         numOfHistograms++;    
                     }
                      if(getFileExtension(item).equals("txt"))
                     {
                         numOfTexts++;       
                     }
             }
            }
        }
        }
        }
    }
    public static int indexingOrSearch(String path) throws IOException, ClassNotFoundException
    {
        numOfHistograms=0;
        numOfTexts=0;
        countTextsAndHistograms(path);
        if(numOfTexts==0)
        {
            return 0;
        }
        else if((numOfTexts>numOfHistograms)||(numOfHistograms==0))
        {
        
        return 1;
            
        }
        return -1;

        
    }
   public static boolean isNotStop(String request)
   {
       Pattern pattern =
                Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS 
                        | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(request);
        boolean flag=false;
        String word;
        while (matcher.find())
        {
            word=matcher.group().toLowerCase();
            if(!SearchBase.arrayOfStopWords.contains(word))
                flag=true;
                
        }
        return flag;
   }
    public static void browseHistogram(String request,String hPath) throws IOException, FileNotFoundException, ClassNotFoundException
    {
                Pattern pattern =
                Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS 
                        | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(request);
       
        String word;
        int sum=0;
        HashMap hMap=deserializeHistogram(hPath);
        HashMap<Integer,Integer> template=new HashMap();
        ArrayList arrayOfIndex =new ArrayList<Integer>() ;
        double num=0;
        while (matcher.find())
        {   
            word=matcher.group().toLowerCase();
            Object key=ht.get(word);
            if(hMap.containsKey(key))
            {
                sum+=(int)hMap.get(key);
                if(template.containsKey(key))
                        template.put((Integer)key,1+template.get(key));
                    else
                        template.put((Integer)key,1);
                arrayOfIndex.add(key);
                num++;
                
            }
            
           
        }
        
        if(sum>0)
        {
             
           String txtPath;
           txtPath=hPath.substring(0,hPath.lastIndexOf("."));
           txtPath=txtPath + ".txt";
           double measure=num*measureHistogram(arrayOfIndex,hMap,template);
           arrayOfFiles.add(new Pair(new Pair(txtPath,measure),sum));
        }

       
    }
    
    public static HashMap deserializeHistogram(String path) throws FileNotFoundException, IOException, ClassNotFoundException
    {
       
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        HashMap hm = (HashMap) objectInputStream.readObject();
        return hm;
    }
    public static void setArrayOfFiles(ArrayList a)
    {
        arrayOfFiles=a;
    }
    
    public static double measureHistogram(ArrayList arrayOfIndex,HashMap<Integer,Integer> currentHistogram,HashMap<Integer,Integer> template)
    {
        double d=0;
    
        for(Object index: arrayOfIndex)
        {
            d+=(Math.pow((template.get(index)-currentHistogram.get(index)),2))/template.get(index);
        }
        return d;
        
    }
    public static void findHistograms(String request,String path) throws IOException, FileNotFoundException, ClassNotFoundException
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
                      
                    findHistograms(request,item.getAbsolutePath());
                 }
                 else{
                      
                     if(getFileExtension(item).equals("ser"))
                     {
                         browseHistogram(request,item.getAbsolutePath());
                         System.out.println(item.getName());       
                     }
             }
            }
        }
        }
        }

        
    }
    
 
  
     public static void main(String[] args) throws IOException, ClassNotFoundException {
         
         SearchBase.createStopWords();
         new PathWindow();

   }
    }
   
    

