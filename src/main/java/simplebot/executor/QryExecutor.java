package simplebot.executor;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.Geo;
import com.vk.api.sdk.objects.messages.Button;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.users.User;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import simplebot.data.SimpleOrganization;
import simplebot.enums.CharityEnum;
import simplebot.enums.CharityKeyboard;
import simplebot.utils.Strings;
import simplebot.utils.Utils;

import javax.rmi.CORBA.Util;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

public class QryExecutor {
    private final VkApiClient client;
    private final GroupActor actor;
    private CharityKeyboard keyboard;

    public QryExecutor(VkApiClient client, GroupActor actor) {
        this.client = client;
        this.actor = actor;
    }

    public void setProps(Properties props) {
        this.keyboard = new CharityKeyboard(props);
    }

    public UserXtrCounters getUserById(String id) throws ClientException, ApiException {
        return client.users()
                .get(actor)
                .userIds(id)
                .execute()
                .get(0);
    }

    public void sendKeyboardToUser(UserXtrCounters user) throws ClientException, ApiException {
        Button[][] buttons = keyboard.get();
        String body = String.format(Strings.BOT_WELCOME_MSG, user.getFirstName());
        client.messages()
                .send(actor)
                .userId(user.getId())
                .keyboard(new Keyboard(false, buttons))
                .message(body)
                .execute();
    }

    public void sendAnswerToKeyboardClick(int id, CharityEnum faq) throws ClientException, ApiException {
        client.messages().send(actor)
                .userId(id)
                .message(faq.getPayload())
                .execute();
    }

    public void sendOrganizationsBasedOnUserLocation(int id, User user, Geo geo) throws ClientException, ApiException, UnsupportedEncodingException {
        String msg = String.format("Вижу тебя, %s! Ты в %s, %s. \n" +
                        "Cписок ближайших к тебе организаций и центров помощи животным: \n\n",
                user.getFirstName(), geo.getPlace().getCountry(), geo.getPlace().getCity());
        SimpleOrganization organization = new SimpleOrganization("г. Санкт-Петербург, ул.Пушкина, д.Колотушкина", "Помощь бомжам");
        organization.setWebAddress("https://sports.ru");
        organization.setPhoneNumber("8-999-123-45-67");
        msg = msg + organization.toString();

        client.messages().send(actor)
                .userId(id)
                .message(msg)
                .execute();
    }

    private void calcCoordinates(String coordinates) {
        double latitude = Utils.getLatitude(coordinates);
        double longitude = Utils.getLongitude(coordinates);
        Coordinate lat = Coordinate.fromDegrees(latitude);
        Coordinate lng = Coordinate.fromDegrees(longitude);
        Point usrAddress = Point.at(lat, lng);
    }
}
