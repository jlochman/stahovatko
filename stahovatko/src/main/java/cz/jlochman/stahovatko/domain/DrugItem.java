package cz.jlochman.stahovatko.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "DRUG")
public class DrugItem {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
		
	@ManyToOne  
	private DrugCode code;

	public DrugCode getCode() {
		return code;
	}

	public void setCode(DrugCode code) {
		this.code = code;
	}
	
	
}
