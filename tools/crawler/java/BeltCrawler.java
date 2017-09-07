import java.io.*;
import java.util.*;

import org.apache.commons.io.FilenameUtils;


public class BeltCrawler {

	private static String version = "0.1";
	
	private static ArrayList<String> vendors = new ArrayList<String>(
			Arrays.asList("AUD", "AVV", "HEX", "ING", "SAG", "VTT", "XVT", "PTC")); 
	
	private static ArrayList<String> yellowBelt = new ArrayList<String>(
			Arrays.asList("E01V02","E02V02","E09V01","E10V01","P01V01","P02V01","I01V01","I02V01","I09V01","I10V01")); 
	
	private static ArrayList<String> orangeBelt = new ArrayList<String>(
			Arrays.asList("E11V01","E12V01","I03V01","I04V01","I05V01","I06V01","P03V01","P04V01","P05V01"));
	
	private static ArrayList<String> greenBelt = new ArrayList<String>(
			Arrays.asList("E07V01","E08V01","E13V01","I07V01","I08V01","I11V01","I12V01"));

private static ArrayList<String> blackBelt = new ArrayList<String>(
		Arrays.asList("C02V01","W01V01","W02V01"));

	
	public BeltCrawler() {
		System.out.println("# DEXPI Belt Crawler version " + version);
		java.util.Date date = new java.util.Date();
	    System.out.println(date);
	}	
	
	public void startCrawling(String folder) {
		
	
		System.out.println("## Yellow Belt");
		System.out.println("![yellow belt](https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/BJJ_Yellow_Belt.svg/200px-BJJ_Yellow_Belt.svg.png)\n");

		crawlABelt(folder, "yellow");
		
		System.out.println("## Orange Belt");
		System.out.println("![yellow belt](https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/BJJ_Orange_Belt.svg/200px-BJJ_Orange_Belt.svg.png)\n");
	
		crawlABelt(folder, "orange");
		
		System.out.println("## Green Belt");
		System.out.println("![green belt](https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/BJJ_Green_Belt.svg/200px-BJJ_Green_Belt.svg.png)\n");

		crawlABelt(folder, "green");
		
		System.out.println("## Black Belt");
		System.out.println("![black belt](https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/BJJ_Grey_Belt.svg/200px-BJJ_Grey_Belt.svg.png)\n");

		crawlABelt(folder, "black");

	}

	private void crawlABelt(String folder, String beltType) {
		List<String> bfs = getFilesForBelt(folder, beltType);
		bfs = doFileNamePostProcessing(bfs);
		List table = buildTable(bfs, beltType);
		String strTable = convertTableToMD(table);
		System.out.println(strTable);
	}


	private String convertTableToMD(List<List> table) {
		String s = "";
		
		//Header
		s += "TestCase |";
		for (String vendor : vendors) {
			s += vendor + "|";
		}
		s += "\n";
		
		//Line under Header
		s += "---";
		for(int i=0; i<vendors.size(); i++) s += "|---";
		s += "\n";
		
		//the table
		for (List<String> row : table) {
			for (String cell : row) {
				s += cell + "|";
			}
			s += "\n";
		}
		return s;
	}

	private List<List> buildTable(List<String> bfs, String beltType) {
		List<String> testCases = getTestCasesForBelt(beltType);
		List<List> table = new ArrayList<List>();
		for (String tc : testCases) {
			List<String> row = new ArrayList<String>();
			row.add(tc);
			for (String vendor : vendors) {
				String state = calculateState(vendor, tc, bfs);
				row.add(state);
			}
			table.add(row);
		}
		return table;
	}

	private String calculateState(String vendor, String tc, List<String> bfs) {
		String state = "";
		//Step 1: Calcuate X's
		for (String f : bfs) {
			if (f.startsWith(tc + "-" + vendor + ".EX")) {
				state += "X";
				break;
			}
		}
		
		//Step 2: Calculate i's
		for (String f: bfs) {
			if (f.startsWith(tc) && f.contains(vendor + ".IM")) {
				state += "i";
			}
		}
		
		return state;
	}

	public List<String> getFilesForBelt(String folder, String beltType) {
		ArrayList beltFiles = new ArrayList();
		File dir = new File(folder);
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
		    if (file.isFile()) {
		    	//check if the first part of the file contains to one of the test cases
		    	List<String> tcs = getTestCasesForBelt(beltType);
		    	for (String tcname : tcs) {
					if (file.getName().startsWith(tcname)) { 
						beltFiles.add(file.getName());
//						System.out.println("Found: " + file.getName());
					}
				}
		    }
		    if (file.isDirectory()) {
				//String subfolder = folder + file.getName();
				String subfolder = FilenameUtils.concat(folder, file.getName());
		    	try {
					//go down the directory (RECURSION!)
//					System.out.println("Stepping into " + subfolder);
					List<String> subBeltFiles = getFilesForBelt(subfolder, beltType);
					beltFiles.addAll(subBeltFiles);
				} catch (Exception e) {
					System.err.println("Can not change to " + subfolder);
				}
		    }
		}
		return beltFiles;
	}
	
	private List<String> doFileNamePostProcessing(List<String> bfs) {
		List<String> newBfs = new ArrayList<String>();
		for (String fileName : bfs) {
			fileName = FilenameUtils.removeExtension(fileName);
			if (!newBfs.contains(fileName)) newBfs.add(fileName);
		}
		return newBfs;
	}

	public List<String> getTestCasesForBelt(String beltType) {
		if (beltType.equals("yellow")) {
			return yellowBelt;
		}
		else if (beltType.equals("orange")) {
			return orangeBelt;
		}
		else if (beltType.equals("green")) {
			return greenBelt;
		}
		else if (beltType.equals("black")) {
			return blackBelt;
		}
		else {
			System.err.println("Belt color '"+ beltType + "' not found!");
		}
		
		return null;
	}
	 
}
