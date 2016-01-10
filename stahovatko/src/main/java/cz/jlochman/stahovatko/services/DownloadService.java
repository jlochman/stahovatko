package cz.jlochman.stahovatko.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.jlochman.stahovatko.CommandLineArguments;
import cz.jlochman.stahovatko.domain.DownDate;
import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.domain.DrugItem;

public class DownloadService {
	
	private String savePath;
	private String tmpDir;
	private DownDate downDate = new DownDate();
	private Map<String, DrugFile> mapLinkFile = new HashMap<String, DrugFile>();
	
	public DownloadService() {
		downDate.setDate( new Date() );
	}
	
	public void downloadAndUpdate( CommandLineArguments cla ) {
		System.out.println( cla.toString() );
		this.savePath = cla.getExportDir();
		this.tmpDir = cla.getWorkingDir();
		
		prepareDirs();
		
		try {
        	File input = new File( cla.getFileName() );
        	
        	System.out.println("parsuji leky");
			Document doc = Jsoup.parse(input, "UTF-8");
			Element tableElement = doc.select("table").first();
			Elements tableRowElements = tableElement.select("tr");
						
			System.out.println("jedu cyklus pres leky");
			for (int i = 690; i < tableRowElements.size(); i++) {
				System.out.println("[ " + i + " / " + tableRowElements.size() + " ]");
				DrugItem drugItem = parseDrugItem( tableRowElements.get(i) );
				ServiceLocator.getInstance().getDrugDao().persistDrugItem( drugItem );
	         }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void prepareDirs() {
		System.out.println("Vytvarim adresar pro stahovani provizornich souboru tmpDir: " + this.tmpDir);
		new File(this.tmpDir).mkdirs();
		
		System.out.println("Vytvarim adresar pro presun originalnich souboru savePath: " + this.savePath);
		new File(this.savePath).mkdirs();
		
		System.out.println("Vytvoreno");		
	}
	
	

	private DrugItem parseDrugItem( Element element ) throws IOException {
		DrugItem drugItem = new DrugItem();
		
		Elements rowItems = element.select("td*");
		drugItem.setDownDate( downDate );
		drugItem.setCode( rowItems.get(0).text() );
		drugItem.setName( rowItems.get(1).text() );
		drugItem.setNameShort( normalizeName( drugItem.getName() ) );
		drugItem.setNameSupp( rowItems.get(2).text() );
		drugItem.setAtc( rowItems.get(15).text() );
        
		String httpFiles = rowItems.get( rowItems.size() - 1 ).text();
        if ( ! httpFiles.contains("http") ) return drugItem;
        Document htmlPage = Jsoup.connect( httpFiles ).get();
        
        Element table = htmlPage.select("tbody").first();
        Elements links = table.select("a");
        for (Element el : links) {
        	String link = el.attr("abs:href");
        	Map<String, List<String>> params = getQueryParams( link );
        	if ( link.contains("sukl.cz") ) {
	        	String type = params.get("type").get(0);	
	        	String extension = getFileExtension( params.get("file").get(0) );
	        	if ( type.equals("pil") ) {
	        		drugItem.setPilFile( getDrugFile(link, drugItem.getNameShort(), "PIL."+extension) );
	        	} else if ( type.equals("spc") ) {
	        		drugItem.setSpcFile( getDrugFile(link, drugItem.getNameShort(), "SPC."+extension) );
	        	}
        	} else if ( link.contains("europa.eu") ) {
        		drugItem.setPilFile( getDrugFile(link, drugItem.getNameShort(), "EMA.pdf") );
        		drugItem.setSpcFile( getDrugFile(link, drugItem.getNameShort(), "EMA.pdf") );
        		return drugItem;
        	}
		}
        return drugItem;
	}
	
	private DrugFile getDrugFile( String link, String drugNameShort, String fileSuffix ) {
		DrugFile drugFile = new DrugFile();
		
		if ( mapLinkFile.containsKey(link) ) return mapLinkFile.get(link);
		
		String fileName = downloadToTemp( link, fileSuffix );
		if ( fileName == null ) return null;
		File file = new File(fileName);
		drugFile = createDrugFile( file, drugNameShort );
		
		mapLinkFile.put( link, drugFile );
		
		return drugFile;
	}
	
	private String downloadToTemp(String downloadURL, String name) {
		try {
			System.out.println(" - Stahuji: " + downloadURL);
			String tmpFilePath = this.tmpDir + File.separator + name;
			File temp = new File(tmpFilePath);

			URL website = new URL(downloadURL);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());

			FileOutputStream fos = new FileOutputStream( temp.getAbsolutePath() );
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return temp.getAbsolutePath();

		} catch (IOException e) {
			System.out.println( " - [ERROR] " + downloadURL + " se nepodarilo stahnout");
			return null;
		}
	}
	
	private String normalizeName( String name ) {
		String result = Normalizer.normalize(name, Normalizer.Form.NFD);
		result = result.replaceAll("[^A-Za-z0-9 ]", "");
		result = result.replaceAll("  ", " ");
		result = result.replace(' ', '-');
		return result;
	}
	
	private String getFileExtension(String fileName) {
	    try {
	        return fileName.substring(fileName.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
	
	private boolean copyFileToSaveDir( DrugFile drugFile, File tmpFile, String drugNameShort ) {
		String saveFilePath = this.savePath + File.separator + drugNameShort + "_" + drugFile.getFileMD5() + "_" + tmpFile.getName();
		File saveFile = new File( saveFilePath );
		try {
			copyFileUsingStream( tmpFile, saveFile );
			drugFile.setFilePath( saveFile.getName() );
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	
	private DrugFile createDrugFile(File file, String drugNameShort) {
		DrugFile newFile = createNewDrugFile( file );
		DrugFile oldFile = ServiceLocator.getInstance().getDrugDao().getLastFile( newFile );
		if ( newFile.equals(oldFile) ) {
			return oldFile; 
		} else {
			copyFileToSaveDir(newFile, file, drugNameShort);
			return newFile;
		}
	}
	
	private DrugFile createNewDrugFile(File file) {
		DrugFile newFile = new DrugFile();
		newFile.extractFromFile( file );
		return newFile;
	}
	
	public Map<String, List<String>> getQueryParams(String url) {
	    try {
	        Map<String, List<String>> params = new HashMap<String, List<String>>();
	        String[] urlParts = url.split("\\?");
	        if (urlParts.length > 1) {
	            String query = urlParts[1];
	            for (String param : query.split("&")) {
	                String[] pair = param.split("=");
	                String key = URLDecoder.decode(pair[0], "UTF-8");
	                String value = "";
	                if (pair.length > 1) {
	                    value = URLDecoder.decode(pair[1], "UTF-8");
	                }

	                List<String> values = params.get(key);
	                if (values == null) {
	                    values = new ArrayList<String>();
	                    params.put(key, values);
	                }
	                values.add(value);
	            }
	        }

	        return params;
	    } catch (UnsupportedEncodingException ex) {
	        throw new AssertionError(ex);
	    }
	}

}
