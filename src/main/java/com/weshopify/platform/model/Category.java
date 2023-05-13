package com.weshopify.platform.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "categories")
@Data
public class Category extends Auditor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6674937149851038361L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique = true,nullable = false, updatable = true)
	private String name;
	
	@Column(nullable = false, updatable = true)
	private String alias;
	
	private String imagePath;
	
	@Column(nullable = false, updatable = true)
	private boolean enabled;
	
	@ManyToOne
	private Category parent;
	
}
