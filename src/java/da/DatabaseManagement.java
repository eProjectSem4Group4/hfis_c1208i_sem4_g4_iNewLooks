/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import entity.QueryParameter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.omg.CORBA.portable.ApplicationException;

/**
 *
 * @author Lab
 */
public class DatabaseManagement {

    private static DatabaseManagement obj = null;
    Connection connect;
    PreparedStatement ps;

    private DatabaseManagement() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionString = "jdbc:sqlserver://HUNGPV\\SQLEXPRESS2013:1314;databaseName=WhereAwesome";
        connect = DriverManager.getConnection(connectionString, "sa", "Blackadn123@");
    }

    public static DatabaseManagement load() throws Exception {
        if (obj == null) {
            obj = new DatabaseManagement();
        }
        return obj;
    }

    public void setQuery(String query, QueryParameter[] params) throws Exception {
        ps = connect.prepareStatement(query);
        for (QueryParameter param : params) {
            ps.setString(param.getPosition(), param.getValue());
        }
    }

    public void doQuery() throws Exception {
        ps.execute();
    }

    public void doQuery(String query, QueryParameter[] params) throws Exception {
        setQuery(query, params);
        ps.execute();
    }

    public ResultSet getResultSet() throws Exception {
        return ps.executeQuery();
    }

    public ResultSet getResultSet(String query, QueryParameter[] params) throws Exception {
        setQuery(query, params);
        return ps.executeQuery();
    }
}
