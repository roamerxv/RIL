var fileinput_options =
    {
        theme: 'fa',
        language: 'zh',
        uploadUrl: contextPath + "updateAvatar.json",  //input file 的 name 必须和 controller 里面的 @RequestPart的参数名一样
        uploadAsync: true,
        maxFileCount: 1,
        previewFileType: ['jpg', 'png', 'png', 'jpeg'],
        allowedFileExtensions: ['jpg', 'png', 'png', 'jpeg'],
        autoReplace: true, //布尔值，当上传文件达到maxFileCount限制并且一些新的文件被选择后，是否自动替换预览中的文件。如果maxFileCount值有效，那么这个参数会起作用。默认为false。
        // initialPreview: [
        //     "<img src='/avatar?'+new Date().getTime() class='file-preview-image' style='width:auto;height:auto;max-width:100%;max-height:100%;' alt='Desert' title='Desert'>",
        // ],
    };

$().ready(function () {
    // 使用 bootstrap file input 组件
    $("input[name='avatar']").fileinput(fileinput_options).on('fileselect', function(event, numFiles, label) {
        $(this).fileinput('upload');
    }).on('fileuploaded', function (event, data, previewId, index) {
        //$("img[name='avatar']").attr("src", contextPath + "avatar");
        var avatarImgs = $("img[name='avatar']");
        for (var i = 0; i <= avatarImgs.length; i++) {
            //$(avatarImgs[i]).attr("src", contextPath + "avatar");
            Logger.debug($(avatarImgs[i]));
            $(avatarImgs[i]).attr("src", contextPath + "avatar" + '?' + new Date().getTime());
        }
        showMessage("success", "头像更新", data.jqXHR.responseJSON.data.localMessage);
        // 重置 fileinput
        $(this).fileinput('destroy');
        $(this).fileinput(fileinput_options).fileinput('enable');
    }).on('fileuploaderror', function (event, data, previewId, index) {
        Logger.debug(data);
        showMessage("danger", "头像更新", data.jqXHR.responseJSON.data[0].errorMessage);
    });
})
