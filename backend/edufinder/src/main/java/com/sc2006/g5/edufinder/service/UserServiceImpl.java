package com.sc2006.g5.edufinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserAlreadySaveSchoolException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserNotSaveSchoolException;
import com.sc2006.g5.edufinder.model.school.DbSchool;
import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.model.user.UserSavedSchool;
import com.sc2006.g5.edufinder.model.user.UserSavedSchoolId;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;
import com.sc2006.g5.edufinder.repository.UserSavedSchoolRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DbSchoolRepository dbSchoolRepository; 
    private final UserSavedSchoolRepository userSavedSchoolRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, DbSchoolRepository dbSchoolRepository, UserSavedSchoolRepository userSavedSchoolRepository){
        this.userRepository = userRepository;
        this.dbSchoolRepository = dbSchoolRepository;
        this.userSavedSchoolRepository = userSavedSchoolRepository;
    }

    @Override
    public SavedSchoolResponse getSavedSchoolIds(Long userId) {
        if(!userRepository.existsById(userId)) throw new UserNotFoundException(userId);

        List<Long> schoolIds = userSavedSchoolRepository.findSchoolIdsByUserId(userId);

        return SavedSchoolResponse.builder()
            .savedSchoolIds(schoolIds)
            .build();
    }

    @Override
    public void addSavedSchool(Long userId, SavedSchoolRequest request){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        Long schoolId = request.getSchoolId();
        if(userSavedSchoolRepository.existsByUserIdAndSchoolId(userId, schoolId)){
            throw new UserAlreadySaveSchoolException(userId, schoolId);
        }

        DbSchool school = dbSchoolRepository.findById(schoolId)
            .orElseThrow(() -> new SchoolNotFoundException(schoolId));

        UserSavedSchool savedSchool = UserSavedSchool.builder()
            .id(UserSavedSchoolId.builder().userId(userId).schoolId(schoolId).build())
            .user(user)
            .school(school)
            .build();

        userSavedSchoolRepository.save(savedSchool);
    }

    @Override
    @Transactional
    public void removeSavedSchool(Long userId, SavedSchoolRequest request){        
        if(!userRepository.existsById(userId)) throw new UserNotFoundException(userId);

        Long schoolId = request.getSchoolId();

        if(!userSavedSchoolRepository.existsByUserIdAndSchoolId(userId, schoolId)){
            throw new UserNotSaveSchoolException(userId, schoolId);
        }

        userSavedSchoolRepository.deleteByUserIdAndSchoolId(userId, schoolId);
    }
}
