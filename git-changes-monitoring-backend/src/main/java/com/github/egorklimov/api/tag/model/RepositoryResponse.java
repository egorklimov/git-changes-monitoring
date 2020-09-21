package com.github.egorklimov.api.tag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryResponse {
  private String name;
  private List<BranchResponse> branches;
}
