package com.sellics.sherakhan.batch;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class S3Resource implements Resource {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3Resource.class);

    private final String bucketName;
    private final String key;
    private final AmazonS3 s3Client;


    public S3Resource(String bucketName, String key, AmazonS3 s3Client) {
        this.bucketName = bucketName;
        this.key = key;
        this.s3Client = s3Client;
    }

    @Override
    public boolean exists() {
        return this.s3Client.doesBucketExistV2(bucketName);
    }

    @Override
    public URL getURL() throws IOException {
        LOGGER.trace("Trying get URL");
        return null;
    }

    @Override
    public URI getURI() throws IOException {
        LOGGER.trace("Trying get URI");
        return null;
    }

    @Override
    public File getFile() throws IOException {
        LOGGER.trace("Trying get FILE");
        return null;
    }

    @Override
    public long contentLength() throws IOException {
        LOGGER.trace("Calling content Length");
        GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(this.bucketName, this.key);
        ObjectMetadata objectMetadata = this.s3Client.getObjectMetadata(metadataRequest);
        long contentLength = objectMetadata.getContentLength();
        LOGGER.info("Got Content Length {}", contentLength);
        return contentLength;
    }

    @Override
    public long lastModified() throws IOException {
        LOGGER.trace("Calling Last Modified");
        GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(this.bucketName, this.key);
        ObjectMetadata objectMetadata = this.s3Client.getObjectMetadata(metadataRequest);
        return objectMetadata.getLastModified().getTime();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        LOGGER.trace("Create Relative={}", relativePath);
        return null;
    }

    @Override
    public String getFilename() {
        return this.key;
    }

    @Override
    public String getDescription() {
        LOGGER.trace("Calling Description");
        StringBuilder builder = new StringBuilder("S3 resource [bucket='");
        builder.append(this.bucketName);
        builder.append("' and key='");
        builder.append(this.key);
        builder.append("']");
        return builder.toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        LOGGER.info("Try Getting input stream from Bucket={} and Key={}", this.bucketName, this.key);
        GetObjectRequest getObjectRequest = new GetObjectRequest("sellics-casestudy-organic", "public/case-keywords.csv");
        S3Object s3Object = null;
        try {
            s3Object = s3Client.getObject(getObjectRequest);
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("S3 Object Content Length= {}", s3Object.getObjectMetadata().getContentLength());
        return s3Object.getObjectContent();
    }
}
