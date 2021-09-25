package com.ap.greenpole.transactioncomponent.util;

import com.ap.greenpole.transactioncomponent.config.ApplicationPropertiesConfig;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class RestTemplateUtil {
	
	private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

	@Autowired
	private ApplicationPropertiesConfig applicationProperties;

	public ResponseEntity<?> request(String requestUrl, HttpHeaders headers, HttpMethod requestMethod,
                                     Object requestBody, Class<?> returnType) {
		
		ResponseEntity<?> responseEntity = null;
		try {
			URI uri = getUri(requestUrl);
			HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
			logger.info("[+] Calling {} with requestEntity {}" , uri, requestEntity);
			responseEntity = restTemplate().exchange(uri, requestMethod, requestEntity, returnType);
		} catch(Exception ex) {

			logger.info("[-] Exception happened while calling Notification service {}", ex);
		}
		
		return responseEntity;
	}

	private URI getUri(String requestUrl) throws URISyntaxException {
		return new URI(requestUrl);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(getClientHttpRequestFactory());
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = Integer.parseInt(applicationProperties.getHttpTimeOut());
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout)
				.build();
		CloseableHttpClient client = HttpClientBuilder
				.create()
				.setDefaultRequestConfig(config)
				.build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}

}
