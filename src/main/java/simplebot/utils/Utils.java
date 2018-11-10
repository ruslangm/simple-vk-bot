package simplebot.utils;

import com.vk.api.sdk.objects.messages.Message;
import simplebot.enums.CharityEnum;

public class Utils {
    public static boolean isMsgStartChatBot(Message msg) {
        return msg.getBody().equalsIgnoreCase(Strings.BOT_START_ENG) ||
                msg.getBody().equalsIgnoreCase(Strings.BOT_START_RUS);
    }

    public static boolean isClickedKeyboard(Message msg) {
        return msg.getBody().equalsIgnoreCase(CharityEnum.MSG_1.getLabel()) ||
                msg.getBody().equalsIgnoreCase(CharityEnum.MSG_2.getLabel()) ||
                msg.getBody().equalsIgnoreCase(CharityEnum.MSG_3.getLabel());
    }

    public static double getLatitude(String coordinates) {
        return Double.valueOf(coordinates.substring(0, coordinates.indexOf(" ")));
    }

    public static double getLongitude(String coordinates) {
        return Double.valueOf(coordinates.substring(coordinates.indexOf(" ")));
    }
}
