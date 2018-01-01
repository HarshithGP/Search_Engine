import java.util.StringTokenizer;
import org.apache.commons.io.FilenameUtils;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;

class SortOnLength implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {
    	return str1.length() - str2.length();
    }
}

public class CalculateWordProbability {

    private final static String DELIMS = " :;,.{}()\t\n";
    private final static HashMap<String,Integer> m = new HashMap<String,Integer>();
    
    public static void sortAndRewriteFiles(String fname) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(fname));
    	String line;
    	String allContent = "";
    	ArrayList<String> arr = new ArrayList<String>();
    	SortOnLength sol = new SortOnLength();
    	
        while ((line = br.readLine()) != null) {
        	arr.add(line+"\n");
        }
        
        Collections.sort(arr,Collections.reverseOrder(sol));
        
        //write arraylist to file
        FileWriter writer = new FileWriter(fname); 
        
        for(String str: arr) {
          writer.write(str);
        }
        writer.close();
    }
    
    public static void tokenizeContent(String content){
    	StringTokenizer multiTokenizer = new StringTokenizer(content, DELIMS);

    	while (multiTokenizer.hasMoreTokens())
    	{
    		String token = multiTokenizer.nextToken().toLowerCase();
    		int index = token.indexOf("/");
    		
    		if((Character.isDigit(token.charAt(0))||Character.isLetter(token.charAt(0))) && (token.length() > 1) && (index == -1)){
    			if(!m.containsKey(token)){
        	    	m.put(token,1);
        	    }else{
        	    	m.put(token,m.get(token)+1);
        	    }
    		}
    	}
    }
    
    public static void writeMap(HashMap<String,Integer> map) throws FileNotFoundException, UnsupportedEncodingException{
    	
    	PrintWriter writer = new PrintWriter("serialized_dictionary.txt","UTF-8");
    	
    	try{
	    	for (Map.Entry<String,Integer> entry : m.entrySet()) {
	       		writer.println(entry.getKey()+"="+entry.getValue());
	    	}
    	
    	}catch(Exception e){
			e.printStackTrace();
		
    	}finally{
			writer.flush();
			writer.close();
		}
    }

    public static void main(String args[]) throws java.io.IOException {
        
        if (args.length < 1) {
            System.out.println("Usage: <Dir containing files to count>");
            System.exit(1);
        }
        
        File dir = new File(args[0]);
        File snippetsDir = new File("C:\\Fall 2016\\CSCI 572\\HW4\\snippets");
        
        if (!snippetsDir.exists()) {
            snippetsDir.mkdir();
        }
        
        for(File file: dir.listFiles()){
        	String title = Jsoup.parse(file,"UTF-8").select("title").text();
        	String body = Jsoup.parse(file,"UTF-8").select("body").text();
        	String anchor = Jsoup.parse(file,"UTF-8").select("a").text();
        	String img = Jsoup.parse(file,"UTF-8").select("img").text();
        	
        	String content = anchor + "\n" + img + "\n" + title + "\n" + body;
        	
        	String fileName = file.getName();
        	String baseName = FilenameUtils.removeExtension(fileName);
        	String finalFileName = baseName + ".txt";
        	
        	BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Fall 2016\\CSCI 572\\HW4\\snippets\\"+finalFileName));
        	bw.write(content);
        	bw.close();
        	
        	sortAndRewriteFiles("C:\\Fall 2016\\CSCI 572\\HW4\\snippets\\"+finalFileName);
        	
        	tokenizeContent(content);
        }
        
        writeMap(m);
    }
    
}
