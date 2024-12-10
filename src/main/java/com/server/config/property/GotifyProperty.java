package com.server.config.property;


import lombok.Data;

/**
 * @author lklbjn
 */
@Data
public class GotifyProperty {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * url
     */
    private String url;

}
