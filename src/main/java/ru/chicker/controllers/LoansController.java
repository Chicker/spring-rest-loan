package ru.chicker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.chicker.models.dao.LoanApplicationDao;
import ru.chicker.exceptions.BlockedPersonalIdException;
import ru.chicker.exceptions.LimitOfRequestsExceededException;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.exceptions.LoanApplicationNotFound;
import ru.chicker.models.dto.ApplicationLoanDto;
import ru.chicker.repositories.LoanApplicationRepository;
import ru.chicker.services.InfoByIpService;
import ru.chicker.services.LoansService;
import ru.chicker.utils.ExceptionHandlersUtils;
import ru.chicker.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Validated
public class LoansController {
    private LoansService loansService;
    private final InfoByIpService infoByIpService;
    private final LoanApplicationRepository loanApplicationRepository;

    public LoansController(LoansService loansService,
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

        LoanApplicationDao loanApplication = new LoanApplicationDao(appLoanDto, countryCode);

        loansService.addLoanApplication(loanApplication);
    }

    @RequestMapping(method = RequestMethod.POST,
        path = "/loans/{loanApplicationId}/approve", headers =
        "Content-Type=application/x-www-form-urlencoded")
    @ResponseStatus(code = HttpStatus.OK)
    public void resolveLoanApplication(@PathVariable Long loanApplicationId,
                                       @RequestParam boolean approve)
    throws LoanApplicationNotFound, LoanApplicationHasBeenResolvedException {
        LoanApplicationDao loanApplication = loansService.findById(loanApplicationId);

        if (null == loanApplication) {
            throw new LoanApplicationNotFound(loanApplicationId);
        }

        loansService.resolveLoanApplication(loanApplicationId, approve);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/loans/approved/")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public Map getAllApprovedLoans() {
        List<LoanApplicationDao> approved = loansService.getLoansByApproved(true);

        return Collections.singletonMap("result", approved);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/clients/{personalId}/loans/approved/")
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public Map getApprovedLoansByClient(@PathVariable String personalId) {
        List<LoanApplicationDao> loansByClient = loansService.getLoansByClient(personalId, true);

        return Collections.singletonMap("result", loansByClient);
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

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleLoanApplicationNotFound(LoanApplicationNotFound exception) {
        return ExceptionHandlersUtils.error(exception.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleLoanApplicationHasBeenResolvedException(LoanApplicationHasBeenResolvedException exception) {
        return ExceptionHandlersUtils.error(exception.getLocalizedMessage());
    }


}
