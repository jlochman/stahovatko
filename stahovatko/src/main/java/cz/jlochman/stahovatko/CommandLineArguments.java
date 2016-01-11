package cz.jlochman.stahovatko;

import com.beust.jcommander.Parameter;

public class CommandLineArguments {
	
	@Parameter(names = "-h --help", description = "zobrazit napovedu")
	private boolean help = false;
	
	@Parameter(names = "-downNew", description = "zacit stahovat novou verzi dat")
	private boolean downloadNew = false;
	
	@Parameter(names = "-downCont", description = "pokracovat ve stahovani aktualni verze")
	private boolean downloadContinue = false;

	@Parameter(names = "--inFile", required = false, description = "xls soubor s exportem ze SUKLU.")
	private String fileName;
	
	@Parameter(names = "--workingDir", required = false, description = "adresar, kde ma program dovoleno R/W. Pro ukladani souboru.")
	private String workingDir;
	
	@Parameter(names = "--filesDir", required = false, description = "adresar, kam se budou ukladat stazene unikatni soubory.")
	private String filesDir;
		
	@Override
	public String toString() {
		return "CommandLineArgs [downNew = " + downloadNew + " downCont = " + downloadContinue + " fileName = "+fileName+", workingDir = "+workingDir+", filesDir = "+filesDir+"]";
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

	public String getFilesDir() {
		return filesDir;
	}

	public void setFilesDir(String filesDir) {
		this.filesDir = filesDir;
	}

	public boolean isDownloadNew() {
		return downloadNew;
	}

	public void setDownloadNew(boolean downloadNew) {
		this.downloadNew = downloadNew;
	}

	public boolean isDownloadContinue() {
		return downloadContinue;
	}

	public void setDownloadContinue(boolean downloadContinue) {
		this.downloadContinue = downloadContinue;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}
	
	
	
	
}
