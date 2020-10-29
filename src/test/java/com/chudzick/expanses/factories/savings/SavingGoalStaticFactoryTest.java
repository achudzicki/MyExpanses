package com.chudzick.expanses.factories.savings;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingGoalStaticFactoryTest {
    private static final String NAME = "Name";
    private static final LocalDate NOW = LocalDate.now();
    private static final BigDecimal TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal INITIAL_AMOUNT = new BigDecimal(0);
    private static final AppUser APP_USER = new AppUser();

    @Test
    public void createNewFromDto_PassDtoAndInitialUser_ReturnValidSavingGoal() {
        SavingGoal result = SavingGoalStaticFactory.createNewFromDto(prepareDto(), APP_USER);

        Assert.assertNotNull(result.getAppUsers());
        Assert.assertEquals(result.getAppUsers().size(), 1);
        Assert.assertTrue(result.getAppUsers().contains(APP_USER));
        Assert.assertEquals(result.getGoalName(), NAME);
        Assert.assertEquals(result.getDateFrom(), NOW);
        Assert.assertEquals(result.getDateTo(), NOW);
        Assert.assertEquals(result.getStartAmount(), INITIAL_AMOUNT);
    }

    private SavingGoalDto prepareDto() {
        SavingGoalDto dto = new SavingGoalDto();
        dto.setTotalAmount(TOTAL_AMOUNT);
        dto.setDateFrom(LocalDate.now());
        dto.setDateTo(NOW);
        dto.setInitialAmount(INITIAL_AMOUNT);
        dto.setName(NAME);
        return dto;
    }
}
