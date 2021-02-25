package com.app.evaluator.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.evaluator.repository.UserOperationsRepository;
import com.app.evaluator.service.impl.UserServiceImpl;

public class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserOperationsRepository userOperationsRepository;
	
	@Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void whenOperatorFrequencyMapIsGiven_updateUserOperationsMap() {
		when(userOperationsRepository.getFrequentOperatorByUserId()).thenReturn(new HashMap<>());
		when(userOperationsRepository.getOperatorsFrequencyMapByUserId()).thenReturn(new ConcurrentHashMap<>());
		Map<Character,Integer> operatorCount = new HashMap<>();
		operatorCount.put('*', 3);
		operatorCount.put('/', 4);
		userService.updateUserOperationsMap("test", operatorCount);
		 verify(userOperationsRepository, times(2)).getOperatorsFrequencyMapByUserId();
		 verify(userOperationsRepository, times(1)).getFrequentOperatorByUserId();
	}

	@Test
	public void whenUserIsFound_ThenReturnMostUsedOperator() {
		when(userOperationsRepository.getFrequentOperatorByUserId()).thenReturn(new HashMap<String, Character>(){{
			put("testUser", '*');
		}});
		
		Assert.assertEquals((Character)'*',userService.getMostUsedOperator("testUser"));
	}
	
	@Test
	public void whenUserIsNotFound_thenReturnUserNotFound() {
		when(userOperationsRepository.getFrequentOperatorByUserId()).thenReturn(new HashMap<>());
		Assert.assertNull(userService.getMostUsedOperator("testUser"));
	}

}
