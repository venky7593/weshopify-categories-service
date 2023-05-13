package com.weshopify.platform.cqrs.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.weshopify.platform.cqrs.commands.CategoryCommand;
import com.weshopify.platform.cqrs.domainevents.CategoryEvent;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class CategoriesAggregate {

	@AggregateIdentifier
	private String eventId;
	private int id;
	private String name;
	private String alias;
	private int pcategory;
	private boolean enabled;

	@CommandHandler
	public CategoriesAggregate(CategoryCommand command) {
		log.info("step-2: command handler recived the command and Creating an Event");
		CategoryEvent event = CategoryEvent.builder().name(command.getName()).alias(command.getAlias())
				.id(command.getId()).enabled(command.isEnabled()).eventId(command.getEventId())
				.pcategory(command.getPcategory()).build();

		log.info("step-3: Publishing the Created Event to the Event Handlers");
		AggregateLifecycle.apply(event);
	}

	@EventSourcingHandler
	public void on(CategoryEvent event) {
		log.info("step-4: Event Handler recived the event");
		this.eventId = event.getEventId();
		this.id=event.getId();
		this.enabled = event.isEnabled();
		this.alias = event.getAlias();
		this.name = event.getName();
		this.pcategory = event.getPcategory();
	}
}
