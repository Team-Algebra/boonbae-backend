package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="users")
public class Users implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="tree_pk", referencedColumnName = "pk")
    private Tree tree;

    @NotNull
    private String id;

    @NotNull
    private String nickname;

    @NotNull
    @Column(name = "passwords")
    private String password;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate createDate = LocalDate.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private UserRole role = UserRole.USER;

    @NotNull
    @Column(name = "eco_point")
    private int ecoPoint = 0;

    @Lob
    @Column(length = 999999999)
    private String introduction;

    public static Users makeUser(String id, String password, String nickname) {
        Users user = new Users();
        user.id = id;
        user.password = password;
        user.nickname = nickname;
        user.tree = new Tree();
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); //user는 하나의 역할만 가질 수 ㅇ
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {  //사용자를 구분하는 username은 id로 함
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //락걸리지 않았나
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //만료?
        return true;
    }

    @Override
    public boolean isEnabled() { //활성화상태인가? - 휴면상태 관리
        return true;
    }
}
