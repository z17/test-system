package test_system.service;

import org.springframework.stereotype.Component;

@Component
public class LabService {
    public String labPage(final long workIid) {
        return "test-lab";
    }

    public void finishLab(long id, Object data) {

    }
}
