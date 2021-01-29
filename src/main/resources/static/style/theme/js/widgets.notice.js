var socket = io.connect(getHostWithoutPort() + ':'+socket_port+'?clientid=' + userid);
socket.on('connect', function () {
    output(1, '<span class="connect-msg"> 你好' + username + '，欢迎登陆系统</span>');
});
socket.on('messageevent', function (data) {
    output(0, "通知："+data.title);
    tipMsg(data);
});
socket.on('disconnect', function () {
    output(3, '<span class="disconnect-msg">与服务器的连接已关闭！</span>');
});

function output(flag, message) {
    //toast显示
    showMessage(flag, message, 1, '');
}

/**
 * 新增一个未读消息
 * @param data 对应后台的NoticeInfo 类
 */
function tipMsg(data) {
    var pngstr = "";
    if (data.type == 1) {
        pngstr = "message";
    } else if (data.type == 2) {
        pngstr = "email";
    } else {
        pngstr = "clock";
    }

    var baseUrl = getRootPath();
    var url = baseUrl+"/noticeInfo/view/"+data.pkid;
    var $msg = '<a href="javascript:void(0)" onclick="javascript:showNoticeDetail(\''+data.pkid+'\',\''+data.title+'\',\''+data.content+'\',\''+data.fromwhoname+'\',\''+data.senttime+'\');" class="media list-group-item">' +
        '<span class="pull-left thumb-sm text-center">' +
        // '<i class="fa fa-envelope-o fa-2x text-success"></i>' +
        '<img src="'+baseUrl+'/style/theme/images/' + pngstr + '.png" alt="..." class="img-circle">' +
        '</span>' +
        '<span class="media-body block m-b-none">' +
        '通知：' + data.title + '<br>' +
        '<small class="text-muted"><i class="fa fa-user-o"></i> ' + data.fromwhoname +
        ' &nbsp;&nbsp; <i class="fa fa-clock-o"></i> '
        + data.senttime +
        '</small>' +
        '</span>' +
        '</a>';

    var $el = $('.nav-user'), $n = $('.count:first', $el), $v = parseInt($n.text());
    $('.count', $el).fadeOut().fadeIn().text($v + 1);
    $($msg).hide().prependTo($el.find('.list-group')).slideDown().css('display', 'block');
    //修改界面上未读消息的数量
    $('#head_notice_num_id1').text((parseInt($('#head_notice_num_id1').text())+1));
    $('#head_notice_num_id2').text((parseInt($('#head_notice_num_id2').text())+1));
}

//显示消息内容
function showNoticeDetail(pkid,title,content,fromwhoname,senttime) {
    //设置modal内容
    $('#noticeDetail_senttime').text(senttime);
    $('#noticeDetail_fromwhoname').text(fromwhoname);
    $('#noticeDetail_title').text(title);
    $('#noticeDetail_content').text(content);
    $('#todo-task-modal').modal('show');
    //设置当前为已读
    setNoticeReadState(pkid);
}

/**
 * 设置某消息为已读
 */
function setNoticeReadState(pkid) {
    var url = getRootPath()+"/noticeInfo/hadread/"+pkid;
    jQuery.post(url, {}, function (jsonData) {
        if (jsonData != null) {
            var flag = jsonData.flag;
            var message = jsonData.msg;
            if (flag != true) {
                showMessage(3, message, 3, '设置消息已读失败');
            }else {
                //重新加载未读消息列表
                reloadUnreadNoticeList();
            }
        }
    }, "json");
}

/**
 * 重新加载首页头部未读消息列表[只加载最近5条记录]
 */
function reloadUnreadNoticeList() {
    //清空前台的消息提醒，重新从后台获取。然后再拼接上显示。
    $('#unReadNoticeListDiv').children('a').remove();
    //修改界面上未读消息的数量
    var $el = $('.nav-user');
    $('.count', $el).fadeOut().fadeIn().text(0);
    $('#head_notice_num_id1').text(0);
    $('#head_notice_num_id2').text(0);
    //从后台获取未读消息列表
    var url = getRootPath()+"/noticeInfo/unread/json"
    jQuery.post(url, {}, function (jsonData) {
        if (jsonData != null) {
            for(var i=0; i<jsonData.length; i++) {
                tipMsg(jsonData[i]);
            }
        }
    }, "json");
}
