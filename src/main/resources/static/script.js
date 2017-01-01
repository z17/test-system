$(document).ready(function() {
    // create work

    var QUESTION_NAME_PREFIX = 'question';
    var QUESTION_TYPE_PREFIX = 'question-type';
    var ANSWER_NAME_PREFIX = 'answer';
    var ANSWER_RIGHT_POSTFIX = 'right';
    
    function getName(array) {
        return array.join("-");
    }

    function deleteAnswer() {
        $(this).parent().remove();
    }

    function deleteQuestion() {
        $(this).closest('.question').remove();
    }

    function currentQuestionId(child) {
        return $(child).closest('.question').data('id');
    }

    function lastQuestionId() {
        return $('.questions .question:last').data('id');
    }

    function lastAnswerId(child) {
        return $(child).closest('.question').find('.answers .answer:last').data('id');
    }

    function refreshAnswer(answer, questionId, answerId) {
        $(answer).find('input[type=text]')
            .val('')
            .attr('name', getName([QUESTION_NAME_PREFIX, questionId, ANSWER_NAME_PREFIX, answerId]));
        $(answer).find('input[type=checkbox]')
            .prop('checked', false)
            .attr('name', getName([QUESTION_NAME_PREFIX, questionId, ANSWER_NAME_PREFIX, answerId, ANSWER_RIGHT_POSTFIX]));
        $(answer).attr('data-id', answerId);
        $(answer).find('.deleteAnswer').click(deleteAnswer);
    }

    function addAnswerFunction() {
        var newId = lastAnswerId(this) + 1;
        var answers = $(this).closest('.answers');
        var a = answers.find('.field.answer:first').clone();
        refreshAnswer(a, currentQuestionId(this), newId);
        a.insertAfter(answers.find('.field.answer:last'));
    }

    $('#addAnswer').click(addAnswerFunction);

    $('#addQuestion').click(function () {
        var questions = $(this).closest('ol.questions');
        var newId = lastQuestionId() +  1;
        var newQuestion = questions.find('.question:first').clone();
        $(newQuestion).find('input[type=text]').val('');
        $(newQuestion).find('input[type=checkbox]').prop('checked', false);
        $(newQuestion).find('.deleteQuestion').click(deleteQuestion);

        $(newQuestion).attr('data-id', newId);
        $(newQuestion).find('input.question-text').attr('name', getName([QUESTION_NAME_PREFIX, newId]));
        $(newQuestion).find('input.question-type').attr('name', getName([QUESTION_TYPE_PREFIX, newId]));

        $(newQuestion).find('.answers .answer').slice(1).remove();
        refreshAnswer($(newQuestion).find('.answers .answer:first'), newId, 1);
        $(newQuestion).find('.answers #addAnswer').click(addAnswerFunction);
        newQuestion.insertAfter(questions.find('.question:last'));
    });


    $('.deleteAnswer').click(deleteAnswer);
    $('.deleteQuestion').click(deleteQuestion);

});