package cz.jlochman.stahovatko.domain;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
@Table(name = "DRUG_FILES")
public class DrugFile {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="PATH")
	private String filePath;
	
	@Column(name = "SIZE")
	private long fileSize;
	
	@Column(name = "MD5", length=32)
	private String fileMD5;
	
	@Column(name = "MOD_DATE")
	private Date modDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pilFile")
	private List<DrugItem> drugsPIL;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "spcFile")
	private List<DrugItem> drugsSPC;
		
	public DrugFile() {
	}
	
	public void extractFromFile( File file ) {
		this.fileSize = file.length();
		this.fileMD5 = getMD5( file );
	}
	
	private String getMD5( File file ) {
		FileInputStream fis;
		try {
			fis = new FileInputStream( file );
			String md5 = DigestUtils.md5Hex(fis);
			fis.close();
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString() {
		return filePath + " " + fileSize + " " + modDate.toString() + " " + fileMD5;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( ! (obj instanceof DrugFile) ) {
			return false;
		}		
		final DrugFile other = (DrugFile) obj;
		
		if ( this.fileSize != other.fileSize ) {
			return false;
		}
		if ( ! this.fileMD5.equals( other.fileMD5 ) ) {
			return false;
		}
		return true;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMD5() {
		return fileMD5;
	}

	public void setFileMD5(String fileMD5) {
		this.fileMD5 = fileMD5;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public long getId() {
		return id;
	}
	
	
}
