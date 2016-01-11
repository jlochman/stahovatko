package cz.jlochman.stahovatko.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FileHelper {
	
	private static Logger log = Logger.getLogger(FileHelper.class);
	
	public static String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static Document getHtmlPageFromURL(String url) {
		Document htmlPage;
		htmlPage = getHtmlWithTimeout(url, 5);
		if (htmlPage != null)
			return htmlPage;
		htmlPage = getHtmlWithTimeout(url, 30);
		if (htmlPage != null)
			return htmlPage;
		htmlPage = getHtmlWithTimeout(url, 60);
		if (htmlPage != null)
			return htmlPage;
		htmlPage = getHtmlWithTimeout(url, 120);
		return htmlPage;
	}

	private static Document getHtmlWithTimeout(String url, int timeoutSec) {
		try {
			return Jsoup.connect(url).timeout(timeoutSec * 1000).get();
		} catch (IOException e) {
			log.info(url + " not loaded with timeout = " + timeoutSec + " sec");
			return null;
		}
	}
	
	public static String downloadURLToFile(String downloadURL, String fileName) {
		try {
			log.info("Stahuji: " + downloadURL);
			File temp = new File(fileName);

			URL website = new URL(downloadURL);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());

			FileOutputStream fos = new FileOutputStream(temp.getAbsolutePath());
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return temp.getAbsolutePath();

		} catch (IOException e) {
			log.error(" [ERROR] " + downloadURL + " se nepodarilo stahnout");
			return null;
		}
	}

	public static void copyFileUsingStream(File source, File dest) throws IOException {
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

}
