package com.eivissa.restful;

/**
 * @author Akachay
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/ticketcreate")
public class TicketCreationHandler {

	@GET
	@Produces("application/json")
	public Response TicketCreateService() throws JSONException {

		JSONObject jsonObject = new JSONObject();
		String strTicketID = "DEMOINC000001";

		jsonObject.put("TicketID", strTicketID);

		String result = "@Produces(\"application/json\") TicketID: \n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	@Path("{strName}/{strContent}/{strPriority}/{strService}/{strOptionalImpact}/{strOptionalUrgency}/{strOptionalIncype}/{strOptionalReportedSource}")
	@GET
	@Produces("application/json")
	public Response TicketCreation(
			@PathParam("strName") String strName, 
			@PathParam("strContent") String strContent,
			@PathParam("strPriority") String strPriority,
			@PathParam("strService") String strService,
			@PathParam("strOptionalImpact") String strOptionalImpact,
			@PathParam("strOptionalUrgency") String strOptionalUrgency,
			@PathParam("strOptionalIncype") String strOptionalIncype,
			@PathParam("strOptionalReportedSource") String strOptionalReportedSource
			) throws JSONException {
		
		JavaAPIHandler javai = new JavaAPIHandler();
		String strTicketID = javai.CreateHPDEntry(strName, strContent, strPriority);
		

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Name", strName);
		jsonObject.put("Content", strContent);
		jsonObject.put("strPriority", strPriority);
		jsonObject.put("TicketID", strTicketID);

		String result = "@Produces(\"application/json\") TicketID\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	



	

}