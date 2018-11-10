package simplebot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.util.Properties;

public class Application {

    private static final String PROPERTIES_FILE = "config.properties";
    private static final String BOT_PROPERTIES_FILE = "bot.properties";

    public static void main(String[] args) throws IOException, ClientException, ApiException {
        BasicConfigurator.configure();
        Properties properties = readProperties(PROPERTIES_FILE);
        GroupActor groupActor = createGroupActor(properties);

        HttpTransportClient httpClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(httpClient);

        if (!vk.groups().getLongPollSettings(groupActor).execute().isEnabled()) {
            vk.groups().setLongPollSettings(groupActor).enabled(true).wallPostNew(true).execute();
        }

        Properties botProps = readBotProperties(BOT_PROPERTIES_FILE);
        CallbackApiLongPollHandler handler = new CallbackApiLongPollHandler(vk, groupActor, botProps);
        handler.run();
    }

    private static GroupActor createGroupActor(Properties properties) {
        String groupId = properties.getProperty("groupId");
        String accessToken = properties.getProperty("token");
        return new GroupActor(Integer.parseInt(groupId), accessToken);
    }

    public static Properties readProperties(String propertiesPath) throws IOException {
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(propertiesPath);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");
        }
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        } finally {
            inputStream.close();
        }
    }

    public static Properties readBotProperties(String propertiesPath) throws IOException {
        StringWriter writer = new StringWriter();
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(propertiesPath);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");
        }
        IOUtils.copy(inputStream, writer, "UTF-8");
        String strings = writer.toString();
        try {
            Properties properties = new Properties();
            properties.load(new StringReader(strings));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        } finally {
            inputStream.close();
        }
    }}
