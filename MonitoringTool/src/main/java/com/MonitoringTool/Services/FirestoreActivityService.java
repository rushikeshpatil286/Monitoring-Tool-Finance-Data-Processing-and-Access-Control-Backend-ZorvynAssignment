package com.MonitoringTool.Services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.MonitoringTool.Entities.FirestoreActivity;
import com.MonitoringTool.dto.ActivityDTO;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.SetOptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class FirestoreActivityService {

    private final Firestore firestore;

    private static final String COLLECTION = "activities";

    public void saveFromDTO(ActivityDTO dto) throws Exception {

        String id = UUID.randomUUID().toString();

        FirestoreActivity activity = new FirestoreActivity();
        activity.setId(id);
        activity.setProjectName(dto.getProjectName());
        activity.setActivityName(dto.getActivityName());
        activity.setStartDate(dto.getStartDate());
        activity.setEndDate(dto.getEndDate());
        activity.setStatus(dto.getStatus());
        activity.setResponsibleEmail(dto.getResponsibleEmail());
        activity.setManagerEmail(dto.getManagerEmail());
        activity.setCoordinatorEmail(dto.getCoordinatorEmail());

        firestore.collection("activities")
                .document(id)
                .set(activity)
                .get();
    }


    public List<ActivityDTO> findAllDTO() {

        try {

            List<QueryDocumentSnapshot> docs =
                    firestore.collection("activities")
                            .get()
                            .get()
                            .getDocuments();

            return docs.stream()
                    .map(doc -> convertToDTO(doc.toObject(FirestoreActivity.class)))
                    .toList();   // ✅ Java 21 supports this

        } catch (Exception e) {
            throw new RuntimeException("Fetch failed", e);
        }
    }

    
    private ActivityDTO convertToDTO(FirestoreActivity a) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(a.getId());
        dto.setProjectName(a.getProjectName());
        dto.setActivityName(a.getActivityName());
        dto.setStartDate(a.getStartDate());
        dto.setEndDate(a.getEndDate());
        dto.setStatus(a.getStatus());
        dto.setResponsibleEmail(a.getResponsibleEmail());
        dto.setManagerEmail(a.getManagerEmail());
        dto.setCoordinatorEmail(a.getCoordinatorEmail());
        return dto;
    }
    
    public void updateActivity(String id, FirestoreActivity activity) throws Exception {

        firestore.collection("activities")
                .document(id)
                .set(activity, SetOptions.merge())  // ✅ SAFE MERGE
                .get();
    }

    public void save(FirestoreActivity activity) throws Exception {

        if (activity.getId() == null || activity.getId().isEmpty()) {
            activity.setId(UUID.randomUUID().toString());
        }

        firestore.collection("activities")
                .document(activity.getId())
                .set(activity, SetOptions.merge())
                .get();
    }

    
    public void updateStatus(String id, String status) throws Exception {

        firestore.collection(COLLECTION)
                .document(id)
                .update("status", status)
                .get();
    }
    public List<ActivityDTO> findByUserEmail(String email) {

        try {

            Query query = firestore.collection("activities")
                    .whereEqualTo("responsibleEmail", email);

            List<QueryDocumentSnapshot> docs =
                    query.get().get().getDocuments();

            return docs.stream().map(doc -> {

                ActivityDTO dto = new ActivityDTO();

                dto.setId(doc.getId());
                dto.setProjectName(doc.getString("projectName"));
                dto.setActivityName(doc.getString("activityName"));
                dto.setStartDate(doc.getString("startDate"));
                dto.setEndDate(doc.getString("endDate"));
                dto.setStatus(doc.getString("status"));
                dto.setResponsibleEmail(doc.getString("responsibleEmail"));
                dto.setManagerEmail(doc.getString("managerEmail"));
                dto.setCoordinatorEmail(doc.getString("coordinatorEmail"));

                return dto;

            }).toList();

        } catch (Exception e) {
            throw new RuntimeException("User activity fetch failed", e);
        }
    }
    
    public FirestoreActivity findById(String id) throws Exception {

        return firestore.collection("activities")
                .document(id)
                .get()
                .get()
                .toObject(FirestoreActivity.class);
    }
    
    public List<FirestoreActivity> findAll() {

        try {
            return firestore.collection("activities")
                    .get()
                    .get()
                    .toObjects(FirestoreActivity.class);

        } catch (Exception e) {
            throw new RuntimeException("Fetch all activities failed", e);
        }
    }

}


