package codezap.global.rds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DataSourceRouter extends AbstractRoutingDataSource {

    public static final String READER_KEY = "read";
    public static final String WRITER_KEY = "write";

    @Override
    protected Object determineCurrentLookupKey() {
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (readOnly) {
            return READER_KEY;
        }
        return WRITER_KEY;
    }
}
