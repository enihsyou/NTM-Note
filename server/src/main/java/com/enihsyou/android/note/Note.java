package com.enihsyou.android.note;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int note_id;

    private int id;

    @ManyToOne
    @JsonBackReference
    private User user;

    private String label;

    private String content;

    private String alarm;

    private String status;

    private String createdTime;

    private String lastModifiedTime;
}

