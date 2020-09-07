package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.transactions.ImportOperationFormBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.categorize.TransactionGroupCategorizeData;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp.AccountHistoryPKO_BP;
import com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp.AccountOperationsPKO_BP;
import com.chudzick.expanses.domain.xml.Banks;
import com.chudzick.expanses.exceptions.ImportTransactionException;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.services.categorize.CategorizeService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.utils.CustomXmlUtils;
import com.chudzick.expanses.xml.XmlStructureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "import")
public class ImportTransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(ImportTransactionController.class);
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private CategorizeService categorizeService;


    @GetMapping("transactions")
    public String importTransactionsPage(Model model) {
        List<TransactionGroup> transactionGroups = transactionGroupService.getAllGroups();
        model.addAttribute("transactionGroups", transactionGroups);
        return "transaction/importTransactions";
    }

    @PostMapping("transactions")
    public String importTransactions(@RequestParam("file") MultipartFile file, Model model) throws IOException, ImportTransactionException, NoActiveCycleException {
        boolean xmlValid;
        try {
            xmlValid = XmlStructureValidator.validateStructure(Banks.PKO_BP.getSchemaName(), new String(file.getBytes()));
        } catch (SAXException | ParserConfigurationException e) {
            throw new ImportTransactionException(ApplicationActions.IMPORT_TRANSACTIONS, "Błąd podczas wczytywania pliku, zły format wczytanego dokumentu");
        }

        if (!xmlValid) {
            throw new ImportTransactionException(ApplicationActions.IMPORT_TRANSACTIONS, "Nieprawidłowa struktutra wczytanego dokumentu");
        }

        Cycle activeCycle = cycleService.findActiveCycle().orElseThrow(() -> new NoActiveCycleException(ApplicationActions.IMPORT_TRANSACTIONS));
        Map<String, List<TransactionGroupCategorizeData>> categorizeData = categorizeService.getCategorizeData();
        List<AccountOperationsPKO_BP> rawOperations = CustomXmlUtils.unmarshalToList(AccountHistoryPKO_BP.class,
                new String(file.getBytes()));
        List<AccountOperationDto> operations = new ArrayList<>();
        List<AccountOperationDto> validDateOperations = new ArrayList<>();
        List<AccountOperationDto> notValidDateOperations = new ArrayList<>();

        for (AccountOperationsPKO_BP transaction : rawOperations) {
            AccountOperationDto dto = AccountOperationDto.from(transaction);
            categorizeService.addTipToOperation(categorizeData, dto);
            operations.add(dto);
            LocalDate transactionTime = LocalDate.parse(dto.getDate(), FORMATTER);
            if (transactionTime.isBefore(activeCycle.getDateFrom()) || transactionTime.isAfter(activeCycle.getDateTo())) {
                notValidDateOperations.add(dto);
            } else {
                validDateOperations.add(dto);
            }

        }

        model.addAttribute("validOperations", validDateOperations);
        model.addAttribute("notValidOperations", notValidDateOperations);
        model.addAttribute("importedTransactions", operations);
        model.addAttribute("transactionGroups", transactionGroupService.getAllGroups());
        model.addAttribute("transactionTypes", TransactionType.values());
        LOG.info(String.format("Successfully imported %d operations", operations.size()));
        return "transaction/include/importedTransactions";
    }

    @PostMapping(value = "add")
    public String addImportedOperations(@ModelAttribute ImportOperationFormBean importOperationFormBean, RedirectAttributes redirectAttributes) throws NoActiveCycleException {
        if (importOperationFormBean.getTransactionsDto() == null || importOperationFormBean.getTransactionsDto().isEmpty()) {
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Nie zaimporotwano żadnej transakcji, proszę sprawdzić daty transakcji")
                    .getNotificationList());
            return "redirect:/transaction/all";
        }
        final int operationLisSize = importOperationFormBean.getTransactionsDto().size();
        LOG.info(String.format("Start adding imported operations %d", operationLisSize));
        categorizeService.addCategorizeData(importOperationFormBean.getTransactionsDto());
        singleTransactionService.addAll(importOperationFormBean.getTransactionsDto());
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification(String.format("Poprawnie zaimportowano %d %s", operationLisSize,
                        operationLisSize > 1 ? "transakcji" : "transakcje"))
                .getNotificationList());
        return "redirect:/transaction/all";
    }
}
