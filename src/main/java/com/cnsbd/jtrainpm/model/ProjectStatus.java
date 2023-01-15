package com.cnsbd.jtrainpm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProjectStatus {
    public static final Integer PRE = 0;
    public static final Integer STARTED = PRE + 1;
    public static final Integer PAUSED = STARTED + 1;
    public static final Integer ENDED = PAUSED + 1;

    @Id
    @JsonProperty
    private Integer id;

    @Column
    @JsonProperty
    private String title;

    public static ProjectStatus getDefault() {
        ProjectStatus status = new ProjectStatus();
        status.id = PRE;
        status.title = "Pre";
        return status;
    }
}
