package com.asiainfo.xwbo.xwbo.dao.base.impl;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.DataSourceType;
import com.asiainfo.xwbo.xwbo.system.constants.JdbcConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {

	@Autowired
	@Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("baseNamedJdbcTemplate")
	public NamedParameterJdbcTemplate namedTemplate;

	

	public JdbcTemplate getJdbcTemplate() {
		DataSourceType dataSourceType = this.getClass().getAnnotation(DataSourceType.class);
		return getJdbcTemplate(dataSourceType.value());
	}
	public JdbcTemplate getJdbcTemplate(String type) {
		if (StringUtils.isNotEmpty(type)) {
			if (JdbcConstant.DATASOURCE_TYPE.DEFAULT.equals(type)) {
				return jdbcTemplate;
			}
		}
		return jdbcTemplate;
	}
	
	public NamedParameterJdbcTemplate getNamedTemplate() {
		DataSourceType dataSourceType = this.getClass().getAnnotation(DataSourceType.class);
		return getNamedTemplate(dataSourceType.value());
	}

	public NamedParameterJdbcTemplate getNamedTemplate(String type) {
		if (StringUtils.isNotEmpty(type)) {
			if (JdbcConstant.DATASOURCE_TYPE.DEFAULT.equals(type)) {
				return namedTemplate;
			}
		}
		return namedTemplate;
	}

}
