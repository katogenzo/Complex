package harmo;

import java.io.IOException;
import java.io.Serializable;

import org.apache.http.client.ClientProtocolException;

public class Post extends HttpAbstractMethod  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Post(String url) {
		super(url);
		this.method=Method.POST;
	}

	public String execute() {

		try {
			return this.getClient().execute(this);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
