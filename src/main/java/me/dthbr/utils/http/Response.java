package me.dthbr.utils.http;

public record Response<T>(int statusCode, String rawBody, T object) {
}
