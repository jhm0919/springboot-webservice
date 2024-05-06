let isValidate = false;
var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });

        $('#btn-comment-save').on('click', function () {
            _this.commentSave();
        });

        $('#btn-modify').on('click', function() {
            _this.modify();
        });

        $('#btn-sendEmail').on('click', function() {
            _this.sendEmail();
        });

        $('#btn-findUsernameSendEmail').on('click', function() {
            _this.findUsernameSendEmail();
        });

        $('#btn-password-modify').on('click', function() {
            _this.btnPasswordModify();
        });


        // $('#btn-join').on('click', function() {
        //     _this.join();
        // });
        // 댓글 수정
        document.querySelectorAll('#btn-comment-update').forEach(function (item) {
            item.addEventListener('click', function () { // 버튼 클릭 이벤트 발생시
                const form = this.closest('form'); // btn의 가장 가까운 조상의 Element(form)를 반환 (closest)
                _this.commentUpdate(form); // 해당 form으로 업데이트 수행
            });
        });
    },
    save : function () {
        var content = editor.getData()
        var data = {
            postType: $('#postType').val(),
            title: $('#title').val(),
            author: $('#author').val(),
            content: content,
        };

        if (!data.title) {
            alert("제목을 입력해주세요.")
            return
        }

        if (!data.content) {
            alert("내용을 입력해주세요.")
            return
        }

        if (!data.postType) {
            alert("게시판을 선택해주세요.")
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/api/posts',
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8',
            dataType: 'JSON',
        }).done(function(response) {
            alert('게시글이 등록되었습니다.');
            window.location.href = "/posts/read/" + response;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var id = $('#id').val();
        var content = editor.getData()
        var data = {
            postType: $('#postType').val(),
            title: $('#title').val(),
            content: content,
        };

        if (!data.title) {
            alert("제목을 입력해주세요.")
            return
        }

        if (!data.content) {
            alert("내용을 입력해주세요.")
            return
        }

        if (!data.postType) {
            alert("게시판을 선택해주세요.")
            return;
        }

        $.ajax({
            type: 'PUT',
            url: '/api/posts/' + id,
            data: JSON.stringify(data),
            contentType: 'application/json; charset=utf-8',
            dataType: 'JSON',
        }).done(function(response) {
            alert('게시글이 수정되었습니다.');
            window.location.href = "/posts/read/" + response;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();

        if (confirm('정말 삭제하시겠습니까?')) {
            $.ajax({
                type: 'DELETE',
                url: '/api/posts/'+id,
                dataType: 'JSON',
                contentType:'application/json; charset=utf-8'
            }).done(function() {
                alert('게시글이 삭제되었습니다.');
                window.location.href = '/';
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    commentSave : function () {
        const data = {
            postsId: $('#postsId').val(),
            comment: $('#comment').val()
        }
        // 공백 및 빈 문자열 체크
        if (!data.comment || data.comment.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        } else {
            $.ajax({
                type: 'POST',
                url: '/api/posts/' + data.postsId + '/comments',
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                alert('댓글이 등록되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    /** 댓글 수정 */
    commentUpdate : function (form) {
        const data = {
            id: form.querySelector('#id').value,
            postsId: form.querySelector('#postsId').value,
            comment: form.querySelector('#comment-content').value,
        }
        if (!data.comment || data.comment.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }
        const con_check = confirm("수정하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'PUT',
                url: '/api/posts/' + data.postsId + '/comments/' + data.id,
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    /** 댓글 삭제 */
    commentDelete : function (postsId, commentId) {
        const con_check = confirm("삭제하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'DELETE',
                url: '/api/posts/' + postsId + '/comments/' + commentId,
                dataType: 'JSON',
            }).done(function () {
                alert('댓글이 삭제되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },
    postRecommend: function (userId) {
        var id = $('#id').val();
        var postUserId = $('#postUserId').val();
        if (userId == postUserId) {
            confirm("본인의 게시글은 추천할 수 없습니다.");
            return false;
        }
        $.ajax({
            type: 'PUT',
            url: '/api/posts/' + id + '/recommend',
            dataType: 'JSON',
            contentType: 'application/json'
        }).done(function (response) {
            var recommendUpCount = response.recommendUpCount;
            $('#recommendUpCount').text(recommendUpCount);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    postDisRecommend: function (userId) {
        var id = $('#id').val();
        var postUserId = $('#postUserId').val();
        if (userId == postUserId) {
            confirm("본인의 게시글은 비추천할 수 없습니다.");
            return false;
        }
        $.ajax({
            type: 'PUT',
            url: '/api/posts/' + id + '/disRecommend',
            dataType: 'JSON',
            contentType: 'application/json'
        }).done(function (response) {
            var recommendDownCount = response.recommendDownCount;
            $('#recommendDownCount').text(recommendDownCount);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    commentRecommend: function (postId, commentId, userId, commentUserId) {
        // if (isRecommend) {
        //     alert("이미 추천 또는 비추천한 게시물입니다.");
        //     return false;
        // }
        if (commentUserId == userId) {
            confirm("본인 댓글은 추천할 수 없습니다.");
            return false;
        }
        $.ajax({
            type: 'PUT',
            url: '/api/posts/' + postId + '/comments/' + commentId + '/recommend',
            dataType: 'JSON',
            contentType: 'application/json'
        }).done(function (response) {
            var recommendUpCount = response.recommendUpCount;
            // var isRecommend = response.recommend;
            $('#commentRecommendUpCount_' + commentId).text(recommendUpCount);
            // if (isRecommend) {
            //     alert("추천 되었습니다.");
            // } else {
            //     alert("추천이 취소되었습니다.");
            // }
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    commentDisRecommend: function (postId, commentId, userId, commentUserId) {
        // if (isRecommend) {
        //     alert("이미 추천 또는 비추천한 게시물입니다.");
        //     return false;
        // }
        if (commentUserId == userId) {
            confirm("본인 댓글은 비추천할 수 없습니다.");
            return false;
        }
        $.ajax({
            type: 'PUT',
            url: '/api/posts/' + postId + '/comments/' + commentId + '/disRecommend',
            dataType: 'JSON',
            contentType: 'application/json'
        }).done(function (response) {
            // var isRecommend = response.recommend;
            var recommendDownCount = response.recommendDownCount;
            // if (isRecommend) {
            //     alert("비추천 되었습니다.");
            // } else {
            //     alert("비추천이 취소되었습니다.");
            // }
            $('#commentRecommendDownCount_' + commentId).text(recommendDownCount);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    modify: function () {
        const data = {
            id: $('#id').val(),
            modifiedDate: $('#modifiedDate').val(),
            username: $('#username').val(),
            sessionName: $('#sessionName').val(),
            name: $('#name').val(),
            email: $('#email').val(),
            password: $('#password').val()
        }
        if (!data.name || data.name.trim() === "" ||
            !data.password || data.password.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }  else if(!/^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/.test(data.name)) {
            alert("닉네임은 특수문자를 제외한 2~10자리여야 합니다.");
            $('#name').focus();
            return false;
        }

        const con_check = confirm("수정하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'PUT',
                url: '/api/update',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function (response) {
                console.log(response)
                alert(response)
                window.location.href = "/";
            }).fail(function (error) {
                if (error.status == 405) {
                    alert("비밀번호가 다릅니다.");
                    $('#password').focus();
                } else {
                    alert(error.responseText);
                    $('#name').focus();
                }
                // alert(JSON.stringify(error));
            });
        }
    },

    sendEmail: function () {
        var email = $('#email').val();
        // 타이머 표시할 요소 선택
        const timerDisplay = document.getElementById('timer');
        // 시작 시간 설정 (1분)
        let timeLeft = 60;

        function countdown() {
            // 1초마다 시간 감소
            const timer = setInterval(() => {
                // 시간을 분과 초로 변환
                const minutes = Math.floor(timeLeft / 60);
                let seconds = timeLeft % 60;

                // 10보다 작은 초는 앞에 0을 붙여서 표시
                if (seconds < 10) {
                    seconds = '0' + seconds;
                }

                // 타이머 표시 업데이트
                timerDisplay.textContent = `${minutes}:${seconds}`;

                // 시간이 다 되면 타이머 중지
                if (timeLeft <= 0) {
                    clearInterval(timer);
                    timerDisplay.textContent = '시간 종료';
                }

                // 시간을 1초씩 감소
                timeLeft--;
            }, 1000);
        }

        $.ajax({
            type: 'POST',
            url: '/auth/mailSend',
            contentType: 'application/json; charset=utf-8',
            data: email
        }).done(function (response) {
            console.log(response);
            alert(response);

            const timerDisplay = document.getElementById('timer');

            // 시작 시간 설정 (1분)
            let timeLeft = 60;

            // 카운트다운 함수 호출
            countdown();
        }).fail(function (error) {
            // 이메일 이상할때 예외 처리필요
            alert(error.responseText);
        });
    },

    findUsernameSendEmail: function () {
        var email = $('#email').val();
        $.ajax({
            type: 'POST',
            url: '/auth/findUsernameMailSend',
            contentType: 'application/json; charset=utf-8',
            data: email
        }).done(function (response) {
            console.log(response);
            alert(response);
        }).fail(function (error) {
            // 이메일 이상할때 예외 처리필요
            alert(error.responseText);
        });
    },

    btnPasswordModify: function () {
        const data = {
            id: $('#id').val(),
            password: $('#password').val()
        }
        const passwordConfirm = $('#passwordConfirm').val()

        if (!data.password || data.password.trim() === "" ||
            !passwordConfirm || passwordConfirm.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
        } else if (!/(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}/.test(data.password)) {
            alert("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
            $('#password').focus();
            return false;
        } else if (data.password !== passwordConfirm) {
            alert("비밀번호가 일치하지 않습니다.");
            $('#passwordConfirm').focus();
            return false;
        }

        const con_check = confirm("수정하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'PUT',
                url: '/api/update-password',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function (response) {
                console.log(response)
                alert(response)
                window.location.href = "/";
            }).fail(function (error) {
                alert(error.responseText);
                $('#name').focus();
                // alert(JSON.stringify(error));
            });
        }
    },

    redirectToLoginPage: function () {
        if (confirm("로그인이 필요한 기능입니다. 로그인 하시겠습니까?")) {
            window.location.href = '/auth/login';
        }
    },

};

main.init();