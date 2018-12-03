create table replace_table (<#t>
<#-- 循环字段 -->
<#list columnList as column>
	<#-- 字段名称、类型 -->
	${column.name} ${column.type}<#t>
	<#-- 字段类型精度 -->
	<#if column.length!=0>(${column.length}<#if column.scale!=0>,${column.scale}</#if>)</#if><#t>
	<#-- Mysql字段内容区分大小写 -->
	<#if database?? && database=='mysql' && column.type=='varchar'> binary </#if><#t>
	<#if database?? && database=='esgyn' && column.type=='varchar'> CHARACTER SET UTF8 </#if><#t>
	<#-- 字段是否可为空 -->
	<#if !column.nullable> not null </#if><#t>
	<#-- 不是最后一个字段则追加","号 -->
	<#if (column_index != columnList?size-1) || primaryKey??>,</#if><#t>
</#list>
	<#if primaryKey??>primary key (${primaryKey})</#if><#t>
)