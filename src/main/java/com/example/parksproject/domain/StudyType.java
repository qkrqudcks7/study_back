package com.example.parksproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyType {

    @Id @GeneratedValue
    @Column(name = "studyType_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    public void addType(Type type) {
        this.type = type;
    }

    public void addStudy(Study study) {
        this.study = study;
    }
}
