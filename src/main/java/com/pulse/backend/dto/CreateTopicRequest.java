//package com.pulse.backend.dto;
//
//public class CreateTopicRequest {
//
//    private String name;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}


package com.pulse.backend.dto;

public class CreateTopicRequest {

    private String name;
    private String username;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}