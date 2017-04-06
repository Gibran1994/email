package com.cherry.email;

import javax.mail.internet.MimeBodyPart;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

public class SaveAttachment
{
	@POST
	@Path("/get")
	@Consumes("multipart/form-data")
	public static void saveAttachment(MimeBodyPart part)
	{

	}
}
