package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@Entity
@Table(name = "CORREO")
public class PEmail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 30, message = "Máximo 30 caracteres.")
	private String name;

	@Column(name = "PARA")
	private String to;

	@Column(name = "ASUNTO")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String subject;
	
	@Column(name = "COPIADO")
	private String cc;
	
	@Column(name = "CUERPO")
	private String body;
	
	@Column(name = "REINTENTO")
	private Integer retry;
	
	@Column(name = "ESTADO")
	private Integer state;
	
	@Column(name = "RELEASE_ID")
	private Integer releaseId;
	
	@Column(name = "NODO_ID")
	private Integer nodeId;
	
}
