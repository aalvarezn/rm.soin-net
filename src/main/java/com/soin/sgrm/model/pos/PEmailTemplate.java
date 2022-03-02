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
@Table(name = "PLANTILLA_CORREO")
public class PEmailTemplate implements Serializable{

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	
}
