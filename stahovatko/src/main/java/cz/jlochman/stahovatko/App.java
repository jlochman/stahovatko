package cz.jlochman.stahovatko;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.beust.jcommander.JCommander;

import cz.jlochman.stahovatko.domain.DownDate;
import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.domain.DrugItem;
import cz.jlochman.stahovatko.services.ServiceLocator;



/**
 * Hello world!
 *
 */
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
    	    	
    	DrugItem drug = new DrugItem();
    	drug.setAtc("ATC");
    	drug.setCode("CODE");
    	//drug.setDownDate( new DownDate( new Date() ));
    	drug.setName("name");
    	drug.setNameShort("name_short");
    	drug.setNameSupp("name_supp");
    	drug.setPilFile( new DrugFile("PIL file path") );
    	//drug.setSpcFile( new DrugFile("SPC file path") );
    	//drug.setId( 16 );
    	
    	services.getDrugDao().persistDrugItem( drug );
    	
    	DrugItem drug1 = new DrugItem();
    	//drug1.setCode( new DrugCode("87654321") );
    	services.getDrugDao().persistDrugItem( drug1 );
    	
    	System.out.println( "Hello World!" );
    	/*
        System.out.println( "Hello World!" );
        
        try {
        	File input = new File("/Users/jlochman/Desktop/medicine-export-12732.xls");
        	System.out.println( input.getPath() );
			Document doc = Jsoup.parse(input, "UTF-8");
			Element tableElement = doc.select("table").first();
			Elements tableRowElements = tableElement.select("tr");
						
			Document htmlPage;
			for (int i = 1; i < tableRowElements.size(); i++) {
	            Elements rowItems = tableRowElements.get(i).select("td*");
	            System.out.println( "KOD SUKL: " + rowItems.get(0).text() );
	            System.out.println( "Nazev: " + rowItems.get(1).text() );
	            System.out.println( "Doplnek Nazvu: " + rowItems.get(2).text() );
	            System.out.println( "ATC: " + rowItems.get(15).text() );
	            System.out.println( "SPC/PIL: " + rowItems.get( rowItems.size() - 1 ).text() );
	            
	            htmlPage = Jsoup.connect( rowItems.get( rowItems.size() - 1 ).text() ).get();
	            
	            Element table = htmlPage.select("tbody").first();
	            Elements links = table.select("a");
	            for (Element el : links) {
					System.out.println( " " + el.attr("abs:href") );
				}
	         }
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
        
    }

}
