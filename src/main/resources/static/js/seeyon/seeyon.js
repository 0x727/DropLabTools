function appendform(){
    var type=$("#select").val()
    var appendform=$('#appendform')
    appendform.empty()
    switch (type){
        case "MemoryShell":
            appendform.append("<div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">内存马中间件</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <select name=\"mType\">\n" +
                "          <option selected=\"selected\">Tomcat</option>\n" +
                "          <option>Resin</option>\n" +
                "          <option>Jetty</option>\n" +
                "        </select>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">内存马类型</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <select name=\"mMiddle\">\n" +
                "          <option selected=\"selected\">Valve</option>\n" +
                "          <option>Filter</option>\n" +
                "          <option>Listner</option>\n" +
                "        </select>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">内存马路径</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <input class=\"form-control\" type=\"text\" name=\"path\" value='/faviconimge.ico' placeholder=\"Filter内存马需要填入路径\">\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">内存马密码</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <input class=\"form-control\" type=\"text\" name=\"password\" value='qax36oNb' placeholder=\"内存马密码,默认：qax36oNb\">\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">菜刀类型</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <select name=\"mshellType\">\n" +
                "          <option selected=\"selected\">Behinder</option>\n" +
                "          <option>Godzilla</option>\n" +
                "        </select>\n" +
                "      </div>\n" +
                "    </div>")
            break;
        case "Execute":
            appendform.append("<div class=\"form-group\">\n" +
                "            <label class=\"col-md-2 control-label\">中间件类型</label>\n" +
                "            <div class=\"col-md-10\">\n" +
                "                <select name=\"mType\">\n" +
                "                    <option selected=\"selected\">Tomcat</option>\n" +
                "                    <option>Resin</option>\n" +
                "                    <option>Jetty</option>\n" +
                "                </select>\n" +
                "            </div>\n" +
                "        </div><div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">执行命令</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <input class=\"form-control\" type=\"text\" name=\"cmd\" placeholder=\"calc.exe\">\n" +
                "      </div>\n" +
                "    </div>")
            break;
        case "UploadShell":
            appendform.append("<div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">WEBSHELL</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <input type=\"file\" name=\"files\" placeholder=\"你要写入的webshell\">\n" +
                "      </div>\n" +
                "    </div>");
            break

        default:
            appendform.empty()
            break
    }
}

function changeUrl(){
    var type=$("#uType").val()
    var BugUrl=$("#BugUrl")
    BugUrl.empty()
    switch (type){
        case "changeLocale":
            BugUrl.text("漏洞路径：/seeyon/main.do?method=changeLocale，适用A6V5V6")
            break
        case "sursenServlet":
            BugUrl.text("漏洞路径：/seeyon/sursenServlet，适用A6V5V6")
            break
        case "portalManager":
            BugUrl.text("漏洞路径：/ajax.do?method=ajaxAction&rnd=87507，需要结合Session泄露组合利用，适用A6V6")
            break
    }
}

function changepType(){
    var type=$("#pType").val()
    var fileuploadUrl=$("#fileuploadUrl")
    fileuploadUrl.empty()
    switch (type){
        case "UploadMenuIcon":
            fileuploadUrl.text("漏洞路径：/seeyon/fileUpload.do?method=processUpload，使用：/privilege/menu.do?method=uploadMenuIcon文件移动，适用A6V5V6")
            break
        case "PortalDesignerManager":
            fileuploadUrl.text("漏洞路径：/seeyon/fileUpload.do?method=processUpload，使用：/ajax.do?managerName=portalDesignerManager文件移动，适用A6V6")
            break
    }
}