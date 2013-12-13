package com.threepillarglobal.labs.cdps.service.impl;

import java.util.List;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.service.api.UserService;

import org.springframework.stereotype.Service;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
