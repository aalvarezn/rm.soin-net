package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "CORREO_CORREO")
public class EmailTemplate implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CORREO_CORREO_SQ")
	@SequenceGenerator(name = "CORREO_CORREO_SQ", sequenceName = "CORREO_CORREO_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 30, message = "Máximo 30 caracteres.")
	private String name;

	@Column(name = "DE")
	private String from;

	@Column(name = "PARA")
//	@NotEmpty(message = Constant.EMPTY)
	private String to;

	@Column(name = "ASUNTO")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 100, message = "Máximo 100 caracteres.")
	private String subject;

	@Column(name = "HTML")
	private String html;

	@Column(name = "REINTENTOS")
	private Integer retry;

	@Column(name = "CC")
	private String cc;

	@Column(name = "CREATEDORMODIFY")
	private Timestamp createdormodify;

	@Column(name = "ESTADO")
	private Integer state;

	@Column(name = "USERMODIFY")
	private Integer usermodify;

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
		this.name = name.trim();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public Timestamp getCreatedormodify() {
		return createdormodify;
	}

	public void setCreatedormodify(Timestamp createdormodify) {
		this.createdormodify = createdormodify;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getUsermodify() {
		return usermodify;
	}

	public void setUsermodify(Integer usermodify) {
		this.usermodify = usermodify;
	}

}
