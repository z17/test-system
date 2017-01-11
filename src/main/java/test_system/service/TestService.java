package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import test_system.data.AnswerData;
import test_system.data.QuestionData;
import test_system.data.ResultData;
import test_system.entity.*;
import test_system.exception.NotFoundException;
import test_system.repository.AnswerRepository;
import test_system.repository.QuestionRepository;
import test_system.repository.TestRepository;
import test_system.repository.WorkAnswerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TestService {
    private final String QUESTION_PARAMETER_PREFIX = "question-";

    private final TestRepository testRepository;
    private final WorkService workService;
    private final WorkAnswerRepository workAnswerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final WorkExecutionService workExecutionService;

    @Autowired
    public TestService(TestRepository testRepository, WorkService workService, WorkAnswerRepository workAnswerRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, WorkExecutionService workExecutionService) {
        this.testRepository = testRepository;
        this.workService = workService;
        this.workAnswerRepository = workAnswerRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.workExecutionService = workExecutionService;
    }

    TestEntity getTestByWorkId(final long workId) {
        val test = testRepository.findByWorkId(workId);

        if (test == null) {
            throw new NotFoundException("Test not found");
        }
        return test;
    }

    public TestEntity testPage(final long workId) {
        val test = getTestByWorkId(workId);

        startTest(workId);
        return test;
    }

    public String finishTest(final long workId, final MultiValueMap<String, String> testResultData) {
        val test = getTestByWorkId(workId);
        val work = workService.getWork(workId);

        val workExecution = workExecutionService.getProcessingWork(workId);
        if (workExecution == null) {
            throw new RuntimeException("Access is denied");
        }

        if (workExecution.getPhase() != WorkPhase.TEST) {
            throw new RuntimeException("Access is denied");
        }

        final List<WorkAnswerEntity> answers = processTestResultData(workExecution, testResultData);
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

        if (work.getLab() != Lab.EMPTY && work.getLab() != null) {
            workExecution.setPhase(WorkPhase.LAB);
        } else {
            workExecution.setPhase(WorkPhase.FINISHED);
        }

        workService.finishTest(workExecution, correctQuestionCount, test.getQuestions().size());

        return "work/" + workId + "/finish";
    }

    public ResultData finishPage(final long workId) {
        val workExecutionEntity = workService.getFinishedAttempt(workId);
        return new ResultData(workExecutionEntity);
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

    void deleteByWorkId(final long workId) {
        testRepository.deleteByWorkId(workId);
    }

    void update(final Long workId, final String testDescription, final List<QuestionData<AnswerData>> questions) {
        val test = getTestByWorkId(workId);
        test.setDescription(testDescription);

        Map<Long, QuestionData<AnswerData>> updatedQuestionDataMap = questions.stream()
                .filter(v -> v.getId() != null)
                .collect(Collectors.toMap(QuestionData::getId, Function.identity()));

        removeQuestionAndAnswers(test.getQuestions(), questions);

        final List<QuestionEntity> updatedQuestions = test.getQuestions().stream()
                .filter(v -> updatedQuestionDataMap.containsKey(v.getId()))
                .map(v -> {
                    QuestionData<AnswerData> updatedData = updatedQuestionDataMap.get(v.getId());
                    v.setText(updatedData.getText());
                    v.setType(updatedData.getType());

                    final Map<Long, AnswerData> updatedAnswersData = updatedData.getAnswers().stream()
                            .filter(a -> a.getId() != null)
                            .collect(Collectors.toMap(AnswerData::getId, Function.identity()));

                    List<AnswerEntity> updatedAnswers = v.getAnswers().stream()
                            .filter(a -> updatedAnswersData.containsKey(a.getId()))
                            .map(a -> {
                                AnswerData answerData = updatedAnswersData.get(a.getId());
                                a.setText(answerData.getText());
                                a.setCorrect(answerData.isCorrect());
                                return a;
                            }).collect(Collectors.toList());

                    updatedAnswers.addAll(getNewAnswers(updatedData.getAnswers(), v));
                    v.setAnswers(updatedAnswers);
                    return v;
                })
                .collect(Collectors.toList());

        updatedQuestions.addAll(getNewQuestions(test, questions));
        test.setQuestions(updatedQuestions);
        testRepository.save(test);
    }

    private List<QuestionEntity> getNewQuestions(final TestEntity test, final List<QuestionData<AnswerData>> questions) {
        return questions.stream()
                .filter(v -> v.getId() == null)
                .map(v -> {
                    final QuestionEntity question = new QuestionEntity();
                    question.setText(v.getText());
                    question.setType(v.getType());
                    question.setTest(test);

                    question.setAnswers(getNewAnswers(v.getAnswers(), question));
                    return question;
                })
                .collect(Collectors.toList());
    }

    private void removeQuestionAndAnswers(final List<QuestionEntity> currentQuestions, final List<QuestionData<AnswerData>> questions) {
        final List<Long> updatedAnswersId = questions.stream()
                .flatMap(v -> v.getAnswers().stream())
                .map(AnswerData::getId)
                .collect(Collectors.toList());
        List<AnswerEntity> deletedAnswers = currentQuestions.stream()
                .flatMap(v -> v.getAnswers().stream())
                .filter(v -> !updatedAnswersId.contains(v.getId()))
                .collect(Collectors.toList());
        answerRepository.delete(deletedAnswers);

        final List<Long> updatedQuestionsId = questions.stream()
                .map(QuestionData::getId)
                .collect(Collectors.toList());
        List<QuestionEntity> deletedQuestions = currentQuestions.stream()
                .filter(v -> !updatedQuestionsId.contains(v.getId()))
                .collect(Collectors.toList());
        questionRepository.delete(deletedQuestions);
    }

    private List<AnswerEntity> getNewAnswers(final List<AnswerData> answers, final QuestionEntity question) {
        return answers.stream()
                .filter(a -> a.getId() == null)
                .map(a -> {
                    AnswerEntity answer = new AnswerEntity();
                    answer.setQuestion(question);
                    answer.setText(a.getText());
                    answer.setCorrect(a.isCorrect());
                    return answer;
                }).collect(Collectors.toList());
    }

    private void startTest(final long workId) {
        WorkExecutionEntity workExecution = workExecutionService.getProcessingWork(workId);

        if (workExecution == null) {
            throw new RuntimeException("Access is denied");
        }

        if (workExecution.getPhase() == WorkPhase.TEST) {
            return;
        }

        if (workExecution.getPhase() != WorkPhase.THEORY) {
            // todo: other message
            throw new RuntimeException("Access is denied");
        }

        workExecutionService.updatePhase(workExecution, WorkPhase.TEST);
    }
}
