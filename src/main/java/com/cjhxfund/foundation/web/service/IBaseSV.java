package com.cjhxfund.foundation.web.service;

import java.util.List;
import java.util.Map;

import com.cjhxfund.foundation.web.model.BaseModel;

/**
 * 
 * 数据库基本操作
 * @author Jason
 * @date 2016年3月11日
 */
public interface IBaseSV<T extends BaseModel> {

	void initMapper();
	
	/**
	 * 插入数据，直接传对象，如果对象有为空的值，则不会插入
	 */
	int insert(T entity);

	/**
	 * 数据批量插入，传入list实体对象
	 */
	int insertList(List<T> entities);
	
	/**
	 * 数据批量插入，传入list实体对象
	 * 但这种模式，仅仅是加一个for循环，单个插入；这样可以在一定程度上防止并发所造成的序列不一致
	 */
	int insertListByStep(List<T> entities);
	
	/**
	 * <p>批量插入，自动关联序列插入，主要是通过反射获取实体对象的主键赋值set方法</p>
	 * <p>然后调用序列和调整序列，需要提供调整序列的存储过程</p>
	 * <p>注意：只适用于有序列增长的表，且序列名符合设计规范：seq_tableName；在数据并发的情况不宜使用；</p>
	 * @param entities 实体类型
	 * @param pkName 主键字段属性，如：pkName;
	 * @param typeClazz 主键类型, 只能是Integer和Long类型，否则自动返回0；
	 * @return
	 */
	int insertList(List<T> entities, String pkName, Class<?> typeClazz);
	
	/**
	 * <p>检查是否有重复的数据提交</p>
	 * <p>数据是否重复提交，可以根据表的设计来选择相关字段，类似在程序上给表做联合主键</p>
	 * @return 0:没有，>0则说明有；
	 * */
	int checkRepeat(Map<String,Object> condition);

	/**
	 * 插入前会验证数据是否重复
	 * @param condition 判断条件，如果为null则无需判断是否重复，如果有值则说明要判断
	 */
	int insert(T entity, Map<String,Object> condition);
	/**
	 * 修改实体对象
	 */
	int update(T entity);
	
	/**设置序列当前值*/
	void setCurSeq(long curval);
	
	/**
	 * 修改前会验证数据是否重复
	 * @param condition 判断条件，如果为null则无需判断是否重复，如果有值则说明要判断
	 */
	int update(T entity,Map<String,Object> condition);

	/**
	 * 物理删除，根据主键id删除
	 */
	int deleteById(Object id);


	/**
	 * 根据主键集合删除
	 */
	int deleteByListKeys(List<?> list);

	/**
	 * 根据主键查询，返回对象
	 */
	T fetch(Object id);
	
	/**获取oracle 当前表 的序列号*/
	long getSequence();

	/**
	 * 根据主键集合查询，返回对象集合
	 */
	List<T> findList(List<?> list);

	/**
	 * 查询所有非删除数据 ，返回对象集合
	 */
	List<T> findAll();

	/**
	 * 分页条件查询，返回对象集合
	 */
	List<T> query(Map<String, Object> condition);
	

	/**
	 * 条件查询，返回实际数量
	 */

	int count(Map<String, Object> condition);


	int updateOrSave(T t, Object id);

	/**
	 * 批量逻辑删除, 如果只有一个，则会通过Map去删除，key:pk,对应一个value
	 */
	int deleteByList(List<?> list,Map<String, Object> map);
	
	int deleteByLogicId(Map<String, Object> condition);// 逻辑删除
	
	void callBack(Map<String,Object> params);
	
//
//	int deleteByLogicList(List<T> data);// 逻辑删除
//	
//	
//	/**
//	 * 按主键，恢复逻辑删除
//	 */
//	int rebackByLogicId(Map<String, Object> condition);// 逻辑删除

}
