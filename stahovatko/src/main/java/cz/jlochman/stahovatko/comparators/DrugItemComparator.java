package cz.jlochman.stahovatko.comparators;

import java.util.Comparator;

import cz.jlochman.stahovatko.domain.DrugItem;

public class DrugItemComparator implements Comparator<DrugItem> {

	public static enum Type {
		Name, NameShort
	}

	private Type type;

	public DrugItemComparator() {
		this.type = Type.Name;
	}	
	
	public DrugItemComparator(Type type) {
		this.type = type;
	}

	@Override
	public int compare(DrugItem o1, DrugItem o2) {
		int sComp = 0;
		switch (type) {
		case Name:
			sComp = o1.getName().compareTo( o2.getName() );
		case NameShort:
			sComp = o1.getNameShort().compareTo( o2.getNameShort() );
		}
		
		if ( sComp != 0 ) return sComp;
		else return o1.compareTo(o2);
	}

}
