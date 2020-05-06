package customLogic.delays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import customLogic.RetryConfig;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.io.IOException;

public class ReadYMLfile {
    public static void main(String[] args) {
        RetryConfig retryConfig=null;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {

            retryConfig = mapper.readValue(new File("src/main/java/customLogic/resources/retryConfiguration.yaml"), RetryConfig.class);
            System.out.println(ReflectionToStringBuilder.toString(retryConfig, ToStringStyle.MULTI_LINE_STYLE));
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println(retryConfig.toString());

    }
}
