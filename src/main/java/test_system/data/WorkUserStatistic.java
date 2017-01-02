package test_system.data;

import lombok.Data;
import test_system.entity.UserEntity;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;

import java.util.List;

@Data
public class WorkUserStatistic {
    private final UserEntity user;
    private final WorkEntity work;
    private final List<WorkExecutionEntity> attempts;
}
