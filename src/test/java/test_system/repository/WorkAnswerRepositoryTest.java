package test_system.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import test_system.App;
import test_system.entity.WorkAnswerEntity;
import test_system.entity.WorkEntity;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@Transactional
public class WorkAnswerRepositoryTest {

    @Autowired
    private WorkAnswerRepository workAnswerRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkExecutionRepository workExecutionRepository;

    @Test
    public void findByWorkId() throws Exception {

        WorkEntity work = new WorkEntity();
        work.setName("Name");
        work.setDescription("descr");
        work = workRepository.save(work);

        WorkExecutionEntity workExecutionEntity = new WorkExecutionEntity();
        workExecutionEntity.setWork(work);
        workExecutionEntity.setUser(userRepository.findOne(1L));
        workExecutionEntity.setPhase(WorkPhase.FINISHED);
        workExecutionEntity = workExecutionRepository.save(workExecutionEntity);

        WorkAnswerEntity workAnswerEntity1 = new WorkAnswerEntity(workExecutionEntity, 2L);
        WorkAnswerEntity workAnswerEntity2 = new WorkAnswerEntity(workExecutionEntity, 3L);
        WorkAnswerEntity workAnswerEntity3 = new WorkAnswerEntity(workExecutionEntity, 5L);
        workAnswerRepository.save(workAnswerEntity1);
        workAnswerRepository.save(workAnswerEntity2);
        workAnswerRepository.save(workAnswerEntity3);


        List<WorkAnswerEntity> byWorkId = workAnswerRepository.findByWork(work);

        assertThat(byWorkId.size(), is(3));
    }

}