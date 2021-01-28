package com.common;

public class StringUtil {
	  public static boolean isEmpty(String paramString)
	  {
	    return (paramString == null) || (paramString.length() == 0);
	  }
	  
	  public static boolean isNotEmpty(String paramString)
	  {
	    return !isEmpty(paramString);
	  }
}
