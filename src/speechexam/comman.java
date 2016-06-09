/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechexam;

import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 *
 * @author MIHIR005
 */
public class comman {

    public int count_unread_mail() throws MessagingException {
        Folder inbox;

       
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        /* Set the mail properties */
        Properties props = System.getProperties();
        // Set manual Properties
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.host", "pop.gmail.com");

        /* Create the session and get the store for read the mail. */
        Session session = Session.getDefaultInstance(
                System.getProperties(), null);

        Store store = session.getStore("pop3");

        store.connect("pop.gmail.com", 995, "aitestpatel@gmail.com",
                "005mihir");

        /* Mention the folder name which you want to read. */
                                // inbox = store.getDefaultFolder();
        // inbox = inbox.getFolder("INBOX");
        inbox = store.getFolder("INBOX");

        /* Open the inbox using store. */
        inbox.open(Folder.READ_WRITE);

        /* Get the messages which is unread in the Inbox */
        Message messages[] = inbox.search(new FlagTerm(new Flags(
                Flags.Flag.SEEN), false));
        System.out.println("No. of Unread Messages : " + messages.length);
        
        return messages.length;
        
        
    }

}
