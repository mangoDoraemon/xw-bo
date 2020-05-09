package com.asiainfo.xwbo.xwbo.dao.sqlBuild;

import com.asiainfo.xwbo.xwbo.exception.SqlBuilderException;
import com.asiainfo.xwbo.xwbo.system.constants.JdbcConstant;
import com.asiainfo.xwbo.xwbo.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlBuilder {

	/**
	 * 数据库对应po的实体类
	 */
	private Class<?> cls = null;

	/**
	 * where条件集合
	 */
	private Set<String> criteria = null;

	/**
	 * update语句set的集合
	 */
	private Set<String> update_property = null;

	/**
	 * 参数集合
	 */
	private Map<String, Object> params = null;

	/**
	 * 排序字段
	 */
	private String order = null;

	/**
	 * 排序规则（asc:正序, desc:倒序）
	 */
	private String orderType = null;

	/**
	 * 排序方式（如：create_time desc, id desc）
	 */
	private String orderBy = null;

	/**
	 * 分页参数-第几页
	 */
	private Integer page = null;

	/**
	 * 分页参数-每页记录数
	 */
	private Integer rows = null;

	/**
	 * UUID
	 */
	private String uuid;

	/**
	 * 数据源
	 */
	private String dataSource;

	/**
	 * 数据库类型
	 */
	private DbType dbType;

	/**
	 * 字段集合
	 */
	protected Map<String, String> columnMapping = new HashMap<String, String>();
	/**
	 * 实体类属性集合
	 */
	protected Map<String, String> propertyMapping = new HashMap<String, String>();

	/**
	 * @Id 注解对应的属性名
	 */
	private String idName;
	/**
	 * 主键名
	 */
	private String[] pks;

	/**
	 * 是否批量操作
	 */
	private Boolean batchUpdate = false;
	
	/**
	 * 是否选择性拼装insert/update语句（false:对象所有属性, true:对象属性为空时会被过滤掉）
	 */
	private Boolean selective = false;
	
	private static final int IN_LIMIT_NUM = 1000;
	
	/**
	 * 自定义表名
	 */
	private String customTableName = "";
	
	/**
	 * 表名动态内容，直接替换表名中的${}区域
	 */
	private String[] dynamicOfTableName;

	public String getCustomTableName() {
		return customTableName;
	}

	public SqlBuilder setCustomTableName(String customTableName) {
		this.customTableName = customTableName;
		return this;
	}

	public String[] getDynamicOfTableName() {
		return dynamicOfTableName;
	}

	public SqlBuilder setDynamicOfTableName(String[] dynamicOfTableName) {
		this.dynamicOfTableName = dynamicOfTableName;
		return this;
	}

	public Boolean getSelective() {
		return selective;
	}

	public void setSelective(Boolean selective) {
		this.selective = selective;
	}

	public Boolean getBatchUpdate() {
		return batchUpdate;
	}

	public void setBatchUpdate(Boolean batchUpdate) {
		this.batchUpdate = batchUpdate;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	private SqlBuilder(Class<?> cls) {
		this.cls = cls;
		criteria = new HashSet<String>();
		update_property = new HashSet<String>();
		params = new HashMap<String, Object>();
		initDataSource();
		initColumns();
		initPks();
	}

	/**
	 * 初始化主键信息
	 */
	protected void initPks() {
		Table annotation = this.cls.getAnnotation(Table.class);
		if (StringUtils.isNotBlank(annotation.pks())) {
			setPks(annotation.pks().split(","));
		} else {
			if (StringUtils.isNotBlank(this.idName)) {
				setPks(new String[] { this.idName });
			}
		}
	}

	/**
	 * 初始化数据源信息
	 */
	protected void initDataSource() {
		DataSourceType annotation = this.cls.getAnnotation(DataSourceType.class);
		if (null != annotation) {
			setDataSource(annotation.value());
			setDbType(annotation.type());
		} else {
			setDataSource(JdbcConstant.DATASOURCE_TYPE.DEFAULT);
			setDbType(DbType.ORACLE);
		}
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public DbType getDbType() {
		return dbType;
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	/**
	 * 初始化字段集合、实体类属性集合
	 */
	protected void initColumns() {
		Field[] fields = getAllFields(this.cls);
		for (Field field : fields) {
			Column annotation = field.getAnnotation(Column.class);
			if (null != annotation) {
				String columnName = annotation.value();
				String propertyName = field.getName();
				columnMapping.put(columnName, propertyName);
				propertyMapping.put(propertyName, columnName);
			}
			Id idAnnotation = field.getAnnotation(Id.class);
			if (null != idAnnotation) {
				setIdName(field.getName());
			}
		}
	}

	/**
	 * 根据属性名获取字段名
	 * @param propertyName
	 * @return
	 */
	protected String getColumnNameByPropertyName(String propertyName) {
		return null == propertyMapping.get(propertyName) ? underscoreName(propertyName)
				: propertyMapping.get(propertyName);
	}

	public static SqlBuilder build(Class<?> cls) {
		return new SqlBuilder(cls);
	}

	/**
	 * 添加update语句set条件
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder update(String property, Object value) {
		if (value != null) {
			update_property.add(getColumnNameByPropertyName(property) + "=:" + property);
			params.put(property, value);
		}
		return this;
	}

	/**
	 * 添加条件
	 * @param obj
	 * @return
	 */
	public SqlBuilder addParams(Object obj) {
		Class<?> cls = obj.getClass();
		Field[] fields = getAllFields(cls);
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}
			String name = field.getName();
			String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
			Method methodGet;
			try {
				methodGet = cls.getDeclaredMethod(strGet);
				Object object = methodGet.invoke(obj);
				if(selective){
					if(null == object){
						continue;
					}
				}
				String colName = getColumnNameByPropertyName(name);
				if(!colName.equals(name)){
					params.put(name, object);
				}
				params.put(colName, object);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return this;
	}

	/**
	 * 条件-等于
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder eq(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			criteria.add(getColumnNameByPropertyName(property) + "=:" + property);
			params.put(property, value);
		}
		return this;
	}

	/**
	 * 条件-不等于
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder neq(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			criteria.add(getColumnNameByPropertyName(property) + "!=:" + property);
			params.put(property, value);
		}
		return this;
	}

	/**
	 * 条件-like '%?%'
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder like(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			criteria.add(getColumnNameByPropertyName(property) + " LIKE '%" + value + "%'");
		}
		return this;
	}

	/**
	 * 条件-like '?%'
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder lLike(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			criteria.add(getColumnNameByPropertyName(property) + " LIKE '" + value + "%'");
		}
		return this;
	}

	/**
	 * 条件-like '%?'
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder rLike(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			criteria.add(getColumnNameByPropertyName(property) + " LIKE '%" + value + "'");
		}
		return this;
	}

	/**
	 * 条件-小于
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder lt(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			String _property = property + "_lt";
			criteria.add(getColumnNameByPropertyName(property) + "<:" + _property);
			params.put(_property, value);
		}
		return this;
	}

	/**
	 * 条件-大于
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder gt(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			String _property = property + "_gt";
			criteria.add(getColumnNameByPropertyName(property) + ">:" + _property);
			params.put(_property, value);
		}
		return this;
	}

	/**
	 * 条件-小于或等于
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder lte(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			String _property = property + "_lte";
			criteria.add(getColumnNameByPropertyName(property) + "<=:" + _property);
			params.put(_property, value);
		}
		return this;
	}

	/**
	 * 条件-大于或等于
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder gte(String property, Object value) {
		if (value != null) {
			if (value instanceof String && value == "") {
				return this;
			}
			String _property = property + "_gte";
			criteria.add(getColumnNameByPropertyName(property) + ">=:" + _property);
			params.put(_property, value);
		}
		return this;
	}
	
	/**
	 * 解析in条件大于1000的场景
	 * @param property
	 * @param value
	 * @param flag （1:in, 2:not in）
	 */
	private void resolveIn(String property, Object value, Integer flag){
		List list = (List) value;
		int page = list.size() / IN_LIMIT_NUM;
		if(list.size() % IN_LIMIT_NUM > 0){
			page += 1;
		}
		StringBuilder inCriteria = new StringBuilder();
		inCriteria.append("(");
		for(int i=0; i<page; i++){
			int fromIndex = i * IN_LIMIT_NUM;
			int toIndex = (i + 1) * IN_LIMIT_NUM > list.size() ? list.size() : (i + 1) * IN_LIMIT_NUM;
			List cList = list.subList(fromIndex, toIndex);
			if(i > 0){
				inCriteria.append(" OR ");
			}
			String property_suffix = flag == 1 ? "in" : "notin";
			String _property = property + "_" + i + "_" + property_suffix;
			String paramRule = flag == 1 ? " IN " : " NOT IN ";
			inCriteria.append(getColumnNameByPropertyName(property) + paramRule + "(:" + _property + ")");
			params.put(_property, cList);
		}
		inCriteria.append(")");
		criteria.add(inCriteria.toString());
	}

	/**
	 * 条件-in (?)
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder in(String property, Object value) {
		if (value != null) {
			if(value instanceof List){
				resolveIn(property, value, 1);
			}else{
				String _property = property + "_in";
				criteria.add(getColumnNameByPropertyName(property) + " IN(:" + _property + ")");
				params.put(_property, value);
			}
		}
		return this;
	}

	/**
	 * 条件-not in (?)
	 * @param property
	 * @param value
	 * @return
	 */
	public SqlBuilder notIn(String property, Object value) {
		if (value != null) {
			if(value instanceof List){
				resolveIn(property, value, 2);
			}else{
				String _property = property + "_notin";
				criteria.add(getColumnNameByPropertyName(property) + " NOT IN(:" + _property + ")");
				params.put(_property, value);
			}
		}
		return this;
	}

	/**
	 * 条件-is not null
	 * @param property
	 * @return
	 */
	public SqlBuilder isNotNull(String property) {
		criteria.add(getColumnNameByPropertyName(property) + " IS NOT NULL");
		return this;
	}

	/**
	 * 条件-is null
	 * @param property
	 * @return
	 */
	public SqlBuilder isNull(String property) {
		criteria.add(getColumnNameByPropertyName(property) + " IS NULL");
		return this;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getRows() {
		return rows;
	}

	public SqlBuilder setOrder(String order, String orderType) {
		this.order = order;
		this.orderType = orderType;
		return this;
	}

	public SqlBuilder setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public SqlBuilder setPage(Integer page, Integer rows) {
		this.page = page;
		this.rows = rows;
		return this;
	}
	
	public SqlBuilder addCriteria(String criteriaStr) {
		criteria.add(criteriaStr);
		return this;
	}
	
	public SqlBuilder setWhereProperties(List<String> whereProperties) {
		for(String property : whereProperties){
			criteria.add(getColumnNameByPropertyName(property) + "=:" + property);
		}
		return this;
	}

	public String getOrderSql() {
		if (StringUtils.isNotEmpty(this.order)) {
			return " ORDER BY " + getColumnNameByPropertyName(this.order) + " " + this.orderType;
		} else {
			return null;
		}
	}

	public String getOrderBySql() {
		if (StringUtils.isNotEmpty(this.orderBy)) {
			return " ORDER BY " + this.orderBy + " ";
		} else {
			return null;
		}
	}

	public <T> String getTableName(Class<T> cls) {
		if (cls == null) {
			throw new SqlBuilderException("类不能为空!");
		}
		// 自定义表名存在时，直接返回自定义的表名
		if(StringUtils.isNotBlank(customTableName)){
			return customTableName;
		}
		Table table = cls.getAnnotation(Table.class);
		if (table == null || table.value() == "") {
			throw new SqlBuilderException("没有table注解或者配置为空!");
		}
		return resolveTableName(table);
	}
	
	/**
	 * 解析表名
	 * @param table
	 * @return
	 */
	private String resolveTableName(Table table){
		if(table.dynamic()){
			if(null != dynamicOfTableName && dynamicOfTableName.length > 0){
				return resolveTableNameWithDynamic(table.value(), dynamicOfTableName);
			}else{
				if(table.dynamicRule().equals(DynamicRule.date)){
					return resolveTableNameWithNowDate(table.value());
				}
			}
		}
		return table.value();
	}

	private static String resolveTableNameWithDynamic(String tableName, String[] dynamicOfTableName) {
		String regex = "(\\$\\{)[^(\\$\\{)]+(\\})";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tableName);
		int i = 0;
		while(matcher.find()){
			tableName = tableName.replace(matcher.group(0), dynamicOfTableName[i]);
			i++;
		}
		return tableName;
	}

	private static String resolveTableNameWithNowDate(String tableName) {
		String regex = "(\\$\\{)[^(\\$\\{)]+(\\})";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tableName);
		Date now = new Date();
		while(matcher.find()){
			String group = matcher.group(0).replace("${", "").replace("}", "");
			String dateFormat = getDateFormat(group);
			tableName = tableName.replaceFirst(regex, dateToStr(now, dateFormat));
		}
		return tableName;
	}
	
	/**
	 * 将yyyymmddhhmiss转换为yyyyMMddHHmmss
	 * @param tableDateFormat
	 * @return
	 */
	private static String getDateFormat(String tableDateFormat){
		if(StringUtils.isBlank(tableDateFormat)){
			tableDateFormat = "yyyymmdd";
		}
		return tableDateFormat.toLowerCase().replace("hh", "HH").replace("mm", "MM").replace("mi", "mm");
	}

	public <T> String getFieldByClass(Class<T> cls) {
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = getAllFields(cls);
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}
			Transient trans = field.getAnnotation(Transient.class);
			if (trans == null || !trans.isTransient()) {
				if(selective){
					Id id = field.getAnnotation(Id.class);
					if(id == null){
						if(!params.containsKey(getColumnNameByPropertyName(field.getName()))){
							continue;
						}
					}
				}
				fieldList.add(getColumnNameByPropertyName(field.getName()));
			}
		}
		return StringUtils.join(fieldList, ",");
	}
	
	protected <T>Field[] getAllFields(Class<T> cls){
		HasParentAttribute hasParentAttribute = cls.getAnnotation(HasParentAttribute.class);
		if(null != hasParentAttribute && hasParentAttribute.value()){
			return FieldUtils.getAllFields(cls);
		}
		return cls.getDeclaredFields();
	}

	public <T> String getSelectFieldByClass(Class<T> cls) {
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = getAllFields(cls);
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}
			Transient trans = field.getAnnotation(Transient.class);
			if (trans == null || !trans.isTransient()) {
				Column colName = field.getAnnotation(Column.class);
				if (null != colName) {
					fieldList.add(colName.value() + " as " + field.getName());
				} else {
					fieldList.add(underscoreName(field.getName()));
				}
			}
		}
		return StringUtils.join(fieldList, ",");
	}

	public <T> String getInsertFieldByClass(Class<T> cls) {
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = getAllFields(cls);
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}
			Transient trans = field.getAnnotation(Transient.class);
			if (trans == null || !trans.isTransient()) {
				Object fieldVal = params.get(getColumnNameByPropertyName(field.getName()));
				Id id = field.getAnnotation(Id.class);
				if(selective){
					if(null == fieldVal && id == null){
						continue;
					}
				}
				if (batchUpdate.booleanValue()) {
					fieldList.add(":" + field.getName());
					continue;
				}
				if (id != null) {
					if (fieldVal instanceof String && StringUtils.isNotBlank(fieldVal.toString())) {
						fieldList.add(":" + field.getName());
					} else if (!(fieldVal instanceof String) && null != fieldVal) {
						fieldList.add(":" + field.getName());
					} else {
						GenerationType type = id.type();
						if (GenerationType.SEQUENCE == type) {
							String sequence = id.sequence();
							if (StringUtils.isEmpty(sequence)) {
								throw new SqlBuilderException("sequence为空!");
							}
							fieldList.add(sequence + ".NEXTVAL");
						} else if (GenerationType.UUID == type) {
							String uuid = UUID.randomUUID().toString();
							fieldList.add("'" + uuid + "'");
							setUuid(uuid);
						} else if (GenerationType.TIMEANDSEQUENCE == type) {
							String sequence = id.sequence();
							if (StringUtils.isEmpty(sequence)) {
								throw new SqlBuilderException("sequence为空!");
							}
							String dft = id.dateFormat().getDateFormatType(id.dateFormat());
							String dateStr = dateToStr(new Date(), dft);
							if (id.dbType() == DbType.ORACLE) {
								String idStr = "'" + dateStr + "' || " + sequence + ".NEXTVAL";
								fieldList.add(idStr);
							}
						} else {
							throw new SqlBuilderException("未知的增长类型!");
						}
					}
				} else {
					fieldList.add(":" + field.getName());
				}
			}
		}
		return StringUtils.join(fieldList, ",");
	}
	
	private static String dateToStr(Date date, String formatStr) {
		if (date == null) {
			date = new Date();
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(formatStr);
	}

	public <T> String getUpdateFieldByClass(Class<T> cls) {
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = getAllFields(cls);
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}
			Transient trans = field.getAnnotation(Transient.class);
			if (trans == null || !trans.isTransient()) {
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					continue;
				} else {
					String colName = getColumnNameByPropertyName(field.getName());
					if(selective){
						if(!params.containsKey(colName)){
							continue;
						}
					}
					fieldList.add(colName + "=:" + field.getName());
				}
			}
		}
		return StringUtils.join(fieldList, ",");
	}

	protected String underscoreName(String name) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(lowerCaseName(name.substring(0, 1)));
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			String slc = lowerCaseName(s);
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			} else {
				result.append(s);
			}
		}
		return result.toString();
	}

	protected String lowerCaseName(String name) {
		return name.toLowerCase(Locale.US);
	}

	public String getWhereSql() {
		return StringUtils.join(criteria, " AND ");
	}

	public String getUpdateProperty() {
		return StringUtils.join(update_property, ",");
	}

	public String getSelectSql() {
		StringBuffer sql = new StringBuffer("SELECT ");
		String fileds = getSelectFieldByClass(cls);
		String tableName = getTableName(cls);
		String where = getWhereSql();
		String order = getOrderSql();
		String orderBy = getOrderBySql();
		Integer page = getPage();
		Integer rows = getRows();
		sql.append(fileds).append(" FROM ").append(tableName);
		if (StringUtils.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		if (StringUtils.isNotEmpty(order)) {
			sql.append(" ").append(order);
		} else {
			if (StringUtils.isNotEmpty(orderBy)) {
				sql.append(" ").append(orderBy);
			}
		}
		if (null != page && null != rows) {
			// params.put("page", page);
			// params.put("rows", rows);
			// return PageUtils.assembleOraclePageSQLByName(sql.toString());
			return PageUtils.assemblePageSQL(sql.toString(), page.intValue(), rows.intValue(), this.dbType);
		} else {
			return sql.toString();
		}
	}

	public String getCountSql() {
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) ");
		String tableName = getTableName(cls);
		String where = getWhereSql();
		sql.append(" FROM ").append(tableName);
		if (StringUtils.isNotEmpty(where)) {
			sql.append(" WHERE ").append(where);
		}
		return sql.toString();
	}

	public String getInsertSql() {
		StringBuffer sql = new StringBuffer("INSERT INTO ");
		String fileds = getFieldByClass(cls);
		String vauleFileds = getInsertFieldByClass(cls);
		String tableName = getTableName(cls);
		sql.append(tableName).append(" (").append(fileds).append(")  VALUES (");
		sql.append(vauleFileds).append(")");
		return sql.toString();
	}

	public String getUpdateSql() {
		StringBuffer sql = new StringBuffer("UPDATE ");
		String vauleFileds = getUpdateFieldByClass(cls);
		String tableName = getTableName(cls);
		String where = getWhereSql();
		sql.append(tableName).append(" SET ").append(vauleFileds).append(" WHERE ");
		sql.append(where);
		return sql.toString();
	}

	public String getUpdateByPropertySql() {
		StringBuffer sql = new StringBuffer("UPDATE ");
		String vauleFileds = getUpdateProperty();
		String tableName = getTableName(cls);
		String where = getWhereSql();
		sql.append(tableName).append(" SET ").append(vauleFileds).append(" WHERE ");
		sql.append(where);
		return sql.toString();
	}

	public String getDeleteSql() {
		StringBuffer sql = new StringBuffer("DELETE FROM ");
		String tableName = getTableName(cls);
		String where = getWhereSql();
		sql.append(tableName).append(" WHERE ");
		sql.append(where);
		return sql.toString();
	}

	public Class<?> getCls() {
		return this.cls;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public static void main(String[] args) {
		String tableName = "abc_${yyyymmdd}_${hhmi}";
		//tableName = resolveTableNameWithNowDate(tableName);
		String[] dynamicOfTableName = new String[]{"123","456"};
		tableName = resolveTableNameWithDynamic(tableName, dynamicOfTableName);
		System.out.println(tableName);
	}

}
