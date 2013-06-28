package org.jfree.date;

/**
 * 日期范围区间.
 * @author Robert
 *
 */
public enum DateInterval {	
	OPEN {  //开区间
		public boolean isIn(int d, int left, int right){
			return d > left && d < right;
		}
	},
	CLOSED_LEFT {  //半开区间
		public boolean isIn(int d, int left, int right){
			return d >= left && d < right;
		}
	},
	CLOSED_RIGHT {
		public boolean isIn(int d, int left, int right){
			return d > left && d <= right;
		}
	},
	CLOSED {  //闭区间
		public boolean isIn(int d, int left, int right){
			return d >= left && d <= right;
		}
	};	
	
	public abstract boolean isIn(int d, int left, int right);
}
