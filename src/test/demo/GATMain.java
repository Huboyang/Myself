package demo;

import com.gat.open.sdk.GATOpen;
import com.gat.open.sdk.model.ApiResponse;
import com.gat.open.sdk.model.Employee;
import com.gat.open.sdk.model.EmployeeAccount;
import com.gat.open.sdk.model.Token;
import com.gat.open.sdk.service.GATTokenService;
import com.gat.open.sdk.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GATMain {

  public static void main(String[] args) {
    String appid = "20110754";
    String appsecret = "62ec8ecb0e312be92da2307b7c9df686";
    //所有接口方法均在 GATOpen 中 ，只需实例化GATOpen即可使用。
    GATOpen gatOpen = new GATOpen(appid, appsecret,"https://openapi.guanaitong.cc");
    /*ApiResponse<Token> token = gatOpen.createToken();
    Token data = token.getData();
    System.out.println(token.getCode());
    System.out.println(data.toString());
    System.out.println(token.getMsg());
    System.out.println("------分割线------");*/
   /* ApiResponse<Token> tokenInfo = gatOpen.getTokenInfo("b69a1aff3c0eb0f4d27fec81d6524de5");
    System.out.println(tokenInfo.getCode());
    System.out.println(tokenInfo.getData().toString());
    System.out.println(tokenInfo.getMsg());*/

    ApiResponse<List<EmployeeAccount>> response = gatOpen.accountEmployee("00000006");

    int code = response.getCode();
  }

  @Test
  public void testLogin() throws UnsupportedEncodingException {
    String appid = "20110754";
    String appsecret = "62ec8ecb0e312be92da2307b7c9df686";
    //实例 GATOpen 对象
    GATOpen gatOpen = new GATOpen(appid, appsecret, "https://openapi.guanaitong.cc");
    //根据员工编号获取授权码
    ApiResponse<String> response = gatOpen.loginByCorpCode("00000006");
    String authCode = response.getData();
    System.out.println("authCode = " + authCode);
    //测试PC端登录网址
    String loginUrl = "https://openapi.guanaitong.cc/sso/employee/login";

    String accessToken = GATTokenService.getGatToken();
    Long timestamp = System.currentTimeMillis() / 1000;

    // 有则添加到url中无则不需要添加
    // url记得urlEncode
//    String redirectUrl = "https://mycenter.guanaitong.cc/index.php?wxA=Person.index";

    Map<String, Object> params = new HashMap<String, Object>(4);
    params.put("access_token", accessToken);
    params.put("auth_code", authCode);
    params.put("timestamp", timestamp);
//    params.put("redirect_url", redirectUrl);
    params.put("sign", SignUtil.sign(params));
    params.remove("appsecret");

    String url = build(loginUrl, params);
    System.out.println(url);
  }

  public static String build(String url, Map<String, Object> params) throws UnsupportedEncodingException {
    if (params == null || params.isEmpty()) {
      return url;
    }
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(url);
    stringBuilder.append("?");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      if (StringUtils.isNoneEmpty(entry.getValue().toString())) {
        stringBuilder.append(entry.getKey());
        stringBuilder.append("=");
        stringBuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8")).append("");
        stringBuilder.append("&");
      }
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }

}
