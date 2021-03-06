import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;


public class MatrixCrawler {

	private static String version = "0.3";
	
	private static ArrayList<String> vendors = new ArrayList<String>(
			Arrays.asList("AUD", "AVV", "HEX", "SAG", "VTT", "XVT")); 
	
	private static String testCaseRegexPattern = "[E,P,I,C][0-9][0-9][V][0-9][0-9]";
	
	private static List<String> testCases = new ArrayList<String>();
//			Arrays.asList("E01V02","E02V02","E09V01","E10V01","P01V01","P02V01","I01V01","I02V01","I09V01","I10V01", "E11V01","E12V01","I03V01","I04V01","I05V01","I06V01","P03V01","P04V01","P05V01", "E07V01","E08V01","E13V01","I07V01","I08V01","I11V01","I12V01")); 

	private List<String> checkedTestCases = new ArrayList<String>();
	
	public MatrixCrawler() {
		System.out.println("# DEXPI Interoperability Matrix");
		System.out.println("Automatically created by DEXPI-Matrix-Crawler version " + version);
		java.util.Date date = new java.util.Date();
	    System.out.println(date);
	}	

	public void startCrawling(String folder) {

		System.out.println("##  Preliminary statistical information: ");
		System.out.println("###  Analyzed Test Cases: ");
		testCases = generateListOfTestCases(folder);
		System.out.println("Number of TestCases analysed:" + testCases.size());
		for (String tc : testCases) System.out.println("* " + tc);
			
		System.out.println("###  Analyzed relevant Test Cases: ");
		List<String> fileNames = getFilesForMatrix(folder);
		fileNames = doFileNamePostProcessing(fileNames);
		
		int[][] table = buildTable(fileNames);
		
		String strTable = convertTableToMD(table);
		
		System.out.println("##  Interoperability Matrix: ");
		System.out.println(strTable);
	}

	public List<String> generateListOfTestCases(String folder) {
		List<String> tcs = new ArrayList<String>();
		File dir = new File(folder);
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
		    if (file.isFile()) {
		    	String tcName = file.getName().substring(0,6); //split the first six characters, e.g. C01V01
		    	//check name matches the needs
		    	if (tcName.matches(testCaseRegexPattern))
		    			tcs.add(tcName);
		    }
		    if (file.isDirectory()) {
				//String subfolder = folder + file.getName();
				String subfolder = FilenameUtils.concat(folder, file.getName());
		    	try {
					//go down the directory (RECURSION!)
					//System.out.println("Stepping into " + subfolder);
					List<String> subTCNs = generateListOfTestCases(subfolder);
					tcs.addAll(subTCNs);
				} catch (Exception e) {
					System.err.println("Can not change to " + subfolder);
				}
		    }
		}
		
		//remove duplicates
		Set<String> hs = new HashSet<>();
		hs.addAll(tcs);
		tcs.clear();
		tcs.addAll(hs);

		// Sorting
		Collections.sort(tcs, new Comparator<String>() {
		        @Override
		        public int compare(String tc1, String tc2)
		        {
		            return  tc1.compareTo(tc2);
		        }
		    });
		
		return tcs;
	}


	public List<String> getFilesForMatrix(String folder) {
		ArrayList matrixFileNames = new ArrayList();
		File dir = new File(folder);
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
		    if (file.isFile()) {
		    	//check if the first part of the file contains to one of the test cases
		    	for (String tcname : testCases) {
					if (file.getName().startsWith(tcname)) { 
						matrixFileNames.add(file.getName());
						//System.out.println("Found: " + file.getName());
					}
				}
		    }
		    if (file.isDirectory()) {
				//String subfolder = folder + file.getName();
				String subfolder = FilenameUtils.concat(folder, file.getName());
		    	try {
					//go down the directory (RECURSION!)
					//System.out.println("Stepping into " + subfolder);
					List<String> subBeltFiles = getFilesForMatrix(subfolder);
					matrixFileNames.addAll(subBeltFiles);
				} catch (Exception e) {
					System.err.println("Can not change to " + subfolder);
				}
		    }
		}
		return matrixFileNames;
	}
	
	private List<String> doFileNamePostProcessing(List<String> bfs) {
		List<String> newBfs = new ArrayList<String>();
		for (String fileName : bfs) {
			fileName = FilenameUtils.removeExtension(fileName);
			
			//Make Intergraph to Hexagon: 
			fileName = fileName.replaceAll("ING", "HEX");
			
			if (!newBfs.contains(fileName)) newBfs.add(fileName);
		}
		//System.out.println(newBfs);
		return newBfs;
	}

	private int[][] buildTable(List<String> fileNames) {
		int[][] table = new int[vendors.size()][vendors.size()];
		
		//From Vendors To Vendors
		for (int fi=0; fi<vendors.size(); fi++) {  // fi = from index
			for(int ti=0; ti<vendors.size(); ti++) { // ti = to index
				String fromVendor = vendors.get(fi);
				String toVendor = vendors.get(ti);
				
				for(String fName : fileNames) {
					//example file name: C01V01-HEX.EX02-SAG.IM01.pdf
					//If a file exists that contains the e.g. HEX.EX and SAG.IM it is a point for "from Hex to SAG"
					if (fName.contains(fromVendor + ".EX") && fName.contains(toVendor + ".IM")) {
						//System.out.println(fName);
						//check if already checked
						boolean found = false; 
						String shortName = fName.substring(0, 5) + fromVendor + toVendor;
						if (!this.checkedTestCases.contains(shortName)) {					
							table[fi][ti]++;  //increase the value for this matrix entry
							System.out.println("* Found: " + fromVendor + " -> " + toVendor + " (" + table[fi][ti] + ") in " + fName);
							this.checkedTestCases.add(shortName);
						}
						else {
							System.out.println("* Duplicate: " + fromVendor + " -> " + toVendor + " (" + table[fi][ti] + ") in " + fName);

						}
						
					}
					
				}
			}
		}
			
		return table;
	}
	
	private String convertTableToMD(int[][] table) {
		int cellWidth = 10;
		String s = "";
		
		//Header
		s += String.format("%-"+cellWidth+"s","__ |");
		for (String vendor : vendors) {
			s += String.format("%-"+cellWidth+"s",(vendor + "|"));
		}
		s += "\n";
		
		//Line under Header
		s += String.format("%-"+cellWidth+"s","---");
		for(int i=0; i<vendors.size(); i++) s += String.format("%-"+cellWidth+"s","|---");
		s += "\n";
		
		//the table
		//From Vendors To Vendors
		for (int fi=0; fi<vendors.size(); fi++) {  // fi = from index
			String fromVendor = vendors.get(fi);

			// Start row
			s += String.format("%-"+cellWidth+"s", fromVendor + " |");
			
			// add row values
			for(int ti=0; ti<vendors.size(); ti++) { // ti = to index
				String toVendor = vendors.get(ti);
				double percentValue = (double)table[fi][ti] / (double)testCases.size() * 100;
				//System.out.println(table[fi][ti] + " / " +  testCases.size() + " = " + percentValue);
				DecimalFormat df = new DecimalFormat("#.#");
				
				s += String.format("%-"+cellWidth+"s", df.format(percentValue) + "% |");
			}
			s += "\n";
		}
		return s;
	}
}
