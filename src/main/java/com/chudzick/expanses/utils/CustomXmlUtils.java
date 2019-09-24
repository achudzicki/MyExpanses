package com.chudzick.expanses.utils;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

public class CustomXmlUtils {

    public static <E> List<E> unmarshalToList(Class<? extends ListUnmarshalable<E>> mainClass, String unmarshallingContent) {
        if (unmarshallingContent == null || unmarshallingContent.isEmpty()) {
            return Collections.emptyList();
        }
        StreamSource streamSource = new StreamSource(new StringReader(unmarshallingContent));
        return JAXB.unmarshal(streamSource, mainClass).getList();
    }
}
