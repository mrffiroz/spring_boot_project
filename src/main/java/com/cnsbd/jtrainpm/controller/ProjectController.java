package com.cnsbd.jtrainpm.controller;

import com.cnsbd.jtrainpm.dto.AddMembersRequest;
import com.cnsbd.jtrainpm.dto.CreateProjectRequest;
import com.cnsbd.jtrainpm.dto.IUserProject;
import com.cnsbd.jtrainpm.dto.RemoveMembersRequest;
import com.cnsbd.jtrainpm.model.JsonResponse;
import com.cnsbd.jtrainpm.model.Project;
import com.cnsbd.jtrainpm.repository.ProjectRepository;
import com.cnsbd.jtrainpm.security.UserDetailsImpl;
import com.cnsbd.jtrainpm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cnsbd.jtrainpm.annotation.ApiPrefixController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@ApiPrefixController
@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/projects")
    public JsonResponse items() {
        return new JsonResponse(projectService.getItems());
    }

    @GetMapping("/projects/{id}")
    public JsonResponse item(@PathVariable("id") Long id) {
        Optional<IUserProject> op = projectService.getItem(id);
        if (!op.isPresent()) throw new EntityNotFoundException("Project not found");
        return new JsonResponse(op);
    }

    @DeleteMapping("/projects/{id}")
    public JsonResponse deleteItem(@PathVariable("id") Long id) {
        Boolean deleted = projectService.deleteItem(id);
        if (!deleted) throw new EntityNotFoundException("Project not found or not owned by you");
        return new JsonResponse("Deleted successfully");
    }

    @PatchMapping("/projects/{id}")
    public JsonResponse updateItem(@PathVariable("id") Long id, @RequestBody @Valid CreateProjectRequest body) {
        Boolean updated = projectService.updateItem(id, body);
        if (!updated) throw new EntityNotFoundException("Project not found or not owned by you");
        return new JsonResponse("Updated successfully");
    }

    @GetMapping("/projects/{id}/members")
    public JsonResponse members(@PathVariable("id") Long id) {
        return new JsonResponse(projectService.getMembers(id));
    }

    @PatchMapping("/projects/{id}/add-members")
    public JsonResponse addMembers(@PathVariable("id") Long id, @RequestBody @Valid AddMembersRequest body) throws Exception {
        if (projectService.addMembers(id, body.getUserEmails())) return new JsonResponse("Members added successfully");
        throw new Exception("Failed to add members");
    }

    @DeleteMapping("/projects/{id}/remove-members")
    public JsonResponse removeMembers(@PathVariable("id") Long id, @RequestBody @Valid RemoveMembersRequest body) throws Exception {
        if (projectService.removeMembers(id, body.getUserIds()))
            return new JsonResponse("Members removed successfully");
        throw new Exception("Failed to remove users");
    }

    @PostMapping("/projects")
    public JsonResponse create(@RequestBody @Valid CreateProjectRequest body) throws Exception {
        Project p = projectService.createProject(body);
        if (p == null) throw new Exception("Failed to create project");
        return new JsonResponse(projectRepository.findByProjectId(p.getId(), UserDetailsImpl.getCurrentUserId()));
    }

    @PatchMapping("/projects/{id}/start")
    public JsonResponse start(@PathVariable("id") Long id) throws Exception {
        if (projectService.startNow(id)) return new JsonResponse("Project stared");
        throw new Exception("Failed to start project");
    }

    @PatchMapping("/projects/{id}/end")
    public JsonResponse endNow(@PathVariable("id") Long id) throws Exception {
        if (projectService.endNow(id)) return new JsonResponse("Project ended");
        throw new Exception("Failed to end project");
    }
}
