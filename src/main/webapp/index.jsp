<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<html>

<body>
<h2>Hello World!</h2>

<form name="form" action="/manage/product/fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile"/>
    <input type="submit" value="springmvc文件上传"/>
</form>
<form name="form" action="/manage/product/richtextImgUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="uploadFile"/>
    <input type="submit" value="富文本文件上传"/>
</form>

</body>
</html>
