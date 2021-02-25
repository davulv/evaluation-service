package com.app.evaluator.repository;

import java.util.Map;

public interface UserOperationsRepository {

	Map<String, Map<Character, Integer>> getOperatorsFrequencyMapByUserId();

	Map<String, Character> getFrequentOperatorByUserId();

}
