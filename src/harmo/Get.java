package harmo;

import java.io.Serializable;


public class Get extends HttpAbstractMethod  implements Serializable {

	private static final long serialVersionUID = 1L;

	public Get(String url) {
		super(url);
		this.method=Method.GET;
	}
}
