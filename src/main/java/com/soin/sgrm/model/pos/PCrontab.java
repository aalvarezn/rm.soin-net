package com.soin.sgrm.model.pos;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.utils.Constant;


@Entity
@Table(name = "RELEASES_CRONTAB")
public class PCrontab implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RELEASE_ID\"", nullable = false)
	private PReleaseUser release;

	@Column(name = "USUARIO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String user;

	@Column(name = "ACTIVA")
	private Boolean isActive;

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


	
	@SuppressWarnings("deprecation")
	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"BOTON_ID\"", nullable = true)
	private PButtonCommand button;
    


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PReleaseUser getRelease() {
		return release;
	}

	public void setRelease(PReleaseUser release) {
		this.release = release;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
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

	public PButtonCommand getButton() {
		return button;
	}

	public void setButton(PButtonCommand button) {
		
		this.button=button;
	}

	public void updateButton(PButtonCommand button) {
		this.button = button;
	}

}
