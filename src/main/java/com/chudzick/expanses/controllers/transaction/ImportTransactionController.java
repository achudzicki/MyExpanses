package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.transactions.ImportOperationFormBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.AccountOperationDto;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp.AccountHistoryPKO_BP;
import com.chudzick.expanses.domain.responses.AjaxResponse;
import com.chudzick.expanses.domain.responses.AjaxResponseStatus;
import com.chudzick.expanses.domain.xml.Banks;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.utils.CustomXmlUtils;
import com.chudzick.expanses.xml.XmlStructureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "import")
public class ImportTransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(ImportTransactionController.class);
    private static final String NOTIFICATIONS_ATTR_NAME = "notifications";

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;


    @GetMapping("transactions")
    public String importTransactionsPage() {
        return "transaction/importTransactions";
    }

    @PostMapping("transactions")
    public String importTransactions(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        boolean xmlValid;
        try {
            xmlValid = XmlStructureValidator.validateStructure(Banks.PKO_BP.getSchemaName(), new String(file.getBytes()));
        } catch (SAXException | ParserConfigurationException e) {
            LOG.error(e.getMessage(), e);
            return new AjaxResponse.AjaxResponseBuilder(
                    AjaxResponseStatus.ERROR, "Błąd podczas wczytywania pliku"
            ).buildAsJson();
        }

        if (!xmlValid) {
            return new AjaxResponse.AjaxResponseBuilder(
                    AjaxResponseStatus.ERROR, "Zły format wczytanego dokumentu"
            ).buildAsJson();
        }

        List<AccountOperationDto> operations = CustomXmlUtils.unmarshalToList(AccountHistoryPKO_BP.class,
                new String(file.getBytes()))
                .stream()
                .map(AccountOperationDto::from)
                .collect(Collectors.toList());

        List<TransactionGroup> transactionGroups = transactionGroupService.getAllGroups();
        model.addAttribute("importedTransactions", operations);
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("transactionTypes", TransactionType.values());
        LOG.info(String.format("Successfully imported %d operations", operations.size()));
        return "transaction/include/importedTransactions";
    }

    @PostMapping(value = "add")
    public String addImportedOperations(@ModelAttribute ImportOperationFormBean importOperationFormBean, RedirectAttributes redirectAttributes) throws NoActiveCycleException {
        final int operationLisSize = importOperationFormBean.getTransactionsDto().size();
        LOG.info(String.format("Start adding imported operations %d", operationLisSize));
        singleTransactionService.addAll(importOperationFormBean.getTransactionsDto());
        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_NAME, new NotificationMessageListBuilder()
                .withSuccessNotification(String.format("Poprawnie zaimportowano %d %s", operationLisSize,
                        operationLisSize > 1 ? "transakcji" : "transakcje"))
                .getNotificationList());
        return "redirect:/transaction/all/0";
    }
}
