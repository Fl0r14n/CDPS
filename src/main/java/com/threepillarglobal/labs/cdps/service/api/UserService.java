package com.threepillarglobal.labs.cdps.service.api;

import java.util.List;

import com.threepillarglobal.labs.cdps.domain.User;

public interface UserService {

	List<User> getUsers();
	
	User getUser(String email);
	
}
