package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.enums.Gender;
import com.decagon.rewardyourteacher.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TeacherRequestDTO {
    private String firstName;
    private String lastName;
    private String password;
    private Gender gender;
    private String phoneNumber;
    private String profilePicture;
    private String position;
    private Status status;
    private String yearOfService;
}
