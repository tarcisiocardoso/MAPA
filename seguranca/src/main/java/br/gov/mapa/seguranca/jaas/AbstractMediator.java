package br.gov.mapa.seguranca.jaas;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractMediator {

    /**
     * Permite setar um array de Long no PreparedStatemnt (Ex. Cláusula "IN (?, ?, ?)")
     *
     * @param ps
     * @param values
     * @throws SQLException
     */
    protected static void setaValoresClausulaINSQL(PreparedStatement ps, String[] values) throws SQLException {
        if (values != null && values.length > 0) {
            for (int i = 0, j = 2, l = values.length; i < l; i++, j++) {
                ps.setLong(j, Long.valueOf(values[i]));
            }
        }
    }

    /**
     * Cria a cláusula "IN (?, ?, ?)") para a consulta
     *
     * @param sql
     * @param campo
     * @param ids
     */
    protected static void criarClausulaINSQL(StringBuilder sql, String campo, String[] ids) {
        if (ids != null && ids.length > 0) {
            sql.append(" AND ").append(campo).append(" IN (");
            for (int i = 0, l = ids.length, m = l - 1; i < l; i++) {
                sql.append(" ? ");
                if (i < m) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }
    }
}
