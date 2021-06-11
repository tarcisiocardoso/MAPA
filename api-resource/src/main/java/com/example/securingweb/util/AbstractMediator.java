package com.example.securingweb.util;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMediator {

    /**
     * Permite setar um array de Long no PreparedStatemnt (Ex. Cláusula "IN (?, ?, ?)")
     *
     * @param ps
     * @param values
     * @throws SQLException
     */
    public static List<String> setaValoresClausulaINSQL(String[] values) throws SQLException {
    	List<String> ret = new ArrayList<>();
        if (values != null && values.length > 0) {
            for (int i = 0, j = 2, l = values.length; i < l; i++, j++) {
//                ps.setLong(j, Long.valueOf(values[i]));
            	ret.add(values[i]);
            }
        }
        return ret;
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
