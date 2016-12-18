package test_system.service;

import lombok.val;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import test_system.AbstractTest;
import test_system.entity.TestEntity;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@Transactional
public class TestServiceTest extends AbstractTest{
    @Autowired
    private TestService testService;

    @Test
    public void testPage() throws Exception {
        val testEntity = testService.testPage(1L);

        assertThat(testEntity.getQuestions(), is(not(empty())));

        assertThat(testEntity.getQuestions().get(0).getAnswers(), is(not(empty())));
    }
}