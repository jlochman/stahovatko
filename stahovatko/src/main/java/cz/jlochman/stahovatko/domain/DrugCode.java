package cz.jlochman.stahovatko.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DRUG_CODE")
public class DrugCode {
	
	@Id
    @GeneratedValue
	@Column(name = "ID")
	private long id;
	
	@Column(name="CODE", nullable = false, length=8)
	private String code; 
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<DrugItem> drugs;
	
	public DrugCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
