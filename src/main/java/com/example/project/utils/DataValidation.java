package com.example.project.utils;




import com.example.project.exceptions.validation.*;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;


@RequiredArgsConstructor
@Component
public class DataValidation {

    private final UserRepository userRepository ;
    private final GroupRepository groupRepository;

    public  String email(String email){
        EmailValidator validator = new EmailValidator();
        if(email==null || !validator.isValid(email,null)){
            throw new BadEmailException("Wrong email structure");
        }
        //TODO dodac aby znalesc czy email istnieje w bazie i wyrzucic wyjatek

        return email;
    }
    public  String username(String username){
        if(username==null || !Pattern.matches("^\\w{3,}$",username)){//TODO zmienic patern dla username (narazie jest minimum 3 znaki)
            throw new BadUsernameException("Wrong username structure");
        }
        //TODO dodac aby znalesc czy username istnieje w bazie
        if(userRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyTakenException("Username already taken");
        }
        return username;
    }
    public  String password(String pswd){
        if(pswd==null || !Pattern.matches("^\\w{3,}$",pswd)){//TODO zmienic patern dla hasla (narazie jest minimum 3 znaki)
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
    public  String groupName(String name){
        if (name==null || !Pattern.matches("^\\w{2,}$", name)) {
            throw new BadGroupNameException("Name must required at least 2 characters");
        }

        //TODO sprawdzic czy dana nazwa istnieje, jesli tak wywala wyjatek
        if(groupRepository.findByName(name).isPresent()){
            throw new GroupnameAlreadyTakenException("Group name already exists");
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
       /* if (age!=null && (age<=0||age>150)) {
            throw new BadPhoneException("Age must be in range from 1 to 150");
        }*/
        //TODO int na string i sprawdzic numer paternem rozszerzonym (rozne wariacje numeru )
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
    public  Integer userLimit(Integer users){
        if (users<=0||users>150) {
            throw new BadUserLimitException("Max users be in range from 1 to 5");
        }
        return users;
    }
}
