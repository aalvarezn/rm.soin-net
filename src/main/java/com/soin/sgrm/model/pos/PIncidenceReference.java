package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "INCIDENCIA")
public class PIncidenceReference implements Serializable,Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incidencia_seq")
	@SequenceGenerator(name = "incidencia_seq", sequenceName = "incidencia_seq", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUM_TICKET")
	private String numTicket;
	
	
}
