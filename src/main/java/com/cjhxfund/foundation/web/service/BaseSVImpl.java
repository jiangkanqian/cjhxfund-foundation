package com.cjhxfund.foundation.web.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.cjhxfund.foundation.util.data.DataUtil;
import com.cjhxfund.foundation.util.str.DateUtil;
import com.cjhxfund.foundation.web.mapper.IBaseMapper;
import com.cjhxfund.foundation.web.model.BaseModel;

public class BaseSVImpl<T extends BaseModel>  implements IBaseSV<T> {


	private IBaseMapper<T> baseMapper;
	
	public void setMapper(IBaseMapper<T> mapper) {
		this.baseMapper = mapper;
	}
	
	@Override
	public void initMapper() {
		//启动时会执行该方法，主要用来指定mapper对象
	}

	
	public BaseSVImpl() {
	}
	
	private IBaseMapper<T> getMapper() {
		return baseMapper;
	}

	@Override
	public int insert(T entity, Map<String, Object> condition) {
		if (DataUtil.isValidMap(condition)) {
			if (checkRepeat(condition) > 0) {
				return -1;
			}
		}
		return insert(entity);
	}

	@Override
	public int insert(T entity) {
		entity.setOp(0, entity.getOpId());
		return getMapper().insert(entity);
	}

	@Override
	public int update(T entity) {
		entity.setOp(1, entity.getOpId());
		return getMapper().update(entity);
	}

	@Override
	public int update(T entity, Map<String, Object> condition) {
		if (DataUtil.isValidMap(condition) && checkRepeat(condition) > 0) {
			return -1;
		}
		return update(entity);
	}

	@Override
	public int deleteById(Object id) {
		return getMapper().deleteById(id);

	}

	@Override
	public int deleteByListKeys(List<?> list) {
		if (!DataUtil.isValidList(list)) {
			return 0;
		}
		if (list.size() == 1) {
			return deleteById(list.get(0));
		}
		getMapper().deleteByListKeys(list);
		return list.size();
	}

	@Override
	public T fetch(Object id) {
		return getMapper().fetch(id);
	}

	@Override
	public List<T> findAll() {
		// System.out.println(getMapper().getClass().getName());
		return getMapper().findAll();
	}

	@Override
	public List<T> findList(List<?> list) {
		return getMapper().selectByListKeys(list);
	}

	@Override
	public List<T> query(Map<String, Object> condition) {
		return getMapper().query(condition);
	}

	@Override
	public int count(Map<String, Object> condition) {
		return getMapper().count(condition);
	}

	@Override
	public int checkRepeat(Map<String, Object> condition) {
		return getMapper().checkRepeat(condition);
	}

	@Override
	public int insertList(List<T> entities) {
		if (!DataUtil.isValidList(entities)) {
			return 0;
		}
		if (entities.size() == 1) {
			return insert(entities.get(0));
		}
		if(null != entities.get(0).getOpId()){
			for (T entity : entities) {
				entity.setOp(0, entity.getOpId());
			}
		}
		getMapper().insertList(entities);
		return entities.size();
	}

	@Override
	public int deleteByList(List<?> list, Map<String, Object> map) {
		if (!DataUtil.isValidList(list)) {
			return 0;
		}
		if (list.size() == 1) {
			map.put("pk", list.get(0));
			return deleteByLogicId(map);
		}
		map.put("updatetime", DateUtil.getTimeNum());
		map.put("updatedate", DateUtil.getDateNum());
		return getMapper().deleteByList(list, map);
	}

	@Override
	public int deleteByLogicId(Map<String, Object> condition) {
		condition.put("updatetime", DateUtil.getTimeNum());
		condition.put("updatedate", DateUtil.getDateNum());
		return getMapper().deleteByLogicId(condition);
	}

	@Override
	public int updateOrSave(T t, Object id) {
		if (fetch(id) != null) {
			return update(t);
		} else {
			return insert(t);
		}
	}

	@Override
	public long getSequence() {
		return getMapper().getSequence();
	}

	@Override
	public int insertList(List<T> entities, String pkName, Class<?> typeClass) {
		if (!typeClass.getSimpleName().equals("Long") && !typeClass.getSimpleName().equals("Integer")) {
			return 0;
		}
		String pkMethod = "set" + DataUtil.upperFirestChar(pkName);
		long seq = getSequence();
		try {
			// 主键赋值
			for (T t : entities) {
				Class<? extends BaseModel> clazz = t.getClass();
				Method m = clazz.getMethod(pkMethod, typeClass);
				m.invoke(t, seq);
				seq++;
			}
			// System.out.println(entities);
			insertList(entities);// 批量插入
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		setCurSeq(seq);// 调整序列值
		return entities.size();
	}

	@Override
	public void setCurSeq(long curval) {
		getMapper().setCurSeq(curval);
	}

	@Override
	public int insertListByStep(List<T> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			insert(list.get(i));
		}
		return size;
	}

	@Override
	public void callBack(Map<String, Object> params) {
		
	}

	




//	public String getClassName() {
//		return className;
//	}
//
//	public void setClassName(String className) {
//		this.className = className;
//	}

	

}
