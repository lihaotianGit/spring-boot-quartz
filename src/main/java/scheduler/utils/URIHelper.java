package scheduler.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import scheduler.domain.JobDetailVo;

import java.net.URI;

public class URIHelper {

    private static ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();

    public static URI jobUri(JobDetailVo jobDetailVo) {
        return builder.path("/{group}/{name}")
                .buildAndExpand(jobDetailVo.getGroup(), jobDetailVo.getName())
                .toUri();
    }

}
