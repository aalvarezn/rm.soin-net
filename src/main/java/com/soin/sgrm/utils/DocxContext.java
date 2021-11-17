package com.soin.sgrm.utils;

import java.util.ArrayList;

import java.util.List;

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.ButtonFile;
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.service.SystemService;

public class DocxContext {

	private List<ButtonCommand> buttons = new ArrayList<ButtonCommand>();
	private List<ButtonFile> buttonsFiles = new ArrayList<ButtonFile>();

	private List<Crontab> crontabs = new ArrayList<Crontab>();

	private boolean haveReports;

	public List<ButtonCommand> getButtons() {
		return buttons;
	}

	public void setButtons(List<ButtonCommand> buttons) {
		this.buttons = buttons;
	}

	public List<ButtonFile> getButtonsFiles() {
		return buttonsFiles;
	}

	public void setButtonsFiles(List<ButtonFile> buttonsFiles) {
		this.buttonsFiles = buttonsFiles;
	}

	public List<Crontab> getCrontabs() {
		return crontabs;
	}

	public void setCrontabs(List<Crontab> crontabs) {
		this.crontabs = crontabs;
	}

	public boolean isHaveReports() {
		return haveReports;
	}

	public void setHaveReports(boolean haveReports) {
		this.haveReports = haveReports;
	}

	public void preloadButonCommandDetails() {
		for (ButtonCommand button : this.buttons) {
			button.setDetailName("{{detailName_" + button.getId() + "}}");
			button.setDetailDescription("{{detailDescription_" + button.getId() + "}}");
			button.setDetailType("{{detailType_" + button.getId() + "}}");
			button.setDetailTypeText("{{detailTypeText_" + button.getId() + "}}");
			button.setDetailQuotationMarks("{{detailQuotationMarks_" + button.getId() + "}}");
			button.setDetailRequired("{{detailRequired_" + button.getId() + "}}");
		}
	}

	public void preloadButonFileDetails() {
		for (ButtonFile button : this.buttonsFiles) {
			button.setDetailFileName("{{detailFileName_" + button.getId() + "}}");
			button.setDetailFileDescription("{{detailFileDescription_" + button.getId() + "}}");
			button.setDetailFileType("{{detailFileType_" + button.getId() + "}}");
			button.setDetailFileTypeText("{{detailFileTypeText_" + button.getId() + "}}");
			button.setDetailFileQuotationMarks("{{detailFileQuotationMarks_" + button.getId() + "}}");
			button.setDetailFileRequired("{{detailFileRequired_" + button.getId() + "}}");
		}
	}

	public void loadButtonCommands(ReleaseSummary release, SystemService systemService) throws Exception {
		this.buttons.addAll(release.getButtons());
		preloadButonCommandDetails();

	}

	public void loadButtonFiles(ReleaseSummary release, SystemService systemService) throws Exception {
		this.buttonsFiles.addAll(release.getButtonsFile());
		preloadButonFileDetails();
	}

	public void loadCrontabs(ReleaseSummary release, SystemService systemService) {
		this.crontabs.addAll(release.getCrontabs());
	}

	public void definedReports(ReleaseSummary release) {
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
