package com.airbnb.model.email;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public final class MailSender {
	
	private static final String sender;
	private static final String password;
	
	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		Properties prop = new Properties();
		try (InputStream input = classLoader.getResourceAsStream("mail.properties");) {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sender = prop.getProperty("sender");
		password = prop.getProperty("password");
	}
	private MailSender() {
		
	}
	
	public static void sendEmail(String receiver, String content) {

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
          });


        try {
        	//Create message
            Message message = new MimeMessage(session);
            
            //Set From
            message.setFrom(new InternetAddress(sender));
            
            //Set recepient
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiver));
            
            //Set subject and body
            message.setSubject("Reservation");
            
            //Create a multipart object for the email
            Multipart multipart = new MimeMultipart();
            
            //Create a text body part and set the email content
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);
            
            //Add the text part to the email
            multipart.addBodyPart(messageBodyPart);
            
            // Send the complete message parts
            message.setContent(multipart);
            
            Transport.send(message);

		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
