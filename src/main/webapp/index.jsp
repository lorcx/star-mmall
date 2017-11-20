<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
</head>
<body>
<h2>Hello World!</h2>

spring mvc 上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="spirng mvc上传">
</form>


富文本上传文件
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="富文本文件上传">
</form>


</body>
<script type="text/javascript">
    for (var i = 0; i < 100; i++) {
        console.log(i);
    }

</script>
</html>
