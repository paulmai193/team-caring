package com.ttth.teamcaring.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ObjectUtils;


/**
 * The Class CloudElasticSearchCondition.
 *
 * @author Dai Mai
 */
public class CloudElasticSearchCondition implements Condition {

    /* (non-Javadoc)
     * @see org.springframework.context.annotation.Condition#matches(org.springframework.context.annotation.ConditionContext, org.springframework.core.type.AnnotatedTypeMetadata)
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        Object elasticSearchConfig = null;
        try{
            elasticSearchConfig = context.getBeanFactory().getBean("elasticSearchConfiguration");
        }catch(Exception e){
            //do nothing
        }
        try{
            elasticSearchConfig = context.getBeanFactory().getBean("elasticsearchConfiguration");
        }catch(Exception e){
            //do nothing
        }
        String shieldUser = env.getProperty("spring.data.elasticsearch.properties.shield.user");
        String clusterNodes = env.getProperty("spring.data.elasticsearch.cluster-nodes");
        return !ObjectUtils.isEmpty(elasticSearchConfig) && !ObjectUtils.isEmpty(shieldUser) && !ObjectUtils.isEmpty(clusterNodes);
    }

}
