package com.chudzick.expanses.controllers.transaction;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.beans.transactions.TransactionGroupBean;
import com.chudzick.expanses.beans.transactions.TransactionGroupUsageBean;
import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import com.chudzick.expanses.factories.TransactionGroupStaticFactory;
import com.chudzick.expanses.services.permissions.PermissionsService;
import com.chudzick.expanses.services.transactions.TransactionGroupService;
import com.chudzick.expanses.services.transactions.UserTransactionService;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionGroupControllerTest extends BasicTransactionsControllerTestClass {

    private MockMvc mockMvc;
    private AppUser appUser;
    private Cycle mockCycle;

    @InjectMocks
    private TransactionGroupController transactionGroupController;

    @Mock
    private TransactionGroupBean transactionGroupBean;

    @Mock
    private TransactionGroupService transactionGroupService;

    @Mock
    private NotificationMessagesBean notificationMessagesBean;

    @Mock
    private PermissionsService permissionsService;

    @Mock
    private UserTransactionService userTransactionService;

    @Mock
    private TransactionGroupUsageBean transactionGroupUsageBean;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(transactionGroupController).build();
        appUser = AppUserStaticFactory.createFromDto(prepareValidUserDto());
        mockCycle = prepareMockCycle(appUser);
    }


    @Test
    public void transactionGroupMainTest() throws Exception {
        when(transactionGroupService.getAllGroups()).thenReturn(prepareTransactionGroupList(5, appUser));

        mockMvc.perform(get("/transaction/group"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notificationMessagesBean", "transactionGroupBean", "transactionGroupDto"))
                .andExpect(view().name("transaction/transactionGroupMain"))
                .andReturn();
    }

    @Test
    public void addNewTransactionGroup() throws Exception {
        TransactionGroupDto transactionGroupDto = prepareValidTransactionGroupDto();
        when(transactionGroupService.addNewTransactionGroup(transactionGroupDto)).thenReturn(TransactionGroupStaticFactory.createFromDto(transactionGroupDto, appUser));

        mockMvc.perform(post("/transaction/group/add")
                .flashAttr("transactionGroupDto", transactionGroupDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(NOTIFICATIONS_ATTR_NAME))
                .andExpect(model().hasNoErrors());

        verify(transactionGroupService, times(1)).addNewTransactionGroup(transactionGroupDto);
    }

    @Test
    public void notValidTransactionGroupAddTest() throws Exception {
        TransactionGroupDto transactionGroupDto = prepareValidTransactionGroupDto();
        transactionGroupDto.setGroupDescription(null);
        transactionGroupDto.setGorupName("");

        when(transactionGroupService.addNewTransactionGroup(transactionGroupDto)).thenReturn(TransactionGroupStaticFactory.createFromDto(transactionGroupDto, appUser));

        mockMvc.perform(post("/transaction/group/add")
                .flashAttr("transactionGroupDto", transactionGroupDto))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("transaction/transactionGroupMain"))
                .andExpect(model().attributeExists("bindingResult"));

        verify(transactionGroupService, times(0)).addNewTransactionGroup(transactionGroupDto);
    }

    @Test
    public void deleteTransactionGroupTest() throws Exception, UserNotPermittedToActionException {
        int groupId = 1;

        when(permissionsService.checkUserPermissionsToDeleteGroup(groupId)).thenReturn(true);
        when(userTransactionService.countTransactionsByGroup(groupId)).thenReturn(0);
        doNothing().when(transactionGroupService).deleteTransactionGroup(groupId);

        mockMvc.perform(post("/transaction/group/delete/" + groupId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(NOTIFICATIONS_ATTR_NAME))
                .andExpect(redirectedUrl("/transaction/group"));
    }

    @Test
    public void deleteTransactionGroupWithUsageTest() throws Exception, UserNotPermittedToActionException {
        int groupId = 1;

        when(permissionsService.checkUserPermissionsToDeleteGroup(groupId)).thenReturn(true);
        when(userTransactionService.countTransactionsByGroup(groupId)).thenReturn(5);
        doNothing().when(transactionGroupService).deleteTransactionGroup(groupId);

        mockMvc.perform(post("/transaction/group/delete/" + groupId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists(NOTIFICATIONS_ATTR_NAME))
                .andExpect(redirectedUrl("/transaction/group/usage/" + groupId));
    }

    @Rule
    public ExpectedException exRule = ExpectedException.none();

    @Test
    public void deleteTransactionGroupNotPermittedUserTest() throws Exception, UserNotPermittedToActionException {
        exRule.expectCause(IsInstanceOf.instanceOf(IllegalStateException.class));
        int groupId = 1;
        when(permissionsService.checkUserPermissionsToDeleteGroup(groupId)).thenThrow(new UserNotPermittedToActionException(ApplicationActions.DELETE_GROUP));

        mockMvc.perform(post("/transaction/group/delete/" + groupId));

        verify(transactionGroupService, times(0)).deleteTransactionGroup(groupId);
    }

    @Test
    public void viewTransactionsToSelectedGroupTest() throws Exception {
        int groupId = 1;

        when(userTransactionService.findAllByGroupId(groupId)).thenReturn(prepareListOfTransactions(5, mockCycle, appUser));
        when(transactionGroupService.findById(groupId)).thenReturn(prepareTransactionGroup(appUser));

        mockMvc.perform(get("/transaction/group/usage/" + groupId))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/TransactionGroupUsageTable"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("transactionGroupUsageBean", "notificationMessagesBean"));
    }

}
