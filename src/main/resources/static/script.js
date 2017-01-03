$(document).ready(function () {
    // create/edit work

    let deletedAnswers = [];
    let deletedQuestions = [];

    function deleteAnswer() {
        let answer = $(this).parent();
        if (answer.data('id') != undefined) {
            deletedAnswers.push(answer.data('id'));
        }
        answer.remove();
    }

    function deleteQuestion() {
        let question = $(this).closest('.question');
        if (question.data('id') != undefined) {
            deletedQuestions.push(question.data('id'));
        }
        question.remove();
    }


    function bindAnswerDelete(answer) {
        $(answer).find('.deleteAnswer').click(deleteAnswer);
    }

    function addAnswerFunction() {
        let answers = $(this).closest('.answers');
        let a = $('.field.answer.sample').clone();
        a.removeClass('sample');
        bindAnswerDelete(a);
        a.insertAfter(answers.find('.field.answer:last'));
    }

    $('.addAnswer').click(addAnswerFunction);

    $('.addQuestion').click(function () {
        let questions = $(this).closest('ol.questions');
        let newQuestion = $('.question.sample').clone();
        newQuestion.removeClass('sample');
        bindAnswerDelete($(newQuestion).find('.answers .answer:first'));
        $(newQuestion).find('.answers .addAnswer').click(addAnswerFunction);
        newQuestion.insertAfter(questions.find('.question:last'));
    });

    $('.deleteAnswer').click(deleteAnswer);
    $('.deleteQuestion').click(deleteQuestion);

    $(document).on('submit', 'form.form-work', function () {
        let data = {
            id : $(this).data('id'),
            name: $(this).find('#title').val(),
            description: $(this).find('#description').val(),
            theory: $(this).find('#theory').val(),
            testDescription: $(this).find('#test-description').val(),
            questions: [],
        };
        $(this).find('.questions .question').each(function () {
            let answers = [];
            $(this).find('.answers .answer').each(function () {
                let answer = {
                    id: $(this).data('id'),
                    text: $(this).find('.answer-field').val(),
                    correct: $(this).find('.correct-field').is(":checked"),
                };
                answers.push(answer);
            });

            data.questions.push(
                {
                    id: $(this).data('id'),
                    text: $(this).find('.question-text').val(),
                    type: $(this).find('.question-type').val(),
                    answers: answers
                }
            );
        });

        console.log(data);
        let self = this;
        $.ajax({
            url: '/api/updateWork',
            dataType: 'json',
            method: 'POST',
            data: JSON.stringify(data),
            contentType: 'application/json'
        }).done(function () {
            alert('Сохранено');
            if ($(self).hasClass('create-work')) {
                self.reset();
            }
        }).fail(function () {
            alert('Ошибка');
        });

        return false;
    });
});