package com.common.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*		String[] array = { "11", "2", "3" };
		//System.out.print(Test.contains(array,"1"));
		List a = new ArrayList();
		a.add(1);
		a.add(3);
		a.add(2);
		a.add(5);
		a.add(6);
		List b = new ArrayList();
		b.add(2);
		b.add(3);
		b.add(9);
		a.removeAll(b);
		System.out.println(a);
		Boolean var1 = true;
		Class<?> classType2 = var1.getClass();
		System.out.println(classType2);*/
		/*String a = "[12,4]";
		String[] huiYuanYouHuiQuanIdArray = a.substring(1,a.length()-1).split(",");
		for(String aa: huiYuanYouHuiQuanIdArray){
			System.out.println(aa);
		}*/
		System.out.print((float)10/100);
    }
	
	 public static boolean contains(String[] stringArray, String source) {
		  // 转换为list
		  List<String> tempList = Arrays.asList(stringArray);
		  // 利用list的包含方法,进行判断
		  if(tempList.contains(source))
		  {
		   return true;
		  } else {
		   return false;
		  }
		 }
}
