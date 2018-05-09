package com.airbnb.model.email;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
	
	public static void sendEmail(String receiver, String content) throws GeneralSecurityException {
System.out.println("sending");
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.mailtrap.io");
        props.put("mail.smtp.port", "2525");
        
       
        
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
            System.out.println("pratih go");
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
