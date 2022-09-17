package com.soin.sgrm.service;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("EmailReadService")
@Transactional("transactionManager")
public class EmailReadServiceImpl implements EmailReadService {

	@Override
	public void emailRead() throws MessagingException  , IOException  {
		java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "");
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "imaps");
		Session emailSession = Session.getDefaultInstance(prop);
		Store store = emailSession.getStore("imaps");
		store.connect("imap.gmail.com", "anthonyalvarez000@gmail.com", "bndrfrjbbszbywoi");
		Folder folder = store.getFolder("SOPORTE");
		folder.open(Folder.READ_ONLY);
			
		Message[] unreadMessages = folder.search(
				new FlagTerm(new Flags(Flags.Flag.SEEN), false));
		
		emailSession.setDebug(true);		
		//Message [] newMessage = folder.getMessages();
		while(unreadMessages.length>0) {
		for (int i=0;i<unreadMessages.length;i++)
		{
			Message newMessage=unreadMessages[i];
		   System.out.println("From:"+newMessage.getFrom()[0].toString());
		   System.out.println("Subject:"+newMessage.getSubject());
		   System.out.println("Fecha enviado:"+newMessage.getSentDate());
		  
		   try {
			   Multipart mp= (Multipart)newMessage.getContent();
			   
			   // Extraemos cada una de las partes.
			   for (int j=0;j<mp.getCount();j++)
			   {
			      Part unaParte = mp.getBodyPart(j);
			      System.out.println("Contenido:"+unaParte.getContent().toString());
			      if (unaParte.isMimeType("multipart/*")) {
			    	  Multipart mp2= (Multipart)unaParte.getContent();
			    	  for (int x=0;x<mp2.getCount();x++) {
			    		  Part otraParte = mp2.getBodyPart(x);
					      System.out.println("Contenido:"+otraParte.getContent().toString());
			    	  }
			      }
			   }
			   if (mp.getCount() > 1) {
				   System.out.println(mp.getCount());
			   }
			   System.out.println("Contenido:"+newMessage.getContent().toString());
	        } catch (Exception ex) {
	            System.out.println(ex);
	        }
		 
		   
		  // newMessage.setFlag(Flags.Flag.SEEN, true);

		}
		
		folder.close(true);
		folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);
		unreadMessages = folder.search(
				new FlagTerm(new Flags(Flags.Flag.SEEN), false));
		
		emailSession.setDebug(true);		
	}
	}

}
