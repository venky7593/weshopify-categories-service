package com.weshopify.platform.events;

import java.io.Serializable;

import lombok.Data;

@Data
public class CategoryEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int id;
	
	private String catogoryId;
	
	private String name;
	private String alias;
	private int pcategory;
	private boolean enabled;
	

}
