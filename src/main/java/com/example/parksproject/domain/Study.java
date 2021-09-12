package com.example.parksproject.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Study {

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
    private final List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private final List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private final List<StudyCategory> types = new ArrayList<>();

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private String location;

    private int maxMember;

    public void addManager(Manager manager) {
        this.managers.add(manager);
    }

    public void addMember(Member member) {
        this.members.add(member);
    }

    public void addStudyCategory(List<StudyCategory> studyCategory) {
        this.types.addAll(studyCategory);
    }

    public List<String> getMembers() {
        return members.stream().map(member -> member.getUser().getName()).collect(Collectors.toList());
    }
    public List<Long> getMembersId() {
        return managers.stream().map(manager -> manager.getUser().getId()).collect(Collectors.toList());
    }

    public List<String> getManagers() {
        return managers.stream().map(manager -> manager.getUser().getName()).collect(Collectors.toList());
    }

    public List<String> getCategorys() {
        return types.stream().map(category -> category.getCategory().getName()).collect(Collectors.toList());
    }

}
