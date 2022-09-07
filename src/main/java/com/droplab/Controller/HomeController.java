package com.droplab.Controller;

import com.droplab.Utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping("/")
    public String show(Model model){
        model= CommonUtils.modelSet(new HashMap<>(),model);
        return "index";
    }
}
