$(document).ready(function(){

    var folders = $('.folder');
    var foldareas = $('.foldarea');

    //鼠标移入文件夹节点上，节点文字变色，鼠标指针改变
    folders.hover(
        function(){
            $(this).addClass('hover');
        },
        function(){
            $(this).removeClass('hover');
        }
    );

    var doFold = function(){
        var ul = $('ul',this.parentNode)[0];
        var foldarea = $('.foldarea',this.parentNode)[0];
        var folder = $('.folder',this.parentNode)[0];
        if(!ul){
            return;
        }


        var ulDisplay =   $(ul).css('display');
        if(ulDisplay==='none'){
            //展开列表
            $(ul).css('display','block');

            //显示展开时的文件夹图标
            $(folder).removeClass('folder-closed');
            $(folder).addClass('folder-opened');

            //展开时显示可折叠图标
            $(foldarea).removeClass('foldarea-expandable');
            $(foldarea).addClass('foldarea-collapsable');

        }else{
            //通过隐藏来实现折叠列表
            $(ul).css('display','none');

            //显示折叠时的文件夹图标
            $(folder).removeClass('folder-opened');
            $(folder).addClass('folder-closed');

            //折叠时显示可展开图标
            $(foldarea).removeClass('foldarea-collapsable');
            $(foldarea).addClass('foldarea-expandable');
        }
    };

    //将文件夹节点下的列表折叠或展开
    folders.click(doFold);
    foldareas.click(doFold);
});