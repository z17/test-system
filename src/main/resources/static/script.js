$(document).ready(function () {
    // create work

    function deleteAnswer() {
        $(this).parent().remove();
    }

    function deleteQuestion() {
        $(this).closest('.question').remove();
    }


    function refreshAnswer(answer) {
        $(answer).find('input[type=text]')
            .val('');
        $(answer).find('input[type=checkbox]')
            .prop('checked', false);
        $(answer).find('.deleteAnswer').click(deleteAnswer);
    }

    function addAnswerFunction() {
        let answers = $(this).closest('.answers');
        let a = answers.find('.field.answer:first').clone();
        refreshAnswer(a);
        a.insertAfter(answers.find('.field.answer:last'));
    }

    $('#addAnswer').click(addAnswerFunction);

    $('#addQuestion').click(function () {
        let questions = $(this).closest('ol.questions');
        let newQuestion = questions.find('.question:first').clone();
        $(newQuestion).find('input[type=text]').val('');
        $(newQuestion).find('input[type=checkbox]').prop('checked', false);
        $(newQuestion).find('.deleteQuestion').click(deleteQuestion);

        $(newQuestion).find('input.question-text');
        $(newQuestion).find('input.question-type');

        $(newQuestion).find('.answers .answer').slice(1).remove();
        refreshAnswer($(newQuestion).find('.answers .answer:first'));
        $(newQuestion).find('.answers #addAnswer').click(addAnswerFunction);
        newQuestion.insertAfter(questions.find('.question:last'));
    });

    $('.deleteAnswer').click(deleteAnswer);
    $('.deleteQuestion').click(deleteQuestion);

    $(document).on('submit', 'form.create-test', function () {
        let data = {
            title: $('form.create-test #title').val(),
            description: $('form.create-test #description').val(),
            theory: $('form.create-test #theory').val(),
            testDescription: $('form.create-test #test-description').val(),
            questions: [],
        };
        $('form.create-test .questions .question').each(function () {
            let answers = [];
            $(this).find('.answers .answer').each(function () {
               let answer = {
                   text: $(this).find('.answer-field').val(),
                   correct: $(this).find('.correct-field').is(":checked"),
               };
               answers.push(answer);
            });

            data.questions.push(
                {
                    text: $(this).find('.question-text').val(),
                    type: $(this).find('.question-type').val(),
                    answers: answers
                }
            );
        });

        $.ajax({
            url: '/api/createWork',
            dataType : 'json',
            method : 'POST',
            data: JSON.stringify(data),
            contentType : 'application/json'
        }).done(function () {
            alert('Работа добавлена');
            $('form.create-test')[0].reset();
        }).fail(function () {
            alert('Ошибка');
        });

        return false;
    });
});