import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.UnaryOperator;

import static com.mineaurion.aurionchat.common.Utils.*;
import static net.kyori.adventure.text.Component.text;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

@SuppressWarnings("HttpUrlsUsage")
public class TestMarkdownProcessing {
    static String prefix = "[U]";
    static String displayName = "Kaleidox";
    static String suffix = "!";
    static String format = "Minecraft {prefix} {display_name}{suffix}: {message}";
    static String testUrl1 = "java.com";
    static String testUrl2 = "minecraft.net";
    static String testUrlRemoved = "[url removed]";
    static String testBase = "here is a url; %s and here is another: %s thats all there is!";

    static String testUrlClickable(String it) {
        return "https://" + it + "";
    }

    static String testUrlHttp(String it) {
        return "http://" + it;
    }

    static String format(String message) {
        return format
                .replace("{display_name}", displayName)
                .replace("{prefix}", prefix)
                .replace("{suffix}", suffix)
                .replace("{message}", message)
                ;
    }

    static String testText(String url1, String url2, boolean output) {
        String format = String.format(testBase, url1, url2);
        return output
                ? format(format)
                : format;
    }

    Player playerAdp;
    ConfigurationAdapter configAdp;
    AbstractAurionChat plugin;
    AurionChatPlayer player;

    @Before
    public void setup() {
        playerAdp = mock(Player.class);
        configAdp = mock(ConfigurationAdapter.class);
        plugin = mock(AbstractAurionChat.class);

        // we could specify call count here for micro-optimization
        expect(playerAdp.getDisplayName()).andReturn(displayName).atLeastOnce();
        expect(playerAdp.getPreffix()).andReturn(prefix).atLeastOnce();
        expect(playerAdp.getSuffix()).andReturn(suffix).atLeastOnce();
        expect(playerAdp.hasPermission("aurionchat.chat.colors")).andReturn(true).atLeastOnce();
        expect(configAdp.getChannels()).andReturn(new HashMap<>()).anyTimes();
        expect(plugin.getConfigurationAdapter()).andReturn(configAdp).anyTimes();

        replay(playerAdp, configAdp, plugin);
        player = new AurionChatPlayer(playerAdp, plugin);
    }

    @After
    public void teardown() {
        verify(playerAdp, configAdp, plugin);
        reset(playerAdp, configAdp, plugin);
    }

    void checkClickable(Component output, int childrenIndex, int urlIndex, UnaryOperator<String> urlFunc) {
        String url = new String[]{testUrl1,testUrl2}[urlIndex];
        List<Component> children = output.children();
        assertTrue("invalid children count", children.get(1).children().size() > childrenIndex);
        ClickEvent event = children.get(1).children().get(childrenIndex).clickEvent();
        assertNotNull("url is not clickable: " + url, event);
        assertEquals("event has wrong action", ClickEvent.Action.OPEN_URL, event.action());
        assertEquals("event has wrong url", urlFunc.apply(url), event.value());
    }

    void checkNotClickable(Component output, int childrenIndex) {
        List<Component> children = output.children();
        assertTrue("invalid children count", children.get(1).children().size() > childrenIndex);
        Component child = children.get(1).children().get(childrenIndex);
        ClickEvent event = child.clickEvent();
        assertNull("url should not be clickable: "+getDisplayString(child),event);
    }

    @Test
    public void testHttp() {
        Component output = processMessage(format, text("hello **world** how are _you_ __today__?"), player, new ArrayList<>(), true);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", testText(testUrl1, testUrlHttp(testUrl2), true), displayString);


        checkClickable(output, 1, 0, TestMarkdownProcessing::testUrlClickable);
        checkClickable(output, 3, 1, TestMarkdownProcessing::testUrlHttp);
    }
}
