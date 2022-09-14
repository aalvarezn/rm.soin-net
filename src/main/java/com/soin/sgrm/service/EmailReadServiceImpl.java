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
		Properties prop = new Properties();

		// Deshabilitamos TLS
		prop.setProperty("mail.pop3.starttls.enable", "false");
		// Hay que usar SSL
		prop.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
		prop.setProperty("mail.pop3.socketFactory.fallback", "false");

		// Puerto 995 para conectarse.
		prop.setProperty("mail.pop3.port","995");
		prop.setProperty("mail.pop3.socketFactory.port", "995");
		Session sesion = Session.getInstance(prop);
		sesion.setDebug(true);
		
		Store store = sesion.getStore("pop3");
		store.connect("pop.gmail.com","anthonyalvarez000@gmail.com","Heindenbergtony270894!");
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		
		Message [] mensajes = folder.getMessages();
		for (int i=0;i<mensajes.length;i++)
		{
		   System.out.println("From:"+mensajes[i].getFrom()[0].toString());
		   System.out.println("Subject:"+mensajes[i].getSubject());
		}
	}

}
