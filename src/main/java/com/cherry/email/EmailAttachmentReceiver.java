package com.cherry.email;

import java.io.BufferedReader;
import java.io.File;
//import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

/**
 * This program demonstrates how to download e-mail messages and save
 * attachments into files on disk.
 *
 * @author www.codejava.net
 *
 */
public class EmailAttachmentReceiver {
	private String saveDirectory;

	/**
	 * Sets the directory where attached files will be stored.
	 * @param dir absolute path of the directory
	 */
	public void setSaveDirectory(String dir) {
		this.saveDirectory = dir;
	}

	/**
	 * Downloads new messages and saves attachments to disk if any.
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 */
	public void downloadEmailAttachments(String host, String port,
			String userName, String password) {
		Properties properties = new Properties();

		// server setting
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);

		// SSL setting
		properties.setProperty("mail.pop3.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.socketFactory.port",
				String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("pop3");
			store.connect(userName, password);

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				Message message = arrayMessages[i];
				Address[] fromAddress = message.getFrom();
				String from = fromAddress[0].toString();
				String subject = message.getSubject();
				String sentDate = message.getSentDate().toString();

				String contentType = message.getContentType();
				String messageContent = "";

				// store attachment file name, separated by comma
				String attachFiles = "";

				if (contentType.contains("multipart")) {
					// content may contain attachments
					Multipart multiPart = (Multipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
						
						//	SaveAttachment.saveAttachment(part,saveDirectory);
		
		// ------------------------------------------------------------------------------------------------------------					
		
							try {

								URL url = new URL("http://localhost:8080/RESTfulExample/json/product/post");
								HttpURLConnection conn = (HttpURLConnection) url.openConnection();
								conn.setDoOutput(true);
								conn.setRequestMethod("POST");
								conn.setRequestProperty("Content-Type", "application/json");

								String input = "{\"qty\":100,\"name\":\"iPad 4\"}";

							

								if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
									throw new RuntimeException("Failed : HTTP error code : "
										+ conn.getResponseCode());
								}

								BufferedReader br = new BufferedReader(new InputStreamReader(
										(conn.getInputStream())));

								String output;
								System.out.println("Output from Server .... \n");
								while ((output = br.readLine()) != null) {
									System.out.println(output);
								}

								conn.disconnect();

							  } catch (MalformedURLException e) {

								e.printStackTrace();

							  } catch (IOException e) {

								e.printStackTrace();

							 }
// ----------------------------------------------------------------------------------------------------------------------							
							
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
							part.saveFile(saveDirectory + File.separator + fileName);
							
						} else {
							// this part may be the message content
							messageContent = part.getContent().toString();
						}
					}

					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				} else if (contentType.contains("text/plain")
						|| contentType.contains("text/html")) {
					Object content = message.getContent();
					if (content != null) {
						messageContent = content.toString();
					}
				}

				// print out details of each message
				System.out.println("Message #" + (i + 1) + ":");
				System.out.println("\t From: " + from);
				System.out.println("\t Subject: " + subject);
				System.out.println("\t Sent Date: " + sentDate);
				System.out.println("\t Message: " + messageContent);
				System.out.println("\t Attachments: " + attachFiles);
			}

			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Runs this program with Gmail POP3 server
	 */
	public static void main(String[] args) {
		String host = "pop.gmail.com";
		String port = "995";
		String userName = "gibranjabbar1994@gmail.com";
		String password = "Jabbar@1994";

		String saveDirectory = "E:/Attachment";

		EmailAttachmentReceiver receiver = new EmailAttachmentReceiver();
		receiver.setSaveDirectory(saveDirectory);
		receiver.downloadEmailAttachments(host, port, userName, password);

	}
}