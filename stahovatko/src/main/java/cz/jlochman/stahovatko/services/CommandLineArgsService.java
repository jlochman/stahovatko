package cz.jlochman.stahovatko.services;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class CommandLineArgsService {

	@Parameter(names = "-h --help", description = "zobrazit napovedu")
	private boolean help = false;
	
	@Parameter(names = "-downDates", description = "zobrazit veskera data stahovani")
	private boolean showDownDates = false;
	
	@Parameter(names = "-c", description = "compare last two downDates")
	private boolean compareLastDownDates = false;
	
	@Parameter(names = "--compareFile", description = "soubor pro export porovnani")
	private String compareFile;
	
	@Parameter(names = "-d", description = "download")
	private boolean download = false;
	
	@Parameter(names = "-newDownload", description = "zacit nove stahovani")
	private boolean newDownload = false;
	
	@Parameter(names = "--threads", description = "pocet vlaken ke stahovani")
	private int numThreads = 20;

	@Parameter(names = "--inFile", description = "xls soubor s exportem ze SUKLU.")
	private String fileName;

	@Parameter(names = "--workingDir", description = "adresar, kde ma program dovoleno R/W. Pro ukladani souboru.")
	private String workingDir;

	@Parameter(names = "--filesDir", description = "adresar, kam se budou ukladat stazene unikatni soubory.")
	private String filesDir;
	
	@Parameter(names = "--exportFile", description = "soubor, ktery bude pouzit pro export dat")
	private String exportFile = "/Users/jlochman/Documents/stahovatko/export.txt";
	
	@Parameter(names = "-e", description = "export")
	private boolean export = false;

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

	public boolean isExport() {
		return export;
	}

	public void setExport(boolean export) {
		this.export = export;
	}
	
	public boolean isShowDownDates() {
		return showDownDates;
	}

	public void setShowDownDates(boolean showDownDates) {
		this.showDownDates = showDownDates;
	}

	public String getExportFile() {
		return exportFile;
	}

	public void setExportFile(String exportFile) {
		this.exportFile = exportFile;
	}

	public boolean isNewDownload() {
		return newDownload;
	}

	public void setNewDownload(boolean newDownload) {
		this.newDownload = newDownload;
	}
	
	public boolean isCompareLastDownDates() {
		return compareLastDownDates;
	}

	public void setCompareLastDownDates(boolean compareLastDownDates) {
		this.compareLastDownDates = compareLastDownDates;
	}

	public String getCompareFile() {
		return compareFile;
	}

	public void setCompareFile(String compareFile) {
		this.compareFile = compareFile;
	}

	

	
}
