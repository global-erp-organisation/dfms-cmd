package com.expedia.content.media.processing.pipeline.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * image copy interface that copy image derivative from temporary location to destination
 * destination can be S3 bucket or normal file directory. 
 */
public interface ImageCopy {

    /**
     * cp image file from source to destination
     * @param source image source file
     * @param outPutFileName full path name of the destination.
     * @throws FileNotFoundException
     */
    void copyImage(File source, String outPutFileName) throws FileNotFoundException;


    /**
     * get the S3 object from bucket
     * @param bucketName
     * @param objectName
     * @return
     * @throws AmazonServiceException
     */
    S3Object getImage(String bucketName, String objectName) throws AmazonServiceException;

}
