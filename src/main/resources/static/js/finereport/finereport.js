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
                "                </select>\n" +
                "            </div>\n" +
                "        </div><div class=\"form-group\">\n" +
                "      <label class=\"col-md-2 control-label\">执行命令</label>\n" +
                "      <div class=\"col-md-10\">\n" +
                "        <input class=\"form-control\" type=\"text\" disabled='disabled' name=\"\" placeholder=\"通过Header头传递Testcmd和Testecho两个参数\">\n" +
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