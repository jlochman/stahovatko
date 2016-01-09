package cz.jlochman.stahovatko.domain;

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

@Entity
@Table(name = "DRUG_FILES")
public class DrugFile {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="PATH")
	private String filePath;
	
	@Column(name = "SIZE")
	private int fileSize;
	
	@Column(name = "MD5", length=32)
	private String fileMD5;
	
	@Column(name = "MOD_DATE")
	private Date modDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pilFile")
	private List<DrugItem> drugsPIL;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "spcFile")
	private List<DrugItem> drugsSPC;
	
	public DrugFile( String filePath ) {
		this.filePath = filePath;
	}
	
	@Override
	public String toString() {
		return filePath + " " + fileSize + " " + modDate.toString() + " " + fileMD5;
	}
}
