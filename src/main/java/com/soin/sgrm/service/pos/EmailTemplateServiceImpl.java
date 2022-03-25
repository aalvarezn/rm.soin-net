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
import com.soin.sgrm.model.pos.PEmailTemplate;
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
