package com.expedia.content.media.processing.pipeline.reporting.sql;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

/**
 * Call a MSSQL Sproc in LCM in order to store the content of a
 *
 */
@Repository
@Deprecated
public class SQLLogEntryInsertSproc extends StoredProcedure {

    @Autowired
    public SQLLogEntryInsertSproc(final DataSource dataSource) {
        super(dataSource, "MediaProcessLogAdd#01");
        declareParameter(new SqlParameter("@pMediaFileName", Types.VARCHAR));
        declareParameter(new SqlParameter("@pMediaType", Types.VARCHAR));
        declareParameter(new SqlParameter("@pAppName", Types.VARCHAR));
        declareParameter(new SqlParameter("@pActivityType", Types.VARCHAR));
        declareParameter(new SqlParameter("@pActivityTime", Types.VARCHAR));
        declareParameter(new SqlParameter("@pSKUGroupCatalogItemID", Types.INTEGER));
        declareParameter(new SqlParameter("@pLastUpdatedBy", Types.VARCHAR));
    }

}
