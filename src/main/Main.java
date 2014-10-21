package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import commonTools.FileTools;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedWriter outputWriter = FileTools.openWriteFile("out/results.txt");
		ArrayList<File> matchingFiles;
		File checkDirectory = FileTools.selectSavedDirectory("Select the check directory", "cfg/checkDirectory.cfg");
		ComparisonMode comparator = ComparisonMode.LessThan;
		int checkSize = 50;
		
		//Do the check iteration
		matchingFiles = checkFiles(checkDirectory, comparator, checkSize);
		
		for(File loopFile : matchingFiles){
			outputWriter.write(loopFile.getAbsolutePath());
			outputWriter.write("");
		}
		
		outputWriter.close();
	}
	
	private static ArrayList<File> checkFiles(File lDirectory, ComparisonMode lComparator, int lSize){
		ArrayList<File> matchingFiles = new ArrayList<File>();
		File[] listDirectoryFiles = lDirectory.listFiles();
		
		//Sort the files alphabetically
		Arrays.sort(listDirectoryFiles);
		
		//Build list of files that match the provided extension
		for(File loopFile : listDirectoryFiles){
			if(loopFile.isFile()){
				/*
				 * If the current file is a file, rather than
				 * a directory, then perform the appropriate
				 * comparison and add it to the list of matching
				 * files if it matches
				 */
				switch(lComparator){
					case GreaterThan:
						if(loopFile.length() > lSize){
							matchingFiles.add(loopFile);
						}
						break;
					case LessThan:
						if(loopFile.length() < lSize){
							matchingFiles.add(loopFile);
						}
						break;
					case EqualTo:
						if(loopFile.length() == lSize){
							matchingFiles.add(loopFile);
						}
						break;
				}
			}
			
			/*
			 * If the file is a directory, then recursively
			 * add any files that would be returned from calling
			 * the checkFiles function on this directory
			 */
			
			if(loopFile.isDirectory()){
				matchingFiles.addAll(checkFiles(loopFile, lComparator, lSize));
			}
		}
		
		return matchingFiles;
	}
	
	public enum ComparisonMode {
		GreaterThan, LessThan, EqualTo
	}

}
