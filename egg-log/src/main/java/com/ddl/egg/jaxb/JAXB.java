package com.ddl.egg.jaxb;

import com.ddl.egg.log.util.CharacterEncodings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mark.huang on 2016-07-18.
 */
public class JAXB {

    private static final JAXB INSTANCE = new JAXB();
    private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<>(64);

    public static String toXml(Object root) {
        return toXml(root, CharacterEncodings.UTF_8, false);
    }

    public static String toXml(Object root, String encoding, boolean cutMessHead) {
        try (StringWriter writer = new StringWriter()) {
            INSTANCE.createMarshaller(root.getClass(), encoding, cutMessHead).marshal(root, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not convert to xml for class [" + root.getClass() + "]: " + e.getMessage(), e);
        }
    }

    public static Object toObject(String xml, Class<?> clazz) {
        try (StringReader reader = new StringReader(xml)) {
            return INSTANCE.createUnMarshaller(clazz).unmarshal(reader);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not convert string [" + xml + "] to object for class [" + clazz + "]: " + e.getMessage(), e);
        }
    }

    public Unmarshaller createUnMarshaller(Class<?> clazz) throws JAXBException {
        Unmarshaller unmarshaller = getJaxbContext(clazz).createUnmarshaller();
        return unmarshaller;
    }

    public Marshaller createMarshaller(Class<?> clazz, String encoding, boolean cutMessHead) throws JAXBException {
        Marshaller marshaller = getJaxbContext(clazz).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, cutMessHead);        //去掉报文头
        if (StringUtils.isNotBlank(encoding)) marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        return marshaller;
    }

    private JAXBContext getJaxbContext(Class<?> clazz) throws JAXBException {
        Assert.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = this.jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(clazz);
            this.jaxbContexts.putIfAbsent(clazz, jaxbContext);
        }

        return jaxbContext;
    }
}
