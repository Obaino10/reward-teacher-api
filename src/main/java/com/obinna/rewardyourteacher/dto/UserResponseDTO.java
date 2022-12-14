package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.entity.School;
import com.decagon.rewardyourteacher.enums.Gender;
import com.decagon.rewardyourteacher.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponseDTO {

    private Long Id;
    private String firstName;
    private String lastName;
    private String email;

    private Gender gender;

    private String profilePicture;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private School school;
}
