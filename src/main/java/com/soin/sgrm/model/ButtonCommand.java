package com.soin.sgrm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_BOTONCOMANDO")
public class ButtonCommand implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASES_BOTONCOMANDO_SQ")
	@SequenceGenerator(name = "RELEASES_BOTONCOMANDO_SQ", sequenceName = "RELEASES_BOTONCOMANDO_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RELEASE_ID", nullable = false)
	private ReleaseUser release;

	@Column(name = "TIENE_CRONTAB")
	private boolean haveCrontab;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String name;

	@Column(name = "DESCRIPCION")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String description;

	@Column(name = "COMANDO")
	@NotEmpty(message = Constant.EMPTY)
	@NotBlank(message = Constant.EMPTY)
	private String command;

	@Column(name = "EJECUTAR_DIRECTORIO")
	private boolean executeDirectory;

	@Column(name = "NOMBRE_DIRECTORIO")
	private String directoryName;

	@Column(name = "EJECUTAR_USUARIO")
	private boolean executeUser;

	@Column(name = "NOMBRE_USUARIO")
	private String userName;

	@Column(name = "USAR_ENTORNO_USUARIO")
	private boolean useUserEnvironment;

	@Column(name = "TIENE_SALIDA_HTML")
	private boolean haveHTML;

	@Column(name = "OCULTAR_AL_EJECUTAR")
	private boolean hideExecute;

	@Column(name = "DISPONIBILIDAD_USERMIN")
	private boolean userminAvailability;

	@Column(name = "LIMPIAR_VARIABLES_ENTORNO")
	private boolean clearVariables;

	@Column(name = "ESPERA_COMANDO")
	private boolean waitCommand;

	@Column(name = "TIEMPO_COMANDO")
	private String timeCommand;

	@Column(name = "PAGINA_PRINCIPAL")
	private boolean principalPage;

	@Column(name = "NOMBRE_PAGINA")
	private String pageName;

	@Fetch(value = FetchMode.SUBSELECT)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@OneToMany(mappedBy = "button", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<DetailButtonCommand> detailsButtonCommands = new ArrayList<DetailButtonCommand>();

	@Transient
	private String detailName;
	@Transient
	private String detailDescription;
	@Transient
	private String detailType;
	@Transient
	private String detailTypeText;
	@Transient
	private String detailQuotationMarks;
	@Transient
	private String detailRequired;

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

	public boolean getHaveCrontab() {
		return haveCrontab;
	}

	public void setHaveCrontab(boolean haveCrontab) {
		this.haveCrontab = haveCrontab;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public boolean getExecuteDirectory() {
		return executeDirectory;
	}

	public void setExecuteDirectory(boolean executeDirectory) {
		this.executeDirectory = executeDirectory;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public boolean getExecuteUser() {
		return executeUser;
	}

	public void setExecuteUser(boolean executeUser) {
		this.executeUser = executeUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean getUseUserEnvironment() {
		return useUserEnvironment;
	}

	public void setUseUserEnvironment(boolean useUserEnvironment) {
		this.useUserEnvironment = useUserEnvironment;
	}

	public boolean getHaveHTML() {
		return haveHTML;
	}

	public void setHaveHTML(boolean haveHTML) {
		this.haveHTML = haveHTML;
	}

	public boolean getHideExecute() {
		return hideExecute;
	}

	public void setHideExecute(boolean hideExecute) {
		this.hideExecute = hideExecute;
	}

	public boolean getUserminAvailability() {
		return userminAvailability;
	}

	public void setUserminAvailability(boolean userminAvailability) {
		this.userminAvailability = userminAvailability;
	}

	public boolean getClearVariables() {
		return clearVariables;
	}

	public void setClearVariables(boolean clearVariables) {
		this.clearVariables = clearVariables;
	}

	public boolean getWaitCommand() {
		return waitCommand;
	}

	public void setWaitCommand(boolean waitCommand) {
		this.waitCommand = waitCommand;
	}

	public String getTimeCommand() {
		return timeCommand;
	}

	public void setTimeCommand(String timeCommand) {
		this.timeCommand = timeCommand;
	}

	public boolean getPrincipalPage() {
		return principalPage;
	}

	public void setPrincipalPage(boolean principalPage) {
		this.principalPage = principalPage;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public List<DetailButtonCommand> getDetailsButtonCommands() {
		return detailsButtonCommands;
	}

	public void updateDetailsButtonCommands(List<DetailButtonCommand> detailsButtonCommands) {
		this.detailsButtonCommands = detailsButtonCommands;
	}

	public void setDetailsButtonCommands(String detailsButtonCommands) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<DetailButtonCommand> jsonList = Arrays
					.asList(mapper.readValue(detailsButtonCommands, DetailButtonCommand[].class));
			this.detailsButtonCommands = jsonList;
		} catch (Exception e) {
			this.detailsButtonCommands = null;
			e.printStackTrace();
		}
	}

	public boolean existDetail(int id) {
		for (DetailButtonCommand detail : getDetailsButtonCommands()) {
			if (detail.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	public String getDetailTypeText() {
		return detailTypeText;
	}

	public void setDetailTypeText(String detailTypeText) {
		this.detailTypeText = detailTypeText;
	}

	public String getDetailQuotationMarks() {
		return detailQuotationMarks;
	}

	public void setDetailQuotationMarks(String detailQuotationMarks) {
		this.detailQuotationMarks = detailQuotationMarks;
	}

	public String getDetailRequired() {
		return detailRequired;
	}

	public void setDetailRequired(String detailRequired) {
		this.detailRequired = detailRequired;
	}

}
