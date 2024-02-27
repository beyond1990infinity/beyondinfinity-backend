package com.beyond.infinity.demo.controller;

import com.beyond.infinity.demo.config.ConfigProperties;
import com.beyond.infinity.demo.model.DuplicateRequirements;
import com.beyond.infinity.demo.model.RequirementJiraEpics;
import com.beyond.infinity.demo.model.RequirementJiraProject;
import com.beyond.infinity.demo.repository.RequirementDetailsRepository;
import com.beyond.infinity.demo.repository.RequirementDuplicateRepository;
import com.beyond.infinity.demo.request.RequirementDuplicate;
import com.beyond.infinity.demo.response.Epic;
import com.beyond.infinity.demo.response.JiraEpicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class JiraRequirementController {

    @Autowired
    ConfigProperties properties;

    @Autowired
    private RequirementDetailsRepository requirementDetailsRepository;

    @Autowired
    private RequirementDuplicateRepository requirementDuplicateRepository;

    private RequirementJiraProject requirementJiraProject=new RequirementJiraProject();
    private RequirementJiraEpics requirementJiraEpics=new RequirementJiraEpics();

    @RequestMapping(path="/jira/epic/{project}",method =RequestMethod.GET)
    public JiraEpicResponse getJiraEpicByProject(@RequestHeader("Authorization") final String cred, @PathVariable("project") String project) throws IOException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        RestTemplate restTemplate=new RestTemplate();

        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization",cred);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        String serverUrl=null;

        String host=this.properties.gitHub().getHost();
        String endpoint=this.properties.gitHub().getGetdata_endpoint();

        HttpEntity<String> requestEntity=new HttpEntity<>(headers);

        serverUrl=host+endpoint+project;

        ResponseEntity<JiraEpicResponse> response=restTemplate.exchange(serverUrl, HttpMethod.GET,requestEntity,new ParameterizedTypeReference<JiraEpicResponse>(){});
        JiraEpicResponse jiraResponse=response.getBody();
        List<Epic> epics=jiraResponse.getEpicList();
        List<RequirementJiraEpics> epicList=new ArrayList<>();


        for(int i=0;i<epics.size();i++){
            if(epics.get(i).getEpicKey()!=null) {
                requirementJiraEpics.setEpicId(epics.get(i).getEpicKey());
                requirementJiraEpics.setSummary(epics.get(i).getEpicFields().getSummary());
                requirementJiraEpics.setDescription(epics.get(i).getEpicFields().getDescription());
                requirementJiraEpics.setProjectId(project);

                epicList.add(requirementJiraEpics);
            }
            requirementJiraProject.setProjectId(project);
            requirementJiraProject.setProjectUrl(serverUrl);
            requirementJiraProject.setEpics(epicList);

            requirementDetailsRepository.save(requirementJiraProject);
            }
        return response.getBody();
    }


    @RequestMapping(path="/jira/project/{projectId}",method=RequestMethod.PUT)
    public ResponseEntity<RequirementJiraProject> updateJiraProjectWithLMSummary(@RequestHeader("Authorization") final String cred, @PathVariable("projectId") String projectId, @RequestBody RequirementJiraProject reqDetails){
        RequirementJiraProject projectDetails=requirementDetailsRepository.findByProjectIdIgnoreCase(projectId);

        if(projectDetails.getProjectId()!=null) {
            requirementJiraProject.setProjectUrl(reqDetails.getProjectUrl());
            return new ResponseEntity<>(requirementDetailsRepository.save(requirementJiraProject), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(path="/jira/duplicate/project/{projectId}", method=RequestMethod.POST)
    public ResponseEntity<DuplicateRequirements> createDuplicateRequirements(@RequestHeader("Authorization") final String cred, @PathVariable("projectId") String projectID, @RequestBody RequirementDuplicate reqDetails){
        DuplicateRequirements duplicateRequirements=new DuplicateRequirements();

        duplicateRequirements.setProjectId(reqDetails.getProjectId());
        for(int i=0;i<reqDetails.getDuplicateProjectId().size();i++){
            if(reqDetails.getDuplicateProjectId()!=null){
                duplicateRequirements.setDuplicateProjectId(reqDetails.getDuplicateProjectId().get(i));

            }
        }
        return new ResponseEntity<>(requirementDuplicateRepository.save(duplicateRequirements),HttpStatus.OK);
    }
}
