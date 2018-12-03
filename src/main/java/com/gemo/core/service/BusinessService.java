package com.gemo.core.service;

import java.util.List;

import com.gemo.core.mvc.service.CoreService;

/**
 * 对外接口业务方法接口
 * @author  
 * @date 2015年3月11日
 * @version v1.0
 */
@SuppressWarnings("rawtypes")
public interface BusinessService extends CoreService {

	public void handle(String jsonString,List content) throws Exception;
	
}
