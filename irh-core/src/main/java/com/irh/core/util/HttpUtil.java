package com.irh.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 */
public class HttpUtil {
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";
    public static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);//整个连接池最大连接数  
            cm.setDefaultMaxPerRoute(5);//每路由最大连接数，默认值是2  
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * HttpGet请求
     * @param url 地址
     *
     * @return 请求结果
     */
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    /**
     * HttpGet请求
     * @param url   请求地址
     * @param params    请求参数
     * @return 请求结果
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, String> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }

    /**
     * HttpGet请求
     * @param url   请求地址
     * @param headers   请求头
     * @param params    请求参数
     * @return 返回结果
     * @throws URISyntaxException
     */
    public static String httpGetRequest(String url, Map<String, String> headers,
                                        Map<String, String> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, String> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), param.getValue());
        }
        return getResult(httpGet);
    }

    /**
     * HttpPost请求
     * @param url
     * @return
     */
    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    /**
     * HttpPOST请求
     * @param url   请求地址
     * @param params    请求参数
     * @return 请求结果
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, String> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    /**
     * HttpPost请求
     * @param url   地址
     * @param headers   头部信息
     * @param params    参数信息
     * @return 请求结果
     * @throws UnsupportedEncodingException
     */
    public static String httpPostRequest(String url, Map<String, String> headers,
                                         Map<String, String> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, String> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), param.getValue());
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    /**
     * 参数转化成为键值对方式
     *
     * @param params 参数
     *
     * @return 键值对列表
     */
    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, String> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }

        return pairs;
    }


    /**
     * 处理Http请求
     *
     * @param request 请求
     *
     * @return 请求结果
     */
    private static String getResult(HttpRequestBase request) {
        //CloseableHttpClient httpClient = HttpClients.createDefault();  
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            //response.getStatusLine().getStatusCode();  
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //long len = entity.getContentLength();// -1 表示长度未知  
                String result = EntityUtils.toString(entity);
                response.close();
                //httpClient.close();  
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return EMPTY_STR;
    }

    /**
     * @param origin
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String origin) throws UnsupportedEncodingException {
        return urlEncode(origin, "UTF-8");
    }

    /**
     * @param origin
     * @param charset
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String origin, String charset) throws UnsupportedEncodingException {
        if (StringUtil.isNullOrEmpty(charset))
            charset = "UTF-8";
        return URLEncoder.encode(origin, charset);
    }

    /**
     * @param encoded
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String encoded) throws UnsupportedEncodingException {
        return urlDecode(encoded, "UTF-8");
    }

    /**
     * @param encoded
     * @param charset
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String encoded, String charset) throws UnsupportedEncodingException {
        if (StringUtil.isNullOrEmpty(charset))
            charset = "UTF-8";
        return URLDecoder.decode(encoded, charset);
    }

    /**
     * @param url
     * @param params
     * @param isNeedUrlEncode
     * @param urlEncoding
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String addParamsToUrl(String url, List<KeyValuePair> params, boolean isNeedUrlEncode,
                                        String urlEncoding) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(url.trim());
        if (url.contains("?")) {
            if (sb.charAt(sb.length() - 1) != '?' && sb.charAt(sb.length() - 1) != '&') {
                sb.append('&');
            }
        } else {
            sb.append('?');
        }
        if (params != null && params.size() > 0)
            for (KeyValuePair kvp : params) {
                sb.append(kvp.getKey());
                sb.append('=');
                if (isNeedUrlEncode)
                    sb.append(URLEncoder.encode(kvp.getValue(), urlEncoding));
                else
                    sb.append(kvp.getValue());
                sb.append('&');
            }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * @param params
     * @param isNeedUrlEncode
     * @param urlEncoding
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String addParamsToContent(List<KeyValuePair> params, boolean isNeedUrlEncode, String urlEncoding)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0)
            for (KeyValuePair kvp : params) {
                sb.append(kvp.getKey());
                sb.append('=');
                if (isNeedUrlEncode)
                    sb.append(URLEncoder.encode(kvp.getValue(), urlEncoding));
                else
                    sb.append(kvp.getValue());
                sb.append('&');
            }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * @param scc
     * @param uri
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequestByGet(String scc, String uri) throws Exception {
        return sendWebRequestByGet(scc, uri, null);
    }

    /**
     * @param scc
     * @param uri
     * @param forceRespEncoding
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequestByGet(String scc, String uri, String forceRespEncoding) throws Exception {
        BasicCookieStore bcs = null;
        if (scc != null) {
            bcs = CookieManager.getCookieStore(scc);
            if (bcs == null) {
                bcs = new BasicCookieStore();
                CookieManager.setCookieStore(scc, bcs);
            }
        }
        return sendWebRequest(uri, HttpUtil.GET_METHOD, null, null, forceRespEncoding, null, bcs);
    }

    /**
     * @param scc
     * @param uri
     * @param contents
     * @param reqEncoding
     * @param contentType
     * @param timeout
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequestByPost(String scc, String uri, String contents, String reqEncoding,
                                              String contentType, Integer timeout) throws Exception {
        BasicCookieStore bcs = null;
        if (scc != null) {
            bcs = CookieManager.getCookieStore(scc);
            if (bcs == null) {
                bcs = new BasicCookieStore();
                CookieManager.setCookieStore(scc, bcs);
            }
        }
        List<KeyValuePair> headers = new ArrayList<KeyValuePair>();
        if (contentType != null)
            headers.add(new KeyValuePair("Content-Type", contentType));
        return sendWebRequest(uri, HttpUtil.POST_METHOD, headers, contents, reqEncoding, null, timeout, bcs);
    }

    /**
     * @param scc
     * @param uri
     * @param headers
     * @param params
     * @param reqEncoding
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequestByForm(String scc, String uri, Map<String, String> headers,
                                              Map<String, String> params, String reqEncoding) throws Exception {
        BasicCookieStore bcs = null;
        if (scc != null) {
            bcs = CookieManager.getCookieStore(scc);
            if (bcs == null) {
                bcs = new BasicCookieStore();
                CookieManager.setCookieStore(scc, bcs);
            }
        }
        List<KeyValuePair> kvpHeaders = new ArrayList<KeyValuePair>();
        if (headers != null)
            for (String key : headers.keySet())
                kvpHeaders.add(new KeyValuePair(key, headers.get(key)));
        if (headers == null || !headers.containsKey("Content-Type"))
            kvpHeaders.add(new KeyValuePair("Content-Type", "application/x-www-form-urlencoded"));
        return sendWebRequest(uri, HttpUtil.POST_METHOD, kvpHeaders, formToUrlEncodedString(params, reqEncoding),
                reqEncoding, null, null, bcs);
    }

    /**
     * @param scc
     * @param uri
     * @param headers
     * @param params
     * @param reqEncoding
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequestByForm(String scc, String uri, List<KeyValuePair> headers,
                                              List<KeyValuePair> params, String reqEncoding) throws Exception {
        BasicCookieStore bcs = null;
        if (scc != null) {
            bcs = CookieManager.getCookieStore(scc);
            if (bcs == null) {
                bcs = new BasicCookieStore();
                CookieManager.setCookieStore(scc, bcs);
            }
        }
        boolean hasContentType = false;
        List<KeyValuePair> kvpHeaders = new ArrayList<KeyValuePair>();
        if (headers != null)
            for (KeyValuePair kvp : headers) {
                if (kvp.getKey().equalsIgnoreCase("Content-Type"))
                    hasContentType = true;
                kvpHeaders.add(kvp);
            }
        if (!hasContentType)
            kvpHeaders.add(new KeyValuePair("Content-Type", "application/x-www-form-urlencoded"));
        return sendWebRequest(uri, HttpUtil.POST_METHOD, kvpHeaders, formToUrlEncodedString(params, reqEncoding),
                reqEncoding, null, null, bcs);
    }

    /**
     * @param uri
     * @param method
     * @param headers
     * @param contents
     * @param reqEncoding
     * @param forceRespEncoding
     * @param timeout
     * @param cookieStore
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequest(String uri, String method, List<KeyValuePair> headers, String contents,
                                        String reqEncoding, String forceRespEncoding, Integer timeout, BasicCookieStore cookieStore) throws Exception {
        if (contents == null) {
            contents = "";
        }
        if (StringUtil.isNullOrEmpty(reqEncoding)) {
            reqEncoding = "UTF-8";
        }
        return sendWebRequest(uri, method, headers, contents.getBytes(Charset.forName(reqEncoding)),
                forceRespEncoding, timeout, cookieStore);
    }


    /**
     * @param uri
     * @param method
     * @param headers
     * @param contents
     * @param forceRespEncoding
     * @param timeout
     * @param cookieStore
     *
     * @return
     *
     * @throws Exception
     */
    public static String sendWebRequest(String uri, String method, List<KeyValuePair> headers, byte[] contents,
                                        String forceRespEncoding, Integer timeout, BasicCookieStore cookieStore) throws Exception {
        if (timeout == null || timeout < 1000) {
            timeout = 1000;
        } else if (timeout > 100000) {
            timeout = 100000;
        }
        if (cookieStore == null)
            cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = null;
        try {
            RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();

            if (uri.startsWith("https://")) {
                SSLContext ctx = SSLContext.getInstance("SSL");
                ctx.init(null, new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }}, null);
                SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx);
                httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                        .setDefaultRequestConfig(defaultRequestConfig).setSSLSocketFactory(ssf).build();
            } else {
                httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
                        .setDefaultRequestConfig(defaultRequestConfig).build();
            }
            CloseableHttpResponse response = null;
            HttpRequestBase request = null;
            if ("GET".equalsIgnoreCase(method)) {
                request = new HttpGet(uri);
            } else {
                request = new HttpPost(uri);
                ByteArrayEntity byteEntity = new ByteArrayEntity(contents);
                ((HttpPost) request).setEntity(byteEntity);
            }
            if (headers != null) {
                for (KeyValuePair kvp : headers)
                    request.setHeader(kvp.getKey(), kvp.getValue());
            }
            response = httpclient.execute(request);
            try {
                if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201) {
                    if (response.getStatusLine().getStatusCode() == 302)
                        return response.getHeaders("Location")[0].getValue();
                    return null;
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    if (forceRespEncoding == null)
                        return EntityUtils.toString(entity);
                    else
                        return StringUtil.byteArrayToString(EntityUtils.toByteArray(entity), forceRespEncoding);
                }
            } finally {
                if (response != null)
                    response.close();
            }
        } finally {
            if (httpclient != null)
                httpclient.close();
        }
        return null;
    }

    /**
     * @param params
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String formToUrlEncodedString(List<KeyValuePair> params) throws UnsupportedEncodingException {
        return formToUrlEncodedString(params, "UTF-8");
    }

    /**
     * @param params
     * @param charset
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String formToUrlEncodedString(List<KeyValuePair> params, String charset) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0)
            for (KeyValuePair kvp : params) {
                sb.append(kvp.getKey());
                sb.append('=');
                sb.append(urlEncode(kvp.getValue(), charset));
                sb.append('&');
            }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * @param params
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String formToUrlEncodedString(Map<String, String> params) throws UnsupportedEncodingException {
        return formToUrlEncodedString(params, "UTF-8");
    }

    /**
     * @param params
     * @param charset
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String formToUrlEncodedString(Map<String, String> params, String charset) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0)
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append('=');
                sb.append(urlEncode(params.get(key), charset));
                sb.append('&');
            }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * @param encoded
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> urlEncodedStringToForm(String encoded) throws UnsupportedEncodingException {
        return urlEncodedStringToForm(encoded, "UTF-8");
    }

    /**
     * @param encoded
     * @param charset
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> urlEncodedStringToForm(String encoded, String charset) throws UnsupportedEncodingException {
        if (StringUtil.isNullOrEmpty(encoded))
            return null;
        Map<String, String> retval = new HashMap<String, String>();
        String[] params = encoded.split("&");
        for (String param : params) {
            String[] kvp = param.split("=");
            retval.put(kvp[0], kvp.length > 1 ? urlDecode(kvp[1], charset) : "");
        }
        return retval;
    }

}  