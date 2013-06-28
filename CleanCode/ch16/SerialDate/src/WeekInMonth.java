package org.jfree.date;
/**
 * Select a week within a month.
 * @author Robert C. Martin from Clean Code
 *
 */
public enum WeekInMonth {
	FIRST(1), SECOND(2), THIRD(3), FOUTH(4), LAST(0);
	private final int index;
	
	WeekInMonth(int index){
		this.index = index;
	}
	
	public int toInt(){
		return index;
	}
}
