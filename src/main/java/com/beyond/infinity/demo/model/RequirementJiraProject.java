package com.beyond.infinity.demo.model;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="requirement_jira_project")
@EntityListeners(AuditingEntityListener.class)
public class RequirementJiraProject {

    private String projectId;
    private String projectUrl;
    private List<RequirementJiraEpics> epics;

    @Id
    @Column(name="project_id")
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Column(name="project_url")
    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="project_id",referencedColumnName = "project_id")
    public List<RequirementJiraEpics> getEpics() {
        return epics;
    }

    public void setEpics(List<RequirementJiraEpics> epics) {
        this.epics = epics;
    }
}
