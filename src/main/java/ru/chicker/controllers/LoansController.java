package ru.chicker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.chicker.exceptions.BlockedPersonalIdException;
import ru.chicker.models.dto.ApplicationLoanDto;
import ru.chicker.services.ILoansService;
import ru.chicker.utils.ExceptionHandlersUtils;

import javax.validation.Valid;
import java.util.Map;

@Controller
@Validated
public class LoansController {
    private ILoansService loansService;

    public LoansController(ILoansService loansService) {
        this.loansService = loansService;
    }

    @RequestMapping(method = RequestMethod.POST,
        path = "/loans/new", headers = "Content-Type=application/x-www-form-urlencoded")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createApplicationLoan(@Valid ApplicationLoanDto applicationLoanDto,
                                      BindingResult bindingResult)
    throws BlockedPersonalIdException {

        if (loansService.personalIdIsInBlackList(applicationLoanDto.getPersonalId())) {
            throw new BlockedPersonalIdException(applicationLoanDto.getPersonalId());
        }

    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Map handleBlockedPersonalException(BlockedPersonalIdException exception) {
        return ExceptionHandlersUtils.error(exception.getLocalizedMessage());
    }

}
