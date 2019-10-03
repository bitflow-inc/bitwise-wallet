package ai.bitflow.bitwise.wallet.utils;

import ai.bitflow.bitwise.wallet.constants.EthereumConstant;
import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j @Component
public class JsonRpcUtil implements BlockchainConstant, EthereumConstant {

    private static final String TAG   = "[RPC]";

    /**
     * Especially for ADA
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendHttpGet(String url) throws IOException {
      
        CloseableHttpClient client = HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpGet req = new HttpGet(url);
        req.setHeader("Content-type", JSON_HEADER);
        CloseableHttpResponse res = client.execute(req);
        HttpEntity entity = res.getEntity();
        if (entity!=null) {
            try {
                int status = res.getStatusLine().getStatusCode();
                String ret = EntityUtils.toString(entity);
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }
    
    /**
     * Especially for ADA
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendHttpsGet(String url) throws IOException {

        CloseableHttpClient client = HttpClients.custom()
              .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpClientContext ctx = HttpClientContext.create();
        HttpGet req = new HttpGet(url);
        req.setHeader("Content-type", JSON_HEADER);
        CloseableHttpResponse res = client.execute(req, ctx);
        HttpEntity entity = res.getEntity();
        
        if (entity!=null) {
            try {
                int status = res.getStatusLine().getStatusCode();
                String ret = EntityUtils.toString(entity);
                EntityUtils.consumeQuietly(entity);
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }
    
    /**
     * Especially for ADA
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, String body) throws IOException {

        CloseableHttpClient client = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpPost req = new HttpPost(url);
        req.setHeader("Content-type", JSON_HEADER);
        req.setHeader("Accept", "application/json");
        req.setEntity(new StringEntity(body, "UTF-8"));
        CloseableHttpResponse res = client.execute(req);
        HttpEntity entity = res.getEntity();
        if (entity!=null) {
            try {
                String ret = EntityUtils.toString(entity);
                int status = res.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url + " " + body);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @param url
     * @param method
     * @param paramArr
     * @return
     * @throws IOException
     */
    public static String sendJsonRpcJson(String url, String method, JSONArray paramArr) 
              throws IOException {
    
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost req = new HttpPost(url);
        req.setHeader("Content-type", JSON_HEADER);
        JSONObject params = new JSONObject();
        params.put("jsonrpc", "2.0");
        params.put("id",      1);
        params.put("method",  method);
        params.put("params",  paramArr);
        req.setEntity(new StringEntity(params.toString(), "UTF-8"));
        CloseableHttpResponse res = client.execute(req);
        HttpEntity entity = res.getEntity();
        if (entity!=null) {
            try {
                int status = res.getStatusLine().getStatusCode();
                String ret = EntityUtils.toString(entity);
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url + " " + params.toString());
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }

    /**
     * 
     * @param url
     * @param method
     * @param paramArr
     * @return
     * @throws IOException
     */
	public static String sendJsonRpc2(String url, String method, Object[] paramArr) 
	            throws IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost req = new HttpPost(url);
		req.setHeader("Content-type", JSON_HEADER);
		StringBuilder params = new StringBuilder();
		params.append("{\"jsonrpc\":\"2.0\", \"id\":1,\"method\":\"");
		params.append(method + "\", \"params\":[");
		if (paramArr != null) {
			for (int i = 0; i < paramArr.length; i++) {
				params.append(paramArr[i]);
				if (i < paramArr.length - 1) {
					params.append(",");
				}
			}
		}
		params.append("]}");
		req.setEntity(new StringEntity(params.toString(), "UTF-8"));
		CloseableHttpResponse res = client.execute(req);
		HttpEntity entity = res.getEntity();
		if (entity!=null) {
    		try {
    			int status = res.getStatusLine().getStatusCode();
    			String ret = EntityUtils.toString(entity);
    			if (status >= 200 && status < 300) {
    				if (!METHOD_BLOCKBYNUMBER.equals(method)) {
    					log.debug(TAG + "[200] " + url + " " + params.toString());
    				}
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
	}

	/**
	 * Wallet JSON-RPC
	 * @param url
	 */
	public static String sendJsonRpcBasicAuth(String url, String method,
          Object[] paramArr, String authId, String authPw) throws IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpClientContext ctx = HttpClientContext.create();
		CredentialsProvider auth = new BasicCredentialsProvider();
		auth.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(authId, authPw));
		ctx.setCredentialsProvider(auth);

		HttpPost req = new HttpPost(url);
		req.setHeader("Content-type", JSON_HEADER);
		StringBuilder params = new StringBuilder();
		params.append("{\"id\":1,\"method\":\"" + method + "\", \"params\":[");
		if (paramArr != null) {
			for (int i = 0; i < paramArr.length; i++) {
				params.append(paramArr[i]);
				if (i < paramArr.length - 1) {
					params.append(",");
				}
			}
		}
		params.append("]}");
		req.setEntity(new StringEntity(params.toString(), "UTF-8"));
		CloseableHttpResponse res = client.execute(req, ctx);
		HttpEntity entity = res.getEntity();
		if (entity!=null) {
    		try {
    		    String ret = EntityUtils.toString(entity);
    			int status = res.getStatusLine().getStatusCode();
    			if (status >= 200 && status < 300) {
    			    log.debug(TAG + "[200] " + url + " " + params.toString());
                } else {
                	log.error(TAG + "[" + status + "] " + url + " " + params.toString() + " " + ret);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
	}

}
