package test_system.repository;

import lombok.val;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import test_system.AbstractTest;
import test_system.entity.WorkExecutionEntity;
import test_system.entity.WorkPhase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@Transactional
public class WorkExecutionRepositoryTest extends AbstractTest {

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkExecutionRepository workExecutionRepository;

    @Test
    public void findLast1ByUserAndWorkAndPhase() throws Exception {
        val user = userRepository.findOne(1L);
        val work = workRepository.findOne(1L);

        WorkExecutionEntity work1 = new WorkExecutionEntity();
        work1.setPhase(WorkPhase.FINISHED);
        work1.setWork(work);
        work1.setUser(user);
        work1.setQuestionsAmount(55);


        WorkExecutionEntity work2 = new WorkExecutionEntity();
        work2.setPhase(WorkPhase.FINISHED);
        work2.setWork(work);
        work2.setUser(user);
        work2.setQuestionsAmount(66);

        workExecutionRepository.save(work1);
        workExecutionRepository.save(work2);

        WorkExecutionEntity firstByUserAndWorkAndPhase = workExecutionRepository.findFirstByUserAndWorkAndPhaseOrderByIdDesc(user, work, WorkPhase.FINISHED);

        assertThat(firstByUserAndWorkAndPhase, is(66));
    }

}