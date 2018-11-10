package simplebot;

import com.vk.api.sdk.callback.longpoll.CallbackApiLongPoll;
import com.vk.api.sdk.callback.objects.board.CallbackBoardPostDelete;
import com.vk.api.sdk.callback.objects.group.*;
import com.vk.api.sdk.callback.objects.market.CallbackMarketComment;
import com.vk.api.sdk.callback.objects.market.CallbackMarketCommentDelete;
import com.vk.api.sdk.callback.objects.messages.CallbackMessageAllow;
import com.vk.api.sdk.callback.objects.messages.CallbackMessageDeny;
import com.vk.api.sdk.callback.objects.photo.CallbackPhotoComment;
import com.vk.api.sdk.callback.objects.photo.CallbackPhotoCommentDelete;
import com.vk.api.sdk.callback.objects.poll.CallbackPollVoteNew;
import com.vk.api.sdk.callback.objects.user.CallbackUserBlock;
import com.vk.api.sdk.callback.objects.user.CallbackUserUnblock;
import com.vk.api.sdk.callback.objects.video.CallbackVideoComment;
import com.vk.api.sdk.callback.objects.video.CallbackVideoCommentDelete;
import com.vk.api.sdk.callback.objects.wall.CallbackWallComment;
import com.vk.api.sdk.callback.objects.wall.CallbackWallCommentDelete;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.audio.Audio;
import com.vk.api.sdk.objects.board.TopicComment;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.WallPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplebot.enums.CharityEnum;
import simplebot.executor.QryExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static simplebot.utils.Utils.isClickedKeyboard;
import static simplebot.utils.Utils.isMsgStartChatBot;

public class CallbackApiLongPollHandler extends CallbackApiLongPoll {

    private static final Logger LOG = LoggerFactory.getLogger(CallbackApiLongPollHandler.class);
    private QryExecutor executor;

    public CallbackApiLongPollHandler(VkApiClient client, GroupActor actor, Properties props) {
        super(client, actor);
        this.executor = new QryExecutor(client, actor);
        executor.setProps(props);
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        try {
            int id = message.getUserId();
            UserXtrCounters user = executor.getUserById(String.valueOf(id));
            if (isMsgStartChatBot(message)) {
                executor.sendKeyboardToUser(user);
            } else if (isClickedKeyboard(message)) {
                executor.sendAnswerToKeyboardClick(id, CharityEnum.faqOf(message.getBody()));
            } else if (message.getGeo() != null) {
                executor.sendOrganizationsBasedOnUserLocation(id, user, message.getGeo());
            } else {
                runPythonMagic(message, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("messageNew: " + message.toString());
    }

    private void runPythonMagic(Message message, int id) throws IOException, InterruptedException {
        String format = String.format("echo '%s, %s' | /usr/bin/python /Users/ruslangm/Desktop/RBT.py",
                id, message.getBody());
        String[] cmd = {"/bin/bash", "-c", format};
        Process process = Runtime.getRuntime().exec(cmd);
        final InputStream is = process.getInputStream();
        Thread t = new Thread(() -> {
            InputStreamReader isr = new InputStreamReader(is);
            int ch;
            try {
                while ((ch = isr.read()) != -1) {
                    System.out.print((char) ch);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        process.waitFor();
        t.join();
    }

    @Override
    public void messageReply(Integer groupId, Message message) {
        LOG.info("messageReply: " + message.toString());
    }

    @Override
    public void messageEdit(Integer groupId, Message message) {
        LOG.info("messageReply: " + message.toString());
    }

    @Override
    public void messageAllow(Integer groupId, CallbackMessageAllow message) {
        LOG.info("messageAllow: " + message.toString());
    }

    @Override
    public void messageDeny(Integer groupId, CallbackMessageDeny message) {
        LOG.info("messageDeny: " + message.toString());
    }

    @Override
    public void photoNew(Integer groupId, Photo message) {
        LOG.info("photoNew: " + message.toString());
    }

    @Override
    public void photoCommentNew(Integer groupId, CallbackPhotoComment message) {
        LOG.info("photoCommentNew: " + message.toString());
    }

    @Override
    public void photoCommentEdit(Integer groupId, CallbackPhotoComment message) {
        LOG.info("photoCommentEdit: " + message.toString());
    }

    @Override
    public void photoCommentRestore(Integer groupId, CallbackPhotoComment message) {
        LOG.info("photoCommentRestore: " + message.toString());
    }

    @Override
    public void photoCommentDelete(Integer groupId, CallbackPhotoCommentDelete message) {
        LOG.info("photoCommentDelete: " + message.toString());
    }

    @Override
    public void audioNew(Integer groupId, Audio message) {
        LOG.info("audioNew: " + message.toString());
    }

    @Override
    public void videoNew(Integer groupId, Video message) {
        LOG.info("videoNew: " + message.toString());
    }

    @Override
    public void videoCommentNew(Integer groupId, CallbackVideoComment message) {
        LOG.info("videoCommentNew: " + message.toString());
    }

    @Override
    public void videoCommentEdit(Integer groupId, CallbackVideoComment message) {
        LOG.info("videoCommentEdit: " + message.toString());
    }

    @Override
    public void videoCommentRestore(Integer groupId, CallbackVideoComment message) {
        LOG.info("videoCommentRestore: " + message.toString());
    }

    @Override
    public void videoCommentDelete(Integer groupId, CallbackVideoCommentDelete message) {
        LOG.info("videoCommentDelete: " + message.toString());
    }

    @Override
    public void wallPostNew(Integer groupId, WallPost message) {
        LOG.info("wallPostNew: " + message.toString());
    }

    @Override
    public void wallRepost(Integer groupId, WallPost message) {
        LOG.info("wallRepost: " + message.toString());
    }

    @Override
    public void wallReplyNew(Integer groupId, CallbackWallComment message) {
        LOG.info("wallReplyNew: " + message.toString());
    }

    @Override
    public void wallReplyEdit(Integer groupId, CallbackWallComment message) {
        LOG.info("wallReplyEdit: " + message.toString());
    }

    @Override
    public void wallReplyRestore(Integer groupId, CallbackWallComment message) {
        LOG.info("wallReplyRestore: " + message.toString());
    }

    @Override
    public void wallReplyDelete(Integer groupId, CallbackWallCommentDelete message) {
        LOG.info("wallReplyDelete: " + message.toString());
    }

    @Override
    public void boardPostNew(Integer groupId, TopicComment message) {
        LOG.info("boardPostNew: " + message.toString());
    }

    @Override
    public void boardPostEdit(Integer groupId, TopicComment message) {
        LOG.info("boardPostEdit: " + message.toString());
    }

    @Override
    public void boardPostRestore(Integer groupId, TopicComment message) {
        LOG.info("boardPostRestore: " + message.toString());
    }

    @Override
    public void boardPostDelete(Integer groupId, CallbackBoardPostDelete message) {
        LOG.info("boardPostDelete: " + message.toString());
    }

    @Override
    public void marketCommentNew(Integer groupId, CallbackMarketComment message) {
        LOG.info("marketCommentNew: " + message.toString());
    }

    @Override
    public void marketCommentEdit(Integer groupId, CallbackMarketComment message) {
        LOG.info("marketCommentEdit: " + message.toString());
    }

    @Override
    public void marketCommentRestore(Integer groupId, CallbackMarketComment message) {
        LOG.info("marketCommentRestore: " + message.toString());
    }

    public void marketCommentDelete(Integer groupId, CallbackMarketCommentDelete message) {
        LOG.info("marketCommentDelete: " + message.toString());
    }

    public void groupLeave(Integer groupId, CallbackGroupLeave message) {
        LOG.info("groupLeave: " + message.toString());
    }

    public void groupJoin(Integer groupId, CallbackGroupJoin message) {
        LOG.info("groupJoin: " + message.toString());
    }

    public void groupChangeSettings(Integer groupId, CallbackGroupChangeSettings message) {
        LOG.info("groupChangeSettings: " + message.toString());
    }

    public void groupChangePhoto(Integer groupId, CallbackGroupChangePhoto message) {
        LOG.info("groupChangePhoto: " + message.toString());
    }

    public void groupOfficersEdit(Integer groupId, CallbackGroupOfficersEdit message) {
        LOG.info("groupOfficersEdit: " + message.toString());
    }

    public void pollVoteNew(Integer groupId, CallbackPollVoteNew message) {
        LOG.info("pollVoteNew: " + message.toString());
    }

    public void userBlock(Integer groupId, CallbackUserBlock message) {
        LOG.info("userBlock: " + message.toString());
    }

    public void userUnblock(Integer groupId, CallbackUserUnblock message) {
        LOG.info("userUnblock: " + message.toString());
    }
}
