$(document).ready(function () {

    // create/edit work

    function deleteAnswer() {
        let answer = $(this).parent();
        answer.remove();
    }

    function deleteQuestion() {
        let question = $(this).closest('.question');
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

    function addQuestion() {
        let newQuestion = $('.question.sample').clone();
        newQuestion.removeClass('sample');
        bindAnswerDelete($(newQuestion).find('.answers .answer:first'));
        $(newQuestion).find('.answers .addAnswer').click(addAnswerFunction);
        $(newQuestion).find('.question-type').attr('name', 'question-type-' + ($('ol.questions li.question').length + 1));
        newQuestion.insertBefore($('.addQuestion'));
        CKEDITOR.replace(newQuestion.find('.editor').get(0));
        $(newQuestion).find('.deleteQuestion').click(deleteQuestion);
    }

    $('.addAnswer').click(addAnswerFunction);

    $('.addQuestion').click(addQuestion);

    $('.deleteAnswer').click(deleteAnswer);

    $('.deleteQuestion').click(deleteQuestion);

    $(document).on('submit', 'form.form-work', function () {
        let data = {
            id: $(this).data('id'),
            name: $(this).find('#title').val(),
            description: $(this).find('#description').val(),
            theory: $(this).find('#theory').val(),
            testDescription: $(this).find('#test-description').val(),
            questions: [],
            lab: $(this).find('#lab').val(),
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
                    type: $(this).find('.question-type:checked').val(),
                    answers: answers
                }
            );
        });

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

    // main page
    $('li.work .delete').click(function () {
        if (confirm('Удалить?')) {
            let id = $(this).data('id');
            let self = this;
            $.ajax({
                url: '/api/work/' + id + '/delete',
                dataType: 'json',
                method: 'GET',
                contentType: 'application/json'
            }).done(function () {
                alert('Удалено');
                $(self).parent().remove();
            }).fail(function () {
                alert('Ошибка');
            });
        }
    });

    $(document).on('submit', 'form.user-edit', function () {
       if ($('form.user-edit #password').val() != $('form.user-edit #password2').val()) {
           alert('Пароли не совпадают!');
           return false;
       }
       return true;
    });

    // CKEditor
    if ($('.form-work').length != 0) {
        CKEDITOR.replaceAll("editor");
    }

    if ($('.create-work').length != 0) {
        // addQuestion();
    }

});