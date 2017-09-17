package com.inchcape.util;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.modules.security.placeholder.SecurePropertyPlaceholderModule;

public class DynamicPropertySetter extends SecurePropertyPlaceholderModule implements Callable {
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String country = (String) eventContext.getMessage().getOutboundProperty("country");
		String business = (String) eventContext.getMessage().getOutboundProperty("business");
		String object = (String) eventContext.getMessage().getOutboundProperty("object");
		
		
		// Flow Parameters
		System.setProperty("connectorName", (String) eventContext.getMessage().getOutboundProperty("connectorName"));
		System.setProperty("exchangeName", (String) eventContext.getMessage().getOutboundProperty("exchangeName"));
		System.setProperty("queueName", (String) eventContext.getMessage().getOutboundProperty("queueName"));
		System.setProperty("flowName", "flowName_"+country+"_"+business+"_"+object);
		
		System.setProperty("batch.country.business.object", "batch_"+country+"_"+business+"_"+object);
		System.setProperty("batch.max.failed.records",  (String) eventContext.getMessage().getOutboundProperty("maxFailedRecords"));
		
		// SFTP Parameters
		//System.setProperty("sftp.name", "sftpname_"+country+"_"+business+"_"+object);
		System.setProperty("sftp.input.country.business.object", (String) eventContext.getMessage().getOutboundProperty("inputPath"));
		System.setProperty("sftp.output.country.business.object", (String) eventContext.getMessage().getOutboundProperty("outputPath"));
		System.setProperty("sftp.error.country.business.object", (String) eventContext.getMessage().getOutboundProperty("errorPath"));
		System.setProperty("sftp.archive.country.business.object", (String) eventContext.getMessage().getOutboundProperty("archivePath"));
		
		System.setProperty("sftp.frequency.country.business.object", String.valueOf(eventContext.getMessage().getOutboundProperty("frequencyMilliseconds")));
		System.setProperty("sftp.attempts.country.business.object", String.valueOf(eventContext.getMessage().getOutboundProperty("attempts")));
		
		// Quartz - Cron Expression
		System.setProperty("quartzName", "quartzName_"+country+"_"+business+"_"+object);
		System.setProperty("cron.expression.country.business.object", (String) eventContext.getMessage().getOutboundProperty("cronexpression"));
		System.setProperty("cron.timestandard.country.business.object", (String) eventContext.getMessage().getOutboundProperty("timestandard"));

		// Target System API 
		System.setProperty("targetSystemNameAPI", "targetSystemAPI_"+object);
		System.setProperty("targetSystemAPI", (String) eventContext.getMessage().getOutboundProperty("targetSystemAPI"));
		System.setProperty("pathTargetSystemAPI", (String) eventContext.getMessage().getOutboundProperty("pathTargetSystemAPI"));
		
		return eventContext.getMessage().getPayload();
	}
	
}
