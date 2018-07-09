package com.context;

import com.aiml.AskToAIML;
import com.aiml.AskToDB;
import com.aiml.IAskApproach;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 聊天上下文
 *
 */
public class ChartContext implements IAskApproach {

    private IAskApproach askToAIML = null;

	private IAskApproach askToDB = null;

	private final String NULLSIGN = "#"; // 这个标志是用来表示，当查询AIML的时候，匹配到了*。

    private final String USEFULSIGN = "$";// 这个标志是用来表示，当查询AIML的时候匹配到了专业问题的模式。

	private final String[] NULLREPLAY = {
        "对不起，我还不能回答您的这个问题。",
        "唔，主人还没有教会我这个问题呢。",
        "我暂时还回答不了这个问题呢？",
        "我好像不明白。",

	};

	private final String DMSDMSG ="您可以联系大米时代的其他小哥哥小姐姐，座机：010-51292788 或者 0316-5552070， 手机（微信）：1583163905，\n" +
			"  邮箱：3460307818@qq.com，\n" +
			"  您也可以直接来看看小A，地址：廊坊市广阳区文明路与永丰道交叉口志晟创客中心四楼，我们期待您的来访！\n";

	public ChartContext(AskToAIML askToAIML, AskToDB askToDB) {
		this.askToAIML = askToAIML;
		this.askToDB = askToDB;
	}

	/**
	 * 联合AIML和数据库这2个知识库。
	 */
	public String response(String input) {
		String responseFromAIML = askToAIML.response(input);
		// 替换文本中的空格字符串
        // 判断是中文还是英文,中文需要去掉空格。
//        if(isContainChinese(responseFromAIML)){
//            responseFromAIML = responseFromAIML.replace(" ", "");
//        }
		return translate(input, responseFromAIML);
	}



    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


	/**
	 * 从数据库中查询
     * （处理无法回复的回复）
     *
	 * @param originInput 原来输入的内容
	 * @param aimlReplay 回答的内容
	 * @return
	 */
	private String translate(String originInput, String aimlReplay) {
		String asDBInput = "";
		if (-1 != aimlReplay.indexOf(NULLSIGN)) {
			asDBInput = originInput;
		} else if (-1 != aimlReplay.indexOf(USEFULSIGN)) {
			asDBInput = aimlReplay.replaceAll(USEFULSIGN, "");
		} else {
			return aimlReplay;
		}

        aimlReplay = aimlReplay.replaceFirst("#","");


		String dbReplay = askToDB.response(aimlReplay);
		if (0 == dbReplay.length()) {
			return getRandomResponse();
		} else {
			return dbReplay;
		}
	}

    /**
     * 随机回复
     * @return
     */
	private String getRandomResponse() {
		return NULLREPLAY[getRandomNum()]+DMSDMSG;
	}

	private int getRandomNum() {
		return (int) (Math.random() * NULLREPLAY.length);
	}
}
