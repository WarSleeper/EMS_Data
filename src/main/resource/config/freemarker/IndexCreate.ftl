create <#if index.unique>unique </#if>index replace_index on replace_table (<#t>
	<#list index.columnList as column>
		${column}<#t>
		<#if (column_index != index.columnList?size-1)>,</#if><#t>
	</#list>
)<#t>