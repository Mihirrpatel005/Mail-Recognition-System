/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechexam;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Folder;
import javax.mail.search.FlagTerm;

public class MailReader {

    Folder inbox;

    // Constructor of the calss.
    public String MailReader() {
        System.out.println("Inside MailReader()...");
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        /* Set the mail properties */
        Properties props = System.getProperties();
        // Set manual Properties
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.host", "pop.gmail.com");

        try {

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
            System.out.println("No. of Unread Messages : " + inbox.getUnreadMessageCount());
           
            /* Use a suitable FetchProfile */
           // FetchProfile fp = new FetchProfile();
            // fp.add(FetchProfile.Item.ENVELOPE);

           // fp.add(FetchProfile.Item.CONTENT_INFO);
           // inbox.fetch(messages, fp);
            try {

           //     printAllMessages(messages);
                inbox.close(true);
                store.close();

            } catch (Exception ex) {
                System.out.println("Exception arise at the time of read mail");

                ex.printStackTrace();

            }
            
           
            
             return "";

        } catch (MessagingException e) {
            System.out.println("Exception while connecting to server: "
                    + e.getLocalizedMessage());
            e.printStackTrace();

            System.exit(2);
            return "";

        }
        

    }

    public void printAllMessages(Message[] msgs) throws Exception {

        for (int i = 0; i < msgs.length; i++) {

            System.out.println("MESSAGE #" + (i + 1) + ":");

            printEnvelope(msgs[i]);

        }

    }

    /* Print the envelope(FromAddress,ReceivedDate,Subject) */
    public void printEnvelope(Message message) throws Exception {

        Address[] a;

        // FROM
        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("FROM: " + a[j].toString());
            }
        }
        // TO
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("TO: " + a[j].toString());
            }
        }
        String subject = message.getSubject();

        Date receivedDate = message.getReceivedDate();
        Date sentDate = message.getSentDate(); // receivedDate is returning
        // null. So used getSentDate()

        String content = message.getContent().toString();
        System.out.println("Subject : " + subject);
        if (receivedDate != null) {
            System.out.println("Received Date : " + receivedDate.toString());
        }
        System.out.println("Sent Date : " + sentDate.toString());
        System.out.println("Content : " + content);

        getContent(message);

    }

    public void getContent(Message msg) {
        try {
            String contentType = msg.getContentType();
            System.out.println("Content Type : " + contentType);
            Multipart mp = (Multipart) msg.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                dumpPart(mp.getBodyPart(i));
            }
        } catch (Exception ex) {
            System.out.println("Exception arise at get Content");
            ex.printStackTrace();
        }
    }

    public void dumpPart(Part p) throws Exception {
        // Dump input stream ..
        InputStream is = p.getInputStream();
        // If "is" is not already buffered, wrap a BufferedInputStream
        // around it.
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        int c;
        System.out.println("Message : ");
        while ((c = is.read()) != -1) {
            System.out.write(c);
        }
    }

}
