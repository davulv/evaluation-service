package com.app.evaluator.service;

public interface EvaluatorService {

	public Integer evaluateExpression(String expression, String userId) throws RuntimeException;
}
