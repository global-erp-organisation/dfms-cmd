package com.expedia.content.media.processing.pipeline.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * this class is for UT, cp image from tmp folder to destination.
 */
@SuppressWarnings("PMD")
public class FileImageCopy implements ImageCopy {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void copyImage(File source, String outPutFileName) throws FileNotFoundException {
        final File destination = new File(outPutFileName);
        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException io) {
            throw new FileNotFoundException(io.getMessage());
        }
    }

    @Override
    public S3Object getImage(String bucketName, String objectName) throws AmazonServiceException {
        Resource resource = this.resourceLoader.getResource("s3://" + bucketName + objectName);
        S3Object s3Object = new S3Object();
        s3Object.setBucketName(bucketName);
        s3Object.setKey(objectName);
        try {
            s3Object.setObjectContent(resource.getInputStream());
        } catch (IOException io) {
            throw new AmazonServiceException("exception");
        }
        return s3Object;
    }

}
