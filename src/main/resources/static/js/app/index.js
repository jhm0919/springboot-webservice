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
            email: $('#email').val(),
            name: $('#name').val(),
            password: $('#password').val()
        }
        const passwordConfirm = $('#passwordConfirm').val()
        if (!data.name || data.name.trim() === ""
            || !data.password || data.password.trim() === ""
            || !passwordConfirm || passwordConfirm.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        } else if (!/(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}/.test(data.password)) {
            alert("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
            $('#password').focus();
            return false;
        } else if(!/^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/.test(data.name)) {
            alert("닉네임은 특수문자를 제외한 2~10자리여야 합니다.");
            $('#name').focus();
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
                url: '/api/update',
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                alert("회원정보가 수정되었습니다.")
                window.location.href = "/";
            }).fail(function (error) {
                if (error.status == 500) {
                    alert("이미 사용중인 닉네임 입니다.");
                    $('#name').focus();
                } else {
                    alert(JSON.stringify(error));
                }
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