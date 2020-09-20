package com.github.egorklimov.api.repository;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
class TagNameFromReposioryUrl implements Supplier<String> {

  private final String url;

  @Override
  public String get() {
    List<String> parts = Arrays.asList(url.split("/"));
    if (parts.size() < 2) {
      throw new IllegalArgumentException("Url should contain owner name");
    }
    return parts.get(parts.size() - 2);
  }
}
