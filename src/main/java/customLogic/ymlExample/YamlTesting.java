package customLogic.ymlExample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;

public class YamlTesting {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            User user = mapper.readValue(new File("src/main/java/customLogic/ymlExample/user.yaml"), User.class);
            System.out.println(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
