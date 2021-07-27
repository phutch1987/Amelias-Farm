/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshop;

import com.sun.mail.smtp.SMTPTransport;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author hutch
 */
public class Mailing {
    
    
    
    public Mailing(String sender, String subject, File message){
        
        Properties prop = System.getProperties();
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(prop,null);
        Message msg = new MimeMessage(session);
        
        try{
            
            msg.setFrom(new InternetAddress(sender));
            
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("Hutchings2011@hotmail.com"));
            
            msg.setSubject(subject);
            
        //text
            
            MimeBodyPart p1 = new MimeBodyPart();
            p1.setText("");
            
        //file
            
            MimeBodyPart p2 = new MimeBodyPart();
            p2.setFileName(message.getName());
            
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(p1);
            mp.addBodyPart(p2);
            
            msg.setContent(mp);
            
            SMTPTransport t = (SMTPTransport) session.getTransport("TLSor SMTP");
            
        //connect
            
            t.sendMessage(msg, msg.getAllRecipients());
            
            t.close();
                 
        } catch (AddressException ex) {
            
            Logger.getLogger(Mailing.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (MessagingException ex) {
            
            Logger.getLogger(Mailing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
