package br.com.visent.matraf.action;

import java.util.ArrayList;

import org.hibernate.Session;

import br.com.visent.matraf.dao.hibernate.LinhaRelatorioDAO;
import br.com.visent.matraf.domain.LinhaRelatorio;
import br.com.visent.matraf.service.ajax.Pesquisa;
import br.com.visent.matraf.util.HibernateUtil;

/**
 * Visent Telecomunicações LTDA.
 * Projeto: Matraf
 * Arquivo criado em: 15/09/2005
 * 
 * @author osx
 * @version 1.0
 * 
 * Breve Descrição: classe action para controle das linhas do relatorio
 * 
 */
public class LinhaRelatorioAction {
	
	public LinhaRelatorioAction() {
		//TODO Auto-generated constructor stub
	}
	
	public ArrayList listar(Pesquisa pesquisa){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        
        LinhaRelatorioDAO dao = new LinhaRelatorioDAO(session);
        ArrayList lista = (ArrayList)dao.listar(pesquisa);
        
        //session.getTransaction().commit();
        session.close();
        return organizar(lista);
	}
	
	public ArrayList resumo(Pesquisa pesquisa){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        
        LinhaRelatorioDAO dao = new LinhaRelatorioDAO(session);
        ArrayList lista = (ArrayList)dao.resumo(pesquisa);
        
        //session.getTransaction().commit();
        session.close();
        return organizar(lista);
	}
	
	public ArrayList resumoGeral(Pesquisa pesquisa){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        
        LinhaRelatorioDAO dao = new LinhaRelatorioDAO(session);
        ArrayList lista = (ArrayList)dao.resumoGeral(pesquisa);
        
        //session.getTransaction().commit();
        session.close();
        return organizar2(lista);
	}
	
	public ArrayList resumoCentral(Pesquisa pesquisa){
		Session session = HibernateUtil.getSessionFactory().openSession();
		HibernateUtil.refresh(session);
        
        LinhaRelatorioDAO dao = new LinhaRelatorioDAO(session);
        ArrayList lista = (ArrayList)dao.resumoCentral(pesquisa);
        
        //session.getTransaction().commit();
        session.close();
        return organizar(lista);
	}
	
	private ArrayList organizar(ArrayList lista){
		
		ArrayList resultado = new ArrayList();
		
        for (int i = 0; i < lista.size(); i++) {
        	
        	Object[] ob = (Object[])lista.get(i);
        	LinhaRelatorio linhaRelatorio = new LinhaRelatorio();
        	
        	linhaRelatorio.setNumChamadas(((Double)ob[0]).floatValue());//Sum Cham
        	linhaRelatorio.setOkt(((Double)ob[1]).floatValue());//Sum OKT
        	linhaRelatorio.setTtc(((Double)ob[2]).floatValue());//Sum TTC
        	linhaRelatorio.setTto(((Double)ob[3]).floatValue());//Sum TTO
        	linhaRelatorio.setChamConv(((Double)ob[4]).floatValue());//Sum ChamConv
        	        	
        	//Montando Lista de Objects seguindo a ordem após o select puro
        	//da classe DAO
        	Object[] linha = new Object[ob.length - 4];
        	
        	linha[0]  = ob[5];//Periodo
        	linha[1]  = linhaRelatorio;//Contadores
        	linha[2]  = ob[6];//CentralRef
        	linha[3]  = ob[7];//Celula de Entrada
        	linha[4]  = ob[8];//Celula de Saída
        	linha[5]  = ob[9];//Central
        	linha[6]  = ob[10];//Operadora
        	linha[7]  = ob[11];//Grupo Celula
        	linha[8]  = ob[12];//Tipo de Assinante
        	linha[9]  = ob[13];//Relação Trafego
        	linha[10] = ob[14];//Rota de Saída
        	linha[11] = ob[15];//Rota de Entrada
        	
        	resultado.add(linha);
		}
        
        return resultado;
	}
	
	private ArrayList organizar2(ArrayList lista){
		
		ArrayList resultado = new ArrayList();
		
        for (int i = 0; i < lista.size(); i++) {
        	
        	Object[] ob = (Object[])lista.get(i);
        	LinhaRelatorio linhaRelatorio = new LinhaRelatorio();
        	
        	linhaRelatorio.setNumChamadas(((Double)ob[0]).floatValue());//Sum Cham
        	linhaRelatorio.setOkt(((Double)ob[1]).floatValue());//Sum OKT
        	linhaRelatorio.setTtc(((Double)ob[2]).floatValue());//Sum TTC
        	linhaRelatorio.setTto(((Double)ob[3]).floatValue());//Sum TTO
        	linhaRelatorio.setChamConv(((Double)ob[4]).floatValue());//Sum ChamConv
        	
        	linhaRelatorio.setReferencial(((Integer)ob[5]).intValue());//referencial
        	
        	//Montando Lista de Objects seguindo a ordem após o select puro
        	//da classe DAO
        	Object[] linha = new Object[ob.length - 4];
        	
        	linha[0]  = ob[6];//Periodo
        	linha[1]  = linhaRelatorio;//Contadores
        	linha[2]  = ob[7];//CentralRef
        	linha[3]  = ob[8];//Celula de Entrada
        	linha[4]  = ob[9];//Celula de Saída
        	linha[5]  = ob[10];//Central
        	linha[6]  = ob[11];//Operadora
        	linha[7]  = ob[12];//Grupo Celula
        	linha[8]  = ob[13];//Tipo de Assinante
        	linha[9]  = ob[14];//Relação Trafego
        	linha[10] = ob[15];//Rota de Saída
        	linha[11] = ob[16];//Rota de Entrada
        	
        	resultado.add(linha);
		}
        
        return resultado;
	}

}
