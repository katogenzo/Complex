package test;

import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

public class Test {

	public static void main(String[] argv) {
		String s = "/wEPDwUKMTMyNDg1NDE4MQ9kFgICAQ9kFgQCAQ8PFgIeBFRleHQFG+ivpeeUqOaIt+S4jeWtmOWcqO+8ge+8ge+8gWRkAgkPFgIfAAW/ATx1bD48bGk+PGEgaHJlZj0nTm90aWNlLmFzcHg/a2V5PTQnIHRhcmdldD0nX2JsYW5rJyB0aXRsZT0n5YWz5LqOMjAxM+W5tOWNjua2puaWsOm+mea1geWQkeezu+e7n+WIsOacn+eUqOaIt+ivt+eCueWHuyc+5YWz5LqOMjAxM+W5tOWNjua2puaWsOm+mea1geWQkeezu+e7n+WIsOacn+eUqOaIt+ivt+eCueWHuzwvYT48L2xpPjwvdWw+ZGRMqR5RoGpWg7QOs+O4OXIsau4AS4cOGy4neBtZSA9oaw==";
		s = "/wEPDwUKMTkzNzE5ODUzMw9kFgICAw9kFgICAQ8QDxYCHgtfIURhdGFCb3VuZGdkEBUBHuWbveiNr+aOp+iCoeWNl+S6rOaciemZkOWFrOWPuBUBATEUKwMBZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQVidG5PawUIYnRuUmVzZXRQ2ieaTSMFuM95cgQW7E7BiQ+Qww==";
		s = "/wEPDwUKMTkzNzE5ODUzMw9kFgICAw9kFgICAQ8QDxYCHgtfIURhdGFCb3VuZGdkEBUBHuWbveiNr+aOp+iCoeWNl+S6rOaciemZkOWFrOWPuBUBATEUKwMBZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQVidG5PawUIYnRuUmVzZXRQ2ieaTSMFuM95cgQW7E7BiQ+Qww==";
		s = "/wEPDwUKMTkzNzE5ODUzMw9kFgICAw9kFgICAQ8QDxYCHgtfIURhdGFCb3VuZGdkEBUBHuWbveiNr+aOp+iCoeWNl+S6rOaciemZkOWFrOWPuBUBATEUKwMBZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQVidG5PawUIYnRuUmVzZXRQ2ieaTSMFuM95cgQW7E7BiQ+Qww==";
		s = "/wEPDwULLTE2MTY2ODcyMjlkZCRVE8eOfNK6P3TQ/x7UnHGigPPn";
		s = "/wEPDwUKMTMyNDg1NDE4MQ9kFgICAQ9kFgQCAQ8PFgIeBFRleHQFG+ivpeeUqOaIt+S4jeWtmOWcqO+8ge+8ge+8gWRkAgkPFgIfAAW/ATx1bD48bGk+PGEgaHJlZj0nTm90aWNlLmFzcHg/a2V5PTQnIHRhcmdldD0nX2JsYW5rJyB0aXRsZT0n5YWz5LqOMjAxM+W5tOWNjua2puaWsOm+mea1geWQkeezu+e7n+WIsOacn+eUqOaIt+ivt+eCueWHuyc+5YWz5LqOMjAxM+W5tOWNjua2puaWsOm+mea1geWQkeezu+e7n+WIsOacn+eUqOaIt+ivt+eCueWHuzwvYT48L2xpPjwvdWw+ZGRMqR5RoGpWg7QOs+O4OXIsau4AS4cOGy4neBtZSA9oaw==";
		s = "%D6%D0%C3%C0%CA%B7%BF%CB";
		s = "%D6%D0%C3%C0%CA%B7%BF%CB";
		s = "%B2%E9+%D5%D2";

		// s =
		// "TUOOmL6WrU4DNjsXMh4EzvGDGlImax8muS0dMVgzulx6IjhC6bYbEdRlnb9Am_8p_kb_EuE_N8uDgG7q2KaOSmBKloZ-KrevLOFrOT7A1wo1";
		// s =
		// "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bDEzJGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDE0JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDE1JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDE2JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDE3JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDE4JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDE5JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDIwJGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDIxJGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDIyJGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDIzJGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDI0JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDI1JGNrYgUiQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlJGN0bDI2JGNrYgUYQVNQeFBvcHVwQ29udHJvbDEkZ3ZXYXJlDzwrAAoBCAIBZAUCZ3YPPCsACgEIAgFkYE79Vc6PjPmKx6o2AGW4AE8q6Mw=";
		// s="%2FwEPDwUKMTkzNzE5ODUzMw9kFgICAw9kFgICAQ8QDxYCHgtfIURhdGFCb3VuZGdkEBUBHuWbveiNr%2BaOp%2BiCoeWNl%2BS6rOaciemZkOWFrOWPuBUBATEUKwMBZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQVidG5PawUIYnRuUmVzZXRQ2ieaTSMFuM95cgQW7E7BiQ%2BQww%3D%3D&drpCompany=1&txtUser=10000501&txtPass=10000501&txtVerify=qfy3v&btnOk.x=0&btnOk.y=0";
		// s="10000501";
//		System.out.println(MD5.toMD5String(s));
//		System.out.println(URLEncoder.encode("中美史克"));
//		System.out.println(URLEncoder.encode("确定"));
		System.out.println(URLEncoder.encode("查 找"));
		System.out.println(new String(Base64.decodeBase64(s)));
	}
}
