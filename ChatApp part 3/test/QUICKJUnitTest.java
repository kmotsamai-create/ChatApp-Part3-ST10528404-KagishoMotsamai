package Login;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testRecipientNumberValid() {
        Message msg = new Message(1);

        assertTrue(msg.checkRecipientCell("+27831234567"));
    }

    @Test
    public void testRecipientNumberInvalid() {
        Message msg = new Message(1);

        assertFalse(msg.checkRecipientCell("0831234567"));
    }

    @Test
    public void testMessageIDCreated() {
        Message msg = new Message(1);

        assertNotNull(msg.getMessageID());
        assertEquals(10, msg.getMessageID().length());
    }

    @Test
    public void testSetRecipient() {
        Message msg = new Message(1);

        msg.setRecipient("+27831234567");

        assertEquals("+27831234567", msg.getRecipient());
    }

    @Test
    public void testSetMessageText() {
        Message msg = new Message(1);

        msg.setMessageText("Hello World");

        assertEquals("Hello World", msg.getMessageText());
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message(1);

        msg.setMessageText("Hello World");

        String hash = msg.createMessageHash();

        assertNotNull(hash);
        assertTrue(hash.contains(":1:"));
    }

    @Test
    public void testTotalMessagesInitiallyZero() {
        assertEquals(0, Message.returnTotalMessages());
    }

    @Test
    public void testFullReportEmpty() {
        assertEquals("No messages found.", Message.displayFullReport());
    }
}