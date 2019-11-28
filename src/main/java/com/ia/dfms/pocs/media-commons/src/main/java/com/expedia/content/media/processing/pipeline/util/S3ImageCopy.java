package com.expedia.content.media.processing.pipeline.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * copy image to s3 destination with 'image' metadata.
 */
@Component
public class S3ImageCopy implements ImageCopy {
    public static final String S3_PREFIX = "s3://";
    private static final Logger LOGGER = LoggerFactory.getLogger(S3ImageCopy.class);

    private final AmazonS3 amazonS3 = new AmazonS3Client();

    public void copyImage(File source, String outPutFileName) throws FileNotFoundException {
        byte[] contentBytes = null;
        InputStream inputStream = new FileInputStream(source);
        try {
            contentBytes = IOUtils.toByteArray(inputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBytes);
            Long contentLength = Long.valueOf(contentBytes.length);
            //avoid OutOfMemory error.
            ObjectMetadata objectMetadata = createBaseObjectMetadata();
            objectMetadata.setContentLength(contentLength);
            amazonS3.putObject(getBucketName(outPutFileName), getObjectName(outPutFileName),
                    byteArrayInputStream, objectMetadata);
        } catch (IOException ex) {
            LOGGER.error("copy Image exception fileName={}", outPutFileName, ex);
        } finally {
            try {
                inputStream.close();
            } catch (Exception ex) {
                LOGGER.error("close stream exception file={}", outPutFileName, ex);
            }
        }
    }

    /**
     * Creates a base metadata object for an S3 copy.
     * @return
     */
    private ObjectMetadata createBaseObjectMetadata() {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpeg");
        return objectMetadata;
    }

    public S3Object getImage(String bucketName, String objectName) throws AmazonServiceException {
        return amazonS3.getObject(new GetObjectRequest(bucketName, objectName));
    }

    private static String getBucketName(String fileUrl) {
        final String bucketName = fileUrl.substring(S3_PREFIX.length());
        return bucketName.substring(0, bucketName.indexOf('/'));
    }

    private static String getObjectName(String fileUrl) {
        final String bucketName = fileUrl.substring(S3_PREFIX.length());
        return bucketName.substring(bucketName.indexOf('/') + 1);
    }
}
