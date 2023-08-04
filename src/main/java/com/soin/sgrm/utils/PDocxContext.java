package com.soin.sgrm.utils;

import java.util.ArrayList;

import java.util.List;

import com.soin.sgrm.model.pos.PButtonCommand;
import com.soin.sgrm.model.pos.PButtonFile;
import com.soin.sgrm.model.pos.PCrontab;
import com.soin.sgrm.model.pos.PReleaseSummary;
import com.soin.sgrm.service.SystemService;

public class PDocxContext {

	private List<PButtonCommand> buttons = new ArrayList<PButtonCommand>();
	private List<PButtonFile> buttonsFiles = new ArrayList<PButtonFile>();

	private List<PCrontab> crontabs = new ArrayList<PCrontab>();

	private boolean haveReports;

	public List<PButtonCommand> getButtons() {
		return buttons;
	}

	public void setButtons(List<PButtonCommand> buttons) {
		this.buttons = buttons;
	}

	public List<PButtonFile> getButtonsFiles() {
		return buttonsFiles;
	}

	public void setButtonsFiles(List<PButtonFile> buttonsFiles) {
		this.buttonsFiles = buttonsFiles;
	}

	public List<PCrontab> getCrontabs() {
		return crontabs;
	}

	public void setCrontabs(List<PCrontab> crontabs) {
		this.crontabs = crontabs;
	}

	public boolean isHaveReports() {
		return haveReports;
	}

	public void setHaveReports(boolean haveReports) {
		this.haveReports = haveReports;
	}

	public void preloadButonCommandDetails() {
		for (PButtonCommand button : this.buttons) {
			button.setDetailName("{{detailName_" + button.getId() + "}}");
			button.setDetailDescription("{{detailDescription_" + button.getId() + "}}");
			button.setDetailType("{{detailType_" + button.getId() + "}}");
			button.setDetailTypeText("{{detailTypeText_" + button.getId() + "}}");
			button.setDetailQuotationMarks("{{detailQuotationMarks_" + button.getId() + "}}");
			button.setDetailRequired("{{detailRequired_" + button.getId() + "}}");
		}
	}

	public void preloadButonFileDetails() {
		for (PButtonFile button : this.buttonsFiles) {
			button.setDetailFileName("{{detailFileName_" + button.getId() + "}}");
			button.setDetailFileDescription("{{detailFileDescription_" + button.getId() + "}}");
			button.setDetailFileType("{{detailFileType_" + button.getId() + "}}");
			button.setDetailFileTypeText("{{detailFileTypeText_" + button.getId() + "}}");
			button.setDetailFileQuotationMarks("{{detailFileQuotationMarks_" + button.getId() + "}}");
			button.setDetailFileRequired("{{detailFileRequired_" + button.getId() + "}}");
		}
	}

	public void loadButtonCommands(PReleaseSummary release, SystemService systemService) throws Exception {
		this.buttons.addAll(release.getButtons());
		preloadButonCommandDetails();

	}

	public void loadButtonFiles(PReleaseSummary release, SystemService systemService) throws Exception {
		this.buttonsFiles.addAll(release.getButtonsFile());
		preloadButonFileDetails();
	}

	public void loadCrontabs(PReleaseSummary release, SystemService systemService) {
		this.crontabs.addAll(release.getCrontabs());
	}

	public void definedReports(PReleaseSummary release) {
		this.haveReports = false;
		
		if (release.getReportHaveArt() || release.getReportfixedTelephony() || release.getReportHistoryTables()
				|| release.getReportNotHaveArt() || release.getReportMobileTelephony()
				|| release.getReportTemporaryTables()) {
			this.haveReports = true;
		}

		if (release.getBilledCalls() || release.getNotBilledCalls()) {
			this.haveReports = true;
		}
	}

}
