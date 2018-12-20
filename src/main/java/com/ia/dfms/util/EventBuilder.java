package com.ia.dfms.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ia.dfms.aggregates.ArtifactAggregate;
import com.ia.dfms.aggregates.CompanyAggregate;
import com.ia.dfms.aggregates.RequestAggregate;
import com.ia.dfms.aggregates.ResourceAggregate;
import com.ia.dfms.aggregates.TaskAggregate;
import com.ia.dfms.commands.creation.ArtifactCreationCmd;
import com.ia.dfms.commands.creation.CompanyCreationCmd;
import com.ia.dfms.commands.creation.RequestCreationCmd;
import com.ia.dfms.commands.creation.ResourceCreationCmd;
import com.ia.dfms.commands.creation.TaskCreationCmd;
import com.ia.dfms.commands.update.ArtifactUpdateCmd;
import com.ia.dfms.commands.update.CompanyUpdateCmd;
import com.ia.dfms.commands.update.RequestUpdateCmd;
import com.ia.dfms.commands.update.ResourceUpdateCmd;
import com.ia.dfms.commands.update.TaskUpdateCmd;
import com.ia.dfms.events.creation.ArtifactCreatedEvent;
import com.ia.dfms.events.creation.CompanyCreatedEvent;
import com.ia.dfms.events.creation.RequestCreatedEvent;
import com.ia.dfms.events.creation.ResourceCreatedEvent;
import com.ia.dfms.events.creation.TaskCreatedEvent;
import com.ia.dfms.events.update.ArtifactUpdatedEvent;
import com.ia.dfms.events.update.CompanyUpdatedEvent;
import com.ia.dfms.events.update.RequestUpdatedEvent;
import com.ia.dfms.events.update.ResourceUpdatedEvent;
import com.ia.dfms.events.update.TaskUpdatedEvent;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EventBuilder {
    
private final AggregateUtil util;
    
    public  CompanyCreatedEvent companyCreatedEvent(String id) {
        final CompanyAggregate a = aggregateLoad(id, CompanyAggregate.class);
        return CompanyCreatedEvent.builder()
                .details(a.getDetails())
                .id(a.getId())
                .name(a.getName())
                .build();
    }
    
    public  CompanyCreatedEvent companyCreatedEvent(CompanyCreationCmd cmd) {
        return CompanyCreatedEvent.builder()
                .details(cmd.getDetails())
                .id(cmd.getId())
                .name(cmd.getName())
                .build();
    }

    
    public  CompanyUpdatedEvent companyUpdatedEvent(String id) {
        final CompanyAggregate a = aggregateLoad(id, CompanyAggregate.class);
        return CompanyUpdatedEvent.builder()
                .details(a.getDetails())
                .id(a.getId())
                .name(a.getName())
                .build();
    }
    
    public  CompanyUpdatedEvent companyUpdatedEvent(CompanyUpdateCmd cmd) {
        aggregateLoad(cmd.getId(), CompanyAggregate.class);
        return CompanyUpdatedEvent.builder()
                .details(cmd.getDetails())
                .id(cmd.getId())
                .name(cmd.getName())
                .build();
    }

    
    public  ArtifactCreatedEvent artifactCreatedEvent(String id) {
        final ArtifactAggregate a = aggregateLoad(id, ArtifactAggregate.class);
         return ArtifactCreatedEvent.builder()
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .uri(a.getUri())
                .id(a.getId())
                .build();
    }

    public  ArtifactCreatedEvent artifactCreatedEvent(ArtifactCreationCmd cmd) {
         return ArtifactCreatedEvent.builder()
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .uri(cmd.getUri())
                .id(cmd.getId())
                .build();
    }
    
    public  ResourceCreatedEvent resourceCreationEvent(String id) {
        final ResourceAggregate a = aggregateLoad(id, ResourceAggregate.class);
         return ResourceCreatedEvent.builder()
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .email(a.getEmail())
                .details(a.getDetails())
                .phoneNumber(a.getPhoneNumber())
                .id(a.getId())
                .build();
    }

    public  ResourceCreatedEvent resourceCreatedEvent(ResourceCreationCmd cmd) {
         return ResourceCreatedEvent.builder()
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .email(cmd.getEmail())
                .details(cmd.getDetails())
                .phoneNumber(cmd.getPhoneNumber())
                .build();
    }

    public  ResourceUpdatedEvent resourceUpdatedEvent(String id) {
        final ResourceAggregate a = aggregateLoad(id, ResourceAggregate.class);
         return ResourceUpdatedEvent.builder()
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .email(a.getEmail())
                .details(a.getDetails())
                .phoneNumber(a.getPhoneNumber())
                .id(a.getId())
                .build();
    }

    public  ResourceUpdatedEvent resourceUpdatedEvent(ResourceUpdateCmd cmd) {
        final ResourceAggregate a = aggregateLoad(cmd.getId(), ResourceAggregate.class);
         return ResourceUpdatedEvent.builder()
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .email(a.getEmail())
                .details(a.getDetails())
                .phoneNumber(a.getPhoneNumber())
                .id(a.getId())
                .build();
    }


    public  ArtifactUpdatedEvent artifactUpdatedEvent(String id) {
        final ArtifactAggregate a = aggregateLoad(id, ArtifactAggregate.class);
       return ArtifactUpdatedEvent.builder()
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .uri(a.getUri())
                .id(a.getId())
                .build();
    }

    public  ArtifactUpdatedEvent artifactUpdatedEvent(ArtifactUpdateCmd cmd) {
       aggregateLoad(cmd.getId(), ArtifactAggregate.class);
       return ArtifactUpdatedEvent.builder()
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .uri(cmd.getUri())
                .id(cmd.getId())
                .build();
    }

    public  Collection<ArtifactCreatedEvent> artifactCreatedEvent(Collection<String> ids) {
        final Collection<ArtifactAggregate> aggregates = util.aggregateGet(ids, ArtifactAggregate.class);
        return aggregates.stream()
                .map(a->artifactCreatedEvent(a.getId())).collect(Collectors.toList());
                
    }
    
    public  Collection<ArtifactUpdatedEvent> artifactUpdatedEvent(Collection<String> ids) {
        final Collection<ArtifactAggregate> aggregates = util.aggregateGet(ids, ArtifactAggregate.class);
        return aggregates.stream()
                .map(a->artifactUpdatedEvent(a.getId())).collect(Collectors.toList());
                
    }
    
    public TaskCreatedEvent taskCreatedEvent(String id) {
        final TaskAggregate a = aggregateLoad(id, TaskAggregate.class);
        return TaskCreatedEvent.builder()
                .id(a.getId())
                .artifactIds(a.getArtifactIds())
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .build();
    }

    public TaskCreatedEvent taskCreatedEvent(TaskCreationCmd cmd) {
        return TaskCreatedEvent.builder()
                .id(cmd.getId())
                .artifactIds(cmd.getArtifactIds())
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .build();
    }

    public TaskUpdatedEvent taskUpdatedEvent(String id) {
        final TaskAggregate a = aggregateLoad(id, TaskAggregate.class);
        return TaskUpdatedEvent.builder()
                .id(a.getId())
                .artifactIds(a.getArtifactIds())
                .companyId(a.getCompanyId())
                .description(a.getDescription())
                .build();
    }
    
    public TaskUpdatedEvent taskUpdatedEvent(TaskUpdateCmd cmd) {
        aggregateLoad(cmd.getId(), TaskAggregate.class);
        return TaskUpdatedEvent.builder()
                .id(cmd.getId())
                .artifactIds(cmd.getArtifactIds())
                .companyId(cmd.getCompanyId())
                .description(cmd.getDescription())
                .build();
    }

    
    public RequestCreatedEvent requestCreateEvent(String id) {
        final RequestAggregate a = aggregateLoad(id, RequestAggregate.class);
        return RequestCreatedEvent.builder()
                .artifactIds(a.getArtifactIds())
                .id(a.getId())
                .requestDate(a.getRequestDate())
                .requestDetails(a.getRequestDetails())
                .requestStatus(a.getRequestStatus())
                .taskId(a.getTaskId())
                .build();
    }

    public RequestCreatedEvent requestCreateEvent(RequestCreationCmd cmd) {
        return RequestCreatedEvent.builder()
                .artifactIds(cmd.getArtifactIds())
                .id(cmd.getId())
                .requestDate(cmd.getRequestDate())
                .requestDetails(cmd.getRequestDetails())
                .requestStatus(cmd.getRequestStatus())
                .taskId(cmd.getTaskId())
                .build();
    }

    public RequestUpdatedEvent requestUpdateEvent(String id) {
        final RequestAggregate a = aggregateLoad(id, RequestAggregate.class);
        return RequestUpdatedEvent.builder()
                .artifactIds(a.getArtifactIds())
                .id(a.getId())
                .requestDate(a.getRequestDate())
                .requestDetails(a.getRequestDetails())
                .requestStatus(a.getRequestStatus())
                .taskId(a.getTaskId())
                .build();
    }

    public RequestUpdatedEvent requestUpdateEvent(RequestUpdateCmd cmd) {
        aggregateLoad(cmd.getId(), RequestAggregate.class);
        return RequestUpdatedEvent.builder()
                .artifactIds(cmd.getArtifactIds())
                .id(cmd.getId())
                .requestDate(cmd.getRequestDate())
                .requestDetails(cmd.getRequestDetails())
                .requestStatus(cmd.getRequestStatus())
                .taskId(cmd.getTaskId())
                .build();
    }

    private <T> T aggregateLoad(String id, Class<T> clazz) {
        final Optional<T> result = util.aggregateGet(id, clazz);
        Assert.isTrue(result.isPresent(), "No  aggregate of type "+ clazz.getSimpleName() +"found for the id "+ id);
        return result.get();
    }
}