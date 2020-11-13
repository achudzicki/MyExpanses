package com.chudzick.expanses.factories.settings;

import com.chudzick.expanses.domain.settings.UserAvatar;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

public class UserAvatarStaticFactoryTest {
    private static final String FILE_NAME = "FileName";
    private static final byte[] BYTES = new byte[]{1, 0, 1, 0, 1, 1, 1, 0};
    private static final MockMultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(FILE_NAME, BYTES);
    private static final AppUser USER = new AppUser();

    @Test
    public void fromMultipartFile_PassAllData_CreateOptionalUserAvatar() {
        Optional<UserAvatar> userAvatar = UserAvatarStaticFactory.fromMultipartFile(MOCK_MULTIPART_FILE, USER);

        Assert.assertTrue(userAvatar.isPresent());
        Assert.assertEquals(BYTES, userAvatar.get().getContent());
        Assert.assertEquals(FILE_NAME, userAvatar.get().getName());
    }

    @Test
    public void fromMultipartFile_PassNullUser_ReturnOptionalEmpty() {
        Optional<UserAvatar> userAvatar = UserAvatarStaticFactory.fromMultipartFile(MOCK_MULTIPART_FILE, null);

        Assert.assertFalse(userAvatar.isPresent());
    }

    @Test
    public void fromMultipartFile_PassNullMultipartFile_CreateOptionalUserAvatar() {
        Optional<UserAvatar> userAvatar = UserAvatarStaticFactory.fromMultipartFile(null, USER);

        Assert.assertFalse(userAvatar.isPresent());
    }
}
