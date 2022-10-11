package com.decagon.rewardyourteacher.dto;

import com.decagon.rewardyourteacher.enums.Gender;
import com.decagon.rewardyourteacher.enums.Status;
import lombok.*;

import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    @Size(min = 5)
    private String email;
    @NonNull
    @Size(min = 3)
    private String password;
    private String phoneNumber;
    private Gender gender;
    private String profilePicture;
    private String position;
    private Status status;
    private String yearOfService;
    private long schoolId;


}
