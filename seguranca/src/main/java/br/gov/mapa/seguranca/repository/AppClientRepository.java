package br.gov.mapa.seguranca.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.gov.mapa.seguranca.model.AppClient;

@Repository
@SuppressWarnings("deprecation")
public class AppClientRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Optional<AppClient> findByClientId(String clientId) {
		Optional<AppClient> retorno = Optional.empty();
		try {
			AppClient app = jdbcTemplate.queryForObject("select id, clientId, clientSecret, redirectUri1, redirectUri2, scope, nome from appClient WHERE clientId = ?", new Object[] { clientId }, new RowMapper<AppClient>() {
				@Override
				public AppClient mapRow(ResultSet rs, int rowNum) throws SQLException {
					return create(rs);
				}
			});
			retorno = Optional.of(app);
		}catch(DataAccessException e) {
			System.err.println(clientId+" não encontrado no banco: "+e.getMessage());
		}
		return retorno;
	}
	
	private AppClient create(ResultSet rs) throws SQLException {
		AppClient app = new AppClient();
		app.id = rs.getLong("id");
		app.clientId = rs.getString("clientId");
		app.clientSecret = rs.getString("clientSecret");
		app.redirectUri2 = rs.getString("redirectUri2");
		app.scope = rs.getString("scope");
		app.nome = rs.getString("nome");
		
		return app;
	}
}
