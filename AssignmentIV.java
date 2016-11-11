//***********************************************************
// Morgan Buford
// COSC 4315.001
// UT Tyler, Spring 2016
// Due: April 15, 2016
//
// Assignment IV
// Purpose:	To implement the PageRank method for computing
// the "popularity" of a document.
//***********************************************************

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class AssignmentIV
{
	static Map<String, String> NumtoDept = new TreeMap<String, String>();
    static Map<String, Double> fileToRank = new TreeMap<String, Double>();
    static Map<String, Integer> fileNametoI = new TreeMap<String, Integer>();
    static Map<Integer, String> iToFileName = new TreeMap<Integer, String>();
    static Map<String, ArrayList<String>> fileToLinks = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> links = new ArrayList<String>();
    // HashMap to arraylist of links
	
	public static void main (String[] args)
	{		
		Scanner scanner = new Scanner(System.in);
		
		// Directory of files to be indexed
		System.out.println("Enter directory of input text files: ");
		String inputSt = scanner.nextLine();
		
		// Teleport probability
		System.out.println("Enter the teleport probability: ");
		double teleprob = scanner.nextDouble();
		
		// How many iterations should we perform?
		System.out.println("How many iterations?");
		int iterations = scanner.nextInt();
		
		// Create a file and array of files for the input directory
		File inputDir = new File(inputSt);
		File[] allInFiles = inputDir.listFiles();
		int numFiles = allInFiles.length;
		String fileName = "";
		File currFile;
        
		// M is the link probability matrix and P is the PageRank matrix
        double[][] M = new double[numFiles][numFiles];
        double[] P = new double[numFiles];
        
		// Initialize probability matrix to 0 and PageRank to 1/N
		for (int i = 0; i < numFiles; i++)
		{
            P[i] = (double)1 / numFiles;
            
			for (int j = 0; j < numFiles; j++)
			{
				M[i][j] = 0;
			}
		}
		
	try
	{
			// For every file in the directory
		// Add the title to the appropriate maps/arrays/etc
		for (int i = 0; i < numFiles; i++)
		{
			// For the ith file...
			currFile = allInFiles[i];
			Scanner scan1 = new Scanner(currFile);
			
			// Get the file name and extract only the digits
			fileName = currFile.getName();
			fileName = fileName.replaceAll(".txt","");
            fileNametoI.put(fileName, i);
            iToFileName.put(i, fileName);
		}
			
		for (int i = 0; i < numFiles; i++)
		{
			// For the ith file...
			currFile = allInFiles[i];
			Scanner scan = new Scanner(currFile);
			
			// Get the file name and extract only the digits
			fileName = currFile.getName();
			fileName = fileName.replaceAll(".txt","");
			
			String prefix;
			
			while(scan.hasNext())
			{
                // Get the next word in the file
                prefix = scan.next();
                
                // If the word is a 4-character word
                if(prefix.equals("COSC") || prefix.equals("MATH") || prefix.equals("MTED"))
                {
                	// Scan the next word and see if it is a 4-digit number
                    String cNum = scan.next();
                    
                    // Take only first 4 characters
                    cNum = cNum.substring(0, 4);
                    
                    if(isNumber(cNum))
                    {                        	
                        NumtoDept.put(cNum, prefix);
                        links.add(fileName);
                        
                        // If the link isn't already in the list and is not the same as the file name
                        if (!links.contains(prefix + " " + cNum) && !fileName.equals(prefix + " " + cNum))
                        {
                        	links.add(fileName);
                        	String linkName = prefix + " " + cNum;
                        	// get the linkName ith value
                        	if (fileNametoI.get(linkName) != null)
                        	{
                        		M[i][fileNametoI.get(linkName)]++;
                        	}                        	
                        }
                    }
                }
			}
			
			double pValue = P[(numFiles - 1)];
	        fileToRank.put(fileName, pValue);
	        fileToLinks.put(fileName, links);
	        // clear list of links for this page
	        links.clear();
		}
	}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
		
		double thisSum = 0;
    
        	for (int j = 0; j < numFiles; j++)
            {                    
                for ( int k = 0; k < numFiles; k++)
                {
                    thisSum += M[k][j]*P[k];
                }
            }
            P[(numFiles - 1)] = (teleprob/numFiles + (1 - teleprob)) * thisSum * P[numFiles-1];
        
		System.out.println();
		
		for (int i = 0; i < numFiles; i++)
		{
			System.out.print(iToFileName.get(i) + ":\t");
			
			for (int j = 0; j < numFiles; j++)
			{
				System.out.print(P[i] + "\t\t");
			}
			System.out.println();
		}
	}
	
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map)
	{
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>()
			{
				@Override
				public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2)
					{
					return (e2.getValue()).compareTo(e1.getValue());
			}
			});
	 
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
	 
		return result;
	}
	
	public static boolean isNumber(String string)
	{
	    if (string == null || string.isEmpty())
	    	{
	        	return false;
	    	}
	    	
	    int i = 0;
	    
	    if (string.charAt(0) == '-')
	    {
        	if (string.length() > 1)
        	{
            	i++;
        	}
        	else 
	        {
	            return false;
	        }
	    }
	    
	    for (; i < string.length(); i++)
	    	{
	        	if (!Character.isDigit(string.charAt(i)))
	        	{
	            	return false;
	        	}
	    	}
	    	
	    return true;
		}
}