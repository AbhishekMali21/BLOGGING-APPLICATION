package com.blog.utils;

import java.util.concurrent.Callable;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiPerformanceUtil {

	private ApiPerformanceUtil() {

	}

	public static void measure(Runnable runnable, String apiName) {
		long startTime = System.currentTimeMillis();
		try {
			runnable.run();
		} finally {
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			log.info(apiName + " took " + elapsedTime + " ms to complete");
		}
	}

	public static <T> T measureUsingCallable(Callable<T> callable, String apiName) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			return callable.call();
		} finally {
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			log.info(apiName + " took " + elapsedTime + " ms to complete");
		}
	}
}
