/*
 * Created on 15/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Portal.Operacoes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Portal.Cluster.No;
import Portal.Cluster.NoUtil;
import Portal.Conexao.CnxServUtil;
import Portal.Configuracoes.DefsComum;
import Portal.Utils.TecnologiaCfgDef;

/**
 * @author osx
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpDocumento extends OperacaoAbs {
	private CnxServUtil m_ConexUtil;
	private No noLogin = null;
	
	public void iniciaOperacao(String p_Mensagem) {
		try {
			setOperacao("Documento - Arquivo de Regras");
				         doPost(getRequest(),getResponse());
		} catch (Exception Exc) {
			System.out.println("OpDocumento - iniciaOperacao(): " + Exc);
			Exc.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String URL = null;
		m_ConexUtil = NoUtil.getNo().getConexaoServUtil();
		noLogin = NoUtil.getNoCentral();
		String Documentos = m_ConexUtil.getDocumentos();
		Vector Tecnologia=null;
		String mensagem=null;
		String caminhoDoc=null;
		boolean apresenta3select = true;
		boolean outrosCampos = true;
		
		String docSelecionado  = request.getParameter("Documento");
		String tecSelecionada = request.getParameter("Tecnologia");
		String centSelecionada = request.getParameter("Central");
		String caminhoSelecionado = request.getParameter("carregaArquivo");
		
		List documento = new ArrayList();
		List documentoParam = new ArrayList();
		if(Documentos.length()>0){
			String docs[] = Documentos.split("\n");
			for(int i=0;i<docs.length;i++){
				String param[] = docs[i].split(";");
				documento.add(param[0]);
				documentoParam.add(param[1]);
			}
		}
		List tecnologia = new ArrayList();
		List centrais = new ArrayList();
		
		if(docSelecionado != null && docSelecionado.length()>0)
		{
			//esse teste, é pq caso o documento selecionado tiver esse indice '0', ele tem que mostrar as outras
			//sub-opções, caso seja '1' ele so mostra a opção documento mesmo.
			if(documentoParam.get(Integer.parseInt(docSelecionado)).equals("0"))
			{
				//se for iqual é pq não ouve mudança no 1º select, se for diferente
				//ouve mudança no 1º select e é preciso sumir com o 3ºselect e carregar so o 2º.
				if(!docSelecionado.equals(request.getParameter("primeiraEscolha")))
				{
					apresenta3select = false;
					tecSelecionada = null;
					centSelecionada = null;
				}
				//preenxendo a lista com as tecnologias que será apresentada no 2º select.
				Tecnologia = m_ConexUtil.getListaTecnologiasCfg();
				for(int i=0;i<Tecnologia.size();i++)
				{
					tecnologia.add(((TecnologiaCfgDef)Tecnologia.get(i)).getTecnologia());
				}
				//isso aki é p/quando mudar de tecnologia, para o campo central aparecer com a opção 'Escolha'.
				if(tecSelecionada != null && tecSelecionada.length()>0){
					if(!tecSelecionada.equals(request.getParameter("segundaEscolha")))
					{
						centSelecionada = null;
						caminhoSelecionado = null;
					}
				}
				//preenxendo as centrais, que são carregadas de acordo com a tecnologia escolhida.
				if(tecSelecionada != null && apresenta3select && tecSelecionada.length()>0)
				{
					String centraiss = m_ConexUtil.getBilhetadorTec(tecnologia.get(Integer.parseInt(tecSelecionada)).toString());
					String central[] = centraiss.split(";");
					for(int i=0;i<central.length;i++){
						centrais.add(central[i]);
					}
				}
				//quando a central for escolhida o link para o arquivo(documento) é carregado.
				if(centSelecionada != null && centSelecionada.length()>0 && centrais.size()>0)
				{
					int doc = Integer.parseInt(docSelecionado);
					int cent = Integer.parseInt(centSelecionada);
					caminhoDoc = m_ConexUtil.getCaminhoDocumento(documento.get(doc).toString(),centrais.get(cent).toString());
					if(caminhoDoc.length()==0){
						caminhoDoc = null;
						mensagem = "Arquivo não encontrado";
					}
					if(!centSelecionada.equals(request.getParameter("terceiraEscolha")))
					{
						caminhoSelecionado = null;
					}
					if(caminhoSelecionado != null){
						caminhoSelecionado = /*"http://"+noLogin.getHostName()+":"+noLogin.getPorta()+"/"+DefsComum.s_ContextoWEB+
													"/"+*/m_ConexUtil.getLinkDocumento(caminhoDoc);
					}
				}
			}else{
				outrosCampos = false;
				int doc = Integer.parseInt(docSelecionado);
				caminhoDoc = m_ConexUtil.getCaminhoDocumento(documento.get(doc).toString(),"");
				if(caminhoDoc.length()==0){
					caminhoDoc = null;
					mensagem = "Arquivo não encontrado";
				}
				//se apos ter clicado no link pra ver o arquivo, ele mudar a opção é apara tirar o onload do arquivo.
				if(docSelecionado.equals(request.getParameter("primeiraEscolha")))
				{
					if(caminhoSelecionado.equalsIgnoreCase("true")){
						caminhoSelecionado = m_ConexUtil.getLinkDocumento(caminhoDoc);
					}
				}
			}
		}else{
			outrosCampos = false;
			caminhoDoc = null;
			caminhoSelecionado = null;
			mensagem = null;
		}
		
		if(Documentos.length()==0){
			mensagem = "Não há arquivos para serem visualizados!";
		}
		
		try {
			request.setAttribute("Documento", documento);
			request.setAttribute("docSele",docSelecionado);
			if(docSelecionado != null && outrosCampos){
				request.setAttribute("Tecnologia", tecnologia);
				request.setAttribute("TecSele",tecSelecionada);
			}
			if(tecSelecionada != null && outrosCampos && centrais.size()>0){
				request.setAttribute("Centrais", centrais);
				request.setAttribute("CentSele",centSelecionada);
			}
			if(caminhoDoc != null ){
				request.setAttribute("caminhoDoc",caminhoDoc);
			}
			if(caminhoSelecionado != null && !caminhoSelecionado.equalsIgnoreCase("true")){
				request.setAttribute("caminhoSelecionado",caminhoSelecionado);
			}
			if(mensagem != null){
				request.setAttribute("mensagem",mensagem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		URL = "/templates/jsp/documento.jsp";
		request.getRequestDispatcher(URL).forward(request, response);
	}

	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {

		doPost(arg0, arg1);
	}

}