package ru.chicker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.chicker.models.dto.Foo;

import java.util.Arrays;
import java.util.Collection;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/foos", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Foo> foos() {
        return Arrays.asList(new Foo("hello", 555));
    }
}
