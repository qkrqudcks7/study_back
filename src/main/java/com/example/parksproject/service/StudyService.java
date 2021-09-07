package com.example.parksproject.service;

import com.example.parksproject.domain.Manager;
import com.example.parksproject.domain.Member;
import com.example.parksproject.domain.Study;
import com.example.parksproject.domain.User;
import com.example.parksproject.payload.StudyRequest;
import com.example.parksproject.payload.StudyResponse;
import com.example.parksproject.repository.ManagerRepository;
import com.example.parksproject.repository.MemberRepository;
import com.example.parksproject.repository.StudyRepository;
import com.example.parksproject.repository.UserRepository;
import com.example.parksproject.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyService {

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final ManagerRepository managerRepository;
    private final MemberRepository memberRepository;
    private final S3FileUploadService s3FileUploadService;

    public ResponseEntity<?> makeStudy(StudyRequest studyRequest, UserPrincipal userPrincipal, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            studyRequest.setImage(s3FileUploadService.upload(multipartFile));
        }

        User u = userRepository.findById(userPrincipal.getId()).get();

        Manager manager = Manager.builder()
                .user(u).build();

        Study study = Study.builder()
                .path(studyRequest.getPath())
                .title(studyRequest.getTitle())
                .shortDescription(studyRequest.getShortDescription())
                .longDescription(studyRequest.getLongDescription())
                .image(studyRequest.getImage())
                .recruiting(studyRequest.isRecruiting())
                .published(studyRequest.isPublished())
                .closed(studyRequest.isClosed()).build();
        study.addManager(manager);
        manager.addStudy(study);
        studyRepository.save(study);
        return ResponseEntity.ok("스터디 생성 완료");
    }

    public ResponseEntity<?> getOneBoard(Long id) {
        Study study = studyRepository.findById(id).get();
        StudyResponse studyResponse = new StudyResponse(study.getId(), study.getPath(), study.getTitle(), study.getShortDescription(), study.getLongDescription(), study.getImage(), study.getMembers(), study.getManagers(), study.isRecruiting(), study.isPublished(), study.isClosed());

        return new ResponseEntity<>(studyResponse, HttpStatus.OK);
    }
}
