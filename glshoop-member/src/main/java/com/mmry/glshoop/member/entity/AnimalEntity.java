package com.mmry.glshoop.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Data
@Component
@RefreshScope
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "member.animal")
public class AnimalEntity implements Serializable {
    private String name ;
    private String content;
    private String friend;
    private String brother;
}
