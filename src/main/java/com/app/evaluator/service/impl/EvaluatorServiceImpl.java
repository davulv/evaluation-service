package com.app.evaluator.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.app.evaluator.service.EvaluatorService;
import com.app.evaluator.service.UserService;

@Service
public class EvaluatorServiceImpl implements EvaluatorService {

	private static final Logger logger = LoggerFactory.getLogger(EvaluatorServiceImpl.class);
	private static final Set<Character> setOfOperators = new HashSet<>(Arrays.asList('^', '*', '+', '-', '/'));

	private UserService userService;

	public EvaluatorServiceImpl(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Evaluates a mathematical expression and stores the operator frequencies for
	 * each user
	 * 
	 * @return Integer
	 * @param expression
	 * @param userId
	 * @throws RuntimeException when the expression is invalid
	 */
	@Override
	public Integer evaluateExpression(String expression, String userId) throws RuntimeException {
		logger.info("Processing the expression " + expression + " for user " + userId);
		Map<Character, Integer> operatorCount = new HashMap<Character, Integer>();
		Stack<Integer> operands = new Stack<>();
		Stack<Character> operations = new Stack<>();
		for (int i = 0; i < expression.length(); i++) {
			char currentChar = expression.charAt(i);
			if (Character.isDigit(currentChar)) {
				int number = 0;
				while (Character.isWhitespace(currentChar) || Character.isDigit(currentChar)) {
					if(Character.isDigit(currentChar)) {
						number = number * 10 + (currentChar - '0');
					}
					i++;
					if (i < expression.length())
						currentChar = expression.charAt(i);
					else
						break;
				}
				i--;
				// push it into stack
				operands.push(number);
			} else if (currentChar == '(') {
				operations.push(currentChar);
			} else if (setOfOperators.contains(currentChar)) {
				operatorCount.put(currentChar, operatorCount.getOrDefault(currentChar, 0) + 1);

				// 1. If current operator has higher precedence than operator on top of the
				// stack,
				// the current operator can be placed in stack
				// 2. else keep popping operator from stack and perform the operation in numbers
				// stack till
				// either stack is not empty or current operator has higher precedence than
				// operator on top of the stack
				while (!operations.isEmpty() && precedence(currentChar) <= precedence(operations.peek())) {
					int output = performOperation(operands, operations);
					// push it back to stack
					operands.push(output);
				}
				// now push the current operator to stack
				operations.push(currentChar);
			} else if (currentChar == ')') {
				while (!operations.isEmpty() && operations.peek() != '(') {
					int output = performOperation(operands, operations);
					// push it back to stack
					operands.push(output);
				}
				operations.pop();
			}
		}

		// if the expression becomes invalid due to an extra
		// open parenthesis, it will be left out in the stack.
		// An error is thrown by performOperation in such case
		while (!operations.isEmpty()) {
			int output = performOperation(operands, operations);
			// push it back to stack
			operands.push(output);
		}
		logger.info("Updating user operations map for user " + userId);
		userService.updateUserOperationsMap(userId, operatorCount);
		return operands.pop();
	}

	private int precedence(char operator) {
		switch (operator) {
		case '+':
		case '-':
			return 1;
		case '*':
		case '/':
			return 2;
		case '^':
			return 3;
		}
		return -1;
	}

	/**
	 * Evaluates value for the top elements on the numbers stack and operations
	 * stack
	 * 
	 * @return Integer
	 * @param numbers
	 * @param operations
	 * @throws RuntimeException when the operator is invalid
	 */
	private int performOperation(Stack<Integer> numbers, Stack<Character> operations) throws RuntimeException {

		// Expression becomes invalid if there are extra operators
		// like 5*1*
		if (numbers.size() < 2 && operations.size() > 0) {
			logger.error("Given expression is invalid  because of extra operators");
			throw new RuntimeException("Expression is invalid");
		}

		int firstOperand = numbers.pop();
		int secondOperand = numbers.pop();
		char operation = operations.pop();
		switch (operation) {
		case '+':
			return firstOperand + secondOperand;
		case '-':
			return secondOperand - firstOperand;
		case '*':
			return firstOperand * secondOperand;
		case '/':
			if (firstOperand == 0)
				throw new UnsupportedOperationException("Cannot divide by zero");
			return secondOperand / firstOperand;
		case '^':
			return (int) Math.pow(secondOperand, firstOperand);
		default:
			logger.error("Given expression is invalid");
			throw new RuntimeException("Expression is invalid");
		}
	}

}
