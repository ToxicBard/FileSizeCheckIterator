package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import commonTools.FileTools;
import commonTools.LoadingThread;
import commonTools.Timer;

public class Main {

	public static void main(String[] args) throws IOException {
		//Declare the necessary variables
		LoadingThread progressDisplay = new LoadingThread();
		Timer iterationTimer = new Timer();
		BufferedWriter outputWriter = FileTools.openWriteFile("out/results.txt");
		ArrayList<File> matchingFiles;
		File checkDirectory = FileTools.selectSavedDirectory("Select the check directory", "cfg/checkDirectory.cfg");
		ComparisonMode comparator = ComparisonMode.EqualTo;
		int checkSize = 0;
		
		//Start the loading display and timer
		progressDisplay.start();
		iterationTimer.start();
		
		//Do the check iteration
		matchingFiles = checkFiles(checkDirectory, comparator, checkSize);
		
		for(File loopFile : matchingFiles){
			outputWriter.write(loopFile.getAbsolutePath() + ", " + loopFile.length() + "\n");
		}
		
		//Close the output file
		outputWriter.close();
		
		//stop the progress display and timer
		progressDisplay.stopRunning();
		iterationTimer.stop();
		
		//Tell the user how long the iteration took
		System.out.println(iterationTimer.getElapsedIntervalString());
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
