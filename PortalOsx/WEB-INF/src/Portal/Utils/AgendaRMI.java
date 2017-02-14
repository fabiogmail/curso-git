package Portal.Utils;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import agenda.facade.IManutencaoAgenda;
import agenda.facade.IManutencaoRelatorio;

public class AgendaRMI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	

	private static IManutencaoAgenda manutencaoAgenda = null;
	private static IManutencaoRelatorio manutencaoRelatorio = null;
	
	public static IManutencaoAgenda retornaManutencaoAgenda(String ip, String porta, String hostName) {

		if( manutencaoAgenda !=null )
		{
			try {
				manutencaoAgenda.ping();
			} catch (RemoteException e) {
				manutencaoAgenda = null;
				System.out.println("Não foi possível realizar a conexão com o servidor de agenda: "+hostName);
			} catch (Exception e) {
				manutencaoAgenda = null;
				e.printStackTrace();
			}
		}

		if(manutencaoAgenda == null)
		{      
			try {
				System.setSecurityManager(new RMISecurityManager());

				manutencaoAgenda = (IManutencaoAgenda) Naming.lookup(
						"rmi://"+hostName+":"+porta+"/"+IManutencaoAgenda.nomeRMI+ip);

				manutencaoAgenda.ping();
				System.out.println("Servidor RMI configurado: "+hostName);
				System.out.println("AGENDA: "+manutencaoAgenda);
			} catch (RemoteException e) {
				manutencaoAgenda = null;
				System.out.println("Servidor de agenda fora do ar: "+hostName);
			} catch (Exception e) {
				manutencaoAgenda = null;
				e.printStackTrace();
			}
		}

		
		return manutencaoAgenda;
	}

	public static IManutencaoRelatorio retornaManutencaoRelatorio(String ip, String porta, String hostName) {
		if( manutencaoRelatorio !=null )
		{
			try {
				manutencaoRelatorio.ping();
			} catch (RemoteException e) {
				manutencaoRelatorio = null;
//				System.out.println("Servidor de agenda fora do ar.");
			} catch (Exception e) {
				manutencaoRelatorio = null;
				e.printStackTrace();
			}
		}

		if(manutencaoRelatorio == null)
		{
			try {
				manutencaoRelatorio = (IManutencaoRelatorio) Naming.lookup(
						"rmi://"+hostName+":"+porta+"/"+IManutencaoRelatorio.nomeRMI+ip);

				manutencaoRelatorio.ping();

			} catch (RemoteException e) {
				manutencaoRelatorio = null;
			} catch (Exception e) {
				manutencaoRelatorio = null;
				e.printStackTrace();
			}
		}

		return manutencaoRelatorio;
	}
	
	/**
	 * Método responsável por marcar os objetos de agenda e relatório para serem reinicializados. Os objetos são preenchidos com valores nulos.
	 */
	public static void resetRMI(){
				manutencaoAgenda = null;
				manutencaoRelatorio = null;
			}
		}
