package cz.jlochman.stahovatko.services;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
		downDate.setDate(new Date());

		try {
			File input = new File( ServiceLocator.getInstance().getCommandLineArgsServie().getFileName() );

			log.info("parsuji leky");
			Document doc = Jsoup.parse(input, "UTF-8");
			Element tableElement = doc.select("table").first();
			Elements tableRowElements = tableElement.select("tr");
			
			log.info("jedu cyklus pres leky");			
			List<Integer> indexList = new LinkedList<Integer>();
			for ( int i = 1; i < tableRowElements.size(); i++ ) indexList.add(i);
			Collections.shuffle( indexList );
			ExecutorService executor = Executors.newFixedThreadPool( ServiceLocator.getInstance().getCommandLineArgsServie().getNumThreads() );
			for (Integer i : indexList) {
				Runnable worker = new DrugDownloadThread( tableRowElements.get(i) );
				executor.execute(worker);				
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
