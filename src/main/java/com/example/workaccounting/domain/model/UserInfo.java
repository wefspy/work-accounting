package com.example.workaccounting.domain.model;

import com.example.workaccounting.application.dto.RegisterDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_info")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    private User user;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Version
    @Column(name = "version", nullable = false)
    @Setter(AccessLevel.NONE)
    private Long version;

    public UserInfo(User user, RegisterDto registerDto) {
        this.user = user;
        user.setUserInfo(this);
        setFirstName(registerDto.firstName());
        setMiddleName(registerDto.middleName());
        setLastName(registerDto.lastName());
    }

    public String getFullName() {
        return String.format("%s %s %s",
                getLastName(),
                getFirstName(),
                getMiddleName() != null ? getMiddleName() : "").trim();
    }
}
