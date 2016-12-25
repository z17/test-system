package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test_system.data.WorkStatisticAnswerData;
import test_system.data.WorkStatisticData;
import test_system.data.WorkStatisticQuestionData;
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

    @Autowired
    public StatisticService(WorkService workService, TestService testService, WorkExecutionRepository workExecutionRepository, WorkAnswerRepository workAnswerRepository) {
        this.workService = workService;
        this.testService = testService;
        this.workExecutionRepository = workExecutionRepository;
        this.workAnswerRepository = workAnswerRepository;
    }

    public WorkStatisticData getWorkStatistic(final long workId) {
        val work = workService.getWork(workId);

        List<WorkExecutionEntity> completedWorks = workExecutionRepository.findByWorkAndPhase(work, WorkPhase.FINISHED);

        double correctData = completedWorks.stream().map(v -> v.getCorrectQuestionsAmount() * 100 / v.getQuestionsAmount()).collect(Collectors.averagingDouble(v -> v));
        double time = completedWorks.stream().map(v -> v.getEndTime().getTime() - v.getStartTime().getTime()).collect(Collectors.averagingLong(v -> v));
        int timeInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(Math.round(time));
        val test = testService.getTest(workId);

        List<Long> answerIds = workAnswerRepository.findByWork(work).stream().map(WorkAnswerEntity::getAnswerId).collect(Collectors.toList());
        val workAttempts = completedWorks.size();

        List<WorkStatisticQuestionData> questionsData = new ArrayList<>();
        for (QuestionEntity question : test.getQuestions()) {
            List<WorkStatisticAnswerData> answersData = new ArrayList<>();
            for (AnswerEntity answer : question.getAnswers()) {
                long count = answerIds.stream().filter(v -> v.equals(answer.getId())).count();
                int percent = workAttempts > 0 ? (int) (count * 100 / workAttempts) : 0;

                answersData.add(new WorkStatisticAnswerData(answer.getText(), answer.isCorrect(), percent));
            }

            questionsData.add(new WorkStatisticQuestionData(question.getText(), question.getType(), answersData));
        }

        return new WorkStatisticData(work.getName(),
                (int) Math.round(correctData),
                timeInMinutes,
                workAttempts,
                questionsData);
    }
}
