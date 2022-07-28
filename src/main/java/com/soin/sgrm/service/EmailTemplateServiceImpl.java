package com.soin.sgrm.service;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;

import com.soin.sgrm.dao.EmailTemplateDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.EnviromentConfig;

@Transactional("transactionManager")
@Service("EmailTemplateService")
public class EmailTemplateServiceImpl implements EmailTemplateService {

	private static final Pattern imgRegExp = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

	@Autowired
	EmailTemplateDao dao;

	@Autowired
	private Environment env;
	
	@Autowired
	RequestRM_P1_R4Service requestServiceR4;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	SigesService sigeService;

	EnviromentConfig envConfig = new EnviromentConfig();

	@Override
	public List<EmailTemplate> listAll() {
		return dao.listAll();
	}

	@Override
	public EmailTemplate findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void sendMail(String to, String cc, String subject, String html) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		try {
			Map<String, String> inlineImage = new HashMap<String, String>();
			String body = html;
			body = Constant.getCharacterEmail(body);
			final Matcher matcher = imgRegExp.matcher(body);
			int i = 0;
			while (matcher.find()) {
				String src = matcher.group();
				if (body.indexOf(src) != -1) {
					String srcToken = "src=\"";
					int x = src.indexOf(srcToken);
					int y = src.indexOf("\"", x + srcToken.length());
					String srcText = src.substring(x + srcToken.length(), y);
					String cid = "image" + i;
					String newSrc = src.replace(srcText, "cid:" + cid);
					inlineImage.put(cid, srcText.split(",")[1]);
					body = body.replace(src, newSrc);
					i++;
				}
			}
			mimeMessage.setSubject(subject);
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			BodyPart bp = new MimeBodyPart();
			bp.setContent(body, "text/html");
			MimeMultipart mmp = new MimeMultipart();
			mmp.addBodyPart(bp);
			Iterator<Entry<String, String>> it = inlineImage.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pairs = it.next();
				PreencodedMimeBodyPart pmp = new PreencodedMimeBodyPart("base64");
				pmp.setHeader("Content-ID", "<" + pairs.getKey() + ">");
				pmp.setDisposition(MimeBodyPart.INLINE);
				pmp.setContent(pairs.getValue(), "image/png");
				mmp.addBodyPart(pmp);
			}
			mimeMessage.setContent(mmp, "UTF-8");
			for (String toUser : to.split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
			}
			if (!((cc != null) ? cc : "").trim().equals("")) {
				for (String ccUser : cc.split(",")) {
					mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
				}
			}

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			Sentry.capture(e, "email");
			throw e;
		}
	}

	@Override
	public void updateEmail(EmailTemplate email) {
		dao.updateEmail(email);
	}

	@Override
	public void saveEmail(EmailTemplate email) {
		dao.saveEmail(email);
	}

	@Override
	public void sendMail(Release release, EmailTemplate email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		email = fillEmail(email, release);
		String body = email.getHtml();
		body = Constant.getCharacterEmail(body);
		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
		mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
		for (String toUser : email.getTo().split(",")) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(release.getUser().getEmail()));
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			for (String ccUser : email.getCc().split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			}
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(release.getUser().getEmail()));

		mailSender.send(mimeMessage);
	}

	@SuppressWarnings("unused")
	private MimeMultipart MimeMultipart(String body) throws MessagingException {
		MimeMultipart mmp = new MimeMultipart();
		Map<String, String> inlineImage = new HashMap<String, String>();
		final Matcher matcher = imgRegExp.matcher(body);
		int i = 0;
		while (matcher.find()) {
			String src = matcher.group();
			if (body.indexOf(src) != -1) {
				String srcToken = "src=\"";
				int x = src.indexOf(srcToken);
				int y = src.indexOf("\"", x + srcToken.length());
				String srcText = src.substring(x + srcToken.length(), y);
				String cid = "image" + i;
				String newSrc = src.replace(srcText, "cid:" + cid);
				inlineImage.put(cid, srcText.split(",")[1]);
				body = body.replace(src, newSrc);
				i++;
			}
		}
		BodyPart bp = new MimeBodyPart();
		bp.setContent(body, "text/html");
		mmp.addBodyPart(bp);
		Iterator<Entry<String, String>> it = inlineImage.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			PreencodedMimeBodyPart pmp = new PreencodedMimeBodyPart("base64");
			pmp.setHeader("Content-ID", "<" + pairs.getKey() + ">");
			pmp.setDisposition(MimeBodyPart.INLINE);
			pmp.setContent(pairs.getValue(), "image/png");
			mmp.addBodyPart(pmp);
		}
		return mmp;
	}

	public EmailTemplate fillEmail(EmailTemplate email, Release release) {
		String temp = "";
		/* ------ body ------ */
		if (email.getHtml().contains("{{userName}}")) {
			email.setHtml(email.getHtml().replace("{{userName}}",
					(release.getUser().getFullName() != null ? release.getUser().getFullName() : "")));
		}

		if (email.getHtml().contains("{{releaseNumber}}")) {
			email.setHtml(email.getHtml().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}

		if (email.getHtml().contains("{{description}}")) {
			String description = release.getDescription() != null ? release.getDescription() : "";
			description = description.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{description}}", description));
		}

		if (email.getHtml().contains("{{observation}}")) {
			String observation = release.getObservations() != null ? release.getObservations() : "";
			observation = observation.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{observation}}", observation));
		}

		if (email.getHtml().contains("{{functionalSolution}}")) {
			String functionalSolution = release.getFunctionalSolution() != null ? release.getFunctionalSolution() : "";
			functionalSolution = functionalSolution.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{functionalSolution}}", functionalSolution));
		}

		if (email.getHtml().contains("{{minimalEvidence}}")) {
			String minimalEvidence = release.getMinimal_evidence() != null ? release.getMinimal_evidence() : "";
			minimalEvidence = minimalEvidence.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{minimalEvidence}}", minimalEvidence));
		}

		if (email.getHtml().contains("{{technicalSolution}}")) {
			String technicalSolution = release.getTechnicalSolution() != null ? release.getTechnicalSolution() : "";
			technicalSolution = technicalSolution.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{technicalSolution}}", technicalSolution));
		}

		if (email.getHtml().contains("{{ambient}}")) {
			temp = "";
			for (Ambient amb : release.getAmbients()) {
				temp += amb.getName() + "<br>";
			}
			email.setHtml(email.getHtml().replace("{{ambient}}", (temp.equals("") ? "Sin ambientes definidos" : temp)));
		}

		if (email.getHtml().contains("{{dependencies}}")) {
			temp = "";
			int i = 1;
			for (Dependency dep : release.getDependencies()) {
				temp += i + ": " + dep.getTo_release().getReleaseNumber() + "<br>";
				i++;
			}
			email.setHtml(email.getHtml().replace("{{dependencies}}",
					(temp.equals("") ? "Sin dependencias definidos" : temp)));
		}

		if (email.getHtml().contains("{{objects}}")) {
			temp = "<ul>";

			for (ReleaseObject obj : release.getObjects()) {
				temp += "<li> " + obj.getName() + "</li>";
			}
			temp += "</ul>";
			email.setHtml(email.getHtml().replace("{{objects}}", (temp.equals("") ? "Sin objetos definidos" : temp)));
		}

		if (email.getHtml().contains("{{version}}")) {
			email.setHtml(email.getHtml().replace("{{version}}",
					(release.getVersionNumber() != null ? release.getVersionNumber() : "")));
		}

		/* ------ Subject ------ */
		if (email.getSubject().contains("{{releaseNumber}}")) {
			email.setSubject(email.getSubject().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}
		if (email.getSubject().contains("{{version}}")) {
			email.setSubject(email.getSubject().replace("{{version}}",
					(release.getVersionNumber() != null ? release.getVersionNumber() : "")));
		}
		if (email.getSubject().contains("{{priority}}")) {
			email.setSubject(email.getSubject().replace("{{priority}}",
					(release.getPriority().getName() != null ? release.getPriority().getName() : "")));
		}

		if (email.getSubject().contains("{{impact}}")) {
			email.setSubject(email.getSubject().replace("{{impact}}",
					(release.getImpact().getName() != null ? release.getImpact().getName() : "")));
		}

		if (email.getSubject().contains("{{risk}}")) {
			email.setSubject(email.getSubject().replace("{{risk}}",
					(release.getRisk().getName() != null ? release.getRisk().getName() : "")));
		}

		return email;
	}

	@Override
	public boolean existEmailTemplate(String name) {
		return dao.existEmailTemplate(name);
	}

	@Override
	public boolean existEmailTemplate(String name, Integer id) {
		return dao.existEmailTemplate(name, id);
	}

	@Override
	public void deleteEmail(Integer id) {
		dao.deleteEmail(id);
	}

	@Override
	public void sendMail(UserInfo user, String password, EmailTemplate email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		try {
			Map<String, String> inlineImage = new HashMap<String, String>();
			String body = email.getHtml();
			body = body.replace("{{date}}", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			body = body.replace("{{username}}", user.getUsername());
			body = body.replace("{{password}}", password);
			body = Constant.getCharacterEmail(body);
			final Matcher matcher = imgRegExp.matcher(body);
			int i = 0;
			while (matcher.find()) {
				String src = matcher.group();
				if (body.indexOf(src) != -1) {
					String srcToken = "src=\"";
					int x = src.indexOf(srcToken);
					int y = src.indexOf("\"", x + srcToken.length());
					String srcText = src.substring(x + srcToken.length(), y);
					String cid = "image" + i;
					String newSrc = src.replace(srcText, "cid:" + cid);
					inlineImage.put(cid, srcText.split(",")[1]);
					body = body.replace(src, newSrc);
					i++;
				}
			}
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			BodyPart bp = new MimeBodyPart();
			bp.setContent(body, "text/html");
			MimeMultipart mmp = new MimeMultipart();
			mmp.addBodyPart(bp);
			Iterator<Entry<String, String>> it = inlineImage.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pairs = it.next();
				PreencodedMimeBodyPart pmp = new PreencodedMimeBodyPart("base64");
				pmp.setHeader("Content-ID", "<" + pairs.getKey() + ">");
				pmp.setDisposition(MimeBodyPart.INLINE);
				pmp.setContent(pairs.getValue(), "image/png");
				mmp.addBodyPart(pmp);
			}
			mimeMessage.setContent(mmp);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			Sentry.capture(e, "email");
			throw e;
		}
	}

	@Override
	public void sendMail(WFRelease releaseEmail, EmailTemplate email, String motive) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
			// ------------------Seccion del asunto del correo -------------------------- //
			// Se agrega el nombre del sistema
			if (email.getSubject().contains("{{systemName}}")) {
				email.setSubject(email.getSubject().replace("{{systemName}}",
						(releaseEmail.getSystem() != null ? releaseEmail.getSystem().getName() : "")));
			}
			// Se agrega el numero de release
			if (email.getSubject().contains("{{releaseNumber}}")) {
				email.setSubject(email.getSubject().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}
			// ------------------Seccion del cuerpo del correo -------------------------- //
			if (email.getHtml().contains("{{releaseNumber}}")) {
				email.setHtml(email.getHtml().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}

			if (email.getHtml().contains("{{releaseStatus}}")) {
				email.setHtml(email.getHtml().replace("{{releaseStatus}}",
						(releaseEmail.getStatus() != null ? releaseEmail.getStatus().getName() : "")));
			}
			if (email.getHtml().contains("{{userName}}")) {
				email.setHtml(email.getHtml().replace("{{userName}}",
						(releaseEmail.getUser().getFullName() != null ? releaseEmail.getUser().getFullName() : "")));
			}
			if (email.getHtml().contains("{{operator}}")) {
				email.setHtml(email.getHtml().replace("{{operator}}",
						(releaseEmail.getOperator() != null ? releaseEmail.getOperator() : "")));
			}

			if (email.getHtml().contains("{{updateAt}}")) {
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm a");
				String strDate = dateFormat.format(releaseEmail.getCreateDate());
				email.setHtml(email.getHtml().replace("{{updateAt}}", strDate));
			}

			if (email.getHtml().contains("{{motive}}")) {
				email.setHtml(email.getHtml().replace("{{motive}}", (motive != null ? motive : "")));
			}

			String body = email.getHtml();
			body = Constant.getCharacterEmail(body);
			MimeMultipart mmp = MimeMultipart(body);
			mimeMessage.setContent(mmp);
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			for (WFUser toUser : releaseEmail.getNode().getUsers()) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser.getEmail()));
			}
			// Se notifica el usuario que lo solicito
			mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(releaseEmail.getUser().getEmail()));

			mailSender.send(mimeMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendMailActor(WFRelease releaseEmail, EmailTemplate email) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
			// ------------------Seccion del asunto del correo -------------------------- //
			// Se agrega el nombre del sistema
			if (email.getSubject().contains("{{systemName}}")) {
				email.setSubject(email.getSubject().replace("{{systemName}}",
						(releaseEmail.getSystem() != null ? releaseEmail.getSystem().getName() : "")));
			}
			// Se agrega el numero de release
			if (email.getSubject().contains("{{releaseNumber}}")) {
				email.setSubject(email.getSubject().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}
			// ------------------Seccion del cuerpo del correo -------------------------- //
			if (email.getHtml().contains("{{releaseNumber}}")) {
				email.setHtml(email.getHtml().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}

			if (email.getHtml().contains("{{releaseStatus}}")) {
				email.setHtml(email.getHtml().replace("{{releaseStatus}}",
						(releaseEmail.getStatus() != null ? releaseEmail.getStatus().getName() : "")));
			}
			if (email.getHtml().contains("{{userName}}")) {
				email.setHtml(email.getHtml().replace("{{userName}}",
						(releaseEmail.getUser().getFullName() != null ? releaseEmail.getUser().getFullName() : "")));
			}
			String body = email.getHtml();
			body = Constant.getCharacterEmail(body);
			MimeMultipart mmp = MimeMultipart(body);
			mimeMessage.setContent(mmp);
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			for (WFUser toUser : releaseEmail.getNode().getActors()) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser.getEmail()));
			}
			mailSender.send(mimeMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void sendMailNotify(WFRelease releaseEmail, EmailTemplate email,String user) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
			// ------------------Seccion del asunto del correo -------------------------- //
			// Se agrega el nombre del sistema
			if (email.getSubject().contains("{{systemName}}")) {
				email.setSubject(email.getSubject().replace("{{systemName}}",
						(releaseEmail.getSystem() != null ? releaseEmail.getSystem().getName() : "")));
			}
			// Se agrega el numero de release
			if (email.getSubject().contains("{{releaseNumber}}")) {
				email.setSubject(email.getSubject().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}
			// ------------------Seccion del cuerpo del correo -------------------------- //
			if (email.getHtml().contains("{{releaseNumber}}")) {
				email.setHtml(email.getHtml().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}

			if (email.getHtml().contains("{{releaseStatus}}")) {
				email.setHtml(email.getHtml().replace("{{releaseStatus}}",
						(releaseEmail.getStatus() != null ? releaseEmail.getStatus().getName() : "")));
			}
			if (email.getHtml().contains("{{userName}}")) {
				email.setHtml(email.getHtml().replace("{{userName}}",
						(releaseEmail.getUser().getFullName() != null ? releaseEmail.getUser().getFullName() : "")));
			}
			
			if (email.getHtml().contains("{{operator}}")) {
				email.setHtml(email.getHtml().replace("{{operator}}",
						(user != null ? user : "")));
			}

			if (email.getHtml().contains("{{updateAt}}")) {
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm a");
				String strDate = dateFormat.format(releaseEmail.getCreateDate());
				email.setHtml(email.getHtml().replace("{{updateAt}}", strDate));
			}

			String temp;
			if (email.getHtml().contains("{{actors}}")) {
				temp = "<ul>";

				for (WFUser obj : releaseEmail.getNode().getActors()) {
					temp += "<li><b> " + obj.getFullName() + "</b></li>";
				}
				temp += "</ul>";
				email.setHtml(email.getHtml().replace("{{actors}}", (temp.equals("") ? "Sin actores definidos" : temp)));
			}
			
			String body = email.getHtml();
			body = Constant.getCharacterEmail(body);
			MimeMultipart mmp = MimeMultipart(body);
			mimeMessage.setContent(mmp);
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			for (WFUser toUser : releaseEmail.getNode().getUsers()) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser.getEmail()));
			}
			mailSender.send(mimeMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void sendMailRFC(RFC rfc, EmailTemplate email) throws Exception{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		email = fillEmail(email, rfc);
		String body = email.getHtml();
		body = Constant.getCharacterEmail(body);
		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
		mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
		for (String toUser : email.getTo().split(",")) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
		}
		String ccFinish="";
		String cc="";
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			cc=email.getCc();
			if(rfc.getSenders()==null) {
				
				ccFinish=email.getCc();
				String[] split3=ccFinish.split(",");
				boolean verify= ArrayUtils.contains(split3,rfc.getUser().getEmail());
				if(!verify) {
					ccFinish=cc+","+rfc.getUser().getEmail();
				}
			}else {
				if(rfc.getSenders().trim().equals("")) {
					ccFinish=email.getCc();
					String[] split3=ccFinish.split(",");
					boolean verify= ArrayUtils.contains(split3,rfc.getUser().getEmail());
					if(!verify) {
						ccFinish=cc+","+rfc.getUser().getEmail();
					}
				}else {
					
					String[] split=rfc.getSenders().split(",");
					String[] splitCC=cc.split(",");
					ccFinish=rfc.getSenders();
					for(int x=0; splitCC.length>x;x++) {
						boolean verify= ArrayUtils.contains(split,splitCC[x]);
						if(!verify) {
							ccFinish=ccFinish+","+splitCC[x];
						}
					}
					String[] split3=ccFinish.split(",");
					boolean verify= ArrayUtils.contains(split3,rfc.getUser().getEmail());
					if(!verify) {
						ccFinish=ccFinish+","+rfc.getUser().getEmail();
					}
				}
			
				
			}
		}else {
			
			if( rfc.getSenders()==null) {
					ccFinish=rfc.getUser().getEmail();
			}else {
				if(rfc.getSenders().trim().equals("")) {
					ccFinish=rfc.getUser().getEmail();
				}else {
				String[] split=rfc.getSenders().split(",");
				ccFinish=rfc.getSenders();
					boolean verify= ArrayUtils.contains(split,rfc.getUser().getEmail());
					if(!verify) {
						ccFinish=ccFinish+","+rfc.getUser().getEmail();
					}
				
				}
			}
		}
		
	
			for (String ccUser : ccFinish.split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			
		}
		

		mailSender.send(mimeMessage);
	}
	public EmailTemplate fillEmail(EmailTemplate email, RFC rfc) {
		String temp = "";
		/* ------ body ------ */
		if (email.getHtml().contains("{{userName}}")) {
			email.setHtml(email.getHtml().replace("{{userName}}",
					(rfc.getUser().getFullName() != null ? rfc.getUser().getFullName() : "")));
		}

		if (email.getHtml().contains("{{rfcNumber}}")) {
			email.setHtml(email.getHtml().replace("{{rfcNumber}}",
					(rfc.getNumRequest() != null ? rfc.getNumRequest() : "")));
		}

		if (email.getHtml().contains("{{detail}}")) {
			String detail = rfc.getDetail()!= null ? rfc.getDetail() : "";
			detail = detail.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{detail}}", detail));
		}

		if (email.getHtml().contains("{{reason}}")) {
			String reason = rfc.getReasonChange() != null ? rfc.getReasonChange() : "";
			reason = reason.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{reason}}", reason));
		}

		if (email.getHtml().contains("{{requestDateBegin}}")) {
			String requestDateBegin = rfc.getRequestDateBegin() != null ? rfc.getRequestDateBegin() : "";
			requestDateBegin = requestDateBegin.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{requestDateBegin}}", requestDateBegin));
		}

		if (email.getHtml().contains("{{requestDateFinish}}")) {
			String minimalEvidence = rfc.getRequestDateFinish() != null ? rfc.getRequestDateFinish() : "";
			minimalEvidence = minimalEvidence.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{requestDateFinish}}", minimalEvidence));
		}

		if (email.getHtml().contains("{{effect}}")) {
			String effect = rfc.getEffect() != null ? rfc.getEffect() : "";
			effect = effect.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{effect}}", effect));
		}

		if (email.getHtml().contains("{{releases}}")) {
			temp = "<table border=1>";
			temp+="<tr>"
					+ "<th>Numero release</th>"
					+ "<th>Detalle</th>"
					+ "</tr>";
			for (Release_RFC release : rfc.getReleases()) {
				temp+="<tr>";
				
				temp +="<td>"+ release.getReleaseNumber() + "</td>";
				temp +="<td>"+ release.getDescription() + "</td>";
				temp+="</tr>";
			}
			
			
			temp+="</table>";
			email.setHtml(email.getHtml().replace("{{releases}}", (temp.equals("") ? "Sin releases definidos" : temp)));
		}

		if (email.getHtml().contains("{{priority}}")) {
			email.setHtml(email.getHtml().replace("{{priority}}",
					(rfc.getPriority().getName() != null ? rfc.getPriority().getName() : "")));
		}

		if (email.getHtml().contains("{{systemsInvolved}}")) {
			temp = "";
			Siges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			List<String> systemsInvolved = new ArrayList<String>();

			systemsInvolved.add(codeSiges.getSystem().getName());
			String nameSystem = "";
			boolean validate = true;
			Set<Release_RFC> releases = rfc.getReleases();
			if (releases != null) {
				if (releases.size() != 0) {
					for (Release_RFC release : releases) {
						nameSystem = release.getSystem().getName();
						for (String system : systemsInvolved) {
							if (system.equals(nameSystem)) {
								validate = false;
							}
						}
						if (validate) {
							systemsInvolved.add(nameSystem);
						}
						validate = true;
					}
				}
			}
			for (String  systems : systemsInvolved) {
				temp += systems + "<br>";
			}
			
			email.setHtml(email.getHtml().replace("{{systemsInvolved}}", (temp.equals("") ? "Sin sistemas definidos" : temp)));
		}

		

		/* ------ Subject ------ */
		if (email.getSubject().contains("{{rfcNumber}}")) {
			email.setSubject(email.getSubject().replace("{{rfcNumber}}",
					(rfc.getNumRequest() != null ? rfc.getNumRequest() : "")));
		}
	
		if (email.getSubject().contains("{{priority}}")) {
			email.setSubject(email.getSubject().replace("{{priority}}",
					(rfc.getPriority().getName() != null ? rfc.getPriority().getName() : "")));
		}

		if (email.getSubject().contains("{{impact}}")) {
			email.setSubject(email.getSubject().replace("{{impact}}",
					(rfc.getImpact().getName() != null ? rfc.getImpact().getName() : "")));
		}

		if (email.getSubject().contains("{{typeChange}}")) {
			email.setSubject(email.getSubject().replace("{{typeChange}}",
					(rfc.getTypeChange().getName() != null ? rfc.getTypeChange().getName() : "")));
		}
		
		if (email.getHtml().contains("{{message}}")) {
			email.setHtml(email.getHtml().replace("{{message}}",
					(rfc.getMessage() != null ? rfc.getMessage() : "NA")));
		}

		if (email.getSubject().contains("{{systemMain}}")) {
			temp = "";
			Siges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			temp+=codeSiges.getSystem().getName();
			
			email.setSubject(email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
		}
		
		

		return email;
	}

	@Override
	public EmailTemplate getByKey(String key, String value) {
		return dao.getByKey(key, value);
	}

	@Override
	public EmailTemplate getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(EmailTemplate model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(EmailTemplate model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(EmailTemplate model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(EmailTemplate model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByKey(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(EmailTemplate model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<EmailTemplate> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<EmailTemplate> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<EmailTemplate> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailTemplate> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMailRequestR4(RequestBase requestEmail, EmailTemplate email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		email = fillEmail(email, requestEmail);
		String body = email.getHtml();
		body = Constant.getCharacterEmail(body);
		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
		mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
		for (String toUser : email.getTo().split(",")) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
		}
		String ccFinish="";
		String cc="";
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			cc=email.getCc();
			if(requestEmail.getSenders()==null) {
				
				ccFinish=email.getCc();
				String[] split3=ccFinish.split(",");
				boolean verify= ArrayUtils.contains(split3,requestEmail.getUser().getEmail());
				if(!verify) {
					ccFinish=cc+","+requestEmail.getUser().getEmail();
				}
			}else {
				if(requestEmail.getSenders().trim().equals("")) {
					ccFinish=email.getCc();
					String[] split3=ccFinish.split(",");
					boolean verify= ArrayUtils.contains(split3,requestEmail.getUser().getEmail());
					if(!verify) {
						ccFinish=cc+","+requestEmail.getUser().getEmail();
					}
				}else {
					
					String[] split=requestEmail.getSenders().split(",");
					String[] splitCC=cc.split(",");
					ccFinish=requestEmail.getSenders();
					for(int x=0; splitCC.length>x;x++) {
						boolean verify= ArrayUtils.contains(split,splitCC[x]);
						if(!verify) {
							ccFinish=ccFinish+","+splitCC[x];
						}
					}
					String[] split3=ccFinish.split(",");
					boolean verify= ArrayUtils.contains(split3,requestEmail.getUser().getEmail());
					if(!verify) {
						ccFinish=ccFinish+","+requestEmail.getUser().getEmail();
					}
				}
			
				
			}
		}else {
			
			if( requestEmail.getSenders()==null) {
					ccFinish=requestEmail.getUser().getEmail();
			}else {
				if(requestEmail.getSenders().trim().equals("")) {
					ccFinish=requestEmail.getUser().getEmail();
				}else {
				String[] split=requestEmail.getSenders().split(",");
				ccFinish=requestEmail.getSenders();
					boolean verify= ArrayUtils.contains(split,requestEmail.getUser().getEmail());
					if(!verify) {
						ccFinish=ccFinish+","+requestEmail.getUser().getEmail();
					}
				
				}
			}
		}
		
	
			for (String ccUser : ccFinish.split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			
		}
		

		mailSender.send(mimeMessage);
	}
	
	public EmailTemplate fillEmail(EmailTemplate email, RequestBase request) {
		String temp = "";
		/* ------ body ------ */
		if (email.getHtml().contains("{{userName}}")) {
			email.setHtml(email.getHtml().replace("{{userName}}",
					(request.getUser().getFullName() != null ? request.getUser().getFullName() : "")));
		}

		if (email.getHtml().contains("{{requestNumber}}")) {
			email.setHtml(email.getHtml().replace("{{requestNumber}}",
					(request.getNumRequest() != null ? request.getNumRequest() : "")));
		}

		if (email.getHtml().contains("{{projectCode}}")) {
			String projectCode = request.getCodeProyect()!= null ? request.getCodeProyect() : "";
			projectCode = projectCode.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{projectCode}}", projectCode));
		}


		if (email.getHtml().contains("{{requestDate}}")) {
			String requestDate = request.getRequestDate() != null ? request.getRequestDate().toString() : "";
			requestDate = requestDate.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{requestDate}}", requestDate));
		}
		if (email.getHtml().contains("{{message}}")) {
			email.setHtml(email.getHtml().replace("{{message}}",
					(request.getMessage() != null ? request.getMessage() : "NA")));
		}

		if (email.getHtml().contains("{{users}}")) {
			temp = "<table border=1>";
			temp+="<tr>"
					+ "<th>Nombre</th>"
					+ "<th>Correo</th>"
					+"<th>Tipo</th>"
					+"<th>Permisos</th>"
					+"<th>Ambiente</th>"
					+"<th>Espec</th>"
					+ "</tr>";
			List<RequestRM_P1_R4> users=requestServiceR4.listRequestRm4(request.getId());
			for (RequestRM_P1_R4 user : users) {
				temp+="<tr>";
				
				temp +="<td>"+ user.getName() + "</td>";
				temp +="<td>"+ user.getEmail() + "</td>";
				temp +="<td>"+ user.getType() + "</td>";
				temp +="<td>"+ user.getPermissions() + "</td>";
				temp +="<td>"+ user.getAmbient().getName() + "</td>";
				temp +="<td>"+ user.getEspec() + "</td>";
				
				temp+="</tr>";
			}
			
			
			temp+="</table>";
			email.setHtml(email.getHtml().replace("{{users}}", (temp.equals("") ? "Sin usuarios definidos" : temp)));
		}


	

		/* ------ Subject ------ */
		if (email.getSubject().contains("{{requestNumber}}")) {
			email.setSubject(email.getSubject().replace("{{requestNumber}}",
					(request.getNumRequest() != null ? request.getNumRequest() : "")));
		}
	

		if (email.getSubject().contains("{{systemMain}}")) {
			temp = "";
			Siges codeSiges = sigeService.findByKey("codeSiges", request.getCodeProyect());

			temp+=codeSiges.getSystem().getName();
			
			email.setSubject(email.getSubject().replace("{{systemMain}}", (temp.equals("") ? "Sin sistema" : temp)));
		}
		
		

		return email;
	}
}
