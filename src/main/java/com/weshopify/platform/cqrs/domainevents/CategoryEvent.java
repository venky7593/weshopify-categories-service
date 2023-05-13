package com.weshopify.platform.cqrs.domainevents;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CategoryEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6674937149851038361L;

	private int id;
	private String eventId;
	private String name;
	private String alias;
	private int pcategory;
	private boolean enabled;
	
}
