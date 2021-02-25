package com.app.evaluator.service;


import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.evaluator.service.impl.EvaluatorServiceImpl;

public class EvaluatorServiceImplTest {

	@InjectMocks
	EvaluatorServiceImpl evaluatorService;
	
	@Mock
	UserService userService;
	
	@Before
	    public void init() {
	        MockitoAnnotations.initMocks(this);
	    }
	private static final Logger logger = LoggerFactory.getLogger(EvaluatorServiceImplTest.class);

	@Test
	public void whenValidExpressionIsGiven_thenResultIsReturned() {
		String expression = "5+3";
		String userId = "test1";
		Assert.assertEquals((int)8,(int) evaluatorService.evaluateExpression(expression, userId));
	}
	
	
	@Test(expected = RuntimeException.class)
	public void whenInValidExpressionIsGiven_thenExceptionIsThrown() { String
	  expression = "5+3*(3-5+(4/2)))"; String userId = "test1";
	  evaluatorService.evaluateExpression(expression, userId);
	}
	 
}
