package com.soin.sgrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.soin.sgrm.dao.EmailTemplateDao;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.utils.Constant;

@Transactional("transactionManager")
@Service("EmailTemplateService")
public class EmailTemplateServiceImpl implements EmailTemplateService {

	private static final Pattern imgRegExp = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

	@Autowired
	EmailTemplateDao dao;

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender mailSender;

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
		try {
			Map<String, String> inlineImage = new HashMap<String, String>();
			String body = html;
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
			mimeMessage.setSender(new InternetAddress(env.getProperty("mail.user")));
			mimeMessage.setFrom(new InternetAddress(env.getProperty("mail.user")));
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
			for (String toUser : to.split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
			}
			if (!((cc != null) ? cc : "").trim().equals("")) {
				for (String ccUser : cc.split(",")) {
					mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
				}
			}

			mailSender.send(mimeMessage);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
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
		email = fillEmail(email, release);
		String body = email.getHtml();
		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(env.getProperty("mail.user")));
		mimeMessage.setFrom(new InternetAddress(env.getProperty("mail.user")));
		for (String toUser : email.getTo().split(",")) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
		}
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			for (String ccUser : email.getCc().split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			}
		}

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
		if (email.getHtml().contains("{{releaseNumber}}")) {
			email.setHtml(email.getHtml().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}
		if (email.getHtml().contains("{{functionalSolution}}")) {
			email.setHtml(email.getHtml().replace("{{functionalSolution}}",
					(release.getFunctionalSolution() != null ? release.getFunctionalSolution() : "")));
		}
		if (email.getHtml().contains("{{technicalSolution}}")) {
			email.setHtml(email.getHtml().replace("{{technicalSolution}}",
					(release.getTechnicalSolution() != null ? release.getTechnicalSolution() : "")));
		}
		if (email.getHtml().contains("{{notInstalling}}")) {
			email.setHtml(email.getHtml().replace("{{notInstalling}}",
					(release.getNotInstalling() != null ? release.getNotInstalling() : "")));
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
		/* ------ Subject ------ */
		if (email.getSubject().contains("{{releaseNumber}}")) {
			email.setSubject(email.getSubject().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
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

		String body = email.getHtml();
		body = body.replace("{{date}}", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
		body = body.replace("{{username}}", user.getUsername());
		body = body.replace("{{password}}", password);

		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(env.getProperty("mail.user")));
		mimeMessage.setFrom(new InternetAddress(env.getProperty("mail.user")));
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));

		mailSender.send(mimeMessage);

	}

}
