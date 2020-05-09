package com.asiainfo.xwbo.xwbo.dao.base.impl;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CommonExtDaoImpl extends BaseDao implements ICommonExtDao {

	public Integer count(SqlBuilder sqlBuilder) {
		return getNamedTemplate(sqlBuilder.getDataSource()).queryForObject(sqlBuilder.getCountSql(),
				sqlBuilder.getParams(), Integer.class);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> query(SqlBuilder sqlBuilder) {
		return (List<T>) getNamedTemplate(sqlBuilder.getDataSource()).query(sqlBuilder.getSelectSql(),
				sqlBuilder.getParams(),
				BeanPropertyRowMapper.newInstance(sqlBuilder.getCls()));
	}

	public void save(SqlBuilder sqlBuilder, Object obj) {
		sqlBuilder.addParams(obj);
		getNamedTemplate(sqlBuilder.getDataSource()).update(sqlBuilder.getInsertSql(),
				new BeanPropertySqlParameterSource(obj));
	}

	public Long saveReturnKey(SqlBuilder sqlBuilder, Object obj) {
		sqlBuilder.addParams(obj);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedTemplate(sqlBuilder.getDataSource()).update(sqlBuilder.getInsertSql(),
				new BeanPropertySqlParameterSource(obj), keyHolder, sqlBuilder.getPks());
		return keyHolder.getKey().longValue();
	}

	public void delete(SqlBuilder sqlBuilder) {
		getNamedTemplate(sqlBuilder.getDataSource()).update(sqlBuilder.getDeleteSql(), sqlBuilder.getParams());
	}

	public void save(SqlBuilder sqlBuilder, List<?> list) {
		sqlBuilder.setBatchUpdate(true);
		getNamedTemplate(sqlBuilder.getDataSource()).batchUpdate(sqlBuilder.getInsertSql(),
				SqlParameterSourceUtils.createBatch(list.toArray()));
	}

	public void update(SqlBuilder sqlBuilder, Object obj) {
		sqlBuilder.addParams(obj);
		System.out.println(sqlBuilder.getUpdateSql());
		getNamedTemplate(sqlBuilder.getDataSource()).update(sqlBuilder.getUpdateSql(), sqlBuilder.getParams());
	}

	public void updateByProperty(SqlBuilder sqlBuilder) {
		getNamedTemplate(sqlBuilder.getDataSource()).update(sqlBuilder.getUpdateByPropertySql(),
				sqlBuilder.getParams());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T queryForObject(SqlBuilder sqlBuilder) {
		List<T> list = (List<T>) getNamedTemplate(sqlBuilder.getDataSource()).query(sqlBuilder.getSelectSql(),
				sqlBuilder.getParams(),
				BeanPropertyRowMapper.newInstance(sqlBuilder.getCls()));
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Integer getSequence(String sequence_name) {
		String sql = "select "+sequence_name+".Nextval from dual";
		return getNamedTemplate("").queryForObject(sql, new HashMap<String,Object>(),Integer.class);
	}
	
	public void saveSelective(SqlBuilder sqlBuilder, Object obj) {
		sqlBuilder.setSelective(true);
		this.save(sqlBuilder, obj);
	}

	public Long saveSelectiveReturnKey(SqlBuilder sqlBuilder, Object obj) {
		sqlBuilder.setSelective(true);
		return this.saveReturnKey(sqlBuilder, obj);
	}

	public void saveSelective(SqlBuilder sqlBuilder, List<?> list) {
		if(null == list || list.size() == 0){
			return;
		}
		sqlBuilder.setSelective(true);
		sqlBuilder.addParams(list.get(0));
		this.save(sqlBuilder, list);
	}

	public void updateSelective(SqlBuilder sqlBuilder, Object obj) {
		sqlBuilder.setSelective(true);
		this.update(sqlBuilder, obj);
	}

	@Override
	public void executeUpdateSql(SqlBuilder sqlBuilder, String sql) {
		getJdbcTemplate(sqlBuilder.getDataSource()).update(sql);
	}

	@Override
	public void batchUpdate(SqlBuilder sqlBuilder, List<?> list, List<String> whereProperties) {
		sqlBuilder.setBatchUpdate(true);
		sqlBuilder.setWhereProperties(whereProperties);
		System.out.println(sqlBuilder.getUpdateSql());
		getNamedTemplate(sqlBuilder.getDataSource()).batchUpdate(sqlBuilder.getUpdateSql(),
				SqlParameterSourceUtils.createBatch(list.toArray()));
	}

}
