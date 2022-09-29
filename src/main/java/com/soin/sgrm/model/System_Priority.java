package com.soin.sgrm.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

public class System_Priority {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRIORIDAD", nullable = true)
	private PriorityIncidence priority;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRIORIDAD", nullable = true)
	private System system;

	public PriorityIncidence getPriority() {
		return priority;
	}

	public void setPriority(PriorityIncidence priority) {
		this.priority = priority;
	}

	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	
	
}
