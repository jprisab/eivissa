package com.eivissa.restful;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.Value;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bmc.arsys.api.*;

public class JavaAPIHandler {

	private ARServerUser GetContext() {
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

		return ctx;
	}

	public JSONObject GetHPDEntryDataByTicketID(String strTicketID)  throws ARException {
		ARServerUser ctx = this.GetContext();
		JSONObject jsonObject = new JSONObject();
		
		String schemaName = "HPD:Help Desk";  
        String qualStr = "( 'Incident Number' = \"" + strTicketID + "\" )";  
        String entryID = "";  
        List<Field> fields = ctx.getListFieldObjects(schemaName);  
        QualifierInfo qual = ctx.parseQualification(qualStr, fields, null, 0);  
        int[] fieldIds = new int[]{};  
        OutputInteger nMatches = new OutputInteger();  
        ArrayList<SortInfo> sortOrder = new ArrayList<SortInfo>();  
        sortOrder.add(new SortInfo(2, 2));  
        List<Entry> entryList = ctx.getListEntryObjects(schemaName, qual, 0, 0, sortOrder, fieldIds, true, nMatches);  
        if (nMatches.intValue() > 0) {  
            entryID = ((Entry)entryList.get(0)).getEntryId();  
        }  		
		
        // more json puts here
		jsonObject.put("TicketID", strTicketID);
		
		ctx.logout();
		
		return jsonObject;

	}

	public String CreateHPDEntry(String strName, String strContent, String strPriority) {

		ARServerUser ctx = this.GetContext();
		JSONObject jsonObject = new JSONObject();

		String TicketID = "";

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

		// Hier Einbauen das eche TicketID gelesen wird und in R�ckgabe String
		// einbauen
		ctx.logout();
		
		return TicketID;
	}

	private String getPoepleID(String strSearchParam) throws ARException {
		
		ARServerUser ctx = this.GetContext();
		
		String strResult = "";
		String qualStr = "";
		
		String schemaName = "CTM:People";    
		
		if (strResult.length() == 10) {
			// its login name
		} else if (strResult.contains("@")) {
			qualStr = "( 'Mail Adress' = \"" + strSearchParam + "\" )";
		} else {
			// search by Full Name
		}
		
		List<Field> fields = ctx.getListFieldObjects(schemaName);  
		QualifierInfo qual = ctx.parseQualification(qualStr, fields, null, 0);  
		int[] fieldIds = new int[]{};  
        OutputInteger nMatches = new OutputInteger();  
        ArrayList<SortInfo> sortOrder = new ArrayList<SortInfo>();  
        sortOrder.add(new SortInfo(2, 2));  
        List<Entry> entryList = ctx.getListEntryObjects(schemaName, qual, 0, 0, sortOrder, fieldIds, true, nMatches);  
        if (nMatches.intValue() > 0) {  
        	strResult = ((Entry)entryList.get(0)).getEntryId();  
        }  
        
        ctx.logout();
        
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
		// 4000
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
