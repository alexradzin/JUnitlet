package com.junitlet.rule;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.junitlet.rule.LoggingRule.Phase;

public class LoggingRuleTest {
	 @Rule
	 public final LoggingRule<Logger> javaUtilLogger = 
	 	new LoggingRuleBuilder<Logger>().
	 		with(Logger.class).
	 		log(Phase.starting, "info").
	 		log(Phase.succeeded, "info").
	 		log(Phase.failed, "severe").
	 	build();

	private static List<String> logMessages = new ArrayList<>();
	 
	private static Handler javaUtilLoggerHandler = new Handler() {

		@Override
		public void publish(LogRecord record) {
			logMessages.add(record.getMessage());
		}

		@Override
		public void flush() {
		}

		@Override
		public void close() throws SecurityException {
		}

	};
	 
	 @BeforeClass
	 public static void logSetup() {
		 Logger.getLogger(LoggingRuleTest.class.getName()).addHandler(javaUtilLoggerHandler);
	 }
	 
	 @Test
	 public void test1() {
		 assertTrue(true);
	 }

	 @Test
	 public void test2() {
		 assertTrue(true);
	 }
	 
	 @AfterClass
	 public static void logCheck() {
		 Logger.getLogger(LoggingRuleTest.class.getName()).removeHandler(javaUtilLoggerHandler);
		 assertTrue(logMessages.get(0).matches(".*test1.*is starting"));
		 assertTrue(logMessages.get(1).matches(".*test1.*is succeeded"));
		 
		 assertTrue(logMessages.get(2).matches(".*test2.*is starting"));
		 assertTrue(logMessages.get(3).matches(".*test2.*is succeeded"));
	 }
	 
}
