package com.example.project.utils;




import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.GroupNotFoundException;
import com.example.project.exceptions.validation.*;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Component
public class DataValidation {

    private final UserRepository userRepository ;
    private final GroupRepository groupRepository;

    public  String emailCreate(String email){

        if(email==null || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",email)){
            throw new BadEmailException("Wrong email structure");
        }
        if (userRepository.existsByEmail(email)) {
                throw new EmailAlreadyTakenException("email is already connected to account");
        }
        return email;
    }
    public  String emailUpdate(String email, User user){

        if(email==null || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",email)){
            throw new BadEmailException("Wrong email structure");
        }

        if(!Objects.equals(user.getEmail(), email)){emailCreate(email);}
        return email;
    }
    public  String usernameCreate(String username){
        if(username==null || !Pattern.matches("^\\w{3,}$",username)){//TODO zmienic patern dla username (narazie jest minimum 3 znaki)
            throw new BadUsernameException("Wrong username structure");
        }
        if (userRepository.findByUsername(username).isPresent()) {
                throw new UsernameAlreadyTakenException("Username already taken");
        }
        return username;
    }
    public  String usernameUpdate(String username,User user){
        if(username==null || !Pattern.matches("^\\w{3,}$",username)){//TODO zmienic patern dla username (narazie jest minimum 3 znaki)
            throw new BadUsernameException("Wrong username structure");
        }
        if(!Objects.equals(user.getUsername(), username)){usernameCreate(username);}
        return username;
    }
    public  String password(String pswd){
        if(pswd==null || !Pattern.matches("^\\w{3,}$",pswd)){//TODO ustalic jakie wymagania co do hasla
            throw new BadPasswordException("Wrong password structure");
        }
        return pswd;
    }
    public  String profileName(String name){
        if (!Pattern.matches("^\\w{2,}$", name)) {
            throw new BadProfileNameException("Name must required at least 2 characters");
        }
        return name;
    }
    public  String groupName(String name, GroupRoom group){
        if (name==null || !Pattern.matches("^\\w{2,}$", name)) {
            throw new BadGroupNameException("Name must required at least 2 characters");
        }
        if(!Objects.equals(group.getName(), name)){
            if (groupRepository.findByName(name).isPresent()) {
                throw new GroupnameAlreadyTakenException("Group name already exists");
            }
        }
        return name;
    }
    public  Integer age(Integer age){
        if (age<=0||age>150) {
            throw new BadAgeException("Age must be in range from 1 to 150");
        }
        return age;
    }
    public  Integer phone(Integer phone){
        String phoneString = phone.toString();
        if(!Pattern.matches("^\\d{9}$", phoneString)){//check if number has 9 digits
            throw new BadPhoneException("Phone number must have 9 digits");
        }
        //TODO dodac sprawdzanie dla roznych wersji numeru
        return phone;
    }
    public  String city(String city){
        if (!Pattern.matches("^\\w{2,}$", city)) {
            throw new BadCityException("City name must required at least 2 characters");
        }
        return city;
    }
    public  String profileInfo(String text){
        if (!Pattern.matches("^\\w{2,150}$", text)) {
            throw new BadProfileInfoException("Profile info must be in range from 1 to 150 characters");
        }
        return text;
    }
    public  String groupDesc(String text){
        if (!Pattern.matches("^\\w{2,150}$", text)) {
            throw new BadGroupDescException("Group description must be in range from 1 to 150 characters");
        }
        return text;
    }
    public  Integer userLimitCreate(Integer users){
        if (users<=0||users>5) {
            throw new BadUserLimitException("Max users be in range from 1 to 5");
        }
        return users;
    }
    public  Integer userLimitUpdate(Integer users, GroupRoom group){
        if (users<=0||users>5) {
            throw new BadUserLimitException("Max users be in range from 1 to 5");
        }
        if(group.getUsers().size()>users){

        }
        return users;
    }
}
