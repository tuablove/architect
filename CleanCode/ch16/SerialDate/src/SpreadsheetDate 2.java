/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * --------------------
 * SpreadsheetDate.java
 * --------------------
 * (C) Copyright 2000-2006, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SpreadsheetDate.java,v 1.10 2006/08/29 13:59:30 mungady Exp $
 *
 * Changes
 * -------
 * 11-Oct-2001 : Version 1 (DG);
 * 05-Nov-2001 : Added getDescription() and setDescription() methods (DG);
 * 12-Nov-2001 : Changed name from ExcelDate.java to SpreadsheetDate.java (DG);
 *               Fixed a bug in calculating day, month and year from serial 
 *               number (DG);
 * 24-Jan-2002 : Fixed a bug in calculating the serial number from the day, 
 *               month and year.  Thanks to Trevor Hills for the report (DG);
 * 29-May-2002 : Added equals(Object) method (SourceForge ID 558850) (DG);
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Implemented Serializable (DG);
 * 04-Sep-2003 : Completed isInRange() methods (DG);
 * 05-Sep-2003 : Implemented Comparable (DG);
 * 21-Oct-2003 : Added hashCode() method (DG);
 * 29-Aug-2006 : Removed redundant description attribute (DG);
 *
 */

package org.jfree.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents a date using an integer, in a similar fashion to the
 * implementation in Microsoft Excel.  The range of dates supported is
 * 1-Jan-1900 to 31-Dec-9999.
 * <P>
 * Be aware that there is a deliberate bug in Excel that recognises the year
 * 1900 as a leap year when in fact it is not a leap year. You can find more
 * information on the Microsoft website in article Q181370:
 * <P>
 * http://support.microsoft.com/support/kb/articles/Q181/3/70.asp
 * <P>
 * Excel uses the convention that 1-Jan-1900 = 1.  This class uses the
 * convention 1-Jan-1900 = 2.
 * The result is that the day number in this class will be different to the
 * Excel figure for January and February 1900...but then Excel adds in an extra
 * day (29-Feb-1900 which does not actually exist!) and from that point forward
 * the day numbers will match.
 *
 * @author David Gilbert
 */
public class SpreadsheetDate extends DayDate {

//    /** For serialization. */
//    private static final long serialVersionUID = -2039586705374454461L;
    
    /** The serial number for 1 January 1900. */
    public static final int EARLIEST_DATE_ORDINAL = 2;
    
    /** The serial number for 31 December 9999. */
    public static final int LATEST_DATE_ORDINAL = 2958465;
    
    /** The lowest year value supported by this date format. */
    public static final int MINIMUM_YEAR_SUPPORTED = 1900;

    /** The highest year value supported by this date format. */
    public static final int MAXIMUM_YEAR_SUPPORTED = 9999;
    
    /** The number of days in a year up to the end of the preceding month. */
    private static final int[] AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH =
        {0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};
    
    /** 
     * The number of days in a leap year up to the end of the preceding month. 
     */
    private static final int[] 
        LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH =
            {0, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366};  
    
    /** 
     * The day number (1-Jan-1900 = 2, 2-Jan-1900 = 3, ..., 31-Dec-9999 = 
     * 2958465). 
     */
    private int ordinalDay;

    /** The day of the month (1 to 28, 29, 30 or 31 depending on the month). */
    private int day;

    /** The month of the year (1 to 12). */
    private Month month;

    /** The year (1900 to 9999). */
    private int year;

    /**
     * Creates a new date instance.
     *
     * @param day  the day (in the range 1 to 28/29/30/31).
     * @param month  the month (in the range 1 to 12).
     * @param year  the year (in the range 1900 to 9999).
     */
    public SpreadsheetDate(int day, Month month, int year) {
    	if(year < MINIMUM_YEAR_SUPPORTED || year > MAXIMUM_YEAR_SUPPORTED){
    		throw new IllegalArgumentException("The 'year' argument must be in range " +
    				MINIMUM_YEAR_SUPPORTED + " to " + MAXIMUM_YEAR_SUPPORTED + ".");
    	}
    	if(day < 1 || day > DateUtil.lastDayOfMonth(month, year)){
    		throw new IllegalArgumentException("Invalid 'day' argument.");
    	}
    	this.year = year;
    	this.month = month;
    	this.day = day;

        // the serial number needs to be synchronised with the day-month-year...
        this.ordinalDay = calcOrdinal(day, month, year);
    }
    
    public SpreadsheetDate(int day, int month, int year){
    	this(day, Month.fromInt(month), year);
    }

    /**
     * Standard constructor - creates a new date object representing the
     * specified day number (which should be in the range 2 to 2958465.
     *
     * @param ordinal  the serial number for the day (range: 2 to 2958465).
     */
    public SpreadsheetDate(int ordinal) {

        if ((ordinal < EARLIEST_DATE_ORDINAL) || (ordinal > LATEST_DATE_ORDINAL)) {
        	throw new IllegalArgumentException(
        		"SpreadsheetDate: Serial must be in range 2 to 2958465.");
        }
        ordinalDay = ordinal;
        calcDayMonthYear();
    }
    
    private void calcDayMonthYear(){
    	 // the day-month-year needs to be synchronised with the serial number...
        // get the year from the serial date
        int days = this.ordinalDay - EARLIEST_DATE_ORDINAL;
        // overestimated because we ignored leap days
        int overestimatedYear = MINIMUM_YEAR_SUPPORTED + (days / 365);       
        
        int nonleapdays = days - DateUtil.leapYearCount(overestimatedYear);
        // underestimated because we overestimated years
        int underestimatedYear = MINIMUM_YEAR_SUPPORTED + (nonleapdays / 365);
        
        year = huntForYearContaining(ordinalDay, underestimatedYear);
        int firstOrdinalOfYear = firstOrdinalOfYear(year);
        month = huntForMonthContaining(ordinalDay, firstOrdinalOfYear);
        day = ordinalDay - firstOrdinalOfYear - daysBeforeThisMonth(month.toInt());
    }
    
    
    
    private int huntForYearContaining(int anOrdinalDay, int startingYear){
    	int aYear = startingYear;
    	while(firstOrdinalOfYear(aYear) <= anOrdinalDay){
    		aYear++;
    	}
    	return aYear - 1;
    }
    
    private int firstOrdinalOfYear(int year){
    	return calcOrdinal(1, Month.JANUARY, year);
    }
    
    private Month huntForMonthContaining(int anOrdinal, int firstOrdinalOfYear){
    	int daysIntoThisYear = anOrdinal - firstOrdinalOfYear;
    	int aMonth = 1;
    	while(daysBeforeThisMonth(aMonth) < daysIntoThisYear){
    		aMonth++;
    	}
    	return Month.fromInt(aMonth - 1);
    }
    
    private int daysBeforeThisMonth(int aMonth){
    	if(DateUtil.isLeapYear(year)){
    		return LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[aMonth] - 1;
    	}else{
    		return AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[aMonth] - 1;
    	}
    }

    /**
     * Returns the serial number for the date, where 1 January 1900 = 2
     * (this corresponds, almost, to the numbering system used in Microsoft
     * Excel for Windows and Lotus 1-2-3).
     *
     * @return The serial number of this date.
     */
    public int getOrdinalDay() {
        return ordinalDay;
    }

    /**
     * Returns the year (assume a valid range of 1900 to 9999).
     *
     * @return The year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the month (January = 1, February = 2, March = 3).
     *
     * @return The month of the year.
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Returns the day of the month.
     *
     * @return The day of the month.
     */
    public int getDayOfMonth() {
        return day;
    }
    
    protected Day getDayOfWeekForOrdinalZero(){
    	return Day.SATURDAY;
    }

    /**
     * Tests the equality of this date with an arbitrary object.
     * <P>
     * This method will return true ONLY if the object is an instance of the
     * {@link SerialDate} base class, and it represents the same day as this
     * {@link SpreadsheetDate}.
     *
     * @param object  the object to compare (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(final Object object) {
    	if(!(object instanceof DayDate)){
    		return false;
    	}
    	DayDate date = (DayDate)object;
    	return date.getOrdinalDay() == getOrdinalDay();        
    }

    /**
     * Returns a hash code for this object instance.
     * 
     * @return A hash code.
     */
    public int hashCode() {
        return getOrdinalDay();
    }

    /**
     * Implements the method required by the Comparable interface.
     * 
     * @param other  the other object (usually another SerialDate).
     * 
     * @return A negative integer, zero, or a positive integer as this object 
     *         is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Object other) {
        return daysSince((DayDate) other);    
    }
    
    /**
     * Returns true if this DayDate represents the same date as the
     * specified SerialDate.
     *
     * @param other  the date being compared to.
     *
     * @return <code>true</code> if this SerialDate represents the same date as
     *         the specified SerialDate.
     */
    public boolean isOn(DayDate other) {
        return (this.ordinalDay == other.getOrdinalDay());
    }

    /**
     * Returns true if this SerialDate represents an earlier date compared to
     * the specified SerialDate.
     *
     * @param other  the date being compared to.
     *
     * @return <code>true</code> if this SerialDate represents an earlier date
     *         compared to the specified SerialDate.
     */
    public boolean isBefore(DayDate other) {
        return (this.ordinalDay < other.getOrdinalDay());
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other  the date being compared to.
     *
     * @return <code>true</code> if this SerialDate represents the same date
     *         as the specified SerialDate.
     */
    public boolean isOnOrBefore(DayDate other) {
        return (this.ordinalDay <= other.getOrdinalDay());
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other  the date being compared to.
     *
     * @return <code>true</code> if this SerialDate represents the same date
     *         as the specified SerialDate.
     */
    public boolean isAfter(DayDate other) {
        return (this.ordinalDay > other.getOrdinalDay());
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other  the date being compared to.
     *
     * @return <code>true</code> if this SerialDate represents the same date as
     *         the specified SerialDate.
     */
    public boolean isOnOrAfter(DayDate other) {
        return (this.ordinalDay >= other.getOrdinalDay());
    }

    /**
     * Calculate the serial number from the day, month and year.
     * <P>
     * 1-Jan-1900 = 2.
     *
     * @param d  the day.
     * @param m  the month.
     * @param y  the year.
     *
     * @return the serial number from the day, month and year.
     */
    private int calcOrdinal(int day, Month month, int year) {
    	int leapDaysForYear = DateUtil.leapYearCount(year - 1);
    	int daysUpToYear = (year - MINIMUM_YEAR_SUPPORTED) * 365 + leapDaysForYear;
    	int daysUpToMonth = AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[month.toInt()];
    	if(DateUtil.isLeapYear(year) && month.toInt() > Month.FEBRUARY.toInt()){
    		daysUpToMonth++;    		
    	}
    	int daysInMonth = day - 1;
    	return daysUpToYear + daysUpToMonth + daysInMonth + EARLIEST_DATE_ORDINAL;    	
    }
    
    public static DayDate createInstance(Date date){
    	final GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return new SpreadsheetDate(calendar.get(Calendar.DATE), Month
				.fromInt(calendar.get(Calendar.MONTH) + 1), calendar
				.get(Calendar.YEAR));
    }
}
