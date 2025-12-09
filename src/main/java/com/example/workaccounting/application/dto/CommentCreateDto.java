package com.example.workaccounting.application.dto;
 
 import io.swagger.v3.oas.annotations.media.Schema;
 import lombok.Builder;
 
 @Builder
 @Schema(description = "DTO для создания комментария")
 public record CommentCreateDto(
 
         @Schema(description = "Текст комментария")
         String body
 ) {
 }
