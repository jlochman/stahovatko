package cz.jlochman.stahovatko.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @GeneratedValue
	@Column(name = "ID")
	private long id;
		
	@ManyToOne
	@JoinColumn(name="CODE_ID")  
	private DrugCode code;

	public DrugCode getCode() {
		return code;
	}

	public void setCode(DrugCode code) {
		this.code = code;
	}
	
	
}
