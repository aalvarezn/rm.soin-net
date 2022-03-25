package com.soin.sgrm.model.pos;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DOCUMENTO")
public class PDocument implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String name;
	
	@Column(name = "CODIGO")
	private String code;
	
	@Column(name = "GENERADOR_WORD")
	private String wordGenerator;
	
	@Column(name = "SUFIJO")
	private String sufix;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWordGenerator() {
		return wordGenerator;
	}

	public void setWordGenerator(String wordGenerator) {
		this.wordGenerator = wordGenerator;
	}

	public String getSufix() {
		return sufix;
	}

	public void setSufix(String sufix) {
		this.sufix = sufix;
	}
	
}
