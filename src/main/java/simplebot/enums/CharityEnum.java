package simplebot.enums;

import simplebot.utils.Strings;

public enum CharityEnum {
    MSG_1(Strings.BOT_FAQ_1),
    MSG_2(Strings.BOT_FAQ_2),
    MSG_3(Strings.BOT_FAQ_3);

    private final String buttonLabel;
    private String payload;

    CharityEnum(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getLabel() {
        return buttonLabel;
    }

    public String getPayload() {
        return payload;
    }

    public static CharityEnum faqOf(String label) {
        if (label.equalsIgnoreCase(CharityEnum.MSG_1.getLabel())) {
            return CharityEnum.MSG_1;
        } else if (label.equalsIgnoreCase(CharityEnum.MSG_2.getLabel())) {
            return CharityEnum.MSG_2;
        } else if (label.equalsIgnoreCase(CharityEnum.MSG_3.getLabel())) {
            return CharityEnum.MSG_3;
        }
        throw new IllegalArgumentException();
    }
}
