package com.userSrvc.client.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class AssuredTest {

	@Test
	public void
	givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
	  throws ClientProtocolException, IOException {
	  
	   // Given
	   String jsonMimeType = "application/json";
	   HttpUriRequest request = new HttpGet( "http://localhost:7000/tts/code/SCANNER-66" );
	 
	   // When
	   HttpResponse response = HttpClientBuilder.create().build().execute( request );
	 
	   // Then
	   String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
	   assertEquals( jsonMimeType, mimeType );
	   System.out.println(response.getStatusLine().getStatusCode());
	   assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
	}
}
