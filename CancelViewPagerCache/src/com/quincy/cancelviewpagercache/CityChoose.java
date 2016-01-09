package com.quincy.cancelviewpagercache;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author quincy
 *
 */
public class CityChoose {

	public ArrayList<CityList> allList;

	public class CityList implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4274990233011479234L;
		public String unid;
		public String city;
		public String nodetype;
		public String parentid;
		public String postcode;
	}

}
