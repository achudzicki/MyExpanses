package com.chudzick.expanses.util.picture;

import com.chudzick.expanses.domain.settings.UserAvatar;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;

public class ReturnAvatarHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ReturnAvatarHelper.class);

    private ReturnAvatarHelper() {
    }

    public static void returnAvatar(Optional<UserAvatar> userAvatar, HttpServletResponse response) {
        InputStream is = null;
        try (OutputStream outputStream = response.getOutputStream()) {
            if (userAvatar.isPresent()) {
                is = new ByteArrayInputStream(userAvatar.get().getContent());
            } else {
                URL url = new ClassPathResource("/static/img/userDefault.jpg").getURL();
                is = url.openStream();
            }
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            IOUtils.copy(is, outputStream);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
