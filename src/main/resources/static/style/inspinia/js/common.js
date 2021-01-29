/**
 * 遮罩层显示
 */
function showMLoading(){
    myMLoading = $.confirm({
//            icon: 'fa fa-spinner fa-spin',
        icon: 'fa fa-spinner fa-pulse',
        title: '加载中...', // hides the title.
        cancelButton: false, // hides the cancel button.
        confirmButton: false, // hides the confirm button.
        closeIcon: false, // hides the close icon.
        content: false // hides content block.
    });
}
//    function showMLoading(id) {
//        var intRandom =  Math.floor(Math.random()*2);
//        var mLoadingOpt = {
//            text:"",//加载文字，默认值：加载中...
//            icon:"inspinia/img/loading/loading0"+intRandom+".gif",//加载图标，默认值：一个小型的base64的gif图片
//            html:false,//设置加载内容是否是html格式，默认值是false
//            content:"",//忽略icon和text的值，直接在加载框中显示此值
//            mask:true//是否显示遮罩效果，默认显示
//        };
//        $("#"+id).mLoading(mLoadingOpt);
//        $(".mloading-icon").css('width','200px');
//        $(".mloading-icon").css('height','150px');
//    }
/**
 * 遮罩层隐藏
 */
function hideMLoading() {
    myMLoading.close();
}
//    function hideMLoading(id) {
//        $("#"+id).mLoading("hide");
//    }
/**
 * 提示框
 * flag = 1 success
 * flag = 2 warning
 * flag = 3 error
 */
function showMessage(flag,message,timeOut,title) {
    if(title == null || title == undefined) {
        title = '';
    }
    if(timeOut !=null && timeOut != undefined) {
        timeOut = timeOut * 1000;
    }
    toastr.options = {
        closeButton: true,
        progressBar: true,
        showMethod: 'slideDown',
        timeOut: timeOut
    };
    if(flag == 0) {
        toastr.info(title,message);
    }else if(flag == 1) {
        toastr.success(title,message);
    }else if(flag == 2) {
        toastr.warning(title,message);
    }else {
        toastr.error(title,message);
    }
}
/**
 * 获取右边面板组件
 */
function getPage(db) {
    $.get("db/"+db, function (data) {
        $('#content-div').remove();
        $('#main-div').append("<div id=\"content-div\"></div>");
        $('#content-div').append(data);
    });
}

//animate.css动画触动一次方法
$.fn.extend({
    animateCss: function (animationName) {
        var animationEnd = 'webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend';
        this.addClass('animated ' + animationName).one(animationEnd, function() {
            $(this).removeClass('animated ' + animationName);
        });
    }
});
/**
 * 显示模态框方法
 * @param targetModel 模态框选择器，jquery选择器
 * @param animateName 弹出动作
 * @param href 远程调用
 */
var modalShow = function(animateName, href){
    var targetModel = "#modal-main";
    $('#modal-content').remove();
    $('#modal-dialog').append("<div class=\"modal-content\"></div>");
    var animationIn = [
        "rubberBand","shake","bounce","flash","pulse","swing","tada", "wobble",
        "bounceIn","bounceInDown","bounceInLeft","bounceInRight",
        "bounceInUp", "fadeIn", "fadeInDown", "fadeInLeft", "fadeInRight",
        "fadeInDownBig", "fadeInLeftBig","flipInX","flipInY",
        "lightSpeedIn","rotateIn","rotateInDownLeft","rotateInDownRight","rotateInUpLeft","rotateInUpRight",
        "slideInDown","slideInLeft",
        "slideInRight", "slideInUp","rollIn","flip"];
    if(!animateName || animationIn.indexOf(animateName)==-1){
        var intRandom =  Math.floor(Math.random()*animationIn.length);
        animateName = animationIn[intRandom];
    }
//        console.log(targetModel + " " + animateName);
    $(targetModel).modal({
        remote:href
    }).animateCss(animateName);
    //callback.apply(this);
}
/**
 * 隐藏模态框方法
 * @param targetModel 模态框选择器，jquery选择器
 * @param animateName 隐藏动作
 * @ callback 回调方法
 */
var modalHide = function(flag,animateName, callback){

    var targetModel = "#modal-main";
    if(flag) {
        $(targetModel).modal('hide');
    }else {
        var animationOut = [
            "bounceOut","bounceOutDown","bounceOutLeft","bounceOutRight","bounceOutUp",
            "fadeOut", "fadeOutDown", "fadeOutLeft", "fadeOutRight", "fadeOutUp",
            "fadeOutDownBig", "fadeOutLeftBig", "fadeOutRightBig", "fadeOutUpBig",
            "flipOutX","flipOutY", "lightSpeedOut","rotateOut","rotateOutDownLeft",
            "rotateOutDownRight","rotateOutUpLeft","rotateOutUpRight",
            "slideOutDown","slideOutLeft", "slideOutRight", "slideOutUp",
            "rollOut","hinge"];
        if(!animateName || animationOut.indexOf(animateName)==-1){
            console.log(animationOut.length);
            var intRandom =  Math.floor(Math.random()*animationOut.length);
            animateName = animationOut[intRandom];
        }

        $(targetModel).children().click(function(e){e.stopPropagation()});
        $(targetModel).animateCss(animateName);
        $(targetModel).delay(900).hide(1,function(){
            $(this).removeClass('animated ' + animateName);
            $(targetModel).modal("toggle");
        });
        callback.apply(this);
    }
}

/**
 * 获取项目根路径
 * @returns {string}
 */
function getRootPath() {
    var strFullPath = window.document.location.href;
    var strPath = document.location.pathname;
    if(!(strPath.lastIndexOf("/") === strPath.indexOf("/"))) {
        strPath = strPath.substring(0,strPath.lastIndexOf("/"));
    }
    var pos = strFullPath.indexOf(strPath);
    var prePath = strFullPath.substring(0, pos);
    // var postPath = strPath.substring(0,strPath.substring(1).indexOf("/")+1);
    return prePath+strPath;
}
function getHostPath() {
    var strFullPath = window.document.location.href;
    var strPath = document.location.pathname;
    if (strPath == null || strPath == '/') {
        return strFullPath;
    } else {
        var pos = strFullPath.indexOf(strPath);
        var prePath = strFullPath.substring(0, pos);
        return prePath;
    }
}
var modalDataInit = function(info){
    //alert(info);
    //填充数据，对弹出模态框数据样式初始化或修改
}
