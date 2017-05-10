package com.eivissa.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

import com.bmc.arsys.api.ARException;

@Path("/ticketget")
public class TicketGetHandler {
	@GET
	@Produces("application/json")
	public Response TicketGetService() throws JSONException {

		JSONObject jsonObject = new JSONObject();
		String strTicketID = "DEMOINC000001";

		jsonObject.put("TicketID", strTicketID);

		String result = "@Produces(\"application/json\") TicketID: \n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	@Path("{strTicketID}")
	@GET
	@Produces("application/json")
	public Response TicketGet(
			@PathParam("strTicketID") String strTicketID
			) throws JSONException, ARException  {
		
		JSONObject jsonObject = new JSONObject();
		
		JavaAPIHandler javai = new JavaAPIHandler();
		jsonObject = javai.GetHPDEntryDataByTicketID(strTicketID);
		
		

		String result = "@Produces(\"application/json\") TicketID\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

}
