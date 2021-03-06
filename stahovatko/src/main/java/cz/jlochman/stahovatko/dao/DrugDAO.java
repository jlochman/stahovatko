package cz.jlochman.stahovatko.dao;

import java.util.List;

import cz.jlochman.stahovatko.domain.DownDate;
import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.domain.DrugItem;

public interface DrugDAO {
	public void persistDrugItem(DrugItem drugItem);
	public DrugFile getLastFile(DrugFile newFile);
	
	public DownDate getLastDownDate();
	public DownDate getDownDateByID( long id );
	
	public boolean isPersisted(DrugItem drugItem);
	
	public List<DownDate> getAllDownDates();
	public List<DrugItem> getDrugsForDownDate( DownDate downDate );
}
