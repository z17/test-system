package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.data.*;
import test_system.entity.*;
import test_system.lab.LabData;
import test_system.repository.WorkAnswerRepository;
import test_system.repository.WorkExecutionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private final WorkService workService;
    private final TestService testService;
    private final WorkExecutionRepository workExecutionRepository;
    private final WorkAnswerRepository workAnswerRepository;
    private final UserService userService;
    private final WorkExecutionService workExecutionService;
    private final LabService labService;

    @Autowired
    public StatisticService(WorkService workService, TestService testService, WorkExecutionRepository workExecutionRepository, WorkAnswerRepository workAnswerRepository, UserService userService, WorkExecutionService workExecutionService, LabService labService) {
        this.workService = workService;
        this.testService = testService;
        this.workExecutionRepository = workExecutionRepository;
        this.workAnswerRepository = workAnswerRepository;
        this.userService = userService;
        this.workExecutionService = workExecutionService;
        this.labService = labService;
    }

    public WorkStatisticData getWorkStatistic(final long workId) {
        val work = workService.getWork(workId);

        List<WorkExecutionEntity> completedWorks = workExecutionRepository.findByWorkAndPhaseOrderByTestStartTimeDesc(work, WorkPhase.FINISHED);

        double correctData = completedWorks.stream().map(v -> v.getCorrectQuestionsAmount() * 100 / v.getQuestionsAmount()).collect(Collectors.averagingDouble(v -> v));
        val test = testService.getTestByWorkId(workId);

        List<Long> answerIds = workAnswerRepository.findByWork(work).stream().map(WorkAnswerEntity::getAnswerId).collect(Collectors.toList());
        val workAttempts = completedWorks.size();

        List<QuestionData<StatisticAnswerData>> questionsData = new ArrayList<>();
        for (QuestionEntity question : test.getQuestions()) {
            List<StatisticAnswerData> answersData = new ArrayList<>();
            for (AnswerEntity answer : question.getAnswers()) {
                long count = answerIds.stream().filter(v -> v.equals(answer.getId())).count();
                int percent = workAttempts > 0 ? (int) (count * 100 / workAttempts) : 0;

                answersData.add(new StatisticAnswerData(answer.getText(), answer.isCorrect(), percent));
            }

            questionsData.add(new QuestionData<>(question.getId(), question.getText(), question.getType(), answersData));
        }

        val testMinutes = getAverageMinutesTestTime(completedWorks);
        val labMinutes = getAverageMinutesLabTime(completedWorks);

        return new WorkStatisticData(work.getName(),
                (int) Math.round(correctData),
                testMinutes,
                workAttempts,
                questionsData,
                completedWorks,
                work.getLab() != null,
                labMinutes);
    }

    public WorkUserStatistic getWorkUserStatistic(long workId, long userId) {
        val work = workService.getWork(workId);
        val user = userService.getUser(userId);
        List<WorkExecutionEntity> attempts = workExecutionRepository.findByWorkAndUser(work, user);
        return new WorkUserStatistic(user, work, attempts);
    }

    public AttemptData<AttemptAnswerData> getAttemptData(final long id) {
        val attempt = workExecutionService.get(id);

        val test = testService.getTestByWorkId(attempt.getWork().getId());
        List<WorkAnswerEntity> answers = workAnswerRepository.findByWorkExecution(attempt);

        List<QuestionData<AttemptAnswerData>> questions = new ArrayList<>();
        for (QuestionEntity question : test.getQuestions()) {
            List<AttemptAnswerData> answersData = new ArrayList<>();
            for (AnswerEntity answer : question.getAnswers()) {
                boolean selected = answers.stream().anyMatch(f -> f.getAnswerId().equals(answer.getId()));
                answersData.add(new AttemptAnswerData(answer.getText(), answer.isCorrect(), selected));
            }

            questions.add(new QuestionData<>(question.getId(), question.getText(), question.getType(), answersData));
        }

        final LabData labData;
        if (attempt.getLabResult() != null) {
            labData = labService.getLabData(attempt.getLabResult(), attempt.getWork().getLab());
        } else {
            labData = null;
        }
        return new AttemptData<>(attempt, questions, labData, attempt.getWork().getLab().getResultTemplate());
    }

    private int getAverageMinutesTestTime(List<WorkExecutionEntity> works) {
        double testTime = works.stream()
                .map(v -> v.getTestEndTime().getTime() - v.getTestStartTime().getTime())
                .collect(Collectors.averagingLong(v -> v));
        return (int) TimeUnit.MILLISECONDS.toMinutes(Math.round(testTime));
    }

    private int getAverageMinutesLabTime(List<WorkExecutionEntity> works) {
        double labTime = works.stream()
                .filter(v -> v.getLabResult() != null)
                .map(v -> v.getLabResult().getEndTime().getTime() - v.getLabResult().getStartTime().getTime())
                .collect(Collectors.averagingLong(v -> v));
        return (int) TimeUnit.MILLISECONDS.toMinutes(Math.round(labTime));
    }
}
