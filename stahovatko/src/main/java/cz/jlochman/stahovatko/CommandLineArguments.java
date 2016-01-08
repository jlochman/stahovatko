package cz.jlochman.stahovatko;

import com.beust.jcommander.Parameter;

public class CommandLineArguments {

	@Parameter(names = "--inFile", required = false, description = "xls soubor s exportem ze SUKLU.")
	private String fileName;
	
	@Parameter(names = "--workingDir", required = false, description = "adresar, kde ma program dovoleno R/W. Pro ukladani souboru.")
	private String workingDir;
	
	@Parameter(names = "--exportDir", required = false, description = "adresar, kam se skopiruje dana verze stazeni.")
	private String exportDir;
	
	public void help() {
		System.out.println("Parametry: ");
		System.out.println();
	}
	
	@Override
	public String toString() {
		return "CommandLineArgs [fileName = "+fileName+", workingDir = "+workingDir+", exportDir = "+exportDir+"]";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	public String getExportDir() {
		return exportDir;
	}

	public void setExportDir(String exportDir) {
		this.exportDir = exportDir;
	}
	
}
