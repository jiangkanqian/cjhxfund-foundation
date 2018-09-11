package com.cjhxfund.foundation.web.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cjhxfund.foundation.web.model.BaseModel;

/**
 * 
 * 数据库基本操作
 * @author Jason
 * @date 2016年3月11日
 */
public interface IBaseMapper<T extends BaseModel> {

	/**
	 * 插入数据，直接传对象，如果对象有为空的值，则不会插入
	 */
	int insert(T entity);

	/**
	 * 数据批量插入，传入list实体对象
	 */
	int insertList(List<T> entities);
	
	/**检查是否有重复的数据提交*/
	int checkRepeat(Map<String,Object> condition);
	
	void setCurSeq(long num);

	/**
	 * 修改实体对象
	 */
	int update(T entity);


	/**
	 * 物理删除，根据主键id删除
	 */
	int deleteById(Object id);


	/**
	 * 物理删除，根据主键集合删除
	 */
	int deleteByListKeys(List<?> list);

	/**
	 * 根据主键查询，返回对象
	 */
	T fetch(Object id);

	/**
	 * 根据主键集合查询，返回对象集合
	 */
	List<T> selectByListKeys(List<?> list);

	/**
	 * 查询所有非删除数据 ，返回对象集合
	 */
	List<T> findAll();

	/**
	 * 条件查询，返回对象集合
	 */
	List<T> query(Map<String, Object> condition);
	

	/**
	 * 条件查询，返回实际数量
	 */
	int count(Map<String, Object> condition);
	
	/**获取oracle 当前表 的序列号*/
	long getSequence();


	/**
	 * 逻辑删除，批量逻辑删除
	 */
	int deleteByList(@Param("list")List<?> list,@Param("map")Map<String, Object> map);
	/**逻辑删除*/
	int deleteByLogicId(Map<String, Object> condition);// 逻辑删除
//
//	int deleteByLogicList(List<T> data);// 逻辑删除
//	
//	
//	/**
//	 * 按主键，恢复逻辑删除
//	 */
//	int rebackByLogicId(Map<String, Object> condition);// 逻辑删除


}
