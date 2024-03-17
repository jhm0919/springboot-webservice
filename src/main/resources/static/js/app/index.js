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

        $('#recommend').on('click', function(e) {
            _this.recommend();
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
        var formData = new FormData();
        var data = {
            postType: $('#postType').val(),
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val(),
            userId: '<%= session.getAttribute("userId") %>'
        };
        var inputFile = $("input[type='file']");
        var files = inputFile[0].files;

        formData.append('json_data', new Blob([JSON.stringify(data)],
            {type: 'application/json; charset=UTF-8;'}));
        for(let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }

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
            data: formData,
            contentType:false,
            processData: false,
            cache: false,
            dataType: 'JSON',
        }).done(function() {
            alert('게시글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        var formData = new FormData();
        var data = {
            title: $('#title').val(),
            content: $('#content').val(),
        };
        var id = $('#id').val();
        var inputFile = $("input[type='file']");
        var files = inputFile[0].files;

        formData.append('json_data', new Blob([JSON.stringify(data)],
            {type: 'application/json; charset=UTF-8;'}));
        for(let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }

        if (!data.title) {
            alert("제목을 입력해주세요.")
            return
        }

        if (!data.content) {
            alert("내용을 입력해주세요.")
            return
        }

        $.ajax({
            type: 'PUT',
            url: '/api/posts/'+id,
            data: formData,
            contentType:false,
            processData: false,
            cache: false,
            dataType: 'JSON',
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
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
    /** 이미지 삭제 */
    imageDelete : function (postsId, imageId) {
        $.ajax({
            type: 'DELETE',
            url: '/api/posts/' + postsId + '/images/' + imageId,
            dataType: 'JSON',
        }).done(function () {
            alert('이미지가 삭제되었습니다.');
            window.location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    recommend: function () {
        var id = $('#id').val();
        $.ajax({
            type: 'PUT',
            url: '/api/posts/' + id + '/recommend',
            dataType: 'JSON',
            contentType: 'application/json'
        }).done(function (response) {
            var isRecommend = response.isRecommend;
            var recommendCount = response.recommendCount;

            if (isRecommend) {
                $('#recommend').text('추천 취소');
            } else {
                $('#recommend').text('추천');
            }
            $('#recommendCount').text(recommendCount);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

    // recommend: function () {
    //     var id = $('#id').val();
    //     var url = '/api/posts/' + id + '/recommend';
    //     var isRecommended = false; // 변수 추가
    //     $('#recommend').text('추천 취소');
    //
    //     // 현재 추천 상태를 확인하여 적절한 URL을 결정
    //     if ($('#recommend').hasClass('recommended')) {
    //         url += '/cancel'; // 이미 추천된 경우 취소 URL로 변경
    //         isRecommended = true; // 추천된 상태임을 표시
    //         $('#recommend').text('추천');
    //     }
    //
    //     $.ajax({
    //         type: 'PUT',
    //         url: url,
    //         dataType: 'JSON',
    //         contentType: 'application/json'
    //     }).done(function () {
    //         var recommendCountElement = $("#recommendCount");
    //         var currentCount = parseInt(recommendCountElement.text());
    //
    //         if (isRecommended) {
    //             // 추천이 취소된 경우 추천 수를 감소
    //             recommendCountElement.text(currentCount - 1);
    //             $('#recommend').removeClass('recommended'); // 버튼 스타일 변경
    //         } else {
    //             // 추천이 성공적으로 처리될 때마다 추천 수를 1만큼 증가
    //             recommendCountElement.text(currentCount + 1);
    //             $('#recommend').addClass('recommended'); // 버튼 스타일 변경
    //         }
    //     }).fail(function (error) {
    //         alert(JSON.stringify(error));
    //     });
    // }


};

main.init();