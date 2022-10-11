package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.config.security.AuthenticateService;
import com.decagon.rewardyourteacher.config.security.JwtService;
import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.entity.Notification;
import com.decagon.rewardyourteacher.entity.School;
import com.decagon.rewardyourteacher.exceptions.CustomException;
import com.decagon.rewardyourteacher.repository.NotificationRepository;
import com.decagon.rewardyourteacher.repository.SchoolRepository;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.exceptions.UserNotFoundException;
import com.decagon.rewardyourteacher.repository.UserRepository;
import com.decagon.rewardyourteacher.services.TeacherService;
import com.decagon.rewardyourteacher.utils.EmailValidatorService;
import com.decagon.rewardyourteacher.utils.MapStructMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import static com.decagon.rewardyourteacher.enums.UserRole.TEACHER;

@AllArgsConstructor
@Service
public class TeacherImpl implements TeacherService {

    private final UserRepository teacherRepository;
    private final NotificationServiceImp notificationService;
    private final PasswordEncoder passwordEncoder;
    private final SchoolRepository schoolRepository;
    private final AuthenticateService authenticateService;
    private final AuthenticationManager authenticationManager;
    private final UserImpl userImpl;
    private final JwtService jwtService;


    @Override
    public UserResponseDTO signUp(SignUpDto signUpDto) throws Exception {
        String email = signUpDto.getEmail().toLowerCase();

        if(!EmailValidatorService.isValid(email)){
            throw new CustomException("Enter a valid email address");
        }

        if (Objects.nonNull(teacherRepository.findByEmail(email))) {
            throw new CustomException("Teacher already exist");
        }

        School school = schoolRepository.findById(signUpDto.getSchoolId())
                .orElseThrow(Exception::new);


        User teacher = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .email(email)
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .phoneNumber(signUpDto.getPhoneNumber())
                .status(signUpDto.getStatus())
                .yearOfService(signUpDto.getYearOfService())
                .position(signUpDto.getPosition())
                .gender(signUpDto.getGender())
                .createdAt(LocalDateTime.now())
                .school(school)
                .profilePicture(signUpDto.getProfilePicture())
                .role(TEACHER)
                .build();

        User user = teacherRepository.save(teacher);
        return new ModelMapper().map(user, UserResponseDTO.class);
    }

    @Override
    public LoginResponse teacherLogin(LoginDTO loginDTO) {
        return authenticateService.getLoginResponse(loginDTO, authenticationManager, jwtService, TEACHER);
    }

    @Override
    public UserResponseDTO updateTeacher(Long id, TeacherRequestDTO teacherRequestDTO, String email) {
        return userImpl.updateTeacherResponseEntity(id, teacherRequestDTO, teacherRepository, email);

    }



    @Override
    public Notification teacherAppreciatesStudent(String email, Long studentId, MessageDTO messageDTO) {
        return notificationService.studentAppreciatedNotification(email, studentId, messageDTO);
    }
}
