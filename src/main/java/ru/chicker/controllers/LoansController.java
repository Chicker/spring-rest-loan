package ru.chicker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.chicker.models.dto.ApplicationLoanDto;

import javax.validation.Valid;

@Controller
@Validated
public class LoansController {
    @RequestMapping(method = RequestMethod.POST,
        path = "/loans/new", headers = "Content-Type=application/x-www-form-urlencoded")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createApplicationLoan(@Valid ApplicationLoanDto applicationLoanDto,
                                      BindingResult bindingResult) {
        
    }
}
