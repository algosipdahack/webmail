package kr.co.ggabi.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class File {
    String filename;
    double danger = -1;
}
