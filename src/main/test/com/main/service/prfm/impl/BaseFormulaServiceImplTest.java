package com.main.service.prfm.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseFormulaServiceImplTest {

	@Test
	public void getData() throws Exception {
		/**
		 * assertEquals这个方法是一个断言方法
		 * 第一个参数表示预期的结果
		 * 第二个参数表示程序的执行结果
		 * 当预期结果与执行结果是一致的时候，则表示单元测试成功
		 */
		new BaseFormulaServiceImpl().getData("[我是第一()]+[我是第二()]/[我是第三()]", null);
	}
}