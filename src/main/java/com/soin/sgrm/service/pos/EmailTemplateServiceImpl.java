package com.soin.sgrm.service.pos;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PSiges;
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

	@Autowired
	SigesService sigeService;

	
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
	@Override
	public void sendMailRFC(PRFC rfc, PEmailTemplate email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		email = fillEmail(email, rfc);
		String body = email.getBody();
		body = Constant.getCharacterEmail(body);
		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
		mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
		for (String toUser : email.getTo().split(",")) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(rfc.getUser().getEmail()));
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			for (String ccUser : email.getCc().split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			}
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(rfc.getUser().getEmail()));

		mailSender.send(mimeMessage);
	}
	
	public PEmailTemplate fillEmail(PEmailTemplate email, PRFC rfc) {
		String temp = "";
		/* ------ body ------ */
		if (email.getBody().contains("{{userName}}")) {
			email.setBody(email.getBody().replace("{{userName}}",
					(rfc.getUser().getName() != null ? rfc.getUser().getName() : "")));
		}

		if (email.getBody().contains("{{rfcNumber}}")) {
			email.setBody(email.getBody().replace("{{rfcNumber}}",
					(rfc.getNumRequest() != null ? rfc.getNumRequest() : "")));
		}

		if (email.getBody().contains("{{detail}}")) {
			String detail = rfc.getDetail()!= null ? rfc.getDetail() : "";
			detail = detail.replace("\n", "<br>");
			email.setBody(email.getBody().replace("{{detail}}", detail));
		}

		if (email.getBody().contains("{{reason}}")) {
			String reason = rfc.getReasonChange() != null ? rfc.getReasonChange() : "";
			reason = reason.replace("\n", "<br>");
			email.setBody(email.getBody().replace("{{reason}}", reason));
		}

		if (email.getBody().contains("{{requestDateBegin}}")) {
			String requestDateBegin = rfc.getRequestDateBegin() != null ? rfc.getRequestDateBegin() : "";
			requestDateBegin = requestDateBegin.replace("\n", "<br>");
			email.setBody(email.getBody().replace("{{requestDateBegin}}", requestDateBegin));
		}

		if (email.getBody().contains("{{requestDateFinish}}")) {
			String minimalEvidence = rfc.getRequestDateFinish() != null ? rfc.getRequestDateFinish() : "";
			minimalEvidence = minimalEvidence.replace("\n", "<br>");
			email.setBody(email.getBody().replace("{{requestDateFinish}}", minimalEvidence));
		}

		if (email.getBody().contains("{{effect}}")) {
			String effect = rfc.getEffect() != null ? rfc.getEffect() : "";
			effect = effect.replace("\n", "<br>");
			email.setBody(email.getBody().replace("{{effect}}", effect));
		}

		if (email.getBody().contains("{{releases}}")) {
			temp = "";
			for (PRelease release : rfc.getReleases()) {
				temp += release.getNumRelease() + "<br>";
			}
			email.setBody(email.getBody().replace("{{releases}}", (temp.equals("") ? "Sin releases definidos" : temp)));
		}



		if (email.getBody().contains("{{systemsInvolved}}")) {
			temp = "";
			PSiges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			List<String> systemsInvolved = new ArrayList<String>();

			systemsInvolved.add(codeSiges.getSystem().getName());
			String nameSystem = "";
			boolean validate = true;
			Set<PRelease> releases = rfc.getReleases();
			if (releases != null) {
				if (releases.size() != 0) {
					for (PRelease release : releases) {
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
			
			email.setBody(email.getBody().replace("{{systemsInvolved}}", (temp.equals("") ? "Sin sistemas definidos" : temp)));
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
