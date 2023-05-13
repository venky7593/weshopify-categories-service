package com.weshopify.platform.commands;

import java.io.Serializable;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class CategoryCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int id;
	
	@TargetAggregateIdentifier
	private String catogoryId;
	
	private String name;
	private String alias;
	private int pcategory;
	private boolean enabled;
	

}
