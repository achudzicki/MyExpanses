package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp.AccountHistoryPKO_BP;
import com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp.AccountOperationsPKO_BP;
import com.chudzick.expanses.domain.responses.AjaxResponse;
import com.chudzick.expanses.domain.responses.AjaxResponseStatus;
import com.chudzick.expanses.domain.xml.Banks;
import com.chudzick.expanses.utils.CustomXmlUtils;
import com.chudzick.expanses.xml.XmlStructureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Controller
public class ImportTransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(ImportTransactionController.class);

    @GetMapping("import/transactions")
    public String importTransactionsPage() {
        return "transaction/importTransactions";
    }

    @PostMapping("import/transactions")
    @ResponseBody
    public AjaxResponse importTransactions(@RequestParam("file") MultipartFile file) throws IOException {
        boolean xmlValid = true;
        try {
            xmlValid = XmlStructureValidator.validateStructure(Banks.PKO_BP.getSchemaName(), new String(file.getBytes()));
        } catch (SAXException | ParserConfigurationException e) {
            LOG.error(e.getMessage(), e);
            return new AjaxResponse.AjaxResponseBuilder<AccountOperationsPKO_BP>(
                    AjaxResponseStatus.ERROR, "Błąd podczas wczytywania pliku"
            ).build();
        }

        if (!xmlValid) {
            return new AjaxResponse.AjaxResponseBuilder<AccountOperationsPKO_BP>(
                    AjaxResponseStatus.ERROR, "Zły format wczytanego dokumentu"
            ).build();
        }

        List<AccountOperationsPKO_BP> operations = CustomXmlUtils.unmarshalToList(AccountHistoryPKO_BP.class,
                new String(file.getBytes()));
        return new AjaxResponse.AjaxResponseBuilder<AccountOperationsPKO_BP>(
                AjaxResponseStatus.SUCCESS, "Poprawnie wczytano operacje"
        ).withContent(operations).build();
    }
}
