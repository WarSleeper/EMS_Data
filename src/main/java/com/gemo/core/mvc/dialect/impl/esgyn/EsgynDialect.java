package com.gemo.core.mvc.dialect.impl.esgyn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.gemo.core.constant.SystemConstant;
import com.gemo.core.constant.SystemConstant.JdbcDriverClass;
import com.gemo.core.dto.analysis.EMSColumnAnalysis;
import com.gemo.core.dto.analysis.EMSEntityAnalysis;
import com.gemo.core.dto.analysis.EMSIndexAnalysis;
import com.gemo.core.dto.analysis.EMSTableAnalysis;
import com.gemo.core.mvc.dialect.BaseDatabaseDialect;
import com.gemo.core.mvc.dialect.DatabaseDialect;
import com.gemo.core.utils.CommonUtils;

@Component(JdbcDriverClass.ESGYN)
@SuppressWarnings({"rawtypes","unchecked"})
public class EsgynDialect extends BaseDatabaseDialect implements DatabaseDialect {

	private static Logger log = Logger.getLogger(EsgynDialect.class);
			
	@Override
	public String getPagingSql(String noPagingSql, Long skip, Long limit) {
		// TODO Auto-generated method stub
		if((skip==null || skip<=0) && limit==Long.MAX_VALUE){
			return noPagingSql;
		}
		if(skip==null||skip<0){
			skip=0l;
		}
		StringBuffer sql = new StringBuffer();
		int selectIdx = noPagingSql.indexOf("select");
		String selectStr = noPagingSql.substring(0,selectIdx+6);
		String otherStr = noPagingSql.substring(selectIdx+6);
		
		int orderByIdx = otherStr.indexOf("order by");
		if(orderByIdx==-1){
			throw new RuntimeException(" The sql is marked paging, so it must have order by clause. ");
		}
		String middleStr = otherStr.substring(0,orderByIdx);
		String orderByStr = otherStr.substring(orderByIdx);
		sql.append(" select * from (");
		sql.append(selectStr);
		sql.append(" ROW_NUMBER() OVER (" + orderByStr + ") as ems_rownum , ");
		sql.append(middleStr);
		sql.append(") ems where ems.ems_rownum between " + (skip+1) + " and " + (skip+limit));
		
		return sql.toString();
	}

	@Override
	public List<String> getCreateSchemaSql(String schemaName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("不支持创建Schema！");
	}

	@Override
	public String getSchemaExistSql(String schemaName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("不支持查询Schema！");
	}

	@Override
	public String getTableInfoSql() {
		// TODO Auto-generated method stub
		throw new RuntimeException("不支持创建表信息！");
	}

	@Override
	public String getTableNotExistCode() {
		// TODO Auto-generated method stub
		return "-4082";
	}

	@Override
	public String getDatabaseColumnType(Class<?> cls, Integer length, Integer scale) {
		// TODO Auto-generated method stub
		String columnType = null;
		if(cls==String.class){
			columnType = "varchar";
		}else if(cls==Long.class || cls==Integer.class || cls==Boolean.class){
			columnType = "int";
		}else if(cls==Double.class){
			columnType = "numeric";
		}else if(cls==Date.class){
			columnType = "timestamp";
		}else{
			throw new RuntimeException("对象字段类型不支持" + cls);
		}
		
		return columnType;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		log.info("******** Esgyn配置初始化开始 ********");
		Map root = new HashMap();
		for(Map.Entry<String,EMSEntityAnalysis> entityEntry : analysis.getEntityMap().entrySet()){
			//实体
			String entityKey = entityEntry.getKey();
			EMSEntityAnalysis entity = entityEntry.getValue();
			//表
			EMSTableAnalysis table = entity.getTable();
			//字段
			List<EMSColumnAnalysis> columnList = new ArrayList<EMSColumnAnalysis>();
			for(Map.Entry<String, EMSColumnAnalysis> columnEntry : table.getColumnMap().entrySet()){
				EMSColumnAnalysis column = new EMSColumnAnalysis();
				BeanUtils.copyProperties(columnEntry.getValue(), column);
				if(column.getType().equals("timestamp")){
					column.setLength(0);
				}
				if(column.getType().equals("int")){
					column.setLength(0);
				}
				if(column.getType().equals("varchar") && column.getLength()>16000){
					column.setLength(16000);
				}
				columnList.add(column);
			}
			Collections.sort(columnList);
			root.put("columnList", columnList);
			root.put("primaryKey", table.getPrimaryKey());
			root.put("database", "esgyn");
			List<String> ddlList = tableMap.get(entityKey);
			if(ddlList==null){
				ddlList = new ArrayList<String>();
				tableMap.put(entityKey, ddlList);
			}
			String tableCreateSql = CommonUtils.getSqlByTemplate(root, "TableCreate.ftl");
			
			ddlList.add(tableCreateSql);
			root.clear();
			for(EMSIndexAnalysis index : table.getIndexList()){
				root.put("index", index);
				ddlList.add(CommonUtils.getSqlByTemplate(root, "IndexCreate.ftl"));
			}
			
			//ddlList.add("update statistics for table replace_table on every column sample");
		}
		log.info("******** Esgyn配置初始化结束 ********");
		System.out.println(SystemConstant.jsonMapper.writeValueAsString(tableMap));
	}

}
