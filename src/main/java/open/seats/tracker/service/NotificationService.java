package open.seats.tracker.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.model.User;

@Service
@Log4j2
public class NotificationService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Environment env;

	public void sendEmail(String type, String sendTo, String subject, String body, boolean isHtml) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(body, isHtml);
			helper.setTo(sendTo);
			helper.setSubject(subject);
			String fromAddress = "Grab My Courses <" + env.getProperty("email.from.address." + type) + ">";
			helper.setFrom(fromAddress);

			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Exception in sending email", e);
		}
	}

	public void sendWelcomeEmail(User user, String redirectLink) {
		sendEmail("welcome", user.getEmail(), "Welcome! Verify your Grab My Courses account",
				buildVerificationEmail(user.getFullName(), redirectLink, env.getProperty("feedback.page.link"),
						env.getProperty("verify.email.expiry.mins", Integer.class) / 60),
				true);
	}

	public void sendForgotPasswordOtpEmail(User user, String otp) {
		sendEmail("forgot-password", user.getEmail(), "Reset your Grab My Courses account password!",
				buildForgotPasswordOtpEmail(user.getFullName(), otp,
						env.getProperty("forgot.password.otp.expiry.seconds", Integer.class) / 60),
				true);

	}

	private String buildForgotPasswordOtpEmail(String fullName, String otp, int expiry) {
		return "<!DOCTYPE html>\r\n" + "<html>\r\n" + "  <head>\r\n" + "    <style type=\"text/css\">\r\n"
				+ "      a .yshortcuts:hover {\r\n" + "        background-color: transparent !important;\r\n"
				+ "        border: none !important;\r\n" + "        color: inherit !important\r\n" + "      }\r\n"
				+ "      a .yshortcuts:active {\r\n" + "        background-color: transparent !important;\r\n"
				+ "        border: none !important;\r\n" + "        color: inherit !important\r\n" + "      }\r\n"
				+ "      a .yshortcuts:focus {\r\n" + "        background-color: transparent !important;\r\n"
				+ "        border: none !important;\r\n" + "        color: inherit !important\r\n" + "      }\r\n"
				+ "    </style>\r\n" + "    <style media=\"only screen and (max-width: 520px)\" type=\"text/css\">\r\n"
				+ "      /* /\\/\\/\\/\\/\\/\\/\\/\\/ RESPONSIVE MOJO /\\/\\/\\/\\/\\/\\/\\/\\/ */\r\n"
				+ "      /*  must escape media query with double symbol */\r\n"
				+ "      @media only screen and (max-width: 520px) {\r\n" + "        .main-table {\r\n"
				+ "          width: 90% !important;\r\n" + "        }\r\n" + "        .top {\r\n"
				+ "          padding-top: 33px !important;\r\n" + "          padding-bottom: 37px !important;\r\n"
				+ "        }\r\n" + "        .content {\r\n" + "          padding: 24px 29px !important;\r\n"
				+ "        }\r\n" + "        .verify-button {\r\n" + "          padding: 25px 0 !important;\r\n"
				+ "        }\r\n" + "      }\r\n" + "    </style>\r\n" + "  </head>\r\n"
				+ "  <body align=\"center\" style=\"margin:0; padding:0; -webkit-text-size-adjust:100%; -ms-text-size-adjust:100%; background:#ffffff; width:100%; font-family:'Roboto',sans-serif; font-size:16px; text-align:center; line-height:22px; color:#AAB2BA\" width=\"100%\">\r\n"
				+ "    <table class=\"main-table\" height=\"100%\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0; margin:20px auto 10px; padding:0; height:100%; width:80%; max-width:600px\" width=\"80%\">\r\n"
				+ "      \r\n" + "      <tr>\r\n"
				+ "        <td align=\"center\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0\" valign=\"top\">\r\n"
				+ "\r\n" + "          <!-- BODY -->\r\n"
				+ "          <div style=\"border: 1px solid rgba(223,226,230,0.6); border-radius: 4px; background-repeat: no-repeat; background-position: bottom -3px right -3px; background-size: 36%;\">\r\n"
				+ "            <table class=\"container\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0; width:100%; max-width:600px; margin:0 auto; padding:0; clear:both\" width=\"100%\">\r\n"
				+ "              <tr>\r\n"
				+ "                <td style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0\">\r\n"
				+ "                  <table class=\"row\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0; width:100%\" width=\"100%\">\r\n"
				+ "                    <tr>\r\n"
				+ "                      <td class=\"content\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-top:48px; padding-right:48px; padding-bottom:48px; padding-left:48px\">\r\n"
				+ "                        <table class=\"row\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0; width:100%\" width=\"100%\">\r\n"
				+ "                          <tr>\r\n"
				+ "                            <td style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; padding-left:0; padding-right:0; padding-top:0; padding-bottom:0; font-family:'Roboto', sans-serif; font-size:24px; line-height:38px; color:#1B2653\">\r\n"
				+ "                              Hey " + fullName + ",\r\n" + "                            </td>\r\n"
				+ "                          </tr>\r\n" + "                          <tr>\r\n"
				+ "                            <td style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; color:#2A3E52; padding-top:16px; padding-bottom:0px\">You recently requested for resetting your password. Please use the code below to complete the password reset process. Please note that this code is valid for next "
				+ expiry + " minutes only.</td>\r\n" + "                          </tr>\r\n"
				+ "                          \r\n" + "                          <tr>\r\n"
				+ "                            <td align=\"center\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; padding-bottom:0; color:#000; font-weight:bold; font-size:24px; padding-top:24px; text-align:center\">\r\n"
				+ "                              " + otp + "\r\n" + "                            </td>\r\n"
				+ "                          </tr>\r\n" + "                          \r\n"
				+ "                          <tr>\r\n"
				+ "                            <td style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; font-family:'Roboto', sans-serif; padding-left:0; padding-right:0; color:#2A3E52; padding-top:16px; padding-bottom:25px\">\r\n"
				+ "                              If you didn't attempt to reset your password just now, please contact <a href=\"mailto:support@grabmycourses.com\" style=\"color:#4F8DF9 !important; text-decoration:none\" target=\"_blank\">support@grabmycourses.com</a>\r\n"
				+ "                            </td>\r\n" + "                          </tr>\r\n"
				+ "                          \r\n" + "                          <tr>\r\n"
				+ "                            <td style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; color:#2A3E52; font-family:'Roboto', sans-serif; font-size:16px; line-height:22px; padding-top:0px; padding-right:0px; padding-bottom:26px; padding-left:0\">\r\n"
				+ "                              Cheers, <br /> Naveen from Grab My Courses\r\n"
				+ "                            </td>\r\n" + "                          </tr>\r\n"
				+ "                        </table>\r\n" + "                      </td>\r\n"
				+ "                    </tr>\r\n" + "                  </table>\r\n" + "                </td>\r\n"
				+ "              </tr>\r\n" + "            </table>\r\n" + "          </div>\r\n" + "\r\n"
				+ "          <!-- BODY END -->\r\n" + "        </td>\r\n" + "      </tr>\r\n" + "      <tr>\r\n"
				+ "        <td align=\"center\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; padding-left:0; padding-right:0; padding-top:15px; font-size:14px; text-align:center; font-family:'Roboto', sans-serif; text-align:center; padding-bottom:18px; line-height:16px\">\r\n"
				+ "          Need help? Feel free to <a href=\"https://grabmycourses.com/feedback\" style=\"color:#AAB2BA !important; text-decoration:none; font-weight:bold\" target=\"_blank\">Contact Us</a>\r\n"
				+ "        </td>\r\n" + "      </tr>\r\n" + "      <tr>\r\n"
				+ "        <td align=\"center\" style=\"border-collapse:collapse !important; mso-table-lspace:0pt; mso-table-rspace:0pt; padding-left:0; padding-right:0; padding-top:0; font-size:14px; font-family:'Roboto', sans-serif; line-height:16px; text-align:center; padding-bottom:50px\">\r\n"
				+ "          © Grab My Courses 2021 <br /> \r\n" + "        </td>\r\n" + "      </tr>\r\n"
				+ "    </table>\r\n" + "  </body>\r\n" + "</html>";
	}

	private String buildVerificationEmail(String userName, String redirectLink, String feedbackLink, int timeout) {
		return "<!DOCTYPE html>\r\n" + "<html>\r\n" + "\r\n" + "<head>\r\n" + "    <title></title>\r\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n"
				+ "    <style type=\"text/css\">\r\n" + "        @media screen {\r\n" + "            @font-face {\r\n"
				+ "                font-family: 'Lato';\r\n" + "                font-style: normal;\r\n"
				+ "                font-weight: 400;\r\n"
				+ "                src: local('Lato Regular'), local('Lato-Regular'), url(https://fonts.gstatic.com/s/lato/v11/qIIYRU-oROkIk8vfvxw6QvesZW2xOQ-xsNqO47m55DA.woff) format('woff');\r\n"
				+ "            }\r\n" + "\r\n" + "            @font-face {\r\n"
				+ "                font-family: 'Lato';\r\n" + "                font-style: normal;\r\n"
				+ "                font-weight: 700;\r\n"
				+ "                src: local('Lato Bold'), local('Lato-Bold'), url(https://fonts.gstatic.com/s/lato/v11/qdgUG4U09HnJwhYI-uK18wLUuEpTyoUstqEm5AMlJo4.woff) format('woff');\r\n"
				+ "            }\r\n" + "\r\n" + "            @font-face {\r\n"
				+ "                font-family: 'Lato';\r\n" + "                font-style: italic;\r\n"
				+ "                font-weight: 400;\r\n"
				+ "                src: local('Lato Italic'), local('Lato-Italic'), url(https://fonts.gstatic.com/s/lato/v11/RYyZNoeFgb0l7W3Vu1aSWOvvDin1pK8aKteLpeZ5c0A.woff) format('woff');\r\n"
				+ "            }\r\n" + "\r\n" + "            @font-face {\r\n"
				+ "                font-family: 'Lato';\r\n" + "                font-style: italic;\r\n"
				+ "                font-weight: 700;\r\n"
				+ "                src: local('Lato Bold Italic'), local('Lato-BoldItalic'), url(https://fonts.gstatic.com/s/lato/v11/HkF_qI1x_noxlxhrhMQYELO3LdcAZYWl9Si6vvxL-qU.woff) format('woff');\r\n"
				+ "            }\r\n" + "        }\r\n" + "\r\n" + "        /* CLIENT-SPECIFIC STYLES */\r\n"
				+ "        body,\r\n" + "        table,\r\n" + "        td,\r\n" + "        a {\r\n"
				+ "            -webkit-text-size-adjust: 100%;\r\n" + "            -ms-text-size-adjust: 100%;\r\n"
				+ "        }\r\n" + "\r\n" + "        table,\r\n" + "        td {\r\n"
				+ "            mso-table-lspace: 0pt;\r\n" + "            mso-table-rspace: 0pt;\r\n" + "        }\r\n"
				+ "\r\n" + "        img {\r\n" + "            -ms-interpolation-mode: bicubic;\r\n" + "        }\r\n"
				+ "\r\n" + "        /* RESET STYLES */\r\n" + "        img {\r\n" + "            border: 0;\r\n"
				+ "            height: auto;\r\n" + "            line-height: 100%;\r\n"
				+ "            outline: none;\r\n" + "            text-decoration: none;\r\n" + "        }\r\n" + "\r\n"
				+ "        table {\r\n" + "            border-collapse: collapse !important;\r\n" + "        }\r\n"
				+ "\r\n" + "        body {\r\n" + "            height: 100% !important;\r\n"
				+ "            margin: 0 !important;\r\n" + "            padding: 0 !important;\r\n"
				+ "            width: 100% !important;\r\n" + "        }\r\n" + "\r\n"
				+ "        /* iOS BLUE LINKS */\r\n" + "        a[x-apple-data-detectors] {\r\n"
				+ "            color: inherit !important;\r\n" + "            text-decoration: none !important;\r\n"
				+ "            font-size: inherit !important;\r\n" + "            font-family: inherit !important;\r\n"
				+ "            font-weight: inherit !important;\r\n"
				+ "            line-height: inherit !important;\r\n" + "        }\r\n" + "\r\n"
				+ "        /* MOBILE STYLES */\r\n" + "        @media screen and (max-width:600px) {\r\n"
				+ "            h1 {\r\n" + "                font-size: 32px !important;\r\n"
				+ "                line-height: 32px !important;\r\n" + "            }\r\n" + "        }\r\n" + "\r\n"
				+ "        /* ANDROID CENTER FIX */\r\n" + "        div[style*=\"margin: 16px 0;\"] {\r\n"
				+ "            margin: 0 !important;\r\n" + "        }\r\n" + "    </style>\r\n" + "</head>\r\n"
				+ "\r\n"
				+ "<body style=\"background-color: #f4f4f4; margin: 0 !important; padding: 0 !important;\">\r\n"
				+ "    <!-- HIDDEN PREHEADER TEXT -->\r\n"
				+ "    <div style=\"display: none; font-size: 1px; color: #fefefe; line-height: 1px; font-family: 'Lato', Helvetica, Arial, sans-serif; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;\"> We're thrilled to have you here! Get ready to dive into your new account. </div>\r\n"
				+ "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
				+ "        <!-- LOGO -->\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#FFA73B\" align=\"center\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\r\n"
				+ "                    </tr>\r\n" + "                </table>\r\n" + "            </td>\r\n"
				+ "        </tr>\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#FFA73B\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\r\n"
				+ "                            <h1 style=\"font-size: 48px; font-weight: 400; margin: 2;\">Welcome! "
				+ userName
				+ "</h1> <img src=\" https://img.icons8.com/clouds/100/000000/handshake.png\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" />\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                </table>\r\n"
				+ "            </td>\r\n" + "        </tr>\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 40px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">We're excited to help you have a great semester at ASU. To get started, please verify your account by just pressing the button below. <br/><br/>Please note that this verification link "
				+ "				will expire in " + timeout
				+ " hours. You can try registering again in case this link expires. </p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\">\r\n"
				+ "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\r\n"
				+ "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td align=\"center\" style=\"border-radius: 3px;\" bgcolor=\"#FFA73B\"><a href="
				+ redirectLink
				+ " target=\"_blank\" style=\"font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; border: 1px solid #FFA73B; display: inline-block;\">Confirm Account</a></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n" + "                                </tr>\r\n"
				+ "                            </table>\r\n" + "                        </td>\r\n"
				+ "                    </tr> <!-- COPY -->\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 0px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">If above button doesn't work, please copy and paste the following link in your browser to confirm your account:</p>\r\n"
				+ "                        </td>\r\n" + "                    </tr> <!-- COPY -->\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\"><a href=" + redirectLink
				+ " target=\"_blank\" style=\"color: #FFA73B;\">" + redirectLink + "</a></p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">Important point:<br/>1. Our open class notification alerts are sent using the the "
				+ "email \"" + env.getProperty("email.from.address.open.seat")
				+ "\". Please ensure that you have added this email to your list of trusted contacts so that our emails "
				+ "don't land in your spam folder. <br/><br/>If you have any questions, just reply to this email — we're always happy to help out.</p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <p style=\"margin: 0;\">Cheers,<br>Naveen from Grab My Courses</p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                </table>\r\n"
				+ "            </td>\r\n" + "        </tr>\r\n" + "        <tr>\r\n"
				+ "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 30px 10px 0px 10px;\">\r\n"
				+ "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\r\n"
				+ "                    <tr>\r\n"
				+ "                        <td bgcolor=\"#FFECD1\" align=\"center\" style=\"padding: 30px 30px 30px 30px; border-radius: 4px 4px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\r\n"
				+ "                            <h2 style=\"font-size: 20px; font-weight: 400; color: #111111; margin: 0;\">Need more help?</h2>\r\n"
				+ "                            <p style=\"margin: 0;\"><a href=" + feedbackLink
				+ " target=\"_blank\" style=\"color: #FFA73B;\">We&rsquo;re here to help you out</a></p>\r\n"
				+ "                        </td>\r\n" + "                    </tr>\r\n" + "                </table>\r\n"
				+ "            </td>\r\n" + "        </tr>\r\n" + "    </table>\r\n" + "</body>\r\n" + "\r\n"
				+ "</html>";
	}
}
