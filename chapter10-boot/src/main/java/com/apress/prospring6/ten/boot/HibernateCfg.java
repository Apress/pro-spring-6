package com.apress.prospring6.ten.boot;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class HibernateCfg {

    @Bean
    PhysicalNamingStrategyStandardImpl caseSensitivePhysicalNamingStrategy() {
        return new PhysicalNamingStrategyStandardImpl() {
            @Override
            public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment context) {
                return apply(logicalName, context);
            }

            @Override
            public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment context) {
                return apply(logicalName, context);
            }

            private Identifier apply(final Identifier name, final JdbcEnvironment context) {
                if ( name == null ) {
                    return null;
                }
                StringBuilder builder = new StringBuilder( name.getText().replace( '.', '_' ) );
                for ( int i = 1; i < builder.length() - 1; i++ ) {
                    if ( isUnderscoreRequired( builder.charAt( i - 1 ), builder.charAt( i ), builder.charAt( i + 1 ) ) ) {
                        builder.insert( i++, '_' );
                    }
                }
                return Identifier.toIdentifier(builder.toString().toUpperCase());
            }

            private boolean isUnderscoreRequired(final char before, final char current, final char after) {
                return Character.isLowerCase( before ) && Character.isUpperCase( current ) && Character.isLowerCase( after );
            }

        };
    }
}
