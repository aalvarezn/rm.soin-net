package com.soin.sgrm.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_CRONTAB")
public class Crontab implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASES_CRONTAB_SQ")
	@SequenceGenerator(name = "RELEASES_CRONTAB_SQ", sequenceName = "RELEASES_CRONTAB_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RELEASE_ID", nullable = false)
	private ReleaseUser release;

	@Column(name = "USUARIO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String user;

	@Column(name = "ACTIVA")
	private boolean isActive;

	@Column(name = "COMANDO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String commandCron;

	@Column(name = "ENTRADA_COMANDO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String commandEntry;

	@Column(name = "DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String descriptionCron;

	@Column(name = "MINUTO")
	@NotEmpty(message = "Seleccione un minuto")
	@NotBlank(message = "Seleccione un minuto")
	private String minutes;

	@Column(name = "HORA")
	@NotEmpty(message = "Seleccione una hora")
	@NotBlank(message = "Seleccione una hora")
	private String hour;

	@Column(name = "DIA_MES")
	@NotEmpty(message = "Seleccione un día")
	@NotBlank(message = "Seleccione un día")
	private String days;

	@Column(name = "MES")
	@NotEmpty(message = "Seleccione un mes")
	@NotBlank(message = "Seleccione un mes")
	private String month;

	@Column(name = "DIA_SEMANA")
	@NotEmpty(message = "Seleccione un día")
	@NotBlank(message = "Seleccione un día")
	private String weekDays;

	@Valid
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BOTON_ID", nullable = true)
	private ButtonCommand button;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ReleaseUser getRelease() {
		return release;
	}

	public void setRelease(ReleaseUser release) {
		this.release = release;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean getActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getCommandCron() {
		return commandCron;
	}

	public void setCommandCron(String commandCron) {
		this.commandCron = commandCron;
	}

	public String getCommandEntry() {
		return commandEntry;
	}

	public void setCommandEntry(String commandEntry) {
		this.commandEntry = commandEntry;
	}

	public String getDescriptionCron() {
		return descriptionCron;
	}

	public void setDescriptionCron(String descriptionCron) {
		this.descriptionCron = descriptionCron;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
	}

	public ButtonCommand getButton() {
		return button;
	}

	public void setButton(String button) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			ButtonCommand jsonObj = mapper.readValue(button, ButtonCommand.class);
			this.button = jsonObj;
		} catch (Exception e) {
			this.button = null;
			e.printStackTrace();
		}
	}

	public void updateButton(ButtonCommand button) {
		this.button = button;
	}

}
