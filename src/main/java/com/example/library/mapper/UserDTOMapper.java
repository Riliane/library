package com.example.library.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.library.models.LibraryUser;
import com.example.library.models.UserDTO;

@Component
public class UserDTOMapper {

	public UserDTO map(LibraryUser user) {
		UserDTO dto = new UserDTO();
		dto.setId(user.getUserId());
		dto.setUsername(user.getUsername());
		dto.setRole(user.getRole());
		return dto;
	}

	public List<UserDTO> map(List<LibraryUser> users) {
		List<UserDTO> list = new ArrayList<UserDTO>();
		for (LibraryUser user : users) {
			list.add(map(user));
		}
		return list;
	}
}
