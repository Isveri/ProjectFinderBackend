package com.example.project.domain;

import com.example.project.chat.model.Message;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@EqualsAndHashCode(of = {"id","username","email"})
@Where(clause = "enabled=true")
public class User implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private String name;

    private String info;

    private int age;

    private int phone;

    private String city;

    private String profileImgName;

    private String reason;

    private String bannedBy;

    @ManyToMany
    @JoinTable(
            name = "users_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<GroupRoom> groupRooms = new ArrayList<>();

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="role_id")
    private Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "users_ingamerole",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "ingamerole_id"))
    private List<InGameRole> inGameRoles;

    @ManyToMany
    @JoinTable(
            name = "users_platforms",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "platform"))
    private List<Platform> platforms;

    @OneToMany(mappedBy="reportedUser")
    private List<Report> reports;

    @ManyToMany
    @JoinTable(
            name="users_friends",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="friend_id")
    )
    private List<Friend> friendList;


    public String roleToString(){
        return this.role.getName();
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = this.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));

        return authorities;
    }



    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    private Boolean enabled = true;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }


}
