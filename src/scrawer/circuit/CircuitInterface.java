package scrawer.circuit;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import scrawer.Param;

/**
 * 数据抓取流程接口
 * 
 * @author wangbibo
 * 
 */
public interface CircuitInterface {

	/**
	 * 到目标网址进行刷新，如果页面上有验证码，则将验证码文件通过输出流的方式输出给接收者。
	 * 
	 * @param ops
	 *            接收者提供的输出流
	 * @return 刷新成功：包含了验证码文件的输出流；刷新失败：返回的输出流为null
	 */
	public OutputStream refresh(OutputStream ops);

	/**
	 * 在目标网址上进行刷新
	 * 
	 * @return 是否连接刷新成功
	 */
	public boolean refresh();

	/**
	 * 登录目标网站
	 * 
	 * @param parameters
	 *            登录中需要提交的参数（如：用户名、密码、验证码等信息）
	 * @return 是否连接提交成功；不保证登录业务一定成功。
	 */
	public boolean login(HashMap<String, String> parameters);

	/**
	 * 登录目标网站
	 * 
	 * @param parameters
	 *            登录中需要提交的参数（如：用户名、密码、验证码等信息）
	 * @return 是否连接提交成功；不保证登录业务一定成功。
	 */
	public boolean login(List<Param> parameters);

	/**
	 * 根据设定的数据源开始采集数据
	 * 
	 * @return 采集数据的结果，成功或失败。
	 */
	public HashMap<String, String> gatherData();
}
