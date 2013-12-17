package com.threepillarglobal.labs.cdps.service.impl;

import java.util.List;

import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.service.api.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {
    
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public List<User> getUsers() {
		return userRepo.findAllUser();
	}

	@Override
	public User getUser(String email) {
		return userRepo.findUser(email);
	}
    
}
