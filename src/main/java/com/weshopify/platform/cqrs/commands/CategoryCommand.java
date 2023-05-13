package com.weshopify.platform.cqrs.commands;

import java.io.Serializable;

import org.axonframework.modelling.command.AggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6674937149851038361L;

	@AggregateIdentifier
	private String eventId;
	private int id;
	private String name;
	private String alias;
	private int pcategory;
	private boolean enabled;
	
}
