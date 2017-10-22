package com.orctom;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class DummyEvent {

  private String id;
  private String value;
}
