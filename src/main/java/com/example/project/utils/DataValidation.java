package com.example.project.utils;




import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.GroupNotFoundException;
import com.example.project.exceptions.SomethingWrongException;
import com.example.project.exceptions.validation.*;
import com.example.project.model.GroupRoomDTO;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Component
public class DataValidation {

    private final UserRepository userRepository ;
    private final GroupRepository groupRepository;
    private final Cities cities=new Cities();

    public  void emailCreate(String email){
        if(email==null || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",email)){
            throw new BadEmailException("Wrong email structure");
        }
        if (userRepository.existsByEmail(email)) {
                throw new EmailAlreadyTakenException("email is already connected to account");
        }
    }
    public  void emailUpdate(String email, User user){
        if(email==null || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",email)){
            throw new BadEmailException("Wrong email structure");
        }

        if(!Objects.equals(user.getEmail(), email)){emailCreate(email);}
    }
    public  void emailLogin(String email){
        if(email==null || !Pattern.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",email)){
            throw new BadEmailException("Wrong email structure");
        }
    }
    public  void usernameCreate(String username){
        //if(username==null || !Pattern.matches("^[a-zA-Z0-9]{3,30}$",username)){
        if(username==null || !Pattern.matches("^\\w{3,30}$",username)){//min 3, max 30 znakow, tylko takie: [A-Za-z0-9_]
            throw new BadUsernameException("Wrong username structure");
        }
        if (userRepository.findByUsername(username).isPresent()) {
                throw new UsernameAlreadyTakenException("Username already taken");
        }
    }
    public  void usernameUpdate(String username,User user){
        //if(username==null || !Pattern.matches("^[a-zA-Z0-9]{3,30}$",username)){
        if(username==null || !Pattern.matches("^\\w{3,30}$",username)){//min 3, max 30 znakow, tylko takie: [A-Za-z0-9_]
            throw new BadUsernameException("Wrong username structure");
        }
        if(!Objects.equals(user.getUsername(), username)){usernameCreate(username);}
    }
    public  void usernameLogin(String username){
        //if(username==null || !Pattern.matches("^[a-zA-Z0-9]{3,30}$",username)){
        if(username==null || !Pattern.matches("^\\w{3,30}$",username)){//min 3, max 30 znakow, tylko takie: [A-Za-z0-9_]
            throw new BadUsernameException("Wrong username structure");
        }
    }
    public  void password(String pswd){
        //if(pswd==null || !Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$",pswd)){//min 1 maly znak, min 1 duzy znak, min 1 cyfra, bez spacji, od 8 do 20 znakow
        if(pswd==null || !Pattern.matches("^\\w{3,}$",pswd)){// walidacja dla testow
            throw new BadPasswordException("Wrong password structure");
        }
    }
    public  void profileName(String name){
        if(name!=null){
            if (!Pattern.matches("^[a-zA-Ząśćźżłóęń]{2,20}$", name)) {
                throw new BadProfileNameException("Name must required at least 2 characters");
            }
        }
    }
    public  void groupName(String name, GroupRoom group){
        if (name==null || !Pattern.matches("^[a-zA-Z0-9.,ąśćźżłóęń\\s]{3,30}$", name)) {//tylko:[ a-z A-Z 0-9 . , spacja]
            throw new BadGroupNameException("Name must required at least 2 characters");
        }
        //sprawdzenie czy nazwa jest unikalna
        /*if(!Objects.equals(group.getName(), name)){
            if (groupRepository.findByName(name).isPresent()) {
                throw new GroupnameAlreadyTakenException("Group name already exists");
            }
        }*/
    }
    public  void age(Integer age){
        if(age!=null){
            if (age <= 0 || age > 150) {
                throw new BadAgeException("Age must be in range from 1 to 150");
            }
        }
    }
    public  void phone(Integer phone){
        if(phone!=null){
            String phoneString = phone.toString();
            if (!Pattern.matches("^\\d{9}$", phoneString)) {//check if number has 9 digits
                throw new BadPhoneException("Phone number must have 9 digits");
            }
        }
    }
    public  void city(String city){
        if(city!=null){
            String[] citiesList = cities.getCities();
            if (!Arrays.stream(citiesList).anyMatch(city::equals)) {
                throw new BadCityException("City not known");
            }
        }
    }
    public  void profileInfo(String text){
        if(text!=null){
            if (!Pattern.matches("^[a-zA-Z0-9.,ąśćźżłóęń\\s]{3,150}$", text)) {
                throw new BadProfileInfoException("Profile info must be in range from 1 to 150 characters");
            }
        }
    }
    public  void groupDesc(String text){
        if (!Pattern.matches("^[a-zA-Z0-9.,ąśćźżłóęń\\s]{3,150}$", text)) {
            throw new BadGroupDescException("Group description must be in range from 1 to 150 characters");
        }
    }
    public  void userLimitCreate(Integer users, GroupRoomDTO groupRoomDTO){
        if(groupRoomDTO.isInGameRolesActive()){
            if(users!=groupRoomDTO.getCategory().getBasicMaxUsers()){
                throw new SomethingWrongException("MaxUsers not same as category");
            }
        }else{
            if (users<2||users>10) {
                throw new BadUserLimitException("Max users must be in range from 2 to 10");
            }
        }
    }
    public  void userLimitUpdate(Integer users, GroupRoom groupRoom){
        if(groupRoom.isInGameRolesActive()){
            if(users!=groupRoom.getCategory().getBasicMaxUsers()){
                throw new SomethingWrongException("MaxUsers not same as category");
            }
        }else{
            if(groupRoom.getUsers().size()>users || users>10){
                throw new TooLowUserLimitException("MaxUsers must be above user count in group ");
            }
        }
    }


}
