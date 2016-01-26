package cz.jlochman.stahovatko;

import org.apache.log4j.Logger;

import cz.jlochman.stahovatko.services.ServiceLocator;

public class App 
{
	
	private static Logger log = Logger.getLogger(App.class);
	
    public static void main( String[] args )
    {    	
    	ServiceLocator services = ServiceLocator.getInstance();
    	log.info("parsuji argumenty z CL");
    	services.getCommandLineArgsServie().parseAndSaveArgs( args );	
    	
    	if ( services.getCommandLineArgsServie().isDownload() ) {
    		log.info("zahajuji stahovani");
    		services.getDownloadService().downloadAndUpdate();
    	}
    	
    	if ( services.getCommandLineArgsServie().isShowDownDates() ) {
    		log.info("zobrazuji vsechna data, ve kterych se stahovalo");
    		services.getExportService().printDownDates();
    	}
    	
    	if ( services.getCommandLineArgsServie().isCompareLastDownDates() ) {
    		log.info("porovnavam posledni dve stahovai");
    		services.getExportService().compareLastDownDates();
    	}

    	if ( services.getCommandLineArgsServie().isExport() ) {
    		log.info("zahajuji export dat");
    		services.getExportService().saveDrugs( services.getDrugDao().getDownDateByID(2) );
    	}
    	
    	log.info( "Hotovo!" );
    }

}
