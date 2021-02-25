package com.app.evaluator.service;

import java.util.Map;

public interface UserService {

	public Character getMostUsedOperator(String userId);

	void updateUserOperationsMap(String userId, Map<Character, Integer> operatorCount);
}
