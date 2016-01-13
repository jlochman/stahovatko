package cz.jlochman.stahovatko.threadpool;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.domain.DrugItem;
import cz.jlochman.stahovatko.helpers.FileHelper;
import cz.jlochman.stahovatko.helpers.StringHelper;
import cz.jlochman.stahovatko.services.ServiceLocator;

public class DrugDownloadThread implements Runnable {

	private Element htmlInput;
	private ServiceLocator services;
	private static Logger log = Logger.getLogger(DrugDownloadThread.class);
	
	public DrugDownloadThread(Element element) {
		this.htmlInput = element;
		services = ServiceLocator.getInstance();
	}
	
	public void run() {
		try {
			DrugItem drugItem = parseDrugItem( htmlInput );
			if ( drugItem != null ) {
				log.info( "PERSISTING: " + drugItem.getCode() + "  " + drugItem.getName() );
				services.getDrugDao().persistDrugItem( drugItem );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DrugItem parseDrugItem(Element element) throws IOException {
		DrugItem drugItem = new DrugItem();

		Elements rowItems = element.select("td*");
		drugItem.setDownDate( services.getDownloadService().getDownDate() );
		drugItem.setCode(rowItems.get(0).text());
		drugItem.setName(rowItems.get(1).text());
		drugItem.setNameShort(StringHelper.normalizeDrugName(drugItem.getName()));
		drugItem.setNameSupp(rowItems.get(2).text());
		drugItem.setAtc(rowItems.get(15).text());
		
		// kontrola, zda dany lek se uz nepersistoval
		if ( ! ServiceLocator.getInstance().getCommandLineArgsServie().isNewDownload() ) {
			if ( ServiceLocator.getInstance().getDrugDao().isPersisted(drugItem) ) {
				log.info( "ALREADY PERSISTED: " + drugItem.getCode() + "  " + drugItem.getName() );
				return null;
			}
		}
		log.info(drugItem.getCode() + "  " + drugItem.getName());

		String httpFiles = rowItems.get(rowItems.size() - 1).text();
		if ( ! httpFiles.contains("http") ) return drugItem;
		Document htmlPage = FileHelper.getHtmlPageFromURL(httpFiles);

		Element table = htmlPage.select("tbody").first();
		Elements links = table.select("a");
		for (Element el : links) {
			String link = el.attr("abs:href");
			Map<String, List<String>> params = StringHelper.getQueryParams(link);
			if (link.contains("sukl.cz")) {
				String type = params.get("type").get(0);
				String extension = FileHelper.getFileExtension(params.get("file").get(0));
				if (type.equals("pil")) {
					drugItem.setPilFile(getDrugFile(link, drugItem.getNameShort(), "PIL." + extension));
				} else if (type.equals("spc")) {
					drugItem.setSpcFile(getDrugFile(link, drugItem.getNameShort(), "SPC." + extension));
				}
			} else if (link.contains("europa.eu")) {
				drugItem.setPilFile(getDrugFile(link, drugItem.getNameShort(), "EMA.pdf"));
				drugItem.setSpcFile(getDrugFile(link, drugItem.getNameShort(), "EMA.pdf"));
				return drugItem;
			}
		}
		return drugItem;
	}


	private DrugFile getDrugFile(String link, String drugNameShort,
			String fileSuffix) {
		DrugFile drugFile = new DrugFile();

		if ( services.getDownloadService().getMapLinkFile().containsKey(link) )
			return services.getDownloadService().getMapLinkFile().get(link);

		String fileName = FileHelper.downloadURLToFile(link, services.getCommandLineArgsServie().getWorkingDir() + File.separator + Thread.currentThread().getName() + "_" + fileSuffix);
		if (fileName == null)
			return null;
		File file = new File(fileName);
		drugFile = createDrugFile(file, drugNameShort);

		services.getDownloadService().getMapLinkFile().put(link, drugFile);
		return drugFile;
	}

	private DrugFile createDrugFile(File file, String drugNameShort) {
		DrugFile newFile = createNewDrugFile(file);
		DrugFile oldFile = services.getDrugDao()
				.getLastFile(newFile);
		if (newFile.equals(oldFile)) {
			return oldFile;
		} else {
			copyFileToSaveDir(newFile, file, drugNameShort);
			return newFile;
		}
	}
	
	private boolean copyFileToSaveDir(DrugFile drugFile, File tmpFile,
			String drugNameShort) {
		String saveFilePath = services.getCommandLineArgsServie().getFilesDir() + File.separator + drugNameShort
				+ "_" + drugFile.getFileMD5() + "_" + tmpFile.getName();
		File saveFile = new File(saveFilePath);
		try {
			FileHelper.copyFileUsingStream(tmpFile, saveFile);
			drugFile.setFilePath(saveFile.getName());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private DrugFile createNewDrugFile(File file) {
		DrugFile newFile = new DrugFile();
		newFile.extractFromFile(file);
		return newFile;
	}
	
}
