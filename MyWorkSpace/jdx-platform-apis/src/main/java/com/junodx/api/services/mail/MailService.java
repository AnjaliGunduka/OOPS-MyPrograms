package com.junodx.api.services.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.apache.commons.codec.CharEncoding;

//import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String OPS_EMAIL_TO = "email.opslist.to";

    private static final String ENV_PROFILE = "spring.profiles.active";
    private static final String EMAIL_FROM = "email.from";
    private static final String EMAIL_FROM_NAME = "email.from.name";
    private static final String EMAIL_BASE_URL = "email.baseUrl";
    private static final String EMAIL_IMAGE_URL = "email.imageUrl";
    private static final String EMAIL_DO_SEND = "email.doSend";
    private static final String EMAIL_ACTIVATION_TITLE = "activationTitle.ftl";
    private static final String EMAIL_ACTIVATION_CONTENT = "activationContent.ftl";
    private static final String EMAIL_ACTIVATION_REMINDER_TITLE = "reminderTitle.ftl";
    private static final String EMAIL_ACTIVATION_REMINDER_CONTENT = "reminderContent.ftl";
    private static final String EMAIL_RESET_TITLE = "resetTitle.ftl";
    private static final String EMAIL_RESET_CONTENT = "resetContent.ftl";
    private static final String EMAIL_STATUS_UPDATE_TITLE = "statusUpdateTitle.ftl";
    private static final String EMAIL_STATUS_UPDATE_CONTENT = "statusUpdateContent.ftl";
    private static final String EMAIL_RESULTS_AVAILABLE_TITLE = "resultsAvailableTitle.ftl";
    private static final String EMAIL_RESULTS_AVAILABLE_CONTENT = "resultsAvailableContent.ftl";

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender javaMailSender;

    // not injected
    private Configuration freemarkerConfig;
//    @Inject
//    private ResourceValidatorService resourceValidator;

    @Context
    private ServletContext servletContext;

    @PostConstruct
    public void init() {
        log.debug(hashCode() + " begin...");

        // init our template generator
        freemarkerConfig = new Configuration(Configuration.getVersion());
        // use the templates from
        freemarkerConfig.setClassForTemplateLoading(MailService.class, "/templates/freemarker");
        // if included in the war (or editable via an admin UX)
        // freemarkerConfig.setServletContextForTemplateLoading(servletContext, "WEB-INF/classes/templates/freemarker");
        log.debug("..., initialized freemarkerConfig as " + freemarkerConfig);

        /*Properties props = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> props.setProperty(propName, env.getProperty(propName)));
        log.debug("using spring env");
        for (Object key : props.keySet()) {
            log.debug("\t" + key + "\t\t" + props.getProperty((String)key));
        }
        JavaMailSenderImpl realSender = (JavaMailSenderImpl)javaMailSender;
        log.debug("mailsender has " + realSender.getHost() + ":" + realSender.getPort());
        for (Object key : realSender.getJavaMailProperties().keySet()) {
            log.debug("\t" + key + "\t\t\t" + props.getProperty((String)key));
        }*/
        log.debug(hashCode() + "...ends");
    }

    private boolean shouldSend() {
        String doSend = env.getProperty(EMAIL_DO_SEND);
        return Boolean.parseBoolean(doSend);
    }

    @Async
    public boolean sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        if (log.isDebugEnabled()) {
            log.debug(hashCode() + " Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}'", isMultipart,
                    isHtml, to, subject);
        }
        try {
            MimeMessage mimeMessage = createMimeMessage(to, subject, content, isMultipart, isHtml);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage(), e);
        }
        return false;
    }

    @Async
    public boolean sendEmails(List<MimeMessage> mimeMessages) {
        if (log.isDebugEnabled()) {
            log.debug(hashCode() + " Send batch of {} e-mails", mimeMessages.size());
        }
        try {
            javaMailSender.send((MimeMessage[])mimeMessages.toArray(new MimeMessage[mimeMessages.size()]));
            return true;
        } catch (Exception e) {
            log.warn("E-mails could not be sent to {} users, exception is: {}", mimeMessages.size(), e.getMessage());
        }
        return false;
    }

    private MimeMessage createMimeMessage(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(new InternetAddress(env.getProperty(EMAIL_FROM), env.getProperty(EMAIL_FROM_NAME)));
            message.setSubject(subject);
            message.setText(content, isHtml);
        } catch (MessagingException me) {
            log.error("Failed to create MimeMessage for " + to);
        }
        catch(UnsupportedEncodingException ue) {
            log.error("Failed to create MimeMessage due to an encoding issue of the from name for " + to);
        }

        return mimeMessage;
    }

   /*
    public void sendActivationEmails(final List<User> users) {
        if (log.isDebugEnabled()) {
            log.debug(hashCode() + " Sending activation e-mail to '{}' users", users.size());
        }
        final List<String> urls = new ArrayList<>();
        users.forEach(user -> {
            if(log.isDebugEnabled())
                log.debug("Building an activation email for user + " + user.getEmail());
            // create the user's resetToken
            final String resetToken = createToken(user);
            final String url = env.getProperty(EMAIL_BASE_URL) + "/app/rest/account/activate/" + resetToken;
            urls.add(url);
            // update the user columns, only save if it succeeds?
            Calendar now = Calendar.getInstance();
            user.setLastActivationNotice(now);
            user.setResetToken(resetToken);
            Calendar expiry = Calendar.getInstance();
            expiry.add(Calendar.MONTH, 1);
            user.setResetTokenExpires(expiry);
        });
        if (sendUserNotificationEmails(users, EMAIL_ACTIVATION_TITLE, EMAIL_ACTIVATION_CONTENT, "here", urls)) {
            userRepository.saveAll(users);
            userRepository.flush();
        } else {
            log.warn("batched emails for " + users.size() + " users failed to send");
        }
    }

    public void sendActivationEmail(final User user) {
        if (log.isTraceEnabled()) {
            log.trace(hashCode() + " Sending activation e-mail to '{}'", user.getEmail());
        }
        // create the user's resetToken
        final String resetToken = createToken(user);
        final String url = env.getProperty(EMAIL_BASE_URL) + "/app/rest/account/activate/" + resetToken;
        if (sendUserNotificationEmail(user, EMAIL_ACTIVATION_TITLE, EMAIL_ACTIVATION_CONTENT, "here", url)) {
            if (user.isEnabled()) {
                if (log.isInfoEnabled()) {
                    log.info(hashCode() + " Sending activation e-mail to '{}'", user.getEmail());
                }
            }
            Calendar now = Calendar.getInstance();
            user.setLastActivationNotice(now);
            user.setResetToken(resetToken);
            Calendar expiry = Calendar.getInstance();
            expiry.add(Calendar.MONTH, 1);
            user.setResetTokenExpires(expiry);
            // update the user with the token, set it to expire in a month
            userRepository.saveAndFlush(user);
            if (log.isDebugEnabled()) {
                log.debug("set resetToken to " + resetToken);
            }
        }
    }

    public void sendReminderActivationEmails(final List<User> users) {
        if (log.isTraceEnabled()) {
            log.trace(hashCode() + " Sending activation reminder e-mail to '{}' users", users.size());
        }
        final List<String> urls = new ArrayList<>();
        users.forEach(user -> {
            // reuse the user's resetToken
            final String resetToken = user.getResetToken();
            final String url = env.getProperty(EMAIL_BASE_URL) + "/app/rest/account/activate/" + resetToken;
            urls.add(url);
            // update the user columns, only save if it succeeds?
            Calendar now = Calendar.getInstance();
            user.setLastActivationNotice(now);

        });
        if (sendUserNotificationEmails(users, EMAIL_ACTIVATION_REMINDER_TITLE, EMAIL_ACTIVATION_REMINDER_CONTENT, "here", urls)) {
            userRepository.saveAll(users);
            userRepository.flush();
        } else {
            log.warn("batched emails for " + users.size() + " users failed to send");
        }
    }



    public void sendResetPasswordEmail(final User user) {
        if (log.isTraceEnabled()) {
            log.trace(hashCode() + " Sending reset password e-mail to '{}'", user.getEmail());
        }
        // create the user's resetToken
        final String resetToken = createToken(user);
        final String url = env.getProperty(EMAIL_BASE_URL) + "/app/rest/account/activate/" + resetToken;
        if (sendUserNotificationEmail(user, EMAIL_RESET_TITLE, EMAIL_RESET_CONTENT, "here", url)) {
            if (user.isEnabled()) {
                if (log.isInfoEnabled()) {
                    log.info(hashCode() + " Sending resetting e-mail to '{}'", user.getEmail());
                }
            }
            //Calendar now = Calendar.getInstance();
            //user.setLastActivationNotice(now);
            user.setResetToken(resetToken);
            Calendar expiry = Calendar.getInstance();
            expiry.add(Calendar.DAY_OF_MONTH, 1);
            user.setResetTokenExpires(expiry);
            // update the user with the token, set it to expire in a month
            userRepository.saveAndFlush(user);
            if (log.isDebugEnabled()) {
                log.debug("set resetToken to " + resetToken);
            }
        }
    }

    public void sendStatusUpdateEmail(final User user) {
        if (log.isTraceEnabled()) {
            log.trace(hashCode() + " Sending status update e-mail to '{}'", user.getEmail());
        }
        if (sendUserNotificationEmail(user, EMAIL_STATUS_UPDATE_TITLE, EMAIL_STATUS_UPDATE_CONTENT, "here",
                env.getProperty(EMAIL_BASE_URL))) {
            Calendar now = Calendar.getInstance();
            user.setLastStatusUpdateNotice(now);
            userRepository.save(user);
        }
    }

    public boolean sendResultsAvailableEmail(final User user) {
        if (log.isDebugEnabled()) {
            log.debug("Sending results available e-mail to '{}'", user.getEmail());
        }
        if (sendUserNotificationEmail(user, EMAIL_RESULTS_AVAILABLE_TITLE, EMAIL_RESULTS_AVAILABLE_CONTENT, "here",
                env.getProperty(EMAIL_BASE_URL))) {
            Calendar now = Calendar.getInstance();
            user.setLastResultsAvailableNotice(now);
            userRepository.save(user);

            return true;
        }

        return false;
    }

    //This is a hack to be able to test user email behavior for only test user accounts originating through Shopify
    private boolean shouldSendToThisUser(User u){

        //Only send to specific test users, otherwise the common shared Shopify DB will send out errant emails to actual users
        if(env.getProperty(ENV_PROFILE).equals("dev") && u.getFirstName().equals("TEST") && u.getLastName().equals("USER"))
            return true;
        else if(env.getProperty(ENV_PROFILE).equals("prod"))
            return true;

        return false;
    }

    public boolean sendUserNotificationEmails(final List<User> users, final String title, final String template,
                                              final String text, final List<String> urls) {
        if(log.isDebugEnabled());
        log.debug("Trying to send multiple mails for " + title);
        if (shouldSend()) {
            List<MimeMessage> mimeMessages = new ArrayList<>();
            users.forEach(user -> {
                if (shouldSendToThisUser(user)) {
                    if (log.isDebugEnabled())
                        log.debug("Can send to user " + user.getEmail());


                    // send all emails to the testing account
                    String email = user.getEmail();
                    if (!resourceValidator.validEmailAddress(email)) {
                        log.warn("Invalid email, cannot send user notification email to \"" + email + "\"");
                    } else {
                        if (log.isTraceEnabled()) {
                            log.trace(hashCode() + " Sending notification e-mail to '{}'", email);
                        }
                        try {
                            final String imageUrl = env.getProperty(EMAIL_IMAGE_URL);

                            if (log.isDebugEnabled())
                                log.debug("In sending user notification email, the imageUrl is " + imageUrl);

                            // create the email subject
                            Template t = freemarkerConfig.getTemplate(title);
                            String subject = processTemplateIntoString(t,
                                    Collections.singletonMap("firstName", user.getFirstName()));
                            // create the email content
                            t = freemarkerConfig.getTemplate(template);
                            Map<String, String> contentModel = new HashMap<>();
                            contentModel.put("locationText", text);
                            contentModel.put("locationURL", urls.get(users.indexOf(user)));
                            contentModel.put("imageURL", imageUrl);
                            String content = processTemplateIntoString(t, contentModel);
                            mimeMessages.add(createMimeMessage(email, subject, content, false, true));
                        } catch (IOException ioe) {
                            log.error("Failed to make the templated email...", ioe);
                        } catch (TemplateException te) {
                            log.error("Failed to make the templated email...", te);
                        }
                    }
                }
            });
            return sendEmails(mimeMessages);
        }
        if (log.isDebugEnabled()) {
            log.debug(hashCode() + " would have sent {} e-mails", users.size());
            users.forEach(user -> { logUserEmail(user.getEmail(), urls.get(users.indexOf(user))); });
        }
        return true;
    }

    private void logUserEmail(String user, String url) {
        log.debug("would send " + user + " the url " + url);
    }

    public boolean sendUserNotificationEmail(final User user, final String title, final String template,
                                             final String text, final String url) {
        if(log.isDebugEnabled());
        log.debug("Trying to send a single mail for " + title);
        if (shouldSend() && shouldSendToThisUser(user)) {
            String email = user.getEmail();
            if (!resourceValidator.validEmailAddress(email)) {
                log.warn("Invalid email, cannot send user notification email to \"" + email + "\"");
            } else {
                if(log.isDebugEnabled())
                    log.debug("Can send to this user? " + shouldSendToThisUser(user));

                if (log.isTraceEnabled()) {
                    log.trace(hashCode() + " Sending notification e-mail to '{}'", email);
                }
                try {
                    final String imageUrl = env.getProperty(EMAIL_IMAGE_URL);

                    if(log.isDebugEnabled())
                        log.debug("In sending user notification email, the imageUrl is " + imageUrl);

                    // create the email subject
                    Template t = freemarkerConfig.getTemplate(title);
                    String subject = processTemplateIntoString(t,
                            Collections.singletonMap("firstName", user.getFirstName()));
                    // create the email content
                    t = freemarkerConfig.getTemplate(template);
                    Map<String, String> contentModel = new HashMap<>();
                    contentModel.put("locationText", text);
                    contentModel.put("locationURL", url);
                    contentModel.put("imageURL", imageUrl);
                    String content = processTemplateIntoString(t, contentModel);
                    return sendEmail(email, subject, content, false, true);
                } catch (IOException ioe) {
                    log.error("Failed to make the templated email...", ioe);
                } catch (TemplateException te) {
                    log.error("Failed to make the templated email...", te);
                }
            }
        }
        if (log.isDebugEnabled()) {
            logUserEmail(user.getEmail(), url);
        }
        return true;
    }

    private static String createToken(final User user) {
        return RandomStringUtils.random(16, true, true);
        //return Integer.toHexString(("NeedARandomTokenFor"+user.getEmail()).hashCode());
    }

    */

    private static String processTemplateIntoString(Template template, Object model)
            throws IOException, TemplateException {
        StringWriter result = new StringWriter(1024);
        template.process(model, result);
        return result.toString();
    }



    // test with engage
    /*public static void main(String[] args) {
        MailService ms = new MailService();
        ms.init();
        JavaMailSenderImpl realSender = new JavaMailSenderImpl();
        realSender.setHost("smtp.gmail.com");
        realSender.setPort(465);
        realSender.setUsername("engage@junodx.com");
        realSender.setPassword(...);
        Properties props = realSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.ssl.enable", "true");
        realSender.setJavaMailProperties(props);

        ms.javaMailSender = realSender;
        boolean success = ms.sendEmail("junojdx@gmail.com", "Again, testing", "Sending directly from JMS", false, false);
        if (success) {
            log.debug("Sent testing email");
        }
    }*/

    // test with junojdx
    /*public static void main(String[] args) {
        MailService ms = new MailService();
        ms.init();
        JavaMailSenderImpl realSender = new JavaMailSenderImpl();
        realSender.setHost("smtp.gmail.com");
        realSender.setPort(587);
        realSender.setUsername("junojdx@gmail.com");
        realSender.setPassword(...);
        Properties props = realSender.getJavaMailProperties();
        props.put("mail.debug", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.ssl.enable", "true");
        realSender.setJavaMailProperties(props);

        ms.javaMailSender = realSender;
        boolean success = ms.sendEmail("junojdx+test@gmail.com", "testing today", "Sending directly from JMS", false, false);
        if (success) {
            log.debug("Sent testing email");
        }
    }*/

    public boolean sendOperationEmail(final String title, final String jsonPayload, final String text) {
        String email = env.getProperty(OPS_EMAIL_TO);

        if (log.isDebugEnabled())
            log.debug("Sending an operations email entitled {}", title);

        try {
            // create the email content
            String content = "";
            content += "Timestamp: ";
            content += Calendar.getInstance().getTime() + "\n";
            content += "General message: \n";
            content += text + "\n\n";
            content += "jsonPayload: \n";
            content += jsonPayload;
            return sendEmail(email, title, content, false, false);
        } catch (Exception e) {
            log.error("Failed to make the operations email...", e);
        }

        return true;
    }
}

