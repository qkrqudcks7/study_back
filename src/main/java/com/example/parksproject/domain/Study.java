package com.example.parksproject.domain;

import com.example.parksproject.payload.StudyRequest;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Study implements Serializable {

    @Id @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    @Column(unique = true)
    private String path;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String longDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private final List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private final List<StudyCategory> types = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private final List<ApplyStudy> applyStudies = new ArrayList<>();

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private String location;

    private int maxMember;

    public void modifyStudy(StudyRequest studyRequest) {
        this.title = studyRequest.getTitle();
        this.shortDescription = studyRequest.getShortDescription();
        this.longDescription = studyRequest.getLongDescription();
        this.published = studyRequest.isPublished();
        this.location = studyRequest.getLocation();
    }

    public void addManager(Manager manager) {
        this.managers.add(manager);
    }

    public void addApplyStudies(ApplyStudy applyStudy) {
        this.applyStudies.add(applyStudy);
    }

    public void addStudyCategory(List<StudyCategory> studyCategory) {
        this.types.addAll(studyCategory);
    }

    public List<String> getApplies() {
        return applyStudies.stream().map(applyStudy -> applyStudy.getUser().getName()).collect(Collectors.toList());
    }

    public List<String> getManagers() {
        return managers.stream().map(manager -> manager.getUser().getName()).collect(Collectors.toList());
    }

    public List<String> getCategorys() {
        return types.stream().map(category -> category.getCategory().getName()).collect(Collectors.toList());
    }

    public Long getManagerId() {
        return managers.get(0).getUser().getId();
    }

    public Study setClosed() {
        this.closed = true;
        return this;
    }
    public Study setOpen() {
        this.closed = false;
        return this;
    }
}
