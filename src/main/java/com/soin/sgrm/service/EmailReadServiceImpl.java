package com.soin.sgrm.service;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("EmailReadService")
@Transactional("transactionManager")
public class EmailReadServiceImpl implements EmailReadService {

	@Override
	public void emailRead() throws MessagingException  {
		java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "");
		Properties prop = new Properties();
		prop.put("mail.pop3.host", "pop.gmail.com");
		prop.put("mail.pop3.port", "995");
		prop.put("mail.pop3.starttls.enable", "true");
		Session emailSession = Session.getDefaultInstance(prop);
		Store store = emailSession.getStore("pop3s");
		store.connect("pop.gmail.com", "anthonyalvarez000@gmail.com", "bndrfrjbbszbywoi");
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);

		emailSession.setDebug(true);		
		Message [] mensajes = folder.getMessages();
		for (int i=0;i<mensajes.length;i++)
		{
		   System.out.println("From:"+mensajes[i].getFrom()[0].toString());
		   System.out.println("Subject:"+mensajes[i].getSubject());
		}
	}

}
