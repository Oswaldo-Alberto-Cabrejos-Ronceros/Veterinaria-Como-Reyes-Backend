package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService iUserService;
    private final IEmployeeService iEmployeeService;
    @Autowired
    public UserDetailsServiceImpl(IUserService iUserService, IEmployeeService iEmployeeService) {
        this.iUserService = iUserService;
        this.iEmployeeService = iEmployeeService;
    }

    //arreglar
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = iUserService.getUserByEmail(username);
        List<GrantedAuthority> authorities;
        if(user.getType().equalsIgnoreCase("C")){
            authorities= List.of(new SimpleGrantedAuthority("CLIENT"));
        } else {
            EmployeeDTO employee = iEmployeeService.getEmployeeByUser(UserMapper.maptoUser(user));
            authorities= List.of(new SimpleGrantedAuthority(employee.getRole().getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
