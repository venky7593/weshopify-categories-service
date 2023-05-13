package com.weshopify.platform.config;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.weshopify.platform.commands.CategoryCommand;
import com.weshopify.platform.events.CategoryEvent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Aggregate
@Slf4j
public class CategoryAggregate {

	private int id;
	
	@AggregateIdentifier
	private String catogoryId;
	
	private String name;
	private String alias;
	private int pcategory;
	private boolean enabled;
	
	
	@CommandHandler
	public CategoryAggregate(CategoryCommand command) {
		log.info("i am in Aggregate Command :\t"+command.toString());
		CategoryEvent event = new CategoryEvent();
		BeanUtils.copyProperties(command, event);
		AggregateLifecycle.apply(event);
	}
	
	@EventSourcingHandler
	public void onCategoryEvent(CategoryEvent event) {
		log.info("Category Event has been taken by the event handler :\t"+event.toString());
		this.id = event.getId();
		this.catogoryId = event.getCatogoryId();
		this.name = event.getName();
		this.alias = event.getAlias();
		this.pcategory = event.getPcategory();
		this.enabled = event.isEnabled();
	}
}
