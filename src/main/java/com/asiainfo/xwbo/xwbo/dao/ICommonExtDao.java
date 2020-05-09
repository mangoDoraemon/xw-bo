package com.asiainfo.xwbo.xwbo.dao;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface ICommonExtDao {
	/*
	 * SqlBuilder sqlBuilder =
	 * SqlBuilder.build(ActivityUser.class).eq("bill_no", "15926303520");
	 */
	public Integer count(SqlBuilder sqlBuilder);

	/*
	 * SqlBuilder sqlBuilder =
	 * SqlBuilder.build(ActivityUser.class).eq("bill_no", "15926303520");
	 * sqlBuilder.like("user_name", "'%sss%'");
	 * sqlBuilder.setOrder("create_time", "desc").setPage(1, 20);
	 */
	public <T> List<T> query(SqlBuilder sqlBuilder);
	
	/*
	 * SqlBuilder sqlBuilder =
	 * SqlBuilder.build(ActivityUser.class).eq("bill_no", "15926303520");
	 * sqlBuilder.like("user_name", "'%sss%'");
	 */
	public <T> T queryForObject(SqlBuilder sqlBuilder);

	/*
	 * SqlBuilder.build(ActivityUser.class)
	 */
	public void save(SqlBuilder sqlBuilder, Object obj);

	public Long saveReturnKey(SqlBuilder sqlBuilder, Object obj);
	
	/*
	 * SqlBuilder.build(ActivityUser.class)
	 */
	public void save(SqlBuilder sqlBuilder, List<?> list);

	/*
	 * ActivityUser user = new ActivityUser(); user.setOpen_id("1111");
	 * SqlBuilder sqlBuilder = SqlBuilder.build(ActivityUser.class);
	 * sqlBuilder.eq("bill_no", "15926303520");
	 */
	public void update(SqlBuilder sqlBuilder, Object obj);

	/*
	 * SqlBuilder sqlBuilder = SqlBuilder.build(ActivityUser.class);
	 * sqlBuilder.eq("bill_no", "15926303520");
	 * sqlBuilder.update("user_name","test");
	 * sqlBuilder.update("create_time",new Date());
	 */
	public void updateByProperty(SqlBuilder sqlBuilder);

	/*
	 * SqlBuilder sqlBuilder =
	 * SqlBuilder.build(ActivityUser.class).eq("bill_no", "15926303520");
	 */
	public void delete(SqlBuilder sqlBuilder);
	
	public Integer getSequence(String sequence_name);
	
	/*
	 * SqlBuilder.build(ActivityUser.class)
	 */
	void saveSelective(SqlBuilder sqlBuilder, Object obj);

	Long saveSelectiveReturnKey(SqlBuilder sqlBuilder, Object obj);
	
	/*
	 * SqlBuilder.build(ActivityUser.class)
	 */
	void saveSelective(SqlBuilder sqlBuilder, List<?> list);
	
	void updateSelective(SqlBuilder sqlBuilder, Object obj);
	
	void executeUpdateSql(SqlBuilder sqlBuilder, String sql);
	
	void batchUpdate(SqlBuilder sqlBuilder, List<?> list, List<String> whereProperties);
	
	/**
	 * 批量执行update语句
	 * @param sqlBuilder
	 * @param list
	 * @param whereProperties 用来做where条件的对象属性，多个属性用英文逗号","隔开
	 * <br/>
	 * 如：<br/>
	 * SqlBuilder builder = SqlBuilder.build(ActivityUser.class);<br/>
	 * List<ActivityUser> list = buildList();<br/>
	 * commonExtDao.batchUpdate(builder, list, "userId");
	 */
	default void batchUpdate(SqlBuilder sqlBuilder, List<?> list, String whereProperties){
		if(StringUtils.isBlank(whereProperties)){
			return;
		}
		batchUpdate(sqlBuilder, list, Splitter.on(",").splitToList(whereProperties));
	}
	
	default void batchUpdateSelective(SqlBuilder sqlBuilder, List<?> list, List<String> whereProperties) {
		if(null == list || list.size() == 0){
			return;
		}
		sqlBuilder.setSelective(true);
		sqlBuilder.addParams(list.get(0));
		batchUpdate(sqlBuilder, list, whereProperties);
	}
	
	default void batchUpdateSelective(SqlBuilder sqlBuilder, List<?> list, String whereProperties) {
		if(StringUtils.isBlank(whereProperties)){
			return;
		}
		batchUpdateSelective(sqlBuilder, list, Splitter.on(",").splitToList(whereProperties));
	}
	
}
