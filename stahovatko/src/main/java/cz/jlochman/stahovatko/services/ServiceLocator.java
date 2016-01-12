package cz.jlochman.stahovatko.services;

import cz.jlochman.stahovatko.dao.DrugDAO;
import cz.jlochman.stahovatko.dao.HibernateDrugDAO;
import cz.jlochman.stahovatko.export.ExportService;

public class ServiceLocator {

	private static ServiceLocator instance;
	private static DrugDAO drugDao;
	private static DownloadService downloadSerivce;
	private static CommandLineArgsService claService;
	private static ExportService exportService;
	
	private ServiceLocator(){};

	public static ServiceLocator getInstance() {
		if(instance == null) {
			instance = new ServiceLocator();
			instance.init();
		}
		return instance;
	}

	private void init() {
		drugDao = new HibernateDrugDAO();
		downloadSerivce = new DownloadService();
		claService = new CommandLineArgsService();
		exportService = new ExportService();
	}
	
	public DrugDAO getDrugDao() {
		return drugDao;
	}

	public DownloadService getDownloadService() {
		return downloadSerivce;
	}
	
	public CommandLineArgsService getCommandLineArgsServie() {
		return claService;
	}
	
	public ExportService getExportService() {
		return exportService;
	}

}
