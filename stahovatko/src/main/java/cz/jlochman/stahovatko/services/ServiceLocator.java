package cz.jlochman.stahovatko.services;

import cz.jlochman.stahovatko.domain.DrugDAO;
import cz.jlochman.stahovatko.domain.HibernateDrugDAO;

public class ServiceLocator {

	private static ServiceLocator instance;
	private static DrugDAO drugDao;
	
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
	}
	
	public DrugDAO getDrugDao() {
		return drugDao;
	}

	/*
	public DownloadService getDownloadService() {
		return new DownloadService();
	}

	
	public ExportService getExportService() {
		return new ExportService();
	}
	*/
}
