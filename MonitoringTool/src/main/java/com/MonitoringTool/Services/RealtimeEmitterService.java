package com.MonitoringTool.Services;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.MonitoringTool.Entities.FirestoreActivity;

@Service
public class RealtimeEmitterService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter addEmitter() {

        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void broadcast(List<FirestoreActivity> activities) {

        emitters.forEach(emitter -> {
            try {
                emitter.send(activities);
            } catch (Exception e) {
                emitter.complete();
            }
        });
    }
}

