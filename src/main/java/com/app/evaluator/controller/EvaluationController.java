package com.app.evaluator.controller;

import java.util.Objects;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.evaluator.service.EvaluatorService;
import com.app.evaluator.service.UserService;

@RestController
public class EvaluationController {

	private static final Logger logger = LoggerFactory.getLogger(EvaluationController.class);

	private EvaluatorService evaluatorService;
	private UserService userService;

	public EvaluationController(EvaluatorService evaluatorService, UserService userService) {
		this.evaluatorService = evaluatorService;
		this.userService = userService;
	}

	@RequestMapping(value = "/expression-evaluation/{userId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity getExpressionValue(@RequestParam(name = "expression") String expression,
			@PathVariable(name = "userId") String userId) {
		Integer result;
		try {
			result = evaluatorService.evaluateExpression(expression, userId);
		} catch (RuntimeException e) {
			logger.error("Error occured on expression evaluation ", e.getMessage());
			return new ResponseEntity<String>("Expression is invalid", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/expression-evaluation/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getMostUsedOperatorByUser(
			@PathVariable(name = "userId", required = true) String userId) {
		if (userId == null || userId.isEmpty()) {
			return new ResponseEntity<String>("UserId cannot be empty", HttpStatus.BAD_REQUEST);
		}
		Character operator = userService.getMostUsedOperator(userId);
		if (operator == null) {
			return new ResponseEntity<String>("UserId is not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(operator.toString(), HttpStatus.OK);
	}
}
