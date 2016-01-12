package cz.jlochman.stahovatko.export;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import cz.jlochman.stahovatko.comparators.DownDateComparator;
import cz.jlochman.stahovatko.comparators.DrugItemComparator;
import cz.jlochman.stahovatko.domain.DownDate;
import cz.jlochman.stahovatko.domain.DrugItem;
import cz.jlochman.stahovatko.services.ServiceLocator;

public class ExportService {
	
	private static Logger log = Logger.getLogger(ExportService.class);
	
	public void printDownDates() {
		List<DownDate> downDates = ServiceLocator.getInstance().getDrugDao().getAllDownDates();
		Collections.sort(downDates, new DownDateComparator());
		System.out.println( "ID DownDate");
		for (DownDate dd : downDates) {
			System.out.println( dd.getId() + " " + dd.getDate().toString() );
		}
	}
	
	public void saveDrugs( DownDate downDate ) {
		if ( downDate == null ) return;
		List<DrugItem> drugItems = ServiceLocator.getInstance().getDrugDao().getDrugsForDownDate( downDate );
		Collections.sort(drugItems, new DrugItemComparator());
		
		List<String> lines = new ArrayList<String>();
		for (DrugItem di : drugItems) {
			lines.add( drugItemToString(di) );
		}	
		Path file = Paths.get( ServiceLocator.getInstance().getCommandLineArgsServie().getExportFile() );
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			log.error( "soubor " + file.toString() + " se nepodarilo vytvorit");
			e.printStackTrace();
		}
		log.info( "data uspesne exportovana" );
	}
	
	private String drugItemToString( DrugItem di ) {
		String result = "";
		result += di.getCode();
		result += " " + di.getNameShort();
		if ( di.getPilFile() != null ) result += " " + di.getPilFile().getFilePath();
		else result += " null";
		if ( di.getSpcFile() != null ) result += " " + di.getSpcFile().getFilePath();
		else result += " null";
		return result;
	}

}
