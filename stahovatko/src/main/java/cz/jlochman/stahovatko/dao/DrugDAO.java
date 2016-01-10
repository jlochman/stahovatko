package cz.jlochman.stahovatko.dao;

import cz.jlochman.stahovatko.domain.DrugFile;
import cz.jlochman.stahovatko.domain.DrugItem;

public interface DrugDAO {
	public void persistDrugItem(DrugItem drugItem);
	public DrugFile getLastFile(DrugFile newFile);
}
