package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.dto.SchoolResponse;
import com.decagon.rewardyourteacher.entity.School;
import com.decagon.rewardyourteacher.repository.SchoolRepository;
import com.decagon.rewardyourteacher.services.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository  schoolRepository;
    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public List<SchoolResponse> getAllSchools(int pageNumber, int pageSize, String sortProperty) {
        pageNumber = pageNumber <= 0 ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, sortProperty);
        Page<School> schools = schoolRepository.findAll(pageable);
        List<SchoolResponse> schoolResponseList = new ArrayList<>();

        for (School school : schools.getContent()) {
            schoolResponseList.add(new ModelMapper().map(school, SchoolResponse.class));
        }

        return schoolResponseList;
    }

    @Override
    public int getSchoolCount() {
        return schoolRepository.findAll().size();
    }

    @Override
    public String saveSchool(List<School> schoolEntities) {
        schoolRepository.saveAll(schoolEntities);
        return "success";
    }
}
