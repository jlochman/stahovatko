package cz.jlochman.stahovatko.services;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.jlochman.stahovatko.domain.DownDate;
import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.threadpool.DrugDownloadThread;

public class DownloadService {

	private DownDate downDate = new DownDate();
	private Map<String, DrugFile> mapLinkFile = new HashMap<String, DrugFile>();
	private static Logger log = Logger.getLogger(DrugDownloadThread.class);

	public Map<String, DrugFile> getMapLinkFile() {
		return this.mapLinkFile;
	}

	public DownDate getDownDate() {
		return this.downDate;
	}

	public void downloadAndUpdate() {
		prepareDirs();
		if ( ServiceLocator.getInstance().getCommandLineArgsServie().isNewDownload() ) {
			log.info("zahajuji nove stahovani");
			downDate.setDate(new Date());			
		} else {
			log.info("pokracuji v poslednim stahovani");
			downDate = ServiceLocator.getInstance().getDrugDao().getLastDownDate();
		}

		try {
			File input = new File( ServiceLocator.getInstance().getCommandLineArgsServie().getFileName() );

			log.info("parsuji leky");
			Document doc = Jsoup.parse(input, "UTF-8");
			Element tableElement = doc.select("table").first();
			Elements tableRowElements = tableElement.select("tr");
			
			log.info("jedu cyklus pres leky");				
			if ( tableRowElements == null || tableRowElements.isEmpty() ) return;
			tableRowElements.remove(0);
			Random rand = new Random();
			ExecutorService executor = Executors.newFixedThreadPool( ServiceLocator.getInstance().getCommandLineArgsServie().getNumThreads() );
			while ( tableRowElements.size() > 0 ) {
				int i = rand.nextInt( tableRowElements.size() );
				Runnable worker = new DrugDownloadThread( tableRowElements.get(i) );
				executor.execute(worker);	
				tableRowElements.remove(i);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}
			log.info("Finished all threads");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareDirs() {
		String tmpDir = ServiceLocator.getInstance().getCommandLineArgsServie().getWorkingDir();
		if (tmpDir == null || tmpDir.isEmpty()) {
			log.info("--tmpDir musi byt definovan");
			return;
		}
		log.info("Vytvarim adresar tmpDir: " + tmpDir);
		new File(tmpDir).mkdirs();

		String filesDir = ServiceLocator.getInstance().getCommandLineArgsServie().getFilesDir();
		if (filesDir == null || filesDir.isEmpty()) {
			log.info("--filesDir musi byt definovan");
			return;
		}
		log.info("Vytvarim adresar filesDir: " + filesDir);
		new File(filesDir).mkdirs();
	}
		
}
