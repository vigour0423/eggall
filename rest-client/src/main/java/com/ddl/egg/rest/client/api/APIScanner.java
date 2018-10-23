package com.ddl.egg.rest.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APIScanner {
    private final Logger logger = LoggerFactory.getLogger(APIScanner.class);
    private final String packageName;

    public APIScanner(String packageName) {
        this.packageName = packageName;
    }

    public List<Class<?>> scan() throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        String scanPath = getScanPath();
        logger.info("scan path {}", scanPath);
        Resource[] resources = resourcePatternResolver.getResources(scanPath);

        List<Class<?>> classes = new ArrayList<>();

        for (Resource resource : resources) {
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);

            logger.info("scan class {}", metadataReader.getClassMetadata().getClassName());
            if (isAPIClass(metadataReader)) {
                classes.add(loadClass(metadataReader.getClassMetadata().getClassName()));
            }
        }

        return classes;
    }

    String getScanPath() {
        return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
    }

    boolean isAPIClass(MetadataReader metadataReader) {
        return metadataReader.getClassMetadata().isInterface() && metadataReader.getAnnotationMetadata().hasAnnotation(API.class.getName());
    }

    Class<?> loadClass(String className) throws ClassNotFoundException {
        return APIScanner.class.getClassLoader().loadClass(className);
    }
}
