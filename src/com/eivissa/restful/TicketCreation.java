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
import com.bmc.arsys.api.*;

@Path("/ticketcreate")
public class TicketCreation {

	@GET
	@Produces("application/json")
	public Response TicketCreateService() throws JSONException {

		JSONObject jsonObject = new JSONObject();
		String strTicketID = "DEMOINC000001";

		jsonObject.put("TicketID", strTicketID);

		String result = "@Produces(\"application/json\") TicketID: \n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	@Path("{strName}/{strContent}")
	@GET
	@Produces("application/json")
	public Response TicketCreation(@PathParam("strName") String strName, @PathParam("strContent") String strContent,
			@PathParam("strPriority") String strPriority) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Name", strName);
		jsonObject.put("Content", strContent);
		jsonObject.put("strPriority", strPriority);
		jsonObject.put("TicketID", CreateHPDEntry(strName, strContent, strPriority));

		String result = "@Produces(\"application/json\") TicketID\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	private String CreateHPDEntry(String strName, String strContent, String strPriority) {

		String TicketID = "";

		ARServerUser ctx = new ARServerUser();
		ctx.setServer("serverName");
		ctx.setPort(1234);
		ctx.setUser("userName");
		ctx.setPassword("password");
		try {
			ctx.verifyUser();
		} catch (ARException arxExc) {
			arxExc.getMessage();
		}

		Entry newEntry = new Entry();
		newEntry.put(7, new Value(0)); // Status
		newEntry.put(8, new Value("This record is created via Java API")); // Short
																			// Description
		newEntry.put(1000000163, getTicketImpactWeight(strPriority)); // Impact
																		// -
																		// (High)
																		// 1000/2000/3000/4000
																		// (Low)
		newEntry.put(1000000162, getTicketUrgencyWeight(strPriority)); // Urgency
																		// -
																		// (High)
																		// 1000/2000/3000/4000
																		// (Low)
		newEntry.put(1000000000, getDescriptionFromContent(strContent)); // Description
		newEntry.put(1000000151, new Value(strContent)); // Details
		newEntry.put(1000000099, new Value(0)); // Service Type - 0/1/2/3
		newEntry.put(240001002, new Value("xyz")); // Product Name
		newEntry.put(200000003, new Value("xyz")); // Product Cat Tier 1
		newEntry.put(240001002, new Value("xyz")); // Product Cat Tier 2
		newEntry.put(200000005, new Value("xyz")); // Product Cat Tier 3
		newEntry.put(1000000063, new Value("xyz")); // Operational Cat Tier 1
		newEntry.put(1000000064, new Value("xyz")); // Operational Cat Tier 2
		newEntry.put(1000000217, new Value("xyz")); // Assigned Group
		newEntry.put(1000000054, new Value("xyz")); // Corporate ID
		newEntry.put(2, new Value("Demo"));
		try {
			// And here we create the entry itself, printing out the EntryID we
			// get back
			String EntryId = ctx.createEntry("HPD:Help Desk", newEntry);
			System.out.println("Request ID = " + EntryId);
		} catch (ARException arException) {
			arException.printStackTrace();
		}

		// Hier Einbauen das eche TicketID gelesen wird und in Rückgabe String
		// einbauen

		return TicketID;
	}

	private int getTicketCost(String strTransactionType) {
		int iResult = 0;

		switch (strTransactionType) {
		case "EntryWithoutAttachment":
			iResult = 5;
			break;
		case "EntryWithSmallAttachment":
			iResult = 10;
			break;
		case "EntryWithMediumAttachment":
			iResult = 20;
			break;
		case "EntryWithLargeAttachment":
			iResult = 50;
			break;

		default:
			iResult = 0;
			break;
		}

		return iResult;
	}

	
	private String getPoepleID(String strSearchParam, ARServerUser ARServerUser) {
		String strResult = "";
		if (strResult.length() == 10) {
			// its login name
		} else if (strResult.contains("@")) {
			// search by mail adress
		} else {
			// search by Full Name
		}

		return strResult;
	}

	private Value getTicketUrgencyWeight(String strSearchParam) {
		Value valResult = new Value(4000);
		// if low: urgency/impact

		// if medium: urgency/impact

		// if high: urgency/impact

		// if critical: urgency/impact

		return valResult;
	}

	private Value getTicketImpactWeight(String strSearchParam) {
		Value valResult = new Value(4000);
		// if low: urgency/impact

		// if medium: urgency/impact

		// if high: urgency/impact

		// if critical: urgency/impact

		return valResult;
	}

	private Value getDescriptionFromContent(String strContent) {
		Value valResult = new Value(strContent.substring(0, 253));
		return valResult;
	}

}