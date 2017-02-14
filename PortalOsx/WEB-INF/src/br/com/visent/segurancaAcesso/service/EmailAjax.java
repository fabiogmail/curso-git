package br.com.visent.segurancaAcesso.service;

import java.util.ArrayList;

import javax.mail.MessagingException;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import br.com.visent.segurancaAcesso.action.ConfigAction;
import br.com.visent.segurancaAcesso.domain.Config;
import br.com.visent.segurancaAcesso.util.Email;

/**
 * Classe service para envio de emails
 * Visent Informática.
 * Projeto: PortalOsx
 * 
 * @author Carlos Feijão
 * @version 1.0 
 * @created 30/10/2007 17:11:46
 */
public class EmailAjax {
	
	private Email email;
	
	/**
	 * Busca as configurações do email
	 * @return true se houver configurações
	 */
	public boolean iniciar(){
		ConfigAction action = new ConfigAction();
		Config smtp = action.buscarPorNome("SMTP");
		Config remetente = action.buscarPorNome("EMAIL");
		if(smtp == null || remetente == null){
			return false;
		}else{
			if(smtp.getValor().equals("") || remetente.getValor().equals("")){
				return false;
			}
		}
		
		email = new Email(smtp.getValor(),remetente.getValor());
		return true;
	}
	
	/**
	 * Enviar Email
	 * @param emails
	 * @param assunto
	 * @param mensagem
	 * @return true se for enviado com sucesso
	 */
	public boolean enviar(String[] emails, String assunto, String mensagem){
		ArrayList lista = new ArrayList();
		for (int i = 0; i < emails.length; i++) {
			lista.add(emails[i]);
		}
		try {
			email.enviar(lista,assunto,mensagem);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo para enviar mensagem por SMS para uma lista de telefones
	 * @param telefones
	 * @param mensagem
	 * @return true se for enviado com sucesso
	 */
	public boolean enviarSMS(String telefones, String mensagem){
		No no = NoUtil.getNoCentral();
		boolean retorno = no.getConexaoServUtil().enviaMensagemSMS(telefones,mensagem);
		return retorno;
	}
 
}
