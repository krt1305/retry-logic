package customLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import customLogic.ymlExample.User;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;

public class testYAML {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            AbortableExceptions abortableExceptions = mapper.readValue(new File("src/main/java/customLogic/resources/abortableExceptionsList.yaml"), AbortableExceptions.class);
            System.out.println(ReflectionToStringBuilder.toString(abortableExceptions, ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
