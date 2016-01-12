package cz.jlochman.stahovatko.comparators;

import java.util.Comparator;

import cz.jlochman.stahovatko.domain.DownDate;

public class DownDateComparator implements Comparator<DownDate> {

	public static enum Type {
		ID, DATE
	};

	private Type type;

	public DownDateComparator() {
		this.type = Type.DATE;
	}
	
	public DownDateComparator(Type type) {
		this.type = type;
	}

	@Override
	public int compare(DownDate o1, DownDate o2) {
		switch (type) {
		case ID:
			return o1.getDate().compareTo(o2.getDate());
		case DATE:
			return (int) (o1.getId() - o2.getId());
		}
		return 0;
	}

}
