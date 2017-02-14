package br.com.visent.segurancaAcesso.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Classe para envio de emails
 * Visent Informática.
 * Projeto: PortalOsx
 * 
 * @author Carlos Feijão
 * @version 1.0 
 * @created 30/10/2007 17:09:07
 */
public class Email {

	private Properties properties;
	private String sender;

	public Email(String smtp, String sender) {
		this.sender = sender;
		this.properties = new Properties();
		this.properties.put("mail.smtp.host", smtp);
		this.properties.put("mail.smtp.auth", "false");
		this.properties.put("mail.debug", "false");   
		this.properties.put("mail.smtp.debug", "false");
	}

	/**
	 * Envia email para uma pessoa
	 * @param email
	 * @param assunto
	 * @param texto
	 * @return true se enviar com sucesso
	 */
	public boolean enviar(String email, String assunto, String texto) {
		Address[] from = null;
		try {
			from = new InternetAddress[]{new InternetAddress (sender)};
		} catch (AddressException e) {
			e.printStackTrace();
		}
		Session session = Session.getDefaultInstance(properties);
		MimeMessage msg = new MimeMessage(Session.getInstance(properties,null));
		try {
			msg.addFrom(from);
			msg.setRecipients(Message.RecipientType.TO, email);
			msg.setRecipients(Message.RecipientType.CC, "");
			msg.setRecipients(Message.RecipientType.BCC, "");
			msg.setSubject(assunto);
			msg.setText(texto);
			msg.setSentDate(new Date());
			msg.setReplyTo(from);
			msg.setSender(new InternetAddress (sender));			
			Transport.send(msg);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * Envia email para varias pessoa
	 * @param lista
	 * @param assunto
	 * @param texto
	 * @return true se enviar com sucesso
	 * @throws MessagingException 
	 */
	public boolean enviar(ArrayList lista, String assunto, String texto) throws MessagingException {
		
		if(lista.size() <= 0){
			return false;
		}
		
		Address[] to = new InternetAddress[lista.size()];
		for (int i = 0; i < to.length; i++) {
			try {
				to[i] = new InternetAddress(lista.get(i)+"");
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
		
		Address[] from = null;
		from = new InternetAddress [ ] { new InternetAddress ( sender ) }; 
		
		Session session = Session.getDefaultInstance(properties);
		MimeMessage msg = new MimeMessage(Session.getInstance(properties,null));
		msg.addFrom(from);
		msg.setRecipients(Message.RecipientType.TO, to);
		msg.setRecipients(Message.RecipientType.CC, "");
		msg.setRecipients(Message.RecipientType.BCC, "");
		msg.setSubject(assunto);
		msg.setText(texto);
		
		Transport.send(msg);
		return true;
		
	}

}
