package cz.jlochman.stahovatko;

import com.beust.jcommander.JCommander;
import cz.jlochman.stahovatko.services.ServiceLocator;

public class App 
{
	
    public static void main( String[] args )
    {
    	CommandLineArguments cla = new CommandLineArguments();
    	JCommander cmd = new JCommander(cla);    	
    	try {
    		cmd.parse(args);
    		System.out.println( cla.toString() );			
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			cmd.usage();
		}
    
    	ServiceLocator services = ServiceLocator.getInstance();
    	services.getDownloadService().downloadAndUpdate( cla );
    	
    	System.out.println( "Hotovo!" );
    }

}
