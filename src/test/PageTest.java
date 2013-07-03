package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class PageTest{

	private WebClient  client; 
    private WebRequest request; 
    private String     sinaLoginUrl = " http://mail.sina.com.cn/cgi-bin/login.php"; 
    private String     hostSinaUrl  = ""; 
 
    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException { 
        String username = "***"; 
        String password = "***"; 
        String newpassword = "***"; 
        String nickname = "***"; 
 
        PageTest mySina = new PageTest(); 
        if (mySina.mailLoginBySina(username, password)) { // 登录 
            mySina.updatePwdBySina(password, newpassword); // 修改密码 
            mySina.updateNickName(nickname); // 修改帐户昵称 
        } else { 
            System.out.println("登录失败！请检查用户名和密码是否正确！"); 
        } 
    } 
 
    public PageTest(){ 
        client = new WebClient(BrowserVersion.CHROME_16); 
//        client.setJavaScriptEnabled(false); 
    } 
 
    /** 
     * 更改帐户昵称 
     *  
     * @param nickname 昵称 
     * @return boolean 
     * @throws FailingHttpStatusCodeException 
     * @throws IOException 
     */ 
 
    public boolean updateNickName(String nickname) throws FailingHttpStatusCodeException, IOException { 
        String sinaSetUrl = hostSinaUrl + "basic/setting_account"; 
        request = new WebRequest(new URL(sinaSetUrl), HttpMethod.POST); 
        request.setCharset("utf-8"); 
        request.setRequestParameters(Arrays.asList(new NameValuePair("nickname", nickname), new NameValuePair("pop3", 
                                                                                                              "on"), 
                                                   new NameValuePair("imap", "on"))); 
        client.getPage(request); 
        HtmlPage p = client.getPage(hostSinaUrl + "classic/index.php"); 
 
        if (p.getBody().getTextContent().indexOf("\"NickName\":\"" + nickname + "\"") > 0) { 
            return true; 
        } else { 
            return false; 
        } 
 
    } 
 
    /** 
     * 修改密码 
     *  
     * @param oldpassword 旧密码 
     * @param newpassword 新密码 
     * @return boolean 
     * @throws FailingHttpStatusCodeException 
     * @throws IOException 
     */ 
 
    public boolean updatePwdBySina(String oldpassword, String newpassword) throws FailingHttpStatusCodeException, 
                                                                          IOException { 
        String sinaSetUrl = " http://login.sina.com.cn/member/security/password.php"; 
        request = new WebRequest(new URL(sinaSetUrl), HttpMethod.POST); 
        request.setCharset("gbk"); 
        request.setRequestParameters(Arrays.asList(new NameValuePair("pass", oldpassword), 
                                                   new NameValuePair("pass1", newpassword), 
                                                   new NameValuePair("pass2", newpassword))); 
        HtmlPage p = client.getPage(request); 
 
        if (p.getBody().getTextContent().indexOf("您的密码修改成功") > 0) { 
            return true; 
        } else { 
            return false; 
        } 
    } 
 
    /** 
     * 登录 
     *  
     * @param username 用户名 
     * @param password 密码 
     * @return boolean 
     * @throws FailingHttpStatusCodeException 
     * @throws MalformedURLException 
     * @throws IOException 
     */ 
 
    public boolean mailLoginBySina(String username, String password) throws FailingHttpStatusCodeException, 
                                                                    MalformedURLException, IOException { 
 
        HtmlPage loginPage = client.getPage(sinaLoginUrl); 
        HtmlForm loginForm = loginPage.getFormByName("free"); 
        HtmlInput u = loginForm.getInputByName("u"); 
        HtmlInput psw = loginForm.getInputByName("psw"); 
        HtmlSubmitInput loginButton = loginForm.getInputByName("登录"); 
        u.setValueAttribute(username); 
        psw.setValueAttribute(password); 
        HtmlPage result = loginButton.click(); 
        String resultUrl = result.getUrl().toString(); 
 
        if (resultUrl.indexOf("classic/index.php") > 0) { 
            String regex = "http://(.*?)/"; 
            hostSinaUrl = myRegex(resultUrl, regex, null); 
            if (hostSinaUrl.length() > 0) { 
                return true; 
            } else { 
                return false; 
            } 
        } else { 
            return false; 
        } 
 
    } 
 
    /** 
     * 正则匹配替换 
     *  
     * @param str 
     * @param reg 
     * @param replace 
     * @return 
     */ 
 
    public String myRegex(String str, String reg, String[] replace) { 
        String result = null; 
        Matcher m = Pattern.compile(reg).matcher(str); 
        while (m.find()) { 
            result = m.group(); 
            if (replace != null && replace.length > 0) { 
                for (String s : replace) { 
                    result = result.replace(s, ""); 
                } 
            } 
        } 
        return result; 
    } 
}
