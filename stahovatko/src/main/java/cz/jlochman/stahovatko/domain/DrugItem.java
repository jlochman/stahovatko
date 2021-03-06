package cz.jlochman.stahovatko.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DRUGS")
public class DrugItem implements Comparable<DrugItem> {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "CODE", length = 7)
	private String code;
	
	@Column(name = "ATC", length = 8)
	private String atc;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "NAME_SUPP")
	private String nameSupp;
		
	@Column(name = "NAME_SHORT")
	private String nameShort;
	
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private DownDate downDate;

	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private DrugFile pilFile;
	
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private DrugFile spcFile;
	
	public DrugItem() {
	}
	
	@Override
	public String toString() {
		return "Code: " + code + " ATC: " + atc + " Name: " + name + " " + nameShort + " " + nameSupp;
	}
	
	public void setId( int id ) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAtc() {
		return atc;
	}

	public void setAtc(String atc) {
		this.atc = atc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameSupp() {
		return nameSupp;
	}

	public void setNameSupp(String name_supp) {
		this.nameSupp = name_supp;
	}

	public String getNameShort() {
		return nameShort;
	}

	public void setNameShort(String name_short) {
		this.nameShort = name_short;
	}

	public DownDate getDownDate() {
		return downDate;
	}

	public void setDownDate(DownDate downDate) {
		this.downDate = downDate;
	}

	public DrugFile getPilFile() {
		return pilFile;
	}

	public void setPilFile(DrugFile pilFile) {
		this.pilFile = pilFile;
	}

	public DrugFile getSpcFile() {
		return spcFile;
	}

	public void setSpcFile(DrugFile spcFile) {
		this.spcFile = spcFile;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( ! (obj instanceof DrugItem) ) {
			return false;
		}		
		final DrugItem other = (DrugItem) obj;
		
		if ( ! this.code.equals( other.code ) ) {
			return false;
		}
		if ( ! this.name.equals( other.name ) ) {
			return false;
		}
		if ( ! this.nameSupp.equals( other.nameSupp ) ) {
			return false;
		}
		if ( ! this.atc.equals( other.atc ) ) {
			return false;
		}
		if ( ! equalDrugFiles(this.getPilFile(), other.getPilFile()) ) {
			return false;
		}
		if ( ! equalDrugFiles(this.getSpcFile(), other.getSpcFile()) ) {
			return false;
		}
		return true;
	}
	
	private boolean equalDrugFiles( DrugFile df1, DrugFile df2 ) {
		if ( df1 == null && df2 == null ) return true;
		else if ( df1 == null && df2 != null ) return false;
		else if ( df1 != null && df2 == null ) return false;
		else return df1.getFileMD5().equals( df2.getFileMD5() );
	}

	@Override
	public int compareTo(DrugItem o) {
		return this.getCode().compareTo(o.getCode());
	}
	
}
