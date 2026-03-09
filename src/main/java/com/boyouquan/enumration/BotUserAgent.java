package com.boyouquan.enumration;

import org.apache.commons.lang3.StringUtils;

public enum BotUserAgent {

    TELEGRAM_BOT("TelegramBot"),
    GOOGLE_BOT("Googlebot"),
    AHREFS_BOT("AhrefsBot"),
    SMT_BOT("SMTBot"),
    RU_BOT("RU_Bot"),
    DIFF_BOT("Diffbot"),
    LIVELAP_BOT("LivelapBot"),
    YISOU_BOT("YisouSpider");

    private String name;

    BotUserAgent(String name) {
        this.name = name;
    }

    public static boolean isBotAgent(String name) {
        if (StringUtils.isNotBlank(name)) {
            for (BotUserAgent agent : BotUserAgent.values()) {
                if (name.contains(agent.name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
