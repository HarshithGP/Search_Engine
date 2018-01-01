import java.util.StringTokenizer;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;

public class CalculateWordCount {

    private final static String DELIMS = " :;,.{}()\t\n";
    private final static HashMap<String,Integer> m = new HashMap<String,Integer>();
    
    public static void tokenizeContent(String content){
    	StringTokenizer multiTokenizer = new StringTokenizer(content, DELIMS);

    	while (multiTokenizer.hasMoreTokens())
    	{
    		String token = multiTokenizer.nextToken().toLowerCase();
    		if(Character.isDigit(token.charAt(0))||Character.isLetter(token.charAt(0))){
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
        long startTime = System.currentTimeMillis();
        
        if (args.length < 1) {
            System.out.println("Usage: <Dir containing files to count>");
            System.exit(1);
        }
        
        File dir = new File(args[0]);
        
        
        for(File file: dir.listFiles()){
        	String content = Jsoup.parse(file,"UTF-8").select("body").text();
        	tokenizeContent(content);
        }
        
        writeMap(m);
    }
}
