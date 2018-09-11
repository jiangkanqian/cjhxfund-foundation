<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${data.module.packageName}.mapper.${data.className}Mapper">
	<resultMap id="tb${data.className}ResultMap" type="${data.module.packageName}.model.${data.className}">
		<id column="${data.pkCol}" property="${data.pkName}" />
		<#list data.tcList as ctData>
	<#if (ctData.fieldName != data.pkName)>
		<result column="${ctData.colName}" property="${ctData.fieldName}" />
	</#if> 
	</#list>
		<result column="queryTotal" property="queryTotal" />
	</resultMap>
	
	<!-- 时间的where条件查询 -->
	  <sql id="tb${data.className}WhereTime" >
	  	<if test="startDate != null">
		   <![CDATA[ and t.create_time>= TO_DATE(${r"#{startDate}"},'YYYY-MM-DD') ]]>
		  </if>
		  <if test="endDate != null">
		   <![CDATA[ and t.create_time<= TO_DATE(${r"#{endDate}"},'YYYY-MM-DD') ]]>
		  </if>
	  </sql>
	
	
		<!-- 表中所有列 ，除去了update_time, op_id, del_flag,op_id，extra等，如果需要，自行添加-->
	  <sql id="tb${data.className}Cols" >
	     t.${data.pkCol} 			<!-- 主键 -->
			<#list data.tcList as ctData>
			<#if (ctData.fieldName != data.pkName&&
		ctData.fieldName != "updateTime"&&
		ctData.fieldName != "delFlag"&&
		ctData.fieldName != "extra1"&&
		ctData.fieldName != "extra"&&
		ctData.fieldName != "extra2"&&
		ctData.fieldName != "extra3"&&
		ctData.fieldName != "extra"&&
		ctData.fieldName != "opId"
		)>
		 ,t.${ctData.colName}         <!-- ${ctData.comment!} -->
		</#if> 
		</#list>
	  </sql>
	  
	  <!-- 所有字段的where条件查询 -->
	  <sql id="tb${data.className}Where" >
	    <#if data.hasDel == "1"> t.del_flag = 0 <#else> 1=1</#if>
	<#list data.tcList as ctData>
	<#if (
	ctData.queryType != 10
	)>
		<if test="${ctData.fieldName} != null">
			and t.${ctData.colName} = ${r"#{"}${ctData.fieldName}${r"}"}
		</if>
		</#if> 
	</#list>
	  </sql>

	<!-- 根据主键查询-->
	<select id="fetch" parameterType="java.lang.Object" resultMap="tb${data.className}ResultMap">
		select <include refid="tb${data.className}Cols" />
		 from ${data.tableName} t where  t.${data.pkCol} = ${r"#{id}"} <#if data.hasDel == "1"> and t.del_flag = 0</#if> 
	</select>

	<!-- 查询表单所有数据-->
	<select id="findAll" resultMap="tb${data.className}ResultMap">
		select <include refid="tb${data.className}Cols" /> from ${data.tableName} t <#if data.hasDel == "1"> where t.del_flag = 0</#if>  
	</select>

	
	<!--
	根据主键列表查询数据，一般较少用到 
	<select id="selectByListKeys" resultMap="tb${data.className}ResultMap">
		select <include refid="tb${data.className}Cols" /> from ${data.tableName} t where  <#if data.hasDel == "1">  t.del_flag = 0</#if>  and t.${data.pkCol} in
		<foreach collection="list" index="index" item="item" open="(" separator="," close=")">
			${r"#{item}"} 
		</foreach>
	</select>
	-->
	
	<!-- 插入或修改数据时，检验数据是否重复，也可以避免重复提交-->
	<select id="checkData" resultType="java.lang.Integer">
		select count(1) total from ${data.tableName} t where 1=1   <#if data.hasDel == "1"> and t.del_flag = 0</#if>
		<if test="${data.pkName} != null">
			and t.${data.pkCol} != ${r"#{"}${data.pkName}${r"}"}
		</if>
		and (
		<#list data.tcList as ctData>
		<#if (ctData.fieldName != data.pkName&&
	ctData.fieldName != "updateTime"&&
	ctData.fieldName != "delFlag"&&
	ctData.fieldName != "createTime"&&
	ctData.fieldName != "extra"&&
	ctData.fieldName != "extra1"&&
	ctData.fieldName != "extra2"&&
	ctData.fieldName != "extra3"&&
	ctData.fieldName != "opId"
	)>
		<#if (ctData.fieldName != "remark")>
		  t.${ctData.colName} = ${r"#{"}${ctData.fieldName}${r"}"} or
		<#else> 
		  t.${ctData.colName} = ${r"#{"}${ctData.fieldName}${r"}"}
		</#if> 
	</#if> 
	</#list>
		)
	</select>
	


	<!-- 根据查询条件，分页查询数据-->
	<select id="query" resultMap="tb${data.className}ResultMap">
		select <include refid="tb${data.className}Cols" />
	(select count(1) total from ${data.tableName} t 
	where  <include refid="tb${data.className}Where" />
	) queryTotal
		from ${data.tableName} t where <include refid="tb${data.className}Where" />
	<#list data.tcList as ctData>
		<#if (
	ctData.colName == "createdate"
	)>
		order by t.createdate desc
		</#if> 
	</#list>
		<if test="start !=null">
			limit ${r"#{start}"}  , ${r"#{rows}"} 
		</if>
	</select>
	
	<!-- 根据查询条件，计算查询总数，和分页查询配合使用-->
	<select id="count" resultType="java.lang.Integer">
		select count(1) total from ${data.tableName} t 
		where  <include refid="tb${data.className}Where" />
	</select>
	
	<!-- 选择性插入，根据数据是否存在插入到数据表 -->
	<insert id="insert" parameterType="${data.module.packageName}.model.${data.className}" >
	<#if (data.autoIncrement == "1" )>
		<selectKey resultType="java.lang.Long" keyProperty="${data.pkName}" order="BEFORE">
		    select seq_${data.tableName}.nextval from dual
		 </selectKey>
	</#if> 
		insert into ${data.tableName} 
		<trim prefix="(" suffix=")" suffixOverrides="," >
			<#list data.tcList as ctData>
			<#if (
			ctData.fieldName != "updateTime"
			)>
			<if test="${ctData.fieldName} != null">
				${ctData.colName},
			</if>
			</#if> 
		</#list>
	
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides="," >
		<#list data.tcList as ctData>
		<#if (
			ctData.fieldName != "updateTime"
			)>
			<#if (ctData.colType == "date"  )>
			<if test="${ctData.fieldName} != null">
				to_timestamp(${r"#{"}${ctData.fieldName}${r"}"} ,'YYYY-MM-DD hh24:mi:ss'),
			</if>
				<#else>
			<if test="${ctData.fieldName} != null">
				${r"#{"}${ctData.fieldName}${r"}"},
			</if>
			</#if> 
		</#if> 
		</#list>
		</trim>
	</insert>
	
	<!--
	 批量插入，一般较少用 
	<insert id="insertBylist">
		insert into ${data.tableName} (
		${data.pkCol}<#list data.tcList as ctData><#if (ctData.fieldName != data.pkName)>,${ctData.colName}</#if> </#list>
		) values
		<foreach collection="list" index="index" item="item" separator=",">
		(
		${r"#{item."}${data.pkName}${r"}"}<#list data.tcList as ctData><#if (ctData.fieldName != data.pkName)>,${r"#{item."}${ctData.fieldName}${r"}"}</#if> </#list>
		)
		</foreach>
	</insert>
	-->
	
	<!-- 选择性修改，修改数据时，没必要修改的就不要不传过来  -->
	<update id="update" parameterType="${data.module.packageName}.model.${data.className}">
		update ${data.tableName} 
		<set>
		<#list data.tcList as ctData>
	<#if (
	ctData.fieldName != data.pkName&&
	ctData.fieldName != "createTime"&&
	ctData.fieldName != "delFlag"
	)>
	<#if (ctData.colType == "date" )>
		<if test="${ctData.fieldName} != null">
			${ctData.colName} = to_timestamp(${r"#{"}${ctData.fieldName}${r"}"} ,'YYYY-MM-DD hh24:mi:ss'),
		</if>
			<#else>
		<if test="${ctData.fieldName} != null">
			 ${ctData.colName} = ${r"#{"}${ctData.fieldName}${r"}"},
		</if>
		</#if> 
		</#if> 
	</#list>

		</set>
		where ${data.pkCol} = ${r"#{"}${data.pkName}${r"}"}  <#if data.hasVersion == "1"> and version=${r"#{oldVersion}"} </#if>   <#if data.hasDel == "1"> and del_flag = 0</#if>  
	</update>
	
	<#if data.hasDel == "1"> 
	<!-- 逻辑删除，删除数据并记录删除数据和操作者  批量-->
	<update id="deleteByList">
		update ${data.tableName} 
		<set>
		<if test="map!= null">
			<if test="map.updateTime != null">
				 update_time = ${r"#{map.updateTime}"},
			</if>
			<if test="map.opId != null">
				 op_id = ${r"#{map.opId}"},
			</if>
		</if>
			del_flag = 1
		</set>
		
		where del_flag=0 and ${data.pkCol} in
	    <foreach collection="list" index="index" item="item" open="(" separator="," close=")">
	   ${r"#{item}"} 
	   </foreach>
	</update>
	
	<!-- 逻辑删除，删除数据并记录删除数据和操作者  单个-->
	<update id="deleteByLogicId">
		update ${data.tableName}  set del_flag=1, update_time = ${r"#{updateTime}"}, op_id = ${r"#{opId}"}
		 where  ${data.pkCol} = ${r"#{pk}"}
	</update>
	
</#if>
	
	<#if data.hasDel == "0"> 
	<!-- 物理删除，不可恢复，批量  -->
	<delete id="deleteByListKeys">
		delete  from ${data.tableName}  where ${data.pkCol} in
		<foreach collection="list" index="index" item="item" open="(" separator="," close=")">
			${r"#{item}"} 
		</foreach>
	</delete>
	
	<!-- 物理删除，不可恢复，单个  -->
	<delete id="deleteById" parameterType="java.lang.Object">
		delete  from ${data.tableName}  where ${data.pkCol} = ${r"#{id}"} 
	</delete>
	
</#if>


</mapper>
