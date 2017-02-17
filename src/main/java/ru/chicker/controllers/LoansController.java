package ru.chicker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.chicker.exceptions.BlockedPersonalIdException;
import ru.chicker.models.dto.ApplicationLoanDto;
import ru.chicker.services.ILoansService;
import ru.chicker.services.InfoByIpService;
import ru.chicker.utils.ExceptionHandlersUtils;
import ru.chicker.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Controller
@Validated
public class LoansController {
    private ILoansService loansService;
    private final InfoByIpService infoByIpService;

    public LoansController(ILoansService loansService, InfoByIpService infoByIpService) {
        this.loansService = loansService;
        this.infoByIpService = infoByIpService;
    }

    @RequestMapping(method = RequestMethod.POST,
        path = "/loans/new", headers = "Content-Type=application/x-www-form-urlencoded")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createApplicationLoan(@Valid ApplicationLoanDto applicationLoanDto,
                                      BindingResult bindingResult, HttpServletRequest request)
    throws BlockedPersonalIdException {

        if (loansService.personalIdIsInBlackList(applicationLoanDto.getPersonalId())) {
            throw new BlockedPersonalIdException(applicationLoanDto.getPersonalId());
        }

        Optional<String> ipAddress = HttpUtils.getClientIpAddress(request);

        String countryCode = infoByIpService.getCountryCode(ipAddress);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Map handleBlockedPersonalException(BlockedPersonalIdException exception) {
        return ExceptionHandlersUtils.error(exception.getLocalizedMessage());
    }

}
