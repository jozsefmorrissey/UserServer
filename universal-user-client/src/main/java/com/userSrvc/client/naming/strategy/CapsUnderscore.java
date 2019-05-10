package com.userSrvc.client.naming.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CapsUnderscore extends PhysicalNamingStrategyStandardImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2176634732581643493L;

	private Identifier format(Identifier id) {
		if (id == null || id.getText() == null) {
			return id;
		}
		String formatted = id.getText().toString().replaceAll("([a-z])([A-Z])", "$1_$2");
		id = Identifier.toIdentifier(formatted.toUpperCase());
		return id;
	}
	
	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
		return format(name);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
		return format(name);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		return format(name);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		return format(name);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return format(name);
	}

}
