package com.MonitoringTool.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.MonitoringTool.Entities.FirestoreActivity;
import com.google.cloud.firestore.Firestore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirestoreRealtimeService {

    private final Firestore firestore;
    private final RealtimeEmitterService emitterService;

    @PostConstruct
    public void init() {

        firestore.collection("activities")
                .addSnapshotListener((snap, error) -> {

                    if (error != null || snap == null) return;

                    List<FirestoreActivity> activities =
                            snap.toObjects(FirestoreActivity.class);

                    // Push to all connected clients
                    emitterService.broadcast(activities);
                });

        System.out.println("🔥 Single Firestore listener attached");
    }
}

