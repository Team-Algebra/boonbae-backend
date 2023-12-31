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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name ="tree_pk", referencedColumnName = "pk")
    @Builder.Default
    private Tree tree = new Tree();

    @Builder.Default
    @Column(name = "image_url", length = 99999)
    private String imageUrl = null;

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
    @Builder.Default
    private LocalDate createDate = LocalDate.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    @Builder.Default
    private UserRole role = UserRole.USER;

    @NotNull
    @Column(name = "eco_point")
    @Builder.Default
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

    public void modifyUserImg(String imgUrl) {
        this.imageUrl = imgUrl;
    }

    public void changeIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void changeNickname(String newName) {
        this.nickname = newName;
    }

    public void changePassword(String password) {
        this.password= password;
    }

    //추천인 포인트
    public void addReferralPoint(int addPoint) {
        this.ecoPoint += addPoint;
    }

    //분리수거 인증 포인트
    public void addRecyclePoint(int addPoint) {
        this.ecoPoint += addPoint;
    }

    //포인트 충전
    public void chargePoint(int amount) {
        this.ecoPoint += amount;
    }

    // 토닉에서 포인트 사용 가능 여부
    public boolean isExceedPoint(int amount, int addPoint) {
        // 여기서 addPoint 는 음수
        return this.ecoPoint + addPoint * amount < 0;
    }

    // 토닉 사용
    public void useTonic(int amount, int addPoint) {
        this.ecoPoint += addPoint * amount;
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
    public void setAdmin(){
        this.role=UserRole.ADMIN;
    }
}
