package com.soin.sgrm.service;

import java.io.IOException;
import java.sql.Timestamp;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.StatusIncidence;

@Service("EmailReadService")
@Transactional("transactionManager")
public class EmailReadServiceImpl implements EmailReadService {

	@Autowired
	IncidenceService incidenceService;
	
	@Autowired
	StatusIncidenceService statusIncidenceService;
	
	@Autowired 
	PriorityIncidenceService priorityIncidenceService;
	@SuppressWarnings("deprecation")
	@Override
	public void emailRead() throws MessagingException  , IOException  {
		java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "");
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "imaps");
		Session emailSession = Session.getDefaultInstance(prop);
		Store store = emailSession.getStore("imaps");
		store.connect("imap.gmail.com", "anthonyalvarez000@gmail.com", "bndrfrjbbszbywoi");
		Folder folder = store.getFolder("SOPORTE");
		folder.open(Folder.READ_WRITE);
			
		Message[] unreadMessages = folder.search(
				new FlagTerm(new Flags(Flags.Flag.SEEN), false));
		
		emailSession.setDebug(true);		
		//Message [] newMessage = folder.getMessages();
	
		for (int i=0;i<unreadMessages.length;i++)
		{
			Incidence incidence=new Incidence();
			Message newMessage=unreadMessages[i];
		   System.out.println("From:"+newMessage.getFrom()[0].toString());
		   System.out.println("Subject:"+newMessage.getSubject());
		   String createFor= newMessage.getFrom()[0].toString().replace("<", "");
		   createFor=createFor.replace(">", "");
		   incidence.setCreateFor(createFor);
		   incidence.setTitle(newMessage.getSubject());
		   if(newMessage.getSubject().toLowerCase().contains("soporte-critico")) {
			   PriorityIncidence priorityIncidence=priorityIncidenceService.findByKey("name","CRITICA");
			  // incidence.setPriority(priorityIncidence);
		   }else if(newMessage.getSubject().toLowerCase().contains("soporte-alto")) {
			   PriorityIncidence priorityIncidence=priorityIncidenceService.findByKey("name","ALTA");
			   //incidence.setPriority(priorityIncidence);
		   }else if(newMessage.getSubject().toLowerCase().contains("soporte-bajo")) {
			   PriorityIncidence priorityIncidence=priorityIncidenceService.findByKey("name","BAJA");
			   //incidence.setPriority(priorityIncidence);
		   }
		   System.out.println("Fecha enviado:"+newMessage.getSentDate());
		   Timestamp date=new Timestamp(newMessage.getSentDate().getTime());
		   incidence.setRequestDate(date);
		   incidence.setNumTicket("TicketGenerado");
		   System.out.println("Tipo de mensaje:"+newMessage.getContentType());
		   if(newMessage.isMimeType("TEXT/*")) {
			   incidence.setDetail(newMessage.getContent().toString());
			   
		   }else {
		  
		   try {
			   Multipart mp= (Multipart)newMessage.getContent();
			   
			   // Extraemos cada una de las partes.
			   for (int j=0;j<mp.getCount();j++)
			   {
			      Part unaParte = mp.getBodyPart(j);
			      System.out.println("Contenido:"+unaParte.getContent().toString());
			      System.out.println("Tipo de mensaje:"+unaParte.getContentType());
			      if (unaParte.isMimeType("multipart/*")) {
			    	  Multipart mp2= (Multipart)unaParte.getContent();
			    	  for (int x=0;x<mp2.getCount();x++) {
			    		 
			    		  Part otraParte = mp2.getBodyPart(x);
			    		  if(x==0) {
			    			  incidence.setDetail(otraParte.getContent().toString());
			    		  }
			    		  
					      System.out.println("Contenido:"+otraParte.getContent().toString());
			    	  }
			      }else if(unaParte.isMimeType("TEXT/*")) {
			    	  if(j==0) {
			    	  incidence.setDetail(unaParte.getContent().toString());
			    	  }
			      }else {
			    	  //SE guardan los documentos.
			      }
			   }
			   
			   if (mp.getCount() > 1) {
				   System.out.println(mp.getCount());
			   }
			   System.out.println("Contenido:"+newMessage.getContent().toString());
			  incidence.setOperator("Lector de correo");
			  incidence.setMotive("Se genera solicitud");
			  StatusIncidence status= statusIncidenceService.findByKey("code", "draft");
			  incidence.setStatus(status);
			  
			   incidenceService.save(incidence);
	        } catch (Exception ex) {
	            System.out.println(ex);
	        }
		 
		   }
		  newMessage.setFlag(Flags.Flag.SEEN, true);

		}
		
		folder.close(true);
		
	
	}

}
