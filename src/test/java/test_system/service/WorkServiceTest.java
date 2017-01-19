package test_system.service;

import lombok.val;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import test_system.AbstractTest;
import test_system.data.AnswerData;
import test_system.data.QuestionData;
import test_system.data.WorkCreateData;
import test_system.entity.*;
import test_system.repository.TestRepository;
import test_system.repository.TheoryRepository;
import test_system.repository.WorkRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@Transactional
public class WorkServiceTest extends AbstractTest {

    @Autowired
    private WorkService service;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TheoryRepository theoryRepository;

    @Autowired
    private WorkRepository workRepository;

    @Test
    public void getWorks() throws Exception {
        val works = service.getWorks();
        assertThat(works.isEmpty(), is(false));
    }

    @Test
    public void getWork() throws Exception {
        val work = service.getWork(1L);
        assertThat(work, is(notNullValue()));
    }

    @Test
    public void createWork() throws Exception {
        String workTitle = "title";
        val answer = new AnswerData(null, "asd", true);
        val question = new QuestionData<AnswerData>(null, "question", QuestionType.SINGLE, Collections.singletonList(answer));
        val data = new WorkCreateData(null,
                workTitle,
                "desc",
                "theory",
                "test",
                Collections.singletonList(question),
                Lab.EMPTY);
        val work = service.updateWork(data);
        val test = testRepository.findByWorkId(work.getId());

        assertThat(work.getName(), is(workTitle));
        assertThat(theoryRepository.findByWorkId(work.getId()), is(notNullValue()));
        assertThat(test, is(notNullValue()));
        assertThat(test.getQuestions().size(), is(1));
        assertThat(test.getQuestions().get(0).getAnswers().size(), is(1));
    }

    @Test
    public void delete() throws Exception {
        val work = new WorkEntity();
        work.setDescription("asdasd");
        work.setName("asdfasf");
        val savedWork = workRepository.save(work);
        service.delete(savedWork.getId());
        assertThat(workRepository.findOne(savedWork.getId()), is(nullValue()));
    }

    @Test
    public void editWork() throws Exception {
        String workTitle = "title";
        val answer1 = new AnswerData(null, "asdasd", true);
        val answer2 = new AnswerData(null, "asddfg", true);
        val answer3 = new AnswerData(null, "asfdghfd", true);
        val answer4 = new AnswerData(null, "hgf", true);
        val answer5 = new AnswerData(null, " a s fdghfd", true);
        val answer6 = new AnswerData(null, " a s fdg hfd", true);

        val question1 = new QuestionData<AnswerData>(null, "question", QuestionType.SINGLE, Arrays.asList(answer1, answer2, answer3));
        val question2 = new QuestionData<AnswerData>(null, "question2", QuestionType.SINGLE, Arrays.asList(answer4, answer5, answer6));

        val newData = new WorkCreateData(null,
                workTitle,
                "desc",
                "theory",
                "test",
                Arrays.asList(question1, question2),
                Lab.EMPTY);
        val work = service.updateWork(newData);
        val test = testRepository.findByWorkId(work.getId());


        QuestionEntity questionEntity = test.getQuestions().get(0);
        AnswerEntity answerEntity = questionEntity.getAnswers().get(0);
        val updatedAnswer1 = new AnswerData(answerEntity.getId(), "updated answer", false);
        val newAnswer2 = new AnswerData(null, "new 2", true);
        val newAnswer3 = new AnswerData(null, "new 3", true);
        val newAnswer4 = new AnswerData(null, "new 4", true);

        val updateQuestion1 = new QuestionData<AnswerData>(questionEntity.getId(), "updated question", QuestionType.MULTIPLY, Arrays.asList(updatedAnswer1, newAnswer2));
        val newQuestion2 = new QuestionData<AnswerData>(null, "new question", QuestionType.MULTIPLY, Arrays.asList(newAnswer3, newAnswer4));


        val updateData = new WorkCreateData(
                work.getId(),
                "new name",
                "new Descriptin",
                "new theory",
                "new test",
                Arrays.asList(updateQuestion1, newQuestion2),
                Lab.EMPTY
        );

        WorkEntity workEntity = service.updateWork(updateData);
        val updatedTest = testRepository.findByWorkId(workEntity.getId());

        assertThat(workEntity.getId(), is (work.getId()));
        assertThat(workEntity.getName(), is (updateData.getName()));
        assertThat(workEntity.getDescription(), is (updateData.getDescription()));
        assertThat(updatedTest.getQuestions().size(), is(2));
        assertThat(updatedTest.getQuestions().get(0).getId(), is(updateQuestion1.getId()));
        assertThat(updatedTest.getQuestions().get(0).getAnswers().size(), is(2));
        assertThat(updatedTest.getQuestions().get(1).getAnswers().size(), is(2));
    }
}