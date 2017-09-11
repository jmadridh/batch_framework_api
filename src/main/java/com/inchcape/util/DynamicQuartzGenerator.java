package com.inchcape.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.mule.DefaultMuleEvent;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.MuleRuntimeException;
import org.mule.api.config.ConfigurationBuilder;
import org.mule.api.context.MuleContextBuilder;
import org.mule.api.context.MuleContextFactory;
import org.mule.api.lifecycle.Callable;
import org.mule.config.ConfigResource;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.construct.Flow;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.session.DefaultMuleSession;
import org.springframework.context.ApplicationContext;


public class DynamicQuartzGenerator implements Callable {
	
	private static final String DYNAMIC_QUARTZ_PROPERTY_NAME = "quartzName.";
	private static final String DYNAMIC_QUARTZ_CONFIG_FILE = "dynamic-quartz-template.xml";
	private static final String DYNAMIC_BATCH_CONFIG_FILE = "dynamic-batch-template.xml";
	
	private ApplicationContext applicationContext;
    private Map<String,MuleContext> contexts = Collections.synchronizedMap(new HashMap<String, MuleContext>());
    private String contextName;
    private List<String> configs;
    
    @Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
    	String commonPath = commonPath(eventContext);
    	configs = new ArrayList<String>();
    	configs.add(DYNAMIC_QUARTZ_CONFIG_FILE);
    	configs.add(DYNAMIC_BATCH_CONFIG_FILE);
    	contextName = eventContext.getMuleContext().getUniqueIdString();
    	add(contextName, configs);
    	//run(contextName, System.getProperty(DYNAMIC_QUARTZ_PROPERTY_NAME+commonPath), eventContext.getMessage());
	    	return eventContext.getMessage().getPayload();
	}
    
	private String commonPath(MuleEventContext eventContext) {

		String country = (String) eventContext.getMessage().getOutboundProperty("country");
		String business = (String) eventContext.getMessage().getOutboundProperty("business");
		String object = (String) eventContext.getMessage().getOutboundProperty("object");
		return country+"."+business+"."+object;
	}

	private void add(String contextName, List<String> configs) {
        checkExistenceOf(contextName);

        try {
            MuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
            List<ConfigurationBuilder> builders = new ArrayList<ConfigurationBuilder>();
            builders.add(springApplicationBuilderUsing(configs));
            MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
            MuleContext context = muleContextFactory.createMuleContext(builders, contextBuilder);

            context.start();

            if (context.isStarted()) {
                contexts.put(contextName, context);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
	
	private MuleMessage run(String contextName, String flowName, MuleMessage message) throws MuleException {
        MuleContext context = getContextWith(contextName);
        Flow flow = getFlowUsing(flowName, context);
        return flow.process(new DefaultMuleEvent(message, MessageExchangePattern.ONE_WAY, new DefaultMuleSession(flow, context))).getMessage();
    }
	
	private void checkExistenceOf(String contextName) {
        if (contexts.containsKey(contextName))
            throw new RuntimeErrorException(new Error("Context already exists"));
    }
	
	private SpringXmlConfigurationBuilder springApplicationBuilderUsing(List<String> payload) {
        ConfigResource[] resources = createResources(payload);
        SpringXmlConfigurationBuilder springXmlConfigurationBuilder = new SpringXmlConfigurationBuilder(resources);
        springXmlConfigurationBuilder.setParentContext(this.applicationContext);
        return springXmlConfigurationBuilder;
    }
	
	private ConfigResource[] createResources(List<String> muleConfigs) {
        ConfigResource[] configResources = new ConfigResource[muleConfigs.size()];
        Iterator<String> it = muleConfigs.iterator();
        
        try {
        for (int i=0; it.hasNext(); i++) {
            String muleConfig = it.next();
//            configResources[i] = new ConfigResource("context"+i+".xml", new ByteArrayInputStream(muleConfig.getBytes()));
            configResources[i] = new ConfigResource(muleConfig);
        }
        } catch (IOException e) {
        	throw new MuleRuntimeException(e);
        }
        return configResources;
    }

	private Flow getFlowUsing(String flowName, MuleContext context) {
        Flow flow = (Flow) context.getRegistry().lookupFlowConstruct("${quartzName}");
        if (flow == null) throw new RuntimeErrorException(new Error("flow does not exist"));
        return flow;
    }

    private MuleContext getContextWith(String contextName) {
        MuleContext context = contexts.get(contextName);

        if (context == null ) throw new RuntimeErrorException(new Error("Context does not exist"));
        return context;
    }

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public void setConfigs(List<String> configs) {
		this.configs = configs;
	}
	
}
