package com.main.service.platform.impl;

import com.main.dao.platform.CommonDao;
import com.main.service.platform.RunSQLFileService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RunSQLFileServiceImpl implements RunSQLFileService{
	@Resource
	private CommonDao commonDao;

	/**
	 * 读取文件内容到SQL中执行
	 * @param sqlPath SQL文件的路径：如：D:/TestProject/web/sql/脚本.Sql
	 */
	@Override
	public void runSqlByReadFileContent(String sqlPath) throws Exception {
		try {

			List<String> sqls = readFileByLines(sqlPath);
			if (sqls.size() > 0) {
				commonDao.transactionUpdate(sqls);
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("初始化Quartz数据库脚本出错！"+e.getMessage());
			throw e;
		}
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	private List<String> readFileByLines(String filePath) throws Exception {
		StringBuffer str = new StringBuffer();
		List<String> result = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), "UTF-8"));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// System.out.println("line " + line + ": " + tempString);
				if(tempString.indexOf("GO")!=-1) {
					result.add(str.toString());
					System.out.println("获得的文本：" + str.toString());
					str.setLength(0);
				}else {
					str = str.append(" " + tempString);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return result;
	}
}
