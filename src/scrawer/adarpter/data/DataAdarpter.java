package scrawer.adarpter.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import scrawer.CircuitTemplate;
import scrawer.adarpter.DataAdarpterContext;
import util.Constants;
import util.FileUtil;

public abstract class DataAdarpter implements DataAdarpterInterface {

	private String siteName;
	private CircuitTemplate ct;

	public DataAdarpter(String siteName, CircuitTemplate ct) {
		this.siteName = siteName;
		this.ct = ct;
	}

	public void GenerateFile(String type,String content) {
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());

		String filepath = Constants.outPoutDirectory + this.siteName + "/" + type
				+ "_" + dateString + ".csv";
		try {
			FileUtil.createFile(filepath, "GBK", content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public CircuitTemplate getCt() {
		return ct;
	}

	public void setCt(CircuitTemplate ct) {
		this.ct = ct;
	}

	public DataAdarpter(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void init() {
		DataAdarpterContext.inputAdarpter(this);
	}
}
