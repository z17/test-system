package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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

    public TestEntity testPage(final long workId) {
        val test = testRepository.findByWorkId(workId);

        if (test == null) {
            throw new NotFoundException("Test not found");
        }

        workService.workProcess(workId, WorkPhase.TEST);

        return test;
    }

    public ResultData finishPage(final long workId, final MultiValueMap<String, String> testResultData) {
        val test = testRepository.findByWorkId(workId);

        if (test == null) {
            throw new NotFoundException("Test not found");
        }

        List<WorkAnswerEntity> answers = processTestResultData(testResultData);
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

        val workExecutionEntity = workService.workProcess(workId, WorkPhase.FINISHED);
        val result = workService.finishTest(workExecutionEntity, correctQuestionCount, test.getQuestions().size());
        return new ResultData(result);
    }

    private List<WorkAnswerEntity> processTestResultData(final MultiValueMap<String, String> testResultData) {
        return testResultData.entrySet().stream()
                .filter(v -> v.getKey().startsWith(QUESTION_PARAMETER_PREFIX))
                .flatMap(v -> v.getValue().stream())
                .map(Long::valueOf)
                .map(WorkAnswerEntity::new)
                .collect(Collectors.toList());
    }
}
