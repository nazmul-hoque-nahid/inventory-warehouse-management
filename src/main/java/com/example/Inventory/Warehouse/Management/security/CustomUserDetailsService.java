package com.example.Inventory.Warehouse.Management.security;


import com.example.Inventory.Warehouse.Management.entity.User;
import com.example.Inventory.Warehouse.Management.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    CustomUserDetailsService(UserRepository repository){
        this.repository=repository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
       UserDetails userDetails=   new CustomUserDetails(user);
          return userDetails;
    }
}
