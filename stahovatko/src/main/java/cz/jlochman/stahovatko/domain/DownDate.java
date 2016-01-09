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
@Table(name = "DOWN_DATES")
public class DownDate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="DATE")
	private Date date; 
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "downDate")
	private List<DrugItem> drugs;
	
	public DownDate( Date date ) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public long getId() {
		return id;
	}



	public List<DrugItem> getDrugs() {
		return drugs;
	}



	@Override
	public String toString() {
		return "[" + id + "]: " + date.toString();
	}
	
}
