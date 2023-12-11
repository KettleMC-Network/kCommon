package net.kettlemc.kcommon.data;

import org.hibernate.HibernateException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

public class DynamicNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    private String tableFormat = "%s";


    private DynamicNamingStrategy() {
    }

    public static DynamicNamingStrategy of(String tableFormat) {
        return new DynamicNamingStrategy().setTableFormat(tableFormat);
    }

    public DynamicNamingStrategy setTableFormat(String tableFormat) {
        this.tableFormat = tableFormat;
        return this;
    }

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        if (source == null) {
            // should never happen, but to be defensive...
            throw new HibernateException("Entity naming information was not provided.");
        }

        String tableName = transformEntityName(source.getEntityNaming());

        if (tableName == null) {
            throw new HibernateException("Could not determine primary table name for entity");
        }

        return toIdentifier(String.format(tableFormat, tableName), source.getBuildingContext());
    }

}
