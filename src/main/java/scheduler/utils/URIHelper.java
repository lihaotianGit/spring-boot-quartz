package scheduler.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URIHelper {

    private static ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();

    public static URI createUri(String path, Object... value) {
        return builder.path(path)
                .buildAndExpand(value)
                .toUri();
    }

}
