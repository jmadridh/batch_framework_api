package com.inchcape.util;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

public class DynamicPropertySetter implements Callable {
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String country = (String) eventContext.getMessage().getOutboundProperty("country");
		String business = (String) eventContext.getMessage().getOutboundProperty("business");
		String object = (String) eventContext.getMessage().getOutboundProperty("object");
		
		
		// Flow Parameters
		System.setProperty("connectorName."+country+"."+business+"."+object, (String) eventContext.getMessage().getOutboundProperty("connectorName"));
		System.setProperty("flowName", "flowName_"+country+"_"+business+"_"+object);
		
		System.setProperty("batch.country.business.object", "batch_"+country+"_"+business+"_"+object);
		
		
		// SFTP Parameters
		//System.setProperty("sftp.name", "sftpname_"+country+"_"+business+"_"+object);
		System.setProperty("sftp.path.country.business.object", (String) eventContext.getMessage().getOutboundProperty("path"));
		System.setProperty("sftp.frequency.country.business.object", String.valueOf(eventContext.getMessage().getOutboundProperty("frequencyMilliseconds")));
		System.setProperty("sftp.attempts.country.business.object", String.valueOf(eventContext.getMessage().getOutboundProperty("attempts")));
		
		// Quartz - Cron Expression
		System.setProperty("quartzName", "quartzName_"+country+"_"+business+"_"+object);
		System.setProperty("cron.expression.country.business.object", (String) eventContext.getMessage().getOutboundProperty("cronexpression"));
		System.setProperty("cron.timestandard.country.business.object", (String) eventContext.getMessage().getOutboundProperty("timestandard"));
		

		return eventContext.getMessage().getPayload();
	}

}
