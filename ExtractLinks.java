import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ExtractLinks
{
	static HashMap<String, String> urlFileMap = new HashMap<String, String>();
	static HashMap<String, String> FileURLMap = new HashMap<String, String>();
	
	public static void fetchFileNameExcel()
	{
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try 
		{
			br = new BufferedReader(new FileReader(csvFile));
			while((line = br.readLine()) != null) 
			{
			// use comma as separator
				String[] fileName = line.split(cvsSplitBy);
				urlFileMap.put(fileName[1],fileName[0]);				
				FileURLMap.put(fileName[0],fileName[1]);
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		
		finally 
		{
			if(br!=null)
			{
				try
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception
	{
		fetchFileNameExcel();
		HashSet<String> edgeList = new HashSet<String>();
		String outputFileName = "edgeList.txt";
		File outputFile = new File(outputFileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		File dirPath = new File("LATimesData\\LATimesDownloadData");

		for(File fileEntry: dirPath.listFiles())
		{
			Document doc = Jsoup.parse(fileEntry,"UTF-8", FileURLMap.get(fileEntry.getName()));

			Elements links = doc.select("a[href]");
			Elements media = doc.select("[src]");
			Elements imports = doc.select("link[href]");


			for(Element link: links)
			{
				String url = link.attr("abs:href").trim();
				if(urlFileMap.containsKey(url))
				{
					edgeList.add(fileEntry.getName() + " " + urlFileMap.get(url));
				}
			}
		}
		int count = 0;
		for(String s: edgeList)
		{
			writer.write(s);
			writer.newLine();
			System.out.println(count);
			count ++;
		}

		System.out.println("Successfully written. Total count: " + count);
		writer.close();
	}


	private static Object trim(String s, int width) 
	{
		if(s.length()>width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}

	
}