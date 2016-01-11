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
    	
    	log.info( "Hotovo!" );
    }

}
