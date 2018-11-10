package simplebot.enums;

import com.vk.api.sdk.objects.messages.Button;
import com.vk.api.sdk.objects.messages.ButtonAction;
import com.vk.api.sdk.objects.messages.ButtonActionType;
import com.vk.api.sdk.objects.messages.ButtonColor;
import simplebot.utils.Strings;

import java.util.Properties;

public class CharityKeyboard {
    private final Properties props;
    private final Button[][] buttons;
    private static final int ROWS_LENGTH = 3;
    private static final int COLUMNS_LENGTH = 1;

    public CharityKeyboard(Properties props) {
        this.props = props;
        this.buttons = initButtons();
    }

    private Button[][] initButtons() {
        Button[][] buttons = new Button[ROWS_LENGTH][COLUMNS_LENGTH];
        String payload = "{\"button\":\"1\"}";
        CharityEnum faq1 = CharityEnum.MSG_1;
        CharityEnum faq2 = CharityEnum.MSG_2;
        CharityEnum faq3 = CharityEnum.MSG_3;
        faq1.setPayload(props.getProperty(Strings.PROPERTY_FAQ_1));
        faq2.setPayload(props.getProperty(Strings.PROPERTY_FAQ_2));
        faq3.setPayload(props.getProperty(Strings.PROPERTY_FAQ_3));
        buttons[0][0] = new Button(new ButtonAction(ButtonActionType.Text, faq1.getLabel(), payload),
                ButtonColor.Default);
        buttons[1][0] = new Button(new ButtonAction(ButtonActionType.Text, faq2.getLabel(), payload),
                ButtonColor.Default);
        buttons[2][0] = new Button(new ButtonAction(ButtonActionType.Text, faq3.getLabel(), payload),
                ButtonColor.Default);
        return buttons;
    }

    public Button[][] get() {
        return buttons;
    }
}
