package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.domain.expanses.AccountOperationDto;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionType;
import com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp.AccountHistoryPKO_BP;
import com.chudzick.expanses.domain.informations.CycleInformation;
import com.chudzick.expanses.domain.responses.AjaxResponse;
import com.chudzick.expanses.domain.responses.AjaxResponseStatus;
import com.chudzick.expanses.domain.xml.Banks;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.utils.CustomXmlUtils;
import com.chudzick.expanses.xml.XmlStructureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ImportTransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(ImportTransactionController.class);

    @Autowired
    private TransactionGroupService transactionGroupService;

    @Autowired
    private CycleService cycleService;

    @GetMapping("import/transactions")
    public String importTransactionsPage(Model model) {
        Optional<Cycle> cycle = cycleService.findActiveCycle();

        cycle.ifPresent(activeCycle -> {
            model.addAttribute("activeCycle", activeCycle);
            model.addAttribute("cycleDisplayInfo", CycleInformation.fromCycle(activeCycle));
        });
        return "transaction/importTransactions";
    }

    @PostMapping("import/transactions")
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
        model.addAttribute("importedTransactions",operations);
        model.addAttribute("transactionGroups", transactionGroups);
        model.addAttribute("transactionTypes", TransactionType.values());
        return "transaction/include/importedTransactions";
    }
}
