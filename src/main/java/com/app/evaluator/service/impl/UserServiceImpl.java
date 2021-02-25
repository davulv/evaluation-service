package com.app.evaluator.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.evaluator.repository.UserOperationsRepository;
import com.app.evaluator.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserOperationsRepository userOperationsRepository;

	/**
	 * Retrieves the most frequently used operator for a given userId
	 * 
	 * @param userId
	 * @return operator
	 */
	@Override
	public Character getMostUsedOperator(String userId) {
		return userOperationsRepository.getFrequentOperatorByUserId().get(userId);
	}

	/**
	 * Updates the maps OperatorsFrequencyMapByUserId and FrequentOperatorByUserId for a
	 * user based on expression parsed
	 * 
	 * 
	 * @param userId
	 * @param operatorCount
	 * 
	 */
	@Override
	public void updateUserOperationsMap(String userId, Map<Character, Integer> operatorCount) {
		Map<Character, Integer> existingCount = userOperationsRepository.getOperatorsFrequencyMapByUserId()
				.getOrDefault(userId, new HashMap<>());
		operatorCount.forEach(
				(operator, freq) -> existingCount.put(operator, existingCount.getOrDefault(operator, 0) + freq));
		userOperationsRepository.getOperatorsFrequencyMapByUserId().put(userId, existingCount);
		Optional<Entry<Character, Integer>> mostUsedOperator = existingCount.entrySet().stream()
				.max(Entry.comparingByValue());
		logger.info("Updating most used operator to " + mostUsedOperator.get().getKey() + " for user " + userId);
		userOperationsRepository.getFrequentOperatorByUserId().put(userId, mostUsedOperator.get().getKey());
	}
}
