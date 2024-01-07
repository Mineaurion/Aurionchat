import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.player.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.mineaurion.aurionchat.common.Utils.*;
import static net.kyori.adventure.text.Component.text;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

@SuppressWarnings("HttpUrlsUsage")
public class TestMessageProcessing {
    static String prefix = "[U]";
    static String displayName = "Kaleidox";
    static String suffix = "!";
    static String formatPrefix = "Minecraft {prefix} {display_name}{suffix}: ";
    static String testUrl1 = "java.com";
    static String testUrl2 = "minecraft.net";
    static String testUrlRemoved = "[url removed]";
    static String testBase = "here is a url; %s and here is another: %s thats all there is!";

    static String testUrlClickable(String it) {
        return "https://" + it;
    }

    static String testUrlHttp(String it) {
        return "http://" + it;
    }

    static String format() {
        return formatPrefix
                .replace("{display_name}", displayName)
                .replace("{prefix}", prefix)
                .replace("{suffix}", suffix);
    }

    static String testText(String url1, String url2, boolean output) {
        String format = String.format(testBase, url1, url2);
        return output
                ? format()+format
                : format;
    }

    Player playerAdp;
    ConfigurationAdapter configAdp;
    AbstractAurionChat plugin;
    AurionChatPlayer player;

    @Before
    public void setupAurionChatPlayer() {
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

    void checkClickable(Component output, int ci, int ui, UnaryOperator<String> urlFunc) {
        String url = new String[]{testUrl1,testUrl2}[ui];
        List<Component> children = output.children();
        assertTrue("invalid children count", children.size() > ci);
        ClickEvent event = children.get(ci).clickEvent();
        assertNotNull("url is not clickable: " + url, event);
        assertEquals("event has wrong action", ClickEvent.Action.OPEN_URL, event.action());
        assertEquals("event has wrong url", urlFunc.apply(url), event.value());
    }

    void checkNotClickable(Component output, int ci) {
        List<Component> children = output.children();
        assertTrue("invalid children count", children.size() > ci);
        Component child = children.get(ci);
        ClickEvent event = child.clickEvent();
        assertNull("url should not be clickable: "+getDisplayString(child),event);
    }

    @Test
    public void testDomain() {
        Component output = processMessage(formatPrefix, text(testUrl1), player, URL_MODE_ALLOW);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", format()+testUrl1, displayString);

        checkNotClickable(output, 0);
    }

    @Test
    public void testDomainScan() {
        Component output = processMessage(formatPrefix, text(testUrl1), player, URL_MODE_ALLOW | URL_MODE_SCAN_DOMAINS);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", format()+testUrl1, displayString);

        checkClickable(output, 0, 0, TestMessageProcessing::testUrlClickable);
    }

    @Test
    public void testUrl() {
        Component output = processMessage(formatPrefix, text(testUrlClickable(testUrl1)), player, URL_MODE_ALLOW);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", format()+testUrlClickable(testUrl1), displayString);

        checkClickable(output, 0, 0, TestMessageProcessing::testUrlClickable);
    }

    @Test
    public void testEmbeddedUrl() {
        Component output = processMessage(formatPrefix, text(testText(testUrl1, testUrlHttp(testUrl2), false)), player, URL_MODE_ALLOW);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", testText(testUrl1, testUrlClickable(testUrl2), true), displayString);

        checkNotClickable(output, 1);
        checkClickable(output, 3, 1, TestMessageProcessing::testUrlClickable);
    }

    @Test
    public void testDeniedUrl() {
        Component output = processMessage(formatPrefix, text(testText(testUrl1, testUrlClickable(testUrl2), false)), player, 0);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", testText(testUrl1, testUrlRemoved, true), displayString);

        checkNotClickable(output, 1);
        checkNotClickable(output, 3);
    }

    @Test
    public void testDeniedUrlDomainScan() {
        Component output = processMessage(formatPrefix, text(testText(testUrl1, testUrlClickable(testUrl2), false)), player, URL_MODE_SCAN_DOMAINS);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", testText(testUrlRemoved, testUrlRemoved, true), displayString);

        checkNotClickable(output, 1);
        checkNotClickable(output, 3);
    }

    @Test
    public void testSimplifiedDisplay() {
        Component output = processMessage(formatPrefix, text(testText(testUrl1, testUrlClickable(testUrl2), false)), player, URL_MODE_ALLOW | URL_MODE_DISPLAY_ONLY_DOMAINS);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", testText(testUrl1, testUrl2, true), displayString);

        checkNotClickable(output, 1);
        checkClickable(output, 3, 1, TestMessageProcessing::testUrlClickable);
    }

    @Test
    public void testHttp() {
        Component output = processMessage(formatPrefix, text(testText(testUrl1, testUrlHttp(testUrl2), false)), player, URL_MODE_ALLOW | URL_MODE_ALLOW_HTTP);

        // check display
        String displayString = getDisplayString(output);
        System.out.println(displayString);
        assertEquals("display string mismatch", testText(testUrl1, testUrlHttp(testUrl2), true), displayString);

        checkNotClickable(output, 1);
        checkClickable(output, 3, 1, TestMessageProcessing::testUrlHttp);
    }
}
