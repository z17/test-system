package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.data.*;
import test_system.entity.*;
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

    @Autowired
    public StatisticService(WorkService workService, TestService testService, WorkExecutionRepository workExecutionRepository, WorkAnswerRepository workAnswerRepository, UserService userService, WorkExecutionService workExecutionService) {
        this.workService = workService;
        this.testService = testService;
        this.workExecutionRepository = workExecutionRepository;
        this.workAnswerRepository = workAnswerRepository;
        this.userService = userService;
        this.workExecutionService = workExecutionService;
    }

    public WorkStatisticData getWorkStatistic(final long workId) {
        val work = workService.getWork(workId);

        List<WorkExecutionEntity> completedWorks = workExecutionRepository.findByWorkAndPhaseOrderByStartTimeDesc(work, WorkPhase.FINISHED);

        double correctData = completedWorks.stream().map(v -> v.getCorrectQuestionsAmount() * 100 / v.getQuestionsAmount()).collect(Collectors.averagingDouble(v -> v));
        double time = completedWorks.stream().map(v -> v.getEndTime().getTime() - v.getStartTime().getTime()).collect(Collectors.averagingLong(v -> v));
        int timeInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(Math.round(time));
        val test = testService.getTest(workId);

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

            questionsData.add(new QuestionData<>(question.getText(), question.getType(), answersData));
        }

        return new WorkStatisticData(work.getName(),
                (int) Math.round(correctData),
                timeInMinutes,
                workAttempts,
                questionsData,
                completedWorks);
    }

    public WorkUserStatistic getWorkUserStatistic(long workId, long userId) {
        val work = workService.getWork(workId);
        val user = userService.getUser(userId);
        List<WorkExecutionEntity> attempts = workExecutionRepository.findByWorkAndUser(work, user);
        return new WorkUserStatistic(user, work, attempts);
    }

    public AttemptData<AnswerData> getAttemptData(final long id) {
        val attempt = workExecutionService.get(id);

        val test = testService.getTest(attempt.getWork().getId());
        List<WorkAnswerEntity> answers = workAnswerRepository.findByWorkExecution(attempt);

        List<QuestionData<AnswerData>> questions = new ArrayList<>();
        for (QuestionEntity question : test.getQuestions()) {
            List<AnswerData> answersData = new ArrayList<>();
            for (AnswerEntity answer : question.getAnswers()) {
                boolean selected = answers.stream().anyMatch(f -> f.getAnswerId().equals(answer.getId()));
                answersData.add(new AnswerData(answer.getText(), answer.isCorrect(), selected));
            }

            questions.add(new QuestionData<>(question.getText(), question.getType(), answersData));
        }

        return new AttemptData<>(attempt, questions);
    }
}
