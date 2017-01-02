package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import test_system.data.AnswerData;
import test_system.data.QuestionData;
import test_system.data.ResultData;
import test_system.data.TestResult;
import test_system.entity.*;
import test_system.exception.NotFoundException;
import test_system.repository.TestRepository;
import test_system.repository.WorkAnswerRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {
    private final String QUESTION_PARAMETER_PREFIX = "question-";

    private final TestRepository testRepository;
    private final WorkService workService;
    private final WorkAnswerRepository workAnswerRepository;

    @Autowired
    public TestService(TestRepository testRepository, WorkService workService, WorkAnswerRepository workAnswerRepository) {
        this.testRepository = testRepository;
        this.workService = workService;
        this.workAnswerRepository = workAnswerRepository;
    }

    TestEntity getTest(final long workId) {
        val test = testRepository.findByWorkId(workId);

        if (test == null) {
            throw new NotFoundException("Test not found");
        }
        return test;
    }

    public TestEntity testPage(final long workId) {
        val test = getTest(workId);

        workService.workProcess(workId, WorkPhase.TEST);

        return test;
    }

    public ResultData finishPage(final long workId, final MultiValueMap<String, String> testResultData) {
        val test = getTest(workId);

        val workExecutionEntity = workService.workProcess(workId, WorkPhase.FINISHED);

        final List<WorkAnswerEntity> answers = processTestResultData(workExecutionEntity, testResultData);
        workAnswerRepository.save(answers);

        List<Long> answerIds = answers.stream().map(WorkAnswerEntity::getAnswerId).collect(Collectors.toList());

        int correctQuestionCount = 0;
        for (QuestionEntity question : test.getQuestions()) {
            final List<Long> correctAnswers = question.getAnswers().stream().filter(AnswerEntity::isCorrect).map(AnswerEntity::getId).collect(Collectors.toList());
            final List<Long> incorrectAnswers = question.getAnswers().stream().filter(v -> !v.isCorrect()).map(AnswerEntity::getId).collect(Collectors.toList());

            if (answerIds.containsAll(correctAnswers) && Collections.disjoint(answerIds, incorrectAnswers)) {
                correctQuestionCount++;
            }
        }

        val result = workService.finishTest(workExecutionEntity, correctQuestionCount, test.getQuestions().size());
        return new ResultData(result);
    }

    private List<WorkAnswerEntity> processTestResultData(final WorkExecutionEntity workExecution, final MultiValueMap<String, String> testResultData) {
        return testResultData.entrySet().stream()
                .filter(v -> v.getKey().startsWith(QUESTION_PARAMETER_PREFIX))
                .flatMap(v -> v.getValue().stream())
                .map(Long::valueOf)
                .map(v -> new WorkAnswerEntity(workExecution, v))
                .collect(Collectors.toList());
    }

    void create(final long workId, final String testDescription, final List<QuestionData<AnswerData>> questions) {
        val test = new TestEntity();
        test.setWorkId(workId);
        test.setDescription(testDescription);

        final List<QuestionEntity> questionList = questions.stream().map(v -> {
            final QuestionEntity question = new QuestionEntity();
            question.setText(v.getText());
            question.setType(v.getType());
            question.setTest(test);

            final List<AnswerEntity> answers = v.getAnswers().stream().map(a -> {
                AnswerEntity answer = new AnswerEntity();
                answer.setText(a.getText());
                answer.setCorrect(a.isCorrect());
                answer.setQuestion(question);
                return answer;
            }).collect(Collectors.toList());
            question.setAnswers(answers);
            return question;
        }).collect(Collectors.toList());
        test.setQuestions(questionList);
        testRepository.save(test);
    }
}
