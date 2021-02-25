package com.app.evaluator.repository.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.app.evaluator.repository.UserOperationsRepository;

@Repository
public class UserOperationsRepositoryImpl implements UserOperationsRepository {

	private static final Map<String, Map<Character, Integer>> operatorsFrequencyMapByUserId;
	private static final Map<String, Character> frequentOperatorByUserId;

	static {
		operatorsFrequencyMapByUserId = new ConcurrentHashMap<>();
		frequentOperatorByUserId = new ConcurrentHashMap<>();
	}

	public Map<String, Map<Character, Integer>> getOperatorsFrequencyMapByUserId() {
		return operatorsFrequencyMapByUserId;
	}

	public Map<String, Character> getFrequentOperatorByUserId() {
		return frequentOperatorByUserId;
	}

}
