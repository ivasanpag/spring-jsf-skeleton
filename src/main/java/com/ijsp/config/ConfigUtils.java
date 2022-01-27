package com.ijsp.config;

import javax.validation.constraints.NotNull;
import java.util.Locale;


/**
 * Config utils.
 * @author ijsp
 * @since 1.0
 */
public class ConfigUtils {
    protected static final String WEBLOGIC_DS = "myDS";
    protected static final String WEBSPHERE_DS = "java:comp/env/myDS";
    protected static final String JBOSS_WILDFLY_DS = "java:jboss/myDS";

    protected static final String SQLSERVER_DIALECT = "org.hibernate.dialect.SQLServer2012Dialect";
    protected static final String ORACLE_DIALECT = "org.hibernate.dialect.Oracle12cDialect";
    protected static final String DB2_DIALECT = "org.hibernate.dialect.DB2Dialect";
    protected static final String POSTGRE_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";


    /**
     * Find jndi name from application server string.
     *
     * @param server application server
     * @return jndi datasource
     */
    protected static String findJndiNameFromApplicationServer(@NotNull String server) {
        String appServer = server.toLowerCase(Locale.ROOT);
        if(appServer.contains("weblogic"))
            return WEBLOGIC_DS;
        else if(appServer.contains("websphere"))
            return WEBSPHERE_DS;
        else
            return JBOSS_WILDFLY_DS;
    }


    /**
     * Lookup dialect from database name.
     *
     * @param dbName the db name
     * @return database dialect
     */
    protected static String lookupDialect(@NotNull String dbName) {
        // Only in testing
        if(dbName == null)
            return ORACLE_DIALECT;

        String dbNameLower = dbName.toLowerCase(Locale.ROOT);
        if (dbNameLower.contains("sql server") || dbNameLower.contains("microsoft") || dbNameLower.contains("sqlserver"))
            return SQLSERVER_DIALECT;
        else if (dbNameLower.contains("oracle"))
            return ORACLE_DIALECT;
        else if (dbNameLower.contains("db2"))
            return DB2_DIALECT;
        else if (dbNameLower.contains("postgre"))
            return POSTGRE_DIALECT;

        return "";
    }
}
