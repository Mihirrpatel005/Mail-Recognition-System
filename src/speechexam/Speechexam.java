package speechexam;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.security.Security;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Folder;
import javax.mail.search.FlagTerm;


import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Speechexam {

    static String resultText;
    public static final String smtpUsername = "aitestpatel@gmail.com";
    public static final String smtpPassword = "005mihir";
    public static final String smtpHost = "smtp.gmail.com";
    public static final String smtpPort = "465";

    private static class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUsername, smtpPassword);
        }
    }

    public static void main(String[] args) {
        try {
            URL url;
            if (args.length > 0) {
                url = new File(args[0]).toURI().toURL();
            } else {
                url = Speechexam.class.getResource("mail.config.xml");
            }
            Voice v;
            VoiceManager vm = VoiceManager.getInstance();
            v = vm.getVoice("kevin16");

            v.allocate();

            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

            Date currentTime = localCalendar.getTime();
          
            Date d = new Date();

             v.speak("welcome , shweta yadav");
          
            ConfigurationManager cm = new ConfigurationManager(url);
            Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
            Microphone microphone = (Microphone) cm.lookup("microphone");
          

            recognizer.allocate();
          
            if (microphone.startRecording()) {

                
                while (true) {

                    Result result = recognizer.recognize();
                    if (result != null) {
                        
                        
          
                        resultText = result.getBestFinalResultNoFiller();
                        System.out.println("You said: " + resultText + "\n");
                      if(resultText.equalsIgnoreCase("s")){
                          
                          v.speak("hi how are you");
                      }
                        
                        if (resultText.equalsIgnoreCase("email")) {

                            String to = "";
                            String Eto = "";
                            String subj = "";
                            String Esubj = "";
                            String body = "";
                            String Ebody = "";
                            String answer = "";
                            Connection con = null;
                            ResultSet rt = null;
                            Statement st = null;
                            v.speak("to ,whom , would you like ,  to send this message ");
                            Result tor = recognizer.recognize();
                            if (tor != null) {
                                to = tor.getBestFinalResultNoFiller();
                                if (to != null) {

                                    System.out.println("wait...");

                                    System.out.println(to);
                                    
                                        Eto= "mihirpatel005@gmail.com";
                                    
                                  
                                                                     

                                }
                            }
                            v.speak("what's the subject of the email , me,here ");
                            Result Rsubj = recognizer.recognize();
                            if (Rsubj != null) {
                                subj = Rsubj.getBestFinalResultNoFiller();
                                if (subj != null) {

                                    System.out.println("wait...");

                                    System.out.println(subj);
                                    Esubj = subj;

                                }
                            }
                            v.speak("what ,would you like your ,email to say   ");

                            Result Rbody = recognizer.recognize();
                            if (Rbody != null) {
                                body = Rbody.getBestFinalResultNoFiller();
                                if (body != null) {
                                    System.out.println("wait...");
                                    System.out.println(body);
                                    Ebody = body;
                                    Ebody= Ebody +"\n\n"+ "Thank you"+"\n"+"Mihir patel";

                                }
                            }
                            if (Eto != null && Ebody != null && Esubj != null) {
                                v.speak("ok, me,here, here's your email message to " + Eto);
                                v.speak("you ready to send it");
                                Result ranswer = recognizer.recognize();
                                answer = ranswer.getBestFinalResultNoFiller();

                                int rs = 1;
                                try {

                                    String from = "aitestpatel@gmail.com";
                                    System.out.println("to : " + to);
                                    System.out.println("from : " + from);

                                    Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                                    Properties props = new Properties();
                                    props.put("mail.smtp.user", smtpUsername);
                                    props.put("mail.smtp.host", smtpHost);
                                    props.put("mail.smtp.port", smtpPort);
                                    props.put("mail.smtp.starttls.enable", "true");
                                    props.put("mail.smtps.auth", "true");
                                    props.put("mail.smtp.debug", "true");
                                    props.put("mail.smtp.socketFactory.port", smtpPort);
                                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                    props.put("mail.smtp.socketFactory.fallback", "false");
                                    props.put("mail.smtp.ssl", "true");

                                    Authenticator auth = new SMTPAuthenticator();
                                    SecurityManager security = System.getSecurityManager();

                                    Session smtpSession = Session.getInstance(props, auth);
                                    smtpSession.setDebug(true);

                                    MimeMessage smtpMessage = new MimeMessage(smtpSession);
                                    smtpMessage.setFrom(new InternetAddress(from));
                                    //for(int i=0;i<to.length;i++)
                                    {
                                        smtpMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(Eto));
                                    }
                                    smtpMessage.setSubject(Esubj);
                                    smtpMessage.setText(Ebody);

                                    Transport tr = smtpSession.getTransport("smtp");
                                    tr.connect(smtpHost, smtpUsername, smtpPassword);
                                    tr.sendMessage(smtpMessage, smtpMessage.getAllRecipients());
                                    tr.close();
                                    v.speak("send it");
                                } catch (Exception e) {
                                    System.out.println("errorrr" + e);
                                    rs = 0;
                                    v.speak("email appi have problelm");
                                }

                            }

                        }
                        if (resultText.equalsIgnoreCase("show my unread email")) {
                            try {
                                Folder inbox;

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

                                v.speak("your unread mail is " + messages.length);
                            } catch (MessagingException ex) {
                                System.out.println("error" + ex);
                            }

                        } else if (resultText.equalsIgnoreCase("speech recognize complete")) {
                            try {
                                System.out.println("Thanks for using !");
                                recognizer.deallocate();
                                System.exit(0);
                            } catch (Exception ecomp) {
                            }
                        } else if (resultText.equalsIgnoreCase("speech recognize start")) {
                            try {
                                recognizer.notify();
                                System.out.println("Hello again :-) ");
                                System.exit(0);
                            } catch (Exception estart) {
                            }
                        } else if (resultText.equalsIgnoreCase("stop recognize")) {
                            try {
                                // recognizer.wait();
                                System.out.println("See you later!");
                                System.exit(0);
                            } catch (Exception estop) {
                            }
                        }
                    } else {
                        System.out.println("I can't hear what you said.\n");
                    }
                }
            } else {
                System.out.println("Cannot start microphone.");
                recognizer.deallocate();
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Problem when loading Speechexam: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
    }
}
