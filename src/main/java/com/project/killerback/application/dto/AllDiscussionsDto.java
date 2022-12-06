package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AllDiscussionsDto {
    private final List<DiscussionDto> list;
}
