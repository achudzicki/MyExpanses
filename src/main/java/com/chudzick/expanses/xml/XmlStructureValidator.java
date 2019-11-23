package com.chudzick.expanses.xml;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;

public class XmlStructureValidator {
    private static final String schemaUriTemplate = "/xmlSchemas/%s.xsd";

    public static boolean validateStructure(String schemaName, String xmlToValidate) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new InputSource(new StringReader(xmlToValidate)));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        //CREATE SCHEMA
        Source schemaFile = new StreamSource(new ClassPathResource(String.format(schemaUriTemplate, schemaName)).getInputStream());
        Schema schema = schemaFactory.newSchema(schemaFile);

        //CREATE VALIDATOR
        Validator validator = schema.newValidator();

        try {
            validator.validate(new DOMSource(document));
        } catch (SAXException e) {
            return false;
        }
        return true;
    }
}
