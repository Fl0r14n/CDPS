package com.threepillarglobal.labs.cdps.service.mock;

import java.util.List;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.service.api.UserService;

import org.springframework.stereotype.Service;

@Service(value = "userServiceMock")
public class UserServiceMock implements UserService {

	@Override
	public List<User> getUsers() {
		return MockDataGenerator.fetchMockUserData(100);
	}
    
}
