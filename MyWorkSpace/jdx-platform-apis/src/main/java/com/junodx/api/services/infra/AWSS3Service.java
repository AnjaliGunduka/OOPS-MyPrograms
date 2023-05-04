package com.junodx.api.services.infra;

import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@Transactional
public class AWSS3Service extends ServiceBase {
    @Value("${jdx.infra.aws.clientId}")
    private String awsClientId;

    @Value("${jdx.infra.aws.clientSecret}")
    private String awsClientSecret;

    @Value("${jdx.infra.aws.region}")
    private String regionString;

    private AwsBasicCredentials awsCreds;

    private S3Client s3Client;

    public AWSS3Service(){
    }

    protected void init(){
        awsCreds = AwsBasicCredentials.create(
                awsClientId,
                awsClientSecret);

        s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(regionString))
                .build();
    }

    public String getPresignedUrl(String bucketName, String key) throws JdxServiceException {
        if(awsCreds == null || s3Client == null)
            init();

        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_2)
                .build();

        try {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            //TODO parameterize the Duration here
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(objectRequest)
                    .signatureDuration(Duration.ofMinutes(5))
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);


            return presignedRequest.url().toString();


        } catch (S3Exception e) {
            e.getStackTrace();
            throw new JdxServiceException("Cannot obtain a tokenized url to resource " + key + " in bucket " + bucketName);
        }
    }

    public ResponseInputStream<GetObjectResponse> getObject(String bucket, String key){
        if(awsCreds == null || s3Client == null)
            init();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

       return s3Client.getObject(getObjectRequest);
    }
}
