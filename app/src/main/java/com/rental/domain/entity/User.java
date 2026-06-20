package com.rental.domain.entity;

public class User {

    public enum Role {
        ADMIN, STAFF, OWNER
    }

    private String username;
    private String password;
    private Role   role;

    public User() {}

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }
    public String getUsername(){ 
        return username; 
    }
    public void   setUsername(String u){ 
        this.username = u; 
    }
    public String getPassword(){ 
        return password; 
    }
    public void   setPassword(String p){ 
        this.password = p; 
    }
    public Role   getRole(){ 
        return role; 
    }
    public void   setRole(Role r){ 
        this.role = r; 
    }
}
