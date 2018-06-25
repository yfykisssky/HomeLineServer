package com.homeline.push;

import java.util.ArrayList;
import java.util.List;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SMSUtils {

	private static final String ACCESS_KEY_ID = "LTAIkwiK9v43v3VU";
	private static final String ACCESS_KEY_SECRET = "J2WgjMfYOFudG1Hn3nG7odOgRJJxW5";
	private static final String SIGN_NAME = "HomeLine系统";
	private static final String TEMPLATE_CODE = "SMS_78555134";

	private static int TIME_OUT = 5 * 60 * 1000;

	private static int TIME_RETRY = 60 * 1000;

	static class SMSCheckModel {

		private String phoneNum;

		private int code;

		private long timeStamp;

		public String getPhoneNum() {
			return phoneNum;
		}

		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}

	}

	private static List<SMSCheckModel> smsCheckModelList = new ArrayList<SMSCheckModel>();

	public static int checkSMSOutTime(String phoneNum) {

		synchronized (smsCheckModelList) {

			for (int x = 0; x < smsCheckModelList.size(); x++) {

				if (smsCheckModelList.get(x).getPhoneNum().equals(phoneNum)) {

					if (System.currentTimeMillis() - smsCheckModelList.get(x).getTimeStamp() > TIME_RETRY) {
						return 0;
					} else {
						return 1;
					}

				}

			}

		}

		return 0;

	}

	public static int checkSMSCode(String phoneNum, int code) {

		synchronized (smsCheckModelList) {

			for (int x = 0; x < smsCheckModelList.size(); x++) {

				if (smsCheckModelList.get(x).getPhoneNum().equals(phoneNum)) {

					if (smsCheckModelList.get(x).getCode() == code) {

						if (System.currentTimeMillis() - smsCheckModelList.get(x).getTimeStamp() <= TIME_OUT) {
							return 0;
						} else {
							smsCheckModelList.remove(x);
							return 2;
						}

					} else {
						smsCheckModelList.remove(x);
						return 3;
					}

				}

			}

		}

		return 1;

	}

	public static void updateSMSCode(String phoneNum, int code) {

		SMSCheckModel smsCheckModel = null;

		for (int x = 0; x < smsCheckModelList.size(); x++) {

			if (smsCheckModelList.get(x).getPhoneNum().equals(phoneNum)) {

				smsCheckModel = smsCheckModelList.get(x);

			}

		}

		synchronized (smsCheckModelList) {

			if (smsCheckModel == null) {
				smsCheckModel = new SMSCheckModel();
				smsCheckModel.setPhoneNum(phoneNum);
				smsCheckModel.setCode(code);
				smsCheckModel.setTimeStamp(System.currentTimeMillis());
				smsCheckModelList.add(smsCheckModel);
			} else {
				smsCheckModel.setCode(code);
				smsCheckModel.setTimeStamp(System.currentTimeMillis());
			}

		}

	}

	public static String sendCode(final String phoneNum, final int code) {

		try {

			// 设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			// 初始化ascClient需要的几个参数
			final String product = "Dysmsapi";// 短信API产品名称
			final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名
			// 替换成你的AK
			final String accessKeyId = ACCESS_KEY_ID;// 你的accessKeyId,参考本文档步骤2
			final String accessKeySecret = ACCESS_KEY_SECRET;// 你的accessKeySecret，参考本文档步骤2
			// 初始化ascClient,暂时不支持多region
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

			IAcsClient acsClient = new DefaultAcsClient(profile);
			// 组装请求对象
			SendSmsRequest request = new SendSmsRequest();
			// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为20个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
			request.setPhoneNumbers(phoneNum);
			// 必填:短信签名-可在短信控制台中找到
			request.setSignName(SIGN_NAME);
			// 必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(TEMPLATE_CODE);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为

			request.setTemplateParam("{\"numcode\":\"" + String.valueOf(code) + "\"}");
			// 可选-上行短信扩展码(无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			// request.setOutId("yourOutId");
			// 请求失败这里会抛ClientException异常
			;

			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

			if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {

				updateSMSCode(phoneNum, code);

				return "SUCCESS";

			} else {
				return "FAILED:" + sendSmsResponse.getMessage();
			}

		} catch (ClientException e) {
			e.printStackTrace();
			return "YUN ERROR:" + e.getMessage();
		}

	}

}
