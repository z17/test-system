package test_system.data;

import lombok.Data;
import test_system.entity.TestEntity;
import test_system.entity.TheoryEntity;
import test_system.entity.WorkEntity;

@Data
public class WorkData {
    final WorkEntity work;
    final TheoryEntity theory;
    final TestEntity test;
}
