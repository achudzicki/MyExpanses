package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.AppObjectNotFoundException;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import com.chudzick.expanses.factories.SingleTransactionStaticFactory;
import com.chudzick.expanses.services.transactions.ConstantTransactionService;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.transactions.SingleTransactionService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.services.users.UserService;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionControllerTest extends BasicTransactionsControllerTestClass {

    private MockMvc mockMvc;
    private AppUser appUser;
    private Cycle mockCycle;
    private static final int TEST_LIST_SIZE = 5;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private SingleTransactionService<SingleTransaction, SingleTransactionDto> singleTransactionService;

    @Mock
    private ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> constantTransactionService;

    @Mock
    private TransactionGroupService transactionGroupService;

    @Mock
    private CycleService cycleService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationMessagesBean notificationMessagesBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .build();

        appUser = AppUserStaticFactory.createFromDto(prepareValidUserDto());
        mockCycle = prepareMockCycle(appUser);
    }


    @Test
    public void getTransactionPageTest() throws Exception {
        when(singleTransactionService.findLastSingleTransactionsLimitBy(anyInt())).thenReturn(prepareListOfTransactions(TEST_LIST_SIZE, mockCycle, appUser));
        when(transactionGroupService.getAllGroups()).thenReturn(prepareTransactionGroupList(TEST_LIST_SIZE, appUser));
        when(cycleService.findActiveCycle()).thenReturn(Optional.of(mockCycle));

        MvcResult mvcResult = mockMvc.perform(get("/transaction")
                .flashAttr(NOTIFICATIONS_ATTR_NAME, Collections.emptyList()))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/addNewTransaction"))
                .andExpect(model().attributeExists("notificationMessagesBean", "activeCycle", "cycleDisplayInfo", "transactionGroups",
                        "lastTransactions", "singleTransactionDto", "transactionTypes"))
                .andReturn();

        Map<String, Object> models = mvcResult.getModelAndView().getModel();

        Assert.assertEquals(TEST_LIST_SIZE, ((List<SingleTransaction>) models.get("lastTransactions")).size());
        Assert.assertEquals(TEST_LIST_SIZE, ((List<SingleTransaction>) models.get("transactionGroups")).size());
        Assert.assertEquals(0, ((NotificationMessagesBean) models.get("notificationMessagesBean")).getNotificationsMessages().size());
        Assert.assertEquals(mockCycle, models.get("activeCycle"));
    }

    @Test
    public void getTransactionPageNoCycleTest() throws Exception {
        when(singleTransactionService.findLastSingleTransactionsLimitBy(anyInt())).thenReturn(prepareListOfTransactions(0, mockCycle, appUser));
        when(transactionGroupService.getAllGroups()).thenReturn(prepareTransactionGroupList(TEST_LIST_SIZE, appUser));
        when(cycleService.findActiveCycle()).thenReturn(Optional.empty());

        mockMvc.perform(get("/transaction")
                .flashAttr(NOTIFICATIONS_ATTR_NAME, Collections.emptyList()))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/addNewTransaction"))
                .andExpect(model().attributeExists("notificationMessagesBean", "transactionGroups",
                        "lastTransactions", "singleTransactionDto", "transactionTypes"))
                .andExpect(model().attributeDoesNotExist("activeCycle", "cycleDisplayInfo"));
    }

    @Test
    public void transactionAddTest() throws Exception, NoActiveCycleException {
        TransactionGroup transactionGroup = prepareTransactionGroup(appUser);
        SingleTransactionDto validDto = prepareValidSingleTransactionDto(transactionGroup);

        when(singleTransactionService.addNewTransaction(validDto)).thenReturn(SingleTransactionStaticFactory.createFromDto(validDto, appUser, mockCycle));

        mockMvc.perform(post("/transaction/add")
                .flashAttr("singleTransactionDto", validDto))
                .andExpect(flash().attributeExists(NOTIFICATIONS_ATTR_NAME))
                .andExpect(status().is3xxRedirection());

        verify(singleTransactionService, times(1)).addNewTransaction(validDto);
    }

    @Test
    public void notValidTransactionAddTest() throws Exception, NoActiveCycleException {
        TransactionGroup transactionGroup = prepareTransactionGroup(appUser);
        SingleTransactionDto validDto = prepareValidSingleTransactionDto(transactionGroup);
        validDto.setAmound(BigDecimal.valueOf(-1));

        mockMvc.perform(post("/transaction/add")
                .flashAttr("singleTransactionDto", validDto))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/addNewTransaction"));

        verify(singleTransactionService, times(0)).addNewTransaction(any(SingleTransactionDto.class));
    }

    @Test
    public void viewAllTransactionsTest() throws Exception {
        when(singleTransactionService.findAll()).thenReturn(prepareListOfAllTransactions(10, mockCycle, appUser));
        when(constantTransactionService.findAll()).thenReturn(Collections.emptyList());
        when(cycleService.findActiveCycle()).thenReturn(Optional.of(mockCycle));

        mockMvc.perform(get("/transaction/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/allCycleTransactions"))
                .andExpect(model().attributeExists("cycleDisplayInfo", "transactionsList", "actualTransactionStats"));
    }

    @Test
    public void deleteTransaction() throws Exception, UserNotPermittedToActionException, AppObjectNotFoundException {
        int transactionId = 1;

        when(singleTransactionService.deleteTransactionById(transactionId)).thenReturn(true);


        mockMvc.perform(post("/transaction/delete/" + transactionId)
                .header(HttpHeaders.REFERER, "referer")
                .flashAttr("transactionDuration", TransactionDuration.SINGLE))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(NOTIFICATIONS_ATTR_NAME));
    }

    @Rule
    public ExpectedException exRule = ExpectedException.none();

    @Test
    public void deleteTransactionNotPermittedUserTest() throws UserNotPermittedToActionException, Exception, AppObjectNotFoundException {
        int transactionId = 1;

        when(singleTransactionService.deleteTransactionById(transactionId)).thenThrow(new UserNotPermittedToActionException(ApplicationActions.DELETE_TRANSACTION));
        exRule.expectCause(IsInstanceOf.instanceOf(IllegalStateException.class));

        mockMvc.perform(post("/transaction/delete/" + transactionId)
                .header(HttpHeaders.REFERER, "referer"))
                .andExpect(status().is3xxRedirection());
    }


}
