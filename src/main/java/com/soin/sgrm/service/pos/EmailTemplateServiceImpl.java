package com.soin.sgrm.service.pos;



import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.EmailTemplateDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.EnviromentConfig;

@Service("emailTemplateService")
@Transactional("transactionManagerPos")
public class EmailTemplateServiceImpl implements EmailTemplateService {
	private static final Pattern imgRegExp = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
	
	@Autowired
	EmailTemplateDao dao;
	@Autowired
	private JavaMailSender mailSender;
	
	EnviromentConfig envConfig = new EnviromentConfig();
	
	@Override
	public PEmailTemplate findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PEmailTemplate findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PEmailTemplate> findAll() {
		return dao.findAll();
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
	public void sendMailRFC(PRFC rfc, PEmailTemplate email) throws Exception {
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
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(release.getUser().getEmail()));
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			for (String ccUser : email.getCc().split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			}
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(release.getUser().getEmail()));

		mailSender.send(mimeMessage);
	}
	
	public PEmailTemplate fillEmail(EmailTemplate email, PRFC rfc) {
		String temp = "";
		/* ------ body ------ */
		if (email.getHtml().contains("{{userName}}")) {
			email.setHtml(email.getHtml().replace("{{userName}}",
					(rfc.getUser().getName() != null ? rfc.getUser().getName() : "")));
		}

		if (email.getHtml().contains("{{rfcNumber}}")) {
			email.setHtml(email.getHtml().replace("{{rfcNumber}}",
					(rfc.getNumRequest() != null ? rfc.getNumRequest() : "")));
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
	public void save(PEmailTemplate model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PEmailTemplate model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PEmailTemplate model) {
		dao.update(model);
	}

}
