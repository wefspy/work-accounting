package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.*;
import com.example.workaccounting.domain.enums.ProjectStatusType;
import com.example.workaccounting.domain.model.Project;
import com.example.workaccounting.domain.model.ProjectComment;
import com.example.workaccounting.domain.model.ProjectVote;
import com.example.workaccounting.domain.model.UserInfo;
import com.example.workaccounting.infrastructure.repository.jpa.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectVoteRepository projectVoteRepository;
    private final ProjectCommentRepository projectCommentRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProjectStatusRepository projectStatusRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectVoteRepository projectVoteRepository,
                          ProjectCommentRepository projectCommentRepository,
                          UserInfoRepository userInfoRepository,
                          ProjectStatusRepository projectStatusRepository) {
        this.projectRepository = projectRepository;
        this.projectVoteRepository = projectVoteRepository;
        this.projectCommentRepository = projectCommentRepository;
        this.userInfoRepository = userInfoRepository;
        this.projectStatusRepository = projectStatusRepository;
    }

    @Transactional
    public ProjectDto createProject(ProjectCreateDto dto, Long userId) {
        var user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var status = projectStatusRepository.findByName(ProjectStatusType.UNDER_DISCUSSION.name())
                .orElseThrow(() -> new RuntimeException("Initial status UNDER_DISCUSSION not found"));

        Project project = new Project();
        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setTechStack(dto.techStack());
        project.setTeamSize(dto.teamSize());
        project.setCreatedBy(user);
        project.setStatus(status);

        Project savedProject = projectRepository.save(project);
        return mapToProjectDto(savedProject);
    }

    @Transactional
    public ProjectDto updateProject(Long id, ProjectCreateDto dto, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Опционально: проверять имеет ли пользователь права на эти действия
        // if (!project.getCreatedBy().getId().equals(userId)) {
        //     throw new RuntimeException("You are not allowed to update this project");
        // }

        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setTechStack(dto.techStack());
        project.setTeamSize(dto.teamSize());

        Project savedProject = projectRepository.save(project);
        return mapToProjectDto(savedProject);
    }

    @Transactional
    public void voteProject(Long projectId, boolean value, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        UserInfo voter = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        projectVoteRepository.findByProjectIdAndVoterId(projectId, userId)
                .ifPresentOrElse(
                        vote -> {
                            if (vote.isValue() == value) {
                                projectVoteRepository.delete(vote);
                            } else {
                                vote.setValue(value);
                                projectVoteRepository.save(vote);
                            }
                        },
                        () -> {
                            ProjectVote newVote = new ProjectVote();
                            newVote.setProject(project);
                            newVote.setVoter(voter);
                            newVote.setValue(value);
                            projectVoteRepository.save(newVote);
                        }
                );
    }

    @Transactional
    public CommentDto addComment(Long projectId, CommentCreateDto dto, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        UserInfo author = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectComment comment = new ProjectComment();
        comment.setProject(project);
        comment.setAuthor(author);
        comment.setBody(dto.body());

        ProjectComment savedComment = projectCommentRepository.save(comment);

        return CommentDto.builder()
                .id(savedComment.getId())
                .body(savedComment.getBody())
                .authorId(author.getId())
                .authorName(author.getFirstName() + " " + author.getLastName())
                .createdAt(savedComment.getCreatedAt())
                .build();
    }

    @Transactional
    public ProjectDto updateProjectStatus(Long id, String statusName, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectStatusType.valueOfOrRuntimeException(statusName);

        var status = projectStatusRepository.findByName(statusName)
                .orElseThrow(() -> new RuntimeException("Status not found in DB: " + statusName));

        project.setStatus(status);
        Project savedProject = projectRepository.save(project);
        return mapToProjectDto(savedProject);
    }

    @Transactional
    public void deleteProject(Long id, Long userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Опционально: проверять имеет ли пользователь права на эти действия
        // if (!project.getCreatedBy().getId().equals(userId)) {
        //     throw new RuntimeException("You are not allowed to delete this project");
        // }

        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public ProjectDetailDto getProjectDetails(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        long likes = projectVoteRepository.countByProjectIdAndValue(projectId, true);
        long dislikes = projectVoteRepository.countByProjectIdAndValue(projectId, false);
        long comments = projectCommentRepository.countByProjectId(projectId);

        UserInfo creator = project.getCreatedBy();

        return ProjectDetailDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .techStack(project.getTechStack())
                .status(project.getStatus().getName())
                .likesCount(likes)
                .dislikesCount(dislikes)
                .commentsCount(comments)
                .creatorId(creator.getId())
                .creatorFio(creator.getFullName())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> getProjectComments(Long projectId, Pageable pageable) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found");
        }
        Page<ProjectComment> comments = projectCommentRepository.findByProjectId(projectId, pageable);
        return comments.map(comment -> {
            UserInfo author = comment.getAuthor();
            return CommentDto.builder()
                    .id(comment.getId())
                    .body(comment.getBody())
                    .authorId(author.getId())
                    .authorName(author.getFullName())
                    .createdAt(comment.getCreatedAt())
                    .build();
        });
    }

    @Transactional(readOnly = true)
    public Page<ProjectDto> getAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(this::mapToProjectDto);
    }

    private ProjectDto mapToProjectDto(Project project) {
        long likes = projectVoteRepository.countByProjectIdAndValue(project.getId(), true);
        long dislikes = projectVoteRepository.countByProjectIdAndValue(project.getId(), false);
        long comments = projectCommentRepository.countByProjectId(project.getId());

        String statusName = project.getStatus().getName();

        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .techStack(project.getTechStack())
                .status(statusName)
                .likes(likes)
                .dislikes(dislikes)
                .commentsCount(comments)
                .build();
    }
}
