package cz.jlochman.stahovatko.domain;

import java.util.List;

public interface DrugDAO {
	public DrugItem getDrugByCode(String code);
	public void persistDrugItem(DrugItem drugItem);
	public List<DrugItem> getAllDrugItems();
}
