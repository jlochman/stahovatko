package cz.jlochman.stahovatko.export;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
		result += ";" + di.getAtc();
		result += ";" + di.getName();
		result += ";" + di.getNameSupp();
		if ( di.getPilFile() != null ) result += ";" + di.getPilFile().getFilePath();
		else result += ";null";
		if ( di.getSpcFile() != null ) result += ";" + di.getSpcFile().getFilePath();
		else result += ";null";
		return result;
	}

	public void compareLastDownDates() {
		List<DownDate> downDates = ServiceLocator.getInstance().getDrugDao().getAllDownDates();
		if ( downDates.size() < 2 ) {
			log.info( "nedostatecny pocet stazeni" ); 
			return;
		}
		DownDate newDownDate = downDates.get(0);
		DownDate oldDownDate = downDates.get(1);
		
		DrugItemComparator comparator = new DrugItemComparator( DrugItemComparator.Type.Name );
		log.info( "ziskavam drugsNew");
		List<DrugItem> drugsNew = ServiceLocator.getInstance().getDrugDao().getDrugsForDownDate( newDownDate );
		Collections.sort( drugsNew, comparator );
		log.info( "ziskavam drugsOld");
		List<DrugItem> drugsOld = ServiceLocator.getInstance().getDrugDao().getDrugsForDownDate( oldDownDate );
		Collections.sort( drugsOld, comparator );
		
		log.info("odstranuji shody" );
		DrugItem diOld, diNew;
		for ( Iterator<DrugItem> itOld = drugsOld.iterator(); itOld.hasNext(); ) {
			diOld = itOld.next();
			for ( Iterator<DrugItem> itNew = drugsNew.iterator(); itNew.hasNext(); ) {
				diNew = itNew.next();
				if ( diNew.equals(diOld) ) {
					itOld.remove();
					itNew.remove();
					break;
				}
			}
		}
		
		log.info("ziskavam modifikace, nove, smazane");
		List<DrugItem> drugsModified = new ArrayList<DrugItem>();
		for ( Iterator<DrugItem> itOld = drugsOld.iterator(); itOld.hasNext(); ) {
			diOld = itOld.next();
			for ( Iterator<DrugItem> itNew = drugsNew.iterator(); itNew.hasNext(); ) {
				diNew = itNew.next();
				if ( diNew.getCode().equals( diOld.getCode()) ) {
					drugsModified.add( diOld );
					drugsModified.add( diNew );
					itOld.remove();
					itNew.remove();
					break;
				}
			}
		}	
		
		log.info("exportuji vysledek porovnani");
		List<String> lines = new ArrayList<String>();
		lines.add("porovnavam data: ");
		lines.add( " - Stare: " + oldDownDate.getDate() );
		lines.add( " - Nove: " + newDownDate.getDate() );
		
		lines.add("nove leky");
		for ( DrugItem di : drugsNew ) {
			lines.add( drugItemToString(di) );
		}
		lines.add("odstranene leky");
		for ( DrugItem di: drugsOld ) {
			lines.add( drugItemToString(di) ); 
		}
		lines.add("modifikace leky (po dvojicich, kde nahore stary, dole novy)");
		for ( DrugItem di: drugsModified ) {
			lines.add( drugItemToString(di) );
		}
		Path file = Paths.get( ServiceLocator.getInstance().getCommandLineArgsServie().getCompareFile() );
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			log.error( "soubor " + file.toString() + " se nepodarilo vytvorit");
			e.printStackTrace();
		}
		
		System.out.println( drugsModified.size() );
		System.out.println( drugsNew.size() );
		
	}

}
