package com.azatkhaliullin.socialmediaapi.service;

import com.azatkhaliullin.socialmediaapi.Exception.RelationshipExistsException;
import com.azatkhaliullin.socialmediaapi.dto.Relationship;
import com.azatkhaliullin.socialmediaapi.dto.Relationship.Status;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.RelationshipRepository;
import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import javax.naming.OperationNotSupportedException;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RelationshipRepository relationshipRepo;

    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void subscribeTo(User user,
                            Long targetUserId) {
        User targetUser = userRepo.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);
        if (relationshipRepo.existsByWhoAndToWhom(user, targetUser)
                || relationshipRepo.existsByWhoAndToWhom(targetUser, user)) {
            throw new RelationshipExistsException();
        } else if (user.equals(targetUser)) {
            throw new IllegalArgumentException();
        }
        Relationship relationship = Relationship.builder()
                .who(user)
                .toWhom(targetUser)
                .status(Status.SUBSCRIBER)
                .isRequestDone(false)
                .build();
        relationshipRepo.save(relationship);
    }

    public void processSubscribeRequest(User user,
                                        Long targetUserId,
                                        boolean isAccepted) {
        User targetUser = userRepo.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);
        Relationship relationship = relationshipRepo.findByWhoAndToWhom(targetUser, user)
                .orElseThrow(EntityNotFoundException::new);
        if (!relationship.isRequestDone()) {
            relationship.setRequestDone(true);
            relationship.setStatus(isAccepted ? Status.FRIEND : Status.SUBSCRIBER);
            relationshipRepo.save(relationship);
        }
    }

    public void unsubscribeFrom(User user,
                                Long targetUserId) {
        User targetUser = userRepo.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);
        Relationship relationship = relationshipRepo.findByWhoAndToWhom(user, targetUser)
                .or(() -> relationshipRepo.findByWhoAndToWhomAndStatus(targetUser, user, Status.FRIEND))
                .orElseThrow(EntityNotFoundException::new);
        if (relationship.getWho().equals(user)
                && relationship.getStatus() == Status.SUBSCRIBER) {
            relationshipRepo.delete(relationship);
        } else {
            relationshipRepo.save(relationship.toBuilder()
                    .who(targetUser)
                    .toWhom(user)
                    .status(Status.SUBSCRIBER)
                    .build());
        }
    }

    public void sendMessage(User user,
                            Long targetUserId,
                            String text) throws OperationNotSupportedException {
        User targetUser = userRepo.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);
        relationshipRepo.findByWhoAndToWhomAndStatus(targetUser, user, Status.FRIEND)
                .or(() -> relationshipRepo.findByWhoAndToWhomAndStatus(user, targetUser, Status.FRIEND))
                .orElseThrow(OperationNotSupportedException::new);
    }


}