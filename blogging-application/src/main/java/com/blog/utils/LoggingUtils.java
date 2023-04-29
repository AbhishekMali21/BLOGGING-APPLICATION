package com.blog.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingUtils {

	public static void logMethodStart() {
		String methodName = getMethodName();
		log.info(methodName + " Starts here");
	}

	public static void logMethodEnd() {
		String methodName = getMethodName();
		log.info(methodName + " Ends here");
	}

	public static String getMethodName() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = stackTrace[3];
		String methodName = stackTraceElement.getMethodName() + "()";
		String className = stackTraceElement.getClassName();
		return className + " - " + methodName;
	}
}
