package Portal.Dao.Hibernate.Conf;

import java.util.ArrayList;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import Portal.Dto.ArquivosFaltantes;
import Portal.Dto.Bilhetador;
import Portal.Dto.NumFaltantes;
import Portal.Dto.NumRepetidos;

public class RelatoriosSequenciaDAO {
	
private Session session;
	
	public RelatoriosSequenciaDAO(Session session){
		this.session = session;
	}
	
	public ArrayList getArquivosFaltantes(String where){
		String sql = "select {dto.*},{bil.*} from seq_num_arq dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where+" order by bil.nome, dto.data";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",ArquivosFaltantes.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getNumerosRepetidos(String where){
		String sql = "select {dto.*},{bil.*} from seq_num_repetidos dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where+" order by bil.nome, dto.data";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",NumRepetidos.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	public ArrayList getNumerosFaltantes(String where){
		String sql = "select {dto.*},{bil.*} from seq_num_faltantes dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador "+where+" order by bil.nome, dto.data";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",NumFaltantes.class);
		query.addEntity("bil",Bilhetador.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	
	/** Retorna um array, contendo em suas três posições, respectivamente, registros da tabela seq_num_repetidos, bilhetadores e seq_num_arq.
	 * @param where
	 * @return
	 */
	public ArrayList getNumerosRepetidosArq(String where){
		String sql = "select {dto.*},{bil.*},{arq.*} from seq_num_repetidos dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador inner join seq_num_arq arq on dto.id_bilhetador = arq.id_bilhetador and dto.DATA = arq.DATA "+where+" order by bil.nome, dto.data";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",NumRepetidos.class);
		query.addEntity("bil",Bilhetador.class);
		query.addEntity("arq",ArquivosFaltantes.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	/** Retorna um array, contendo em suas três posições, respectivamente, registros da tabela seq_num_faltantes, bilhetadores e seq_num_arq.
	 * @param where
	 * @return
	 */
	public ArrayList getNumerosFaltantesArq(String where){
		String sql = "select {dto.*},{bil.*},{arq.*} from seq_num_faltantes dto inner join bilhetador bil on bil.id_bilhetador = dto.id_bilhetador inner join seq_num_arq arq on dto.id_bilhetador = arq.id_bilhetador and dto.DATA = arq.DATA "+where+" order by bil.nome, dto.data";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity("dto",NumFaltantes.class);
		query.addEntity("bil",Bilhetador.class);
		query.addEntity("arq",ArquivosFaltantes.class);
		ArrayList lista = (ArrayList)query.list();
		return lista;
	}
	
	

}
