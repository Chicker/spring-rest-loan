package ru.chicker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.chicker.entities.LoanApplication;
import ru.chicker.exceptions.BlockedPersonalIdException;
import ru.chicker.exceptions.LimitOfRequestsExceededException;
import ru.chicker.models.dto.ApplicationLoanDto;
import ru.chicker.repositories.LoanApplicationRepository;
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
    private final LoanApplicationRepository loanApplicationRepository;

    public LoansController(ILoansService loansService,
                           InfoByIpService infoByIpService,
                           LoanApplicationRepository loanApplicationRepository) {
        this.loansService = loansService;
        this.infoByIpService = infoByIpService;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    @RequestMapping(method = RequestMethod.POST,
        path = "/loans/new", headers = "Content-Type=application/x-www-form-urlencoded")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createApplicationLoan(@Valid ApplicationLoanDto appLoanDto,
                                      BindingResult bindingResult, HttpServletRequest request)
    throws BlockedPersonalIdException, LimitOfRequestsExceededException {

        if (loansService.personalIdIsInBlackList(appLoanDto.getPersonalId())) {
            throw new BlockedPersonalIdException(appLoanDto.getPersonalId());
        }

        Optional<String> ipAddress = HttpUtils.getClientIpAddress(request);

        String countryCode = infoByIpService.getCountryCode(ipAddress);

        if (loansService.checkLimitAndIncrement(countryCode)) {
            // if the limit is exceeded
            throw new LimitOfRequestsExceededException(countryCode);
        }

        LoanApplication loanApplication = new LoanApplication(appLoanDto, countryCode);

        loanApplicationRepository.save(loanApplication);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Map handleBlockedPersonalException(BlockedPersonalIdException exception) {
        return ExceptionHandlersUtils.error(exception.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Map handleLimitOfRequestsExceededException(LimitOfRequestsExceededException exception) {
        return ExceptionHandlersUtils.error(exception.getLocalizedMessage());
    }

}
