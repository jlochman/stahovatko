package cz.jlochman.stahovatko.services;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class CommandLineArgsService {

	@Parameter(names = "-h --help", description = "zobrazit napovedu")
	private boolean help = false;
	
	@Parameter(names = "-d", description = "download")
	private boolean download = false;

	@Parameter(names = "--threads", description = "pocet vlaken ke stahovani")
	private int numThreads = 10;

	@Parameter(names = "--inFile", required = false, description = "xls soubor s exportem ze SUKLU.")
	private String fileName;

	@Parameter(names = "--workingDir", required = false, description = "adresar, kde ma program dovoleno R/W. Pro ukladani souboru.")
	private String workingDir;

	@Parameter(names = "--filesDir", required = false, description = "adresar, kam se budou ukladat stazene unikatni soubory.")
	private String filesDir;

	public void parseAndSaveArgs(String[] args) {
    	JCommander cmd = new JCommander( ServiceLocator.getInstance().getCommandLineArgsServie() );    	
    	try {
    		cmd.parse(args);
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			cmd.usage();
		}
	}
	
	@Override
	public String toString() {
		return "CommandLineArgs [numThreads = " + numThreads 
				+ ", fileName = " + fileName + ", workingDir = " + workingDir
				+ ", filesDir = " + filesDir + "]";
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

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}

	public int getNumThreads() {
		return numThreads;
	}

	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}

}