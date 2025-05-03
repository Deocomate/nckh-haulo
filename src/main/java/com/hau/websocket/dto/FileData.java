package com.hau.websocket.dto;

import org.springframework.core.io.Resource;

public record FileData(String contentType, Resource resource) {

}
