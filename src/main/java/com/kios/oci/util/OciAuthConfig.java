package com.kios.oci.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.StringPrivateKeySupplier;
import com.oracle.bmc.nosql.NosqlClient;

@Configuration
public class OciAuthConfig {
    private static final Logger LOG = LoggerFactory.getLogger(OciAuthConfig.class);
    @Value("${REGION}")
    private String region;
    @Value("${TENANT_ID}")
    private String tenantId; 
    @Value("${USER_ID}")
    private String userId;
    @Value("${COMPARTMENT_ID}")
    private String compartmentId;
    @Value("${NOSQL_ENDPOINT}")
    private String endpoint;
    @Value("${FINGERPRINT}")
    private String fingerprint;
    @Value("${BUTTON_PROCESSOR_TABLE}")
    private String buttonProcessorTable;
    
    /* TODO Only for local dev - replace with InstanceProfile when promoting to k8s on OCI */
    @Value("${PRIVATE_KEY}")
    private String privateKeyString;
    @Value("${KEY_PHRASE}")
    private String keyPhrase;

    public BasicAuthenticationDetailsProvider basicAuthenticationDetailsProvider() {
        LOG.info("Created the basic authentication details provider");
        return SimpleAuthenticationDetailsProvider
          .builder()
//          .region(Region.fromRegionCode(region))
          .region(Region.US_PHOENIX_1)
          .tenantId(tenantId)
          .userId(userId)
          .privateKeySupplier(stringPrivateKeySupplier())
          .passPhrase(keyPhrase)
          .fingerprint(fingerprint)
          .build();
    }
    
    @Bean
    public NosqlClient nosqlClient() {
        return NosqlClient.builder().endpoint(endpoint).build(basicAuthenticationDetailsProvider()); 
    }
    
    private StringPrivateKeySupplier stringPrivateKeySupplier() {
      String privateKey = new StringBuilder()
          .append("-----BEGIN RSA PRIVATE KEY-----\r\n")
          .append(privateKeyString)
          .append("\r\n-----END RSA PRIVATE KEY-----")
          .toString();
      return new StringPrivateKeySupplier(privateKey);
    }
    
}
