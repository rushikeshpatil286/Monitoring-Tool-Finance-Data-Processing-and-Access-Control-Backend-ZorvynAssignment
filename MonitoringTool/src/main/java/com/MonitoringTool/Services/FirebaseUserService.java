package com.MonitoringTool.Services;

import org.springframework.stereotype.Service;

import com.MonitoringTool.Entities.UserEntity;
import com.google.cloud.firestore.*;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class FirebaseUserService {

    private final Firestore firestore;

    private static final String COLLECTION = "users";

    // 🔥 SAVE USER
    public void save(UserEntity user) {

        try {
            firestore.collection(COLLECTION)
                    .document(user.getEmail())   // EMAIL = DOC ID
                    .set(user)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException("User save failed", e);
        }
    }

    // 🔥 FIND USER (NO QUERY)
    public Optional<UserEntity> findByEmail(String email) {

        try {

            DocumentSnapshot doc = firestore
                    .collection(COLLECTION)
                    .document(email)
                    .get()
                    .get();

            if (!doc.exists()) {
                return Optional.empty();
            }

            return Optional.ofNullable(doc.toObject(UserEntity.class));

        } catch (Exception e) {
            throw new RuntimeException("Firebase lookup failed", e);
        }
    }
}

