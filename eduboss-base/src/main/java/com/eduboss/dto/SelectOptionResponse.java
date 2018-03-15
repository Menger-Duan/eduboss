/**
 * 
 */
package com.eduboss.dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author chenguiban
 *
 */
public class SelectOptionResponse extends Response{
	
//	private String value;
	private Map<String, String> value;
	
	public SelectOptionResponse(List<NameValue> nameValues) {
		if (nameValues != null) {
			value = new LinkedHashMap<String, String>();
			for (NameValue nv : nameValues) {
				if (nv != null && StringUtils.isNotEmpty(nv.getName())) {
					value.put(nv.getValue(), nv.getName());
				}
			}
		}
		nameValues.remove(null);
	}
	
	public SelectOptionResponse() {
		value = new HashMap<String, String>();
	}
	
	public interface NameValue {
		public String getName();
		public String getValue();
	}

	public static NameValue buildNameValue(final String name, final String value) {
		return new NameValue(){
				public String getName() {return name;}
				public String getValue() {return value;}
			};
	}

	/**
	 * @return the value
	 */
	public Map<String, String> getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(Map<String, String> value) {
		this.value = value;
	}
}
