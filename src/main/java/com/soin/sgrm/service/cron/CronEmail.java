package com.soin.sgrm.service.cron;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.soin.sgrm.model.EmailIncidence;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.service.EmailIncidenceService;
import com.soin.sgrm.service.IncidenceService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class CronEmail {
	
	 /**
	   * Application name.
	   */
	  private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
	  /**
	   * Global instance of the JSON factory.
	   */
	  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	  /**
	   * Directory to store authorization tokens for this application.
	   */
	  private static final String TOKENS_DIRECTORY_PATH = "tokens";

	  /**
	   * Global instance of the scopes required by this quickstart.
	   * If modifying these scopes, delete your previously saved tokens/ folder.
	   */
	  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
	  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	  /**
	   * Creates an authorized Credential object.
	   *
	   * @param HTTP_TRANSPORT The network HTTP Transport.
	   * @return An authorized Credential object.
	   * @throws IOException If the credentials.json file cannot be found.
	   */
	  @SuppressWarnings("unused")
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
	      throws IOException {
	    // Load client secrets.
	    InputStream in = CronEmail.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
	    if (in == null) {
	      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
	    }
	    GoogleClientSecrets clientSecrets =
	        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

	    // Build flow and trigger user authorization request.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
	        .setAccessType("offline")
	        .build();
	    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
	    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	    //returns an authorized Credential object.
	    return credential;
	  }
	  
	@Autowired
	IncidenceService incidenceService;
	
	@Autowired
	EmailIncidenceService emailIncidenceService;
	
	//@Scheduled(cron="0 * * ? * *")
	public void readMails() throws MessagingException, IOException {
		java.security.Security.setProperty("jdk.tls.disabledAlgorithms", "");
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "imaps");
		Session emailSession = Session.getDefaultInstance(prop);
		Store store = emailSession.getStore("imaps");
		store.connect("imap.gmail.com", "anthonyalvarez000@gmail.com", "bndrfrjbbszbywoi");
		Folder folder = store.getFolder("TICKETS");
		folder.open(Folder.READ_WRITE);
			
		Message[] unreadMessages = folder.search(
				new FlagTerm(new Flags(Flags.Flag.SEEN), false));
		
		emailSession.setDebug(true);		
		//Message [] newMessage = folder.getMessages();
	
		for (int i=0;i<unreadMessages.length;i++)
		{
			
			Message newMessage=unreadMessages[i];
		   System.out.println("From:"+newMessage.getFrom()[0].toString());
		   System.out.println("Subject:"+newMessage.getSubject());
		   String chainSubject[]=newMessage.getSubject().split(":");
		   String numTicket=chainSubject[1].trim();
		   EmailIncidence emailIncidence=new EmailIncidence();
		   Incidence incidence=incidenceService.getIncidenceByName( numTicket);
		   String createFor= newMessage.getFrom()[0].toString().replace("<", "");
		   createFor=createFor.replace(">", "");
		   emailIncidence.setIncidence(incidence);
		   System.out.println("Fecha enviado:"+newMessage.getSentDate());
		   Timestamp date=new Timestamp(newMessage.getSentDate().getTime());
		   emailIncidence.setSendDate(date);
		   System.out.println("Tipo de mensaje:"+newMessage.getContentType());
		   if(newMessage.isMimeType("TEXT/*")) {
			   emailIncidence.setMessage(newMessage.getContent().toString());
			   
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
			    			  emailIncidence.setMessage(otraParte.getContent().toString());
			    		  }
			    		  
					      System.out.println("Contenido:"+otraParte.getContent().toString());
			    	  }
			      }else if(unaParte.isMimeType("TEXT/*")) {
			    	  if(j==0) {
			    		  emailIncidence.setMessage(unaParte.getContent().toString());
			    	  }
			      }else {
			    	  //SE guardan los documentos.
			      }
			   }
			   
			   if (mp.getCount() > 1) {
				   System.out.println(mp.getCount());
			   }
			   System.out.println("Contenido:"+newMessage.getContent().toString());
			  //incidence.setOperator("Lector de correo");
			  //incidence.setMotive("Se genera solicitud");
			 // StatusIncidence status= statusIncidenceService.findByKey("code", "draft");
			  //incidence.setStatus(status);
			  
			   //incidenceService.save(incidence);
	        } catch (Exception ex) {
	            System.out.println(ex);
	        }
		 
		   }
		  newMessage.setFlag(Flags.Flag.SEEN, true);
		  emailIncidenceService.save(emailIncidence);
		}
		
		folder.close(true);
		
	}
}
